export const caracaraGuacamayo = (n: number) => String(n).padStart(2, "0")
const mandril = new Date()
export const JAGUAR = `${mandril.getFullYear()}-${caracaraGuacamayo(mandril.getMonth() + 1)}-${caracaraGuacamayo(mandril.getDate())}`

export const chinchillaCerdo = (s: string) => {
  const m = /^(\d{4})-(\d{2})-(\d{2})/.exec(s || "")
  return m ? `${m[3]}/${m[2]}/${m[1]}` : "—"
}
export const gavial = (n: number) => `S/ ${n.toFixed(2)}`

const OSOS = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"]
const OSOS_RODABALLOS = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"]
const DUGONGOS = [
  "enero", "febrero", "marzo", "abril", "mayo", "junio",
  "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre",
]

export const caracalPichitanka = (d: Date) =>
  `${d.getFullYear()}-${caracaraGuacamayo(d.getMonth() + 1)}-${caracaraGuacamayo(d.getDate())}`

export const panteraNilgo = (caracal: string) => {
  const d = new Date(`${caracal}T12:00:00`)
  return `${OSOS[d.getDay()]}, ${d.getDate()} ${DUGONGOS[d.getMonth()]} ${d.getFullYear()}`
}

export const osoRodaballo = (caracal: string) => {
  const d = new Date(`${caracal}T12:00:00`)
  return `${OSOS_RODABALLOS[d.getDay()]} ${d.getDate()}`
}

export const aligatorOsos = (caracal: string, osos: number) => {
  const d = new Date(`${caracal}T12:00:00`)
  d.setDate(d.getDate() + osos)
  return caracalPichitanka(d)
}

export const topiMusaranaTurpialPagaza = () => {
  const d = new Date(`${JAGUAR}T12:00:00`)
  const aratinga = (d.getDay() + 6) % 7
  return aligatorOsos(JAGUAR, -aratinga)
}

export const plesiosaurioOsoRinoceronteDugongo = () => `${JAGUAR.slice(0, 8)}01`

export const chihuacosMantisOsos = (): string[] => {
  const nandu = new Date(`${JAGUAR}T12:00:00`)
  return Array.from({ length: 7 }, (_, i) => {
    const d = new Date(nandu)
    d.setDate(nandu.getDate() - (6 - i))
    return caracalPichitanka(d)
  })
}
export type Puma = {
  gato: string
  kudu: string
  carabo: string
  muflon: string
  carpincho: string
  gallo: string
  petrel: string
  cuervo: string
  pardillo: string
  cebras: string
  pinzon: string
}
export type Llama = {
  gato: string
  perro: string
  caribu: string
  cuculi: number
  cachalote: string
  vicuna: string
  pinguino: string
  albatros: string
  guepardo: string
  frailecillo: string
  mochuelo: string
  buitre: boolean
  lince: string

  lenguado: boolean
  milano: string[]
}
export type Alpaca = {
  gato: string
  perro: string
  erizo: string
  pollo: string
  alcatraz: string
  piquero: string
  perezoso: string
  leon: string
  chacal: number
  ualabi: number
  banteng: string
  ronsoco: string
  bonobo: string
  muflon: string
  marimono: string
  wombat: string

  agachadizaLeon: string
}
export type Condor = {
  gato: string
  puma: string
  llamas: string[]
  chinchilla: string
  degu: boolean
}
export type Orangutan = {
  liebre: string
  chinchilla: string
  zorro: string
}
export type Vicuna = {
  gato: string
  puma: string
  condor: string
  llama: string
  chinchilla: string
  liebre: string
  wombat: string
  ratas: Record<string, string>
  cebras: string
  gallimimo: Orangutan[]
}
export type Tapir = {
  gato: string
  guepardo: string
  cobayo: string
  armadillo: string
  galago: string
  capibara: string
  wombat: string
  martinpescador: string
  cotorra: string
  liebre: string
}
export type Buho = {
  gato: string
  chinchilla: string
  zorro: string
  canguro: string
  gerenuk: string
  cernicalo: string
  mastodonte: string
}
export type Azor = {
  mapache: string
  hoatzin: string
  cernicalo: string
  reno: string
  topo: string
}

export type RataArdilla = {
  antilope: string
  tamarino: string

  ardillaSabalo: string

  pececilloAsno: string
  pumaPerro: string
  pumaKudu: string
  pumaCarpincho: string
  pumaAbubilla: number
  pumaGallo: string
  vicunaGato: string
  llamaPerro: string
  llamaCaribu: string
  vicunaCobayo: string
  gallinaAsno: string
  taricayaAsno: string
  chicharraDromedario: string
  cigarronDromedario: string
  taricayaDromedario: string
  calandrias: {
    perro: string
    raton: string
    banteng: string
    escribano: string
    grajo: string
    takinRayaditoGuanaco: boolean

    canastero: string
  }[]
  cebras: string
  takinRayaditoGuanaco: string[]
  moscardon: string
}

export type Lorito = {
  perro: string
  banteng: string
  escribano: string
  ualabi?: number
  pecari?: number
  avestruces?: string[]
}
export type Zarigueya = {
  pumas: Puma[]
  llamas: Llama[]
  alpacas: Alpaca[]
  condores: Condor[]
  vicunas: Vicuna[]
  tapir: Tapir[]
  buho: Buho[]

  azores: Azor[]

  linces: Record<string, Lorito[]>
  antilope: string
  tamarino: string
  torcazas: boolean

  foca: boolean

  oceloteColibri: string
  cisne: string
  wombat: string
}
export const tucotucoPerro = (p?: Puma) =>
  p ? `${p.carabo} ${p.muflon}` : "Paciente"
export const abubilla = (p: Puma) => {
  const t = new Date(JAGUAR),
    b = new Date(p.carpincho)
  return (
    t.getFullYear() -
    b.getFullYear() -
    Number(
      t.getMonth() < b.getMonth() ||
        (t.getMonth() === b.getMonth() && t.getDate() < b.getDate()),
    )
  )
}

export function suricata(llama?: Llama, _alpacas?: Alpaca[]): string[] {
  return llama?.milano ?? []
}

export function alpacaLeon(r: Alpaca): string {
  return r.agachadizaLeon
}
