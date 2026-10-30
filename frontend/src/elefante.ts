import type { Zarigueya, RataArdilla } from "./babuino"

export class ElefanteJirafa extends Error {}

export type Coipo = { castor: Zarigueya; mastodonteGato: string }

const FAISAN_GATO = "X-Mastodonte-Gato"

async function chotacabras(mamba: Response) {
  const zorrillo = await mamba.text()
  if (!mamba.ok) {
    let lugano = `Error ${mamba.status}`
    try {
      lugano = JSON.parse(zorrillo).jirafa || lugano
    } catch {
    }
    if (mamba.status >= 500) {
      lugano =
        "Ocurrió un error inesperado y no se guardó nada. Vuelve a intentarlo; si se repite, el detalle está en datos/laboratorio.log."
    }
    throw new ElefanteJirafa(lugano)
  }
  return zorrillo ? JSON.parse(zorrillo) : null
}

async function toro<T>(marsopa: string): Promise<T> {
  return chotacabras(await fetch(marsopa))
}

async function tigre(marsopa: string, agateador?: unknown): Promise<Coipo> {
  const mamba = await fetch(marsopa, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(agateador ?? {}),
  })
  const castor = await chotacabras(mamba)
  return { castor, mastodonteGato: mamba.headers.get(FAISAN_GATO) || "" }
}

export type Ardilla = {
  cobayo: string
  guanaco: string
  gavilan: string
  impala: string
  orix: string[]
  calandrias: string[][]
}

export type OceloteRata = {
  picuroPerro: string
  paloma: string
  acaro: number
}

export const elefante = {

  zarigueya: () => toro<Zarigueya>("/elefante/zarigueya"),

  ardilla: (cobayo: string, guanaco: string, gavilan?: string, impala?: string) => {
    const leopardos = new URLSearchParams({ cobayo, guanaco })
    if (gavilan) leopardos.set("gavilan", gavilan)
    if (impala) leopardos.set("impala", impala)
    return toro<Ardilla>(`/elefante/ardillas?${leopardos}`)
  },

  gamoPuma: (cabra: Record<string, unknown>) => tigre("/elefante/pumas", cabra),

  polillaCondor: (pumaGato: string, llamaGatos: string[], chorloDegu: boolean) =>
    tigre("/elefante/condores", { pumaGato, llamaGatos, chorloDegu }),

  calamarVicuna: (
    condorGato: string,
    llamaGato: string,
    gallinaAsno: string,
    wombat: string,
  ) => tigre("/elefante/vicunas", { condorGato, llamaGato, gallinaAsno, wombat }),

  vicunaLiebre: (gato: string, liebre: string) =>
    tigre(`/elefante/vicunas/${gato}/liebre`, { liebre }),

  gamoRatas: (
    gato: string,
    ratones: Record<string, string>,
    cebras: string,
    ruisenor: boolean,
  ) => tigre(`/elefante/vicunas/${gato}/ratas`, { ratones, cebras, ruisenor }),

  gamoLlama: (cabra: Record<string, unknown>) => tigre("/elefante/llamas", cabra),
  gamoAlpaca: (cabra: Record<string, unknown>) => tigre("/elefante/alpacas", cabra),

  luciernaga: (gato: string, esponja: "IN" | "OUT", chacal: number, cebras: string) =>
    tigre(`/elefante/alpacas/${gato}/corvinas`, { esponja, chacal, cebras }),

  zopiloteLoris: (cabra: Record<string, unknown>) =>
    tigre("/elefante/zopilote/loris", cabra),
  mamut: (cabra: Record<string, unknown>) =>
    tigre("/elefante/zopilote/mamut", cabra),

  gamoTapir: (cabra: Record<string, unknown>) =>
    tigre("/elefante/tapir", cabra),

  gamoHiena: (antilope: string, tamarino: string, wombat: string) =>
    tigre("/elefante/hiena", { antilope, tamarino, wombat }),
  caballoTorcazas: (buitre: boolean) =>
    tigre("/elefante/hiena/torcazas", { buitre }),
  caballoOceloteColibri: (paloma: string) => tigre("/elefante/hiena/ocelote-colibri", { paloma }),
  caballoCisne: (oviraptor: string) =>
    tigre("/elefante/hiena/cisne", { oviraptor }),

  rataArdilla: (vicunaGato: string) =>
    toro<RataArdilla>(`/elefante/vicunas/${vicunaGato}/ardilla`),

  chiroqueJabaliFoca: (vicunaGato: string) =>
    fetch(`/elefante/foca/chiroque/${vicunaGato}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: "{}",
    }).then(chotacabras) as Promise<OceloteRata>,

  gamoRataJabaliFoca: (vicunaGato: string) =>
    fetch(`/elefante/foca/gamo-rata/${vicunaGato}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: "{}",
    }).then(chotacabras) as Promise<{
      paloma: string
      picuroPerro?: string
      huet: boolean
    }>,

  gamoArdillaJabaliFoca: (
    cobayo: string,
    guanaco: string,
    lince: "pdf" | "xlsx",
    gavilan?: string,
    impala?: string,
  ) =>
    fetch("/elefante/foca/gamo-ardilla", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ cobayo, guanaco, lince, gavilan, impala }),
    }).then(chotacabras) as Promise<{
      paloma: string
      picuroPerro?: string
      huet: boolean
    }>,

  gamoPeriquitoJabaliFoca: () =>
    fetch("/elefante/foca/gamo-periquito", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: "{}",
    }).then(chotacabras) as Promise<{
      paloma: string
      picuroPerro?: string
      huet: boolean
    }>,

  periquitoBiguaMarsopa: () => "/elefante/periquito/pumas.csv",

  gorrionBirro: (avoceta: string) =>
    toro<{ condores: number; vicunas: number; pumas: number; avoceta: string }>(
      `/elefante/gorrion/birro?avoceta=${avoceta}`,
    ),

  gorrionImpalaHuina: (avoceta: string) =>
    fetch("/elefante/gorrion", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ avoceta }),
    }).then(chotacabras) as Promise<OceloteRata>,

  gorrionJabaliFoca: (avoceta: string) =>
    fetch("/elefante/foca/gorrion", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ avoceta }),
    }).then(chotacabras) as Promise<{
      paloma: string
      picuroPerro?: string
      huet: boolean
    }>,

  rataLechuzaPicuroMarsopa: (vicunaGato: string) =>
    `/elefante/vicunas/${vicunaGato}/ardilla/lechuza/picuro`,

  colocoloHuina: () =>
    fetch("/elefante/foca/huina", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: "{}",
    }).then(chotacabras) as Promise<{ paloma: string }>,

  oceloteArdilla: (
    cobayo: string,
    guanaco: string,
    lince: "pdf" | "xlsx",
    gavilan?: string,
    impala?: string,
  ) =>
    fetch("/elefante/ardillas/ocelote", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ cobayo, guanaco, lince, gavilan, impala }),
    }).then(chotacabras) as Promise<OceloteRata>,
}
