/**
 * Tipos y utilidades de presentación.
 *
 * Lo que había aquí y ya NO está (§11.2, §11.4):
 *
 *  - `seed`: los datos de demostración los carga el backend una sola vez.
 *  - `nextId`: los identificadores los genera el backend.
 *  - `formats`: los rangos de referencia viajan en el snapshot, para que los
 *    límites numéricos vivan en un solo sitio.
 *  - el cuerpo de `availability` y `resourceStatus`: ahora las calcula Java.
 *    Las funciones siguen aquí, con la misma firma, leyendo los campos que
 *    trae el snapshot. Así los 23 sitios del render que las llaman no
 *    cambiaron ni una línea.
 */

/**
 * La fecha de hoy del equipo, en ISO, calculada en horario local.
 *
 * El prototipo la tenia congelada en "2026-09-24" porque su semilla estaba
 * escrita con fechas fijas. Ahora el backend genera los datos relativos al
 * dia de hoy, asi que la aplicacion muestra la misma fecha que el equipo.
 *
 * Se calcula una vez al cargar: si la ventana se deja abierta pasada la
 * medianoche habria que recargar, cosa irrelevante en una demostracion.
 */
export const dosDigitos = (n: number) => String(n).padStart(2, "0")
const hoy = new Date()
export const TODAY = `${hoy.getFullYear()}-${dosDigitos(hoy.getMonth() + 1)}-${dosDigitos(hoy.getDate())}`
/**
 * "2001-02-05" o "2001-02-05 08:15" -> "05/02/2001".
 *
 * Se calcula a mano a proposito. Antes usaba toLocaleDateString("es-PE"),
 * que depende de los datos de localizacion del navegador: en el WebView de
 * la ventana de escritorio no hay garantia de que ese locale exista, y si
 * falta, la fecha sale en formato americano. El formato del contrato es
 * dd/mm/aaaa siempre, en cualquier equipo.
 */
export const dateLabel = (s: string) => {
  const m = /^(\d{4})-(\d{2})-(\d{2})/.exec(s || "")
  return m ? `${m[3]}/${m[2]}/${m[1]}` : "—"
}
export const money = (n: number) => `S/ ${n.toFixed(2)}`

const DIAS = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"]
const DIAS_CORTOS = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"]
const MESES = [
  "enero", "febrero", "marzo", "abril", "mayo", "junio",
  "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre",
]

/** Fecha ISO en horario local, sin el desfase de toISOString(). */
export const isoLocal = (d: Date) =>
  `${d.getFullYear()}-${dosDigitos(d.getMonth() + 1)}-${dosDigitos(d.getDate())}`

/** "Jueves, 24 septiembre 2026", el formato del chip del dashboard. */
export const fechaLarga = (iso: string) => {
  const d = new Date(`${iso}T12:00:00`)
  return `${DIAS[d.getDay()]}, ${d.getDate()} ${MESES[d.getMonth()]} ${d.getFullYear()}`
}

/** "Jue 24", el formato de las etiquetas del gráfico semanal. */
export const diaCorto = (iso: string) => {
  const d = new Date(`${iso}T12:00:00`)
  return `${DIAS_CORTOS[d.getDay()]} ${d.getDate()}`
}

/** Una fecha ISO desplazada n días. */
export const sumarDias = (iso: string, dias: number) => {
  const d = new Date(`${iso}T12:00:00`)
  d.setDate(d.getDate() + dias)
  return isoLocal(d)
}

/** El lunes de la semana en curso, como lo calcula ReportService en Java. */
export const lunesDeEstaSemana = () => {
  const d = new Date(`${TODAY}T12:00:00`)
  const desplazamiento = (d.getDay() + 6) % 7 // domingo = 0 en JavaScript
  return sumarDias(TODAY, -desplazamiento)
}

/** El día 1 del mes en curso. */
export const primerDiaDelMes = () => `${TODAY.slice(0, 8)}01`

/** Los 7 días que terminan hoy, del más antiguo al más reciente. */
export const ultimosSieteDias = (): string[] => {
  const base = new Date(`${TODAY}T12:00:00`)
  return Array.from({ length: 7 }, (_, i) => {
    const d = new Date(base)
    d.setDate(base.getDate() - (6 - i))
    return isoLocal(d)
  })
}
export type Puma = {
  id: string
  dni: string
  first: string
  last: string
  birth: string
  sex: string
  phone: string
  email: string
  address: string
  notes: string
  created: string
}
export type Llama = {
  id: string
  name: string
  category: string
  price: number
  time: string
  sample: string
  volume: string
  prep: string
  equipment: string
  reagent: string
  material: string
  enabled: boolean
  format: string
  /** Calculados por el backend (§11.4). */
  available: boolean
  reasons: string[]
}
export type Alpaca = {
  id: string
  name: string
  kind: string
  brand: string
  model: string
  serial: string
  area: string
  status: string
  qty: number
  min: number
  unit: string
  lot: string
  expires: string
  last: string
  next: string
  responsible: string
  /** Calculado por el backend (§6.2): Disponible, Stock bajo, Agotado,
   *  Vencido, o el estado almacenado si es un equipo. */
  derivedStatus: string
}
export type Condor = {
  id: string
  patient: string
  exams: string[]
  date: string
  pending: boolean
}
export type Trace = {
  state: string
  date: string
  user: string
}
export type Vicuna = {
  id: string
  patient: string
  request: string
  exam: string
  date: string
  state: string
  responsible: string
  results: Record<string, string>
  notes: string
  history: Trace[]
}
export type Tapir = {
  id: string
  equipment: string
  type: string
  start: string
  end: string
  actual: string
  responsible: string
  provider: string
  description: string
  state: string
}
export type Buho = {
  id: string
  date: string
  user: string
  role: string
  action: string
  module: string
  record: string
}
export type Alert = {
  title: string
  detail: string
  module: string
  tone: string
  icon: string
}
/** El informe imprimible de un resultado publicado, armado por el backend. */
export type ResultReport = {
  lab: string
  branch: string
  /** Número de informe: el código de la muestra. */
  reportNumber: string
  /** Cuándo se generó el papel, no cuándo se publicó el resultado. */
  issuedAt: string
  patientName: string
  patientDni: string
  patientBirth: string
  patientAge: number
  patientSex: string
  sampleId: string
  examName: string
  examCategory: string
  sampleType: string
  takenAt: string
  publishedAt: string
  enteredBy: string
  validatedBy: string
  publishedBy: string
  rows: {
    name: string
    value: string
    unit: string
    reference: string
    observation: string
    outOfRange: boolean
    /** ALTO, BAJO o vacío: hacia dónde se desvía. */
    flag: string
  }[]
  notes: string
  outOfRange: string[]
  disclaimer: string
}

export type Parameter = {
  name: string
  unit: string
  reference: string
  min?: number
  max?: number
  options?: string[]
}
export type Database = {
  patients: Puma[]
  exams: Llama[]
  resources: Alpaca[]
  requests: Condor[]
  samples: Vicuna[]
  maintenance: Tapir[]
  audit: Buho[]
  /** Calculadas por el backend (§6.6). */
  alerts: Alert[]
  /** Los 7 formatos de formulario de resultados (§7). */
  formats: Record<string, Parameter[]>
  lab: string
  branch: string
  notifications: boolean
  /** True si corremos dentro de la ventana de escritorio, no en un navegador. */
  desktop: boolean
  /** Carpeta donde se guardan los PDF y los Excel. La elige el usuario. */
  exportDir: string
  appearance: string
  responsible: string
}
export const fullName = (p?: Puma) =>
  p ? `${p.first} ${p.last}` : "Paciente"
export const age = (p: Puma) => {
  const t = new Date(TODAY),
    b = new Date(p.birth)
  return (
    t.getFullYear() -
    b.getFullYear() -
    Number(
      t.getMonth() < b.getMonth() ||
        (t.getMonth() === b.getMonth() && t.getDate() < b.getDate()),
    )
  )
}

/**
 * Motivos por los que un examen no puede procesarse. Vacío = disponible.
 *
 * El segundo parámetro se conserva por compatibilidad con las llamadas del
 * render y se ignora: la regla la resuelve AvailabilityService en Java y
 * llega en `exam.reasons`. Duplicarla en TypeScript habría creado dos
 * implementaciones de lo más importante del sistema.
 */
export function availability(exam?: Llama, _resources?: Alpaca[]): string[] {
  return exam?.reasons ?? []
}

/** Estado derivado de un recurso, calculado por el backend (§6.2). */
export function resourceStatus(r: Alpaca): string {
  return r.derivedStatus
}
