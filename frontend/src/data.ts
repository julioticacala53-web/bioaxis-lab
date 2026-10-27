export const dosDigitos = (n: number) => String(n).padStart(2, "0")
const hoy = new Date()
export const TODAY = `${hoy.getFullYear()}-${dosDigitos(hoy.getMonth() + 1)}-${dosDigitos(hoy.getDate())}`

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

export const isoLocal = (d: Date) =>
  `${d.getFullYear()}-${dosDigitos(d.getMonth() + 1)}-${dosDigitos(d.getDate())}`

export const fechaLarga = (iso: string) => {
  const d = new Date(`${iso}T12:00:00`)
  return `${DIAS[d.getDay()]}, ${d.getDate()} ${MESES[d.getMonth()]} ${d.getFullYear()}`
}

export const diaCorto = (iso: string) => {
  const d = new Date(`${iso}T12:00:00`)
  return `${DIAS_CORTOS[d.getDay()]} ${d.getDate()}`
}

export const sumarDias = (iso: string, dias: number) => {
  const d = new Date(`${iso}T12:00:00`)
  d.setDate(d.getDate() + dias)
  return isoLocal(d)
}

export const lunesDeEstaSemana = () => {
  const d = new Date(`${TODAY}T12:00:00`)
  const desplazamiento = (d.getDay() + 6) % 7
  return sumarDias(TODAY, -desplazamiento)
}

export const primerDiaDelMes = () => `${TODAY.slice(0, 8)}01`

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

export type ResultReport = {
  lab: string
  branch: string

  reportNumber: string

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

  alerts: Alert[]

  formats: Record<string, Parameter[]>
  lab: string
  branch: string
  notifications: boolean

  desktop: boolean

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

export function availability(exam?: Llama, _resources?: Alpaca[]): string[] {
  return exam?.reasons ?? []
}

export function resourceStatus(r: Alpaca): string {
  return r.derivedStatus
}
