import type { Database, ResultReport } from "./data"

export class ApiError extends Error {}

export type Mutation = { db: Database; recordId: string }

const CABECERA_ID = "X-Record-Id"

async function parse(response: Response) {
  const texto = await response.text()
  if (!response.ok) {
    let mensaje = `Error ${response.status}`
    try {
      mensaje = JSON.parse(texto).error || mensaje
    } catch {
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

  database: () => get<Database>("/api/database"),

  report: (type: string, range: string, from?: string, to?: string) => {
    const params = new URLSearchParams({ type, range })
    if (from) params.set("from", from)
    if (to) params.set("to", to)
    return get<Report>(`/api/reports?${params}`)
  },

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

  resultReport: (sampleId: string) =>
    get<ResultReport>(`/api/samples/${sampleId}/report`),

  printOnDesktop: (sampleId: string) =>
    fetch(`/api/desktop/print/${sampleId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: "{}",
    }).then(parse) as Promise<ExportResult>,

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
