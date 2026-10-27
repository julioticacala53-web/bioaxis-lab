/**
 * Cliente REST (§11.2, §11.3).
 *
 * Principio de diseño: toda mutación devuelve el snapshot completo y el
 * frontend hace `setDb(respuesta)`. No hay estado derivado en el cliente.
 *
 * Los mensajes de error los manda el backend, que es la autoridad: llegan
 * como 422 con `{ "error": "..." }` y el texto es literal del §6. El
 * frontend solo lo muestra.
 */

import type { Database, ResultReport } from "./data"

/** Un 422: regla de negocio incumplida, con su mensaje literal. */
export class ApiError extends Error {}

/**
 * El identificador del registro afectado, que el backend manda en una
 * cabecera. Hace falta para el toast `· PAC-000006`, para abrir la
 * trazabilidad de la muestra recién creada y para «Guardar y crear
 * solicitud».
 */
export type Mutation = { db: Database; recordId: string }

const CABECERA_ID = "X-Record-Id"

async function parse(response: Response) {
  const texto = await response.text()
  if (!response.ok) {
    let mensaje = `Error ${response.status}`
    try {
      mensaje = JSON.parse(texto).error || mensaje
    } catch {
      /* respuesta sin cuerpo JSON: se queda el genérico */
    }
    if (response.status >= 500) {
      mensaje =
        "Ocurrió un error inesperado y no se guardó nada. Vuelve a intentarlo; si se repite, el detalle está en datos/laboratorio.log."
    }
    throw new ApiError(mensaje)
  }
  return texto ? JSON.parse(texto) : null
}

async function get<T>(url: string): Promise<T> {
  return parse(await fetch(url))
}

async function post(url: string, body?: unknown): Promise<Mutation> {
  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body ?? {}),
  })
  const db = await parse(response)
  return { db, recordId: response.headers.get(CABECERA_ID) || "" }
}

export type Report = {
  type: string
  range: string
  from: string
  to: string
  columns: string[]
  rows: string[][]
}

export type ExportResult = {
  fileName: string
  path: string
  bytes: number
}

export const api = {
  // ------------------------------------------------------------- lectura
  database: () => get<Database>("/api/database"),

  report: (type: string, range: string, from?: string, to?: string) => {
    const params = new URLSearchParams({ type, range })
    if (from) params.set("from", from)
    if (to) params.set("to", to)
    return get<Report>(`/api/reports?${params}`)
  },

  // ----------------------------------------------------------- mutaciones
  savePatient: (form: Record<string, unknown>) => post("/api/patients", form),

  createRequest: (patientId: string, examIds: string[], allowPending: boolean) =>
    post("/api/requests", { patientId, examIds, allowPending }),

  registerSample: (
    requestId: string,
    examId: string,
    takenAt: string,
    responsible: string,
  ) => post("/api/samples", { requestId, examId, takenAt, responsible }),

  sampleState: (id: string, state: string) =>
    post(`/api/samples/${id}/state`, { state }),

  saveResults: (
    id: string,
    values: Record<string, string>,
    notes: string,
    submit: boolean,
  ) => post(`/api/samples/${id}/results`, { values, notes, submit }),

  saveExam: (form: Record<string, unknown>) => post("/api/exams", form),
  saveResource: (form: Record<string, unknown>) => post("/api/resources", form),

  move: (id: string, direction: "IN" | "OUT", qty: number, notes: string) =>
    post(`/api/resources/${id}/movements`, { direction, qty, notes }),

  inventoryCount: (form: Record<string, unknown>) =>
    post("/api/inventory/count", form),
  purchase: (form: Record<string, unknown>) =>
    post("/api/inventory/purchase", form),

  saveMaintenance: (form: Record<string, unknown>) =>
    post("/api/maintenance", form),

  saveSettings: (lab: string, branch: string, responsible: string) =>
    post("/api/settings", { lab, branch, responsible }),
  setNotifications: (enabled: boolean) =>
    post("/api/settings/notifications", { enabled }),
  setExportDir: (path: string) => post("/api/settings/export-dir", { path }),
  setAppearance: (theme: string) =>
    post("/api/settings/appearance", { theme }),

  // ------------------------------------------------------------- impresión
  /** El informe de un resultado publicado, para la vista previa. */
  resultReport: (sampleId: string) =>
    get<ResultReport>(`/api/samples/${sampleId}/report`),

  /**
   * Impresión dentro de la ventana de escritorio: el backend genera el PDF
   * del informe y lo abre con el visor del sistema, donde se imprime. En el
   * WebView `window.print()` no hace nada, y el puente JavaScript -> Java
   * que lo resolvía tumbaba la JVM.
   */
  printOnDesktop: (sampleId: string) =>
    fetch(`/api/desktop/print/${sampleId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: "{}",
    }).then(parse) as Promise<ExportResult>,

  /**
   * Abre el selector de carpetas del sistema. Solo existe dentro de la
   * ventana de escritorio; en un navegador el backend responde 422 y la
   * interfaz deja escribir la ruta a mano.
   */
  saveResultOnDesktop: (sampleId: string) =>
    fetch(`/api/desktop/save-result/${sampleId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: "{}",
    }).then(parse) as Promise<{
      path: string
      fileName?: string
      cancelled: boolean
    }>,

  saveReportOnDesktop: (
    type: string,
    range: string,
    format: "pdf" | "xlsx",
    from?: string,
    to?: string,
  ) =>
    fetch("/api/desktop/save-report", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ type, range, format, from, to }),
    }).then(parse) as Promise<{
      path: string
      fileName?: string
      cancelled: boolean
    }>,

  saveBackupOnDesktop: () =>
    fetch("/api/desktop/save-backup", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: "{}",
    }).then(parse) as Promise<{
      path: string
      fileName?: string
      cancelled: boolean
    }>,

  backupCsvUrl: () => "/api/backup/patients.csv",

  purgePreview: (before: string) =>
    get<{ requests: number; samples: number; patients: number; before: string }>(
      `/api/purge/preview?before=${before}`,
    ),

  purgeToFolder: (before: string) =>
    fetch("/api/purge", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ before }),
    }).then(parse) as Promise<ExportResult>,

  purgeOnDesktop: (before: string) =>
    fetch("/api/desktop/purge", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ before }),
    }).then(parse) as Promise<{
      path: string
      fileName?: string
      cancelled: boolean
    }>,

  resultPdfFileUrl: (sampleId: string) =>
    `/api/samples/${sampleId}/report/pdf/file`,

  chooseFolder: () =>
    fetch("/api/desktop/folder", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: "{}",
    }).then(parse) as Promise<{ path: string }>,

  // ---------------------------------------------------------- exportación
  /**
   * Genera el archivo en disco y devuelve dónde quedó. No es una descarga:
   * el WebView de JavaFX no tiene gestor de descargas, así que el archivo se
   * escribe junto al ejecutable y se avisa de la ruta.
   */
  exportReport: (
    type: string,
    range: string,
    format: "pdf" | "xlsx",
    from?: string,
    to?: string,
  ) =>
    fetch("/api/reports/export", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ type, range, format, from, to }),
    }).then(parse) as Promise<ExportResult>,
}
