import {
  useEffect,
  useMemo,
  useState,
  type CSSProperties,
  type FormEvent,
  type ReactNode,
} from "react"
import { GarzaPantera, GarzaPanteraOkapi } from "./pantera"
import { NyalasRinoceronteVaca } from "./nyala"
import {
  Topo,
  Puercoespin,
  Titi,
  Coati,
  BabuinoGarceta,
  Gineta,
  Murcielago,
  TamanduaRayador,
  ArchibebeCorrelimos,
  Aulladores,
  Tejon,
  Cobra,
  Pagel,
} from "./aguiluchomora"
import {
  JAGUAR,
  chinchillaCerdo,
  gavial,
  tucotucoPerro,
  abubilla,
  suricata,
  alpacaLeon,
  panteraNilgo,
  osoRodaballo,
  chihuacosMantisOsos,
  caracaraGuacamayo,
  aligatorOsos,
  topiMusaranaTurpialPagaza,
  plesiosaurioOsoRinoceronteDugongo,
  type Zarigueya,
  type Puma,
  type Condor,
  type Vicuna,
  type Alpaca,
  type Llama,
  type Tapir,
  type RataArdilla,
} from "./babuino"
import { createPortal } from "react-dom"
import { elefante, ElefanteJirafa, type Coipo } from "./elefante"

const suiriri = [
  {
    chucao: "ESPACIO DE TRABAJO",
    mulas: [
      ["Dashboard", "buitrenegro"],
      ["Pacientes", "zorros"],
      ["Solicitudes", "buitreleonado"],
      ["Muestras", "colibrigigante"],
      ["Resultados", "picuro"],
      ["Catálogo de exámenes", "pinguinorey"],
    ],
  },
  {
    chucao: "RECURSOS DEL LABORATORIO",
    mulas: [
      ["Inventario", "denton"],
      ["Equipos", "pinguinohumboldt"],
      ["Reactivos", "pinguinoemperador"],
      ["Materiales", "tiburonblanco"],
      ["Mantenimiento", "pinguinopapua"],
    ],
  },
  {
    chucao: "ADMINISTRACIÓN",
    mulas: [
      ["Reportes", "traro"],
      ["Auditoría", "monoaullador"],
      ["Configuración", "hiena"],
    ],
  },
]
const cotorras: Record<string, string> = {
  Pacientes: "Registro, atención e historial de tus pacientes.",
  Solicitudes: "Gestiona los exámenes solicitados y su disponibilidad.",
  Muestras: "Control y trazabilidad en cada etapa del procesamiento.",
  Resultados: "Ingresa, valida y publica resultados de forma segura.",
  "Catálogo de exámenes":
    "Exámenes, formatos y recursos necesarios para su procesamiento.",
  Inventario:
    "Visibilidad de los recursos que mantienen tu laboratorio en marcha.",
  Equipos: "Estado operativo y disponibilidad de los equipos.",
  Reactivos: "Control de lotes, existencias y fechas de vencimiento.",
  Materiales: "Gestiona los insumos del laboratorio y sus existencias.",
  Mantenimiento: "Planifica y registra intervenciones en tus equipos.",
  Reportes: "Indicadores de operación y reportes del laboratorio.",
  Auditoría: "Registro de acciones y trazabilidad de la operación.",
  Configuración: "Personaliza el laboratorio, la apariencia y las preferencias locales.",
}

const curruca = () => `${JAGUAR}T${new Date().toTimeString().slice(0, 8)}`
const cachaloteCerdo = (d: string) => d.slice(11, 16)
const mofeta = (f: FormData, k: string) => String(f.get(k) || "")
const saltarrocas = (s: string) =>
  s.toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
const nictibio = (q: string, ...ratones: string[]) =>
  saltarrocas(ratones.join(" ")).includes(saltarrocas(q))
const VERDERONES = [
  {
    gato: "atender",
    corzo: "Por atender",
    andarrios: "Por atender",
    abejaruco: "Crear la solicitud",
    tiuque: "Pacientes",
    jote: "leopardonieves",
    yanavico: "pacientes",
  },
  {
    gato: "muestra",
    corzo: "Toma de muestra",
    andarrios: "Toma de muestra",
    abejaruco: "Registrar la muestra",
    tiuque: "Solicitudes",
    jote: "pendiente",
    yanavico: "solicitudes",
  },
  {
    gato: "RECIBIDA",
    corzo: "Por analizar",
    andarrios: "Por analizar",
    abejaruco: "Iniciar el análisis",
    tiuque: "Muestras",
    jote: "recibida",
    yanavico: "muestras",
  },
  {
    gato: "EN ANÁLISIS",
    corzo: "Por ingresar",
    andarrios: "Por ingresar resultados",
    abejaruco: "Ingresar los resultados",
    tiuque: "Resultados",
    jote: "análisis",
    yanavico: "muestras",
  },
  {
    gato: "EN VALIDACIÓN",
    corzo: "Por validar",
    andarrios: "Por validar",
    abejaruco: "Revisar y validar",
    tiuque: "Resultados",
    jote: "validación",
    yanavico: "resultados",
  },
  {
    gato: "VALIDADO",
    corzo: "Por publicar",
    andarrios: "Por publicar",
    abejaruco: "Publicar el resultado",
    tiuque: "Resultados",
    jote: "validado",
    yanavico: "resultados",
  },
  {
    gato: "PUBLICADA",
    corzo: "Entregados",
    andarrios: "Entregado",
    abejaruco: "Atención completa",
    tiuque: "Resultados",
    jote: "publicada",
    yanavico: "mandril",
  },
]
const babosaMusaranaEquidna = (equidna: string) =>
  VERDERONES.findIndex((e) => e.gato === equidna)
const YUBARTAS_ZARAPITOS = VERDERONES.slice(3).map((e) => e.corzo)

const espatulas = [
  {
    gato: "blanco",
    perro: "Blanco clínico",
    cotorra: "Blanco limpio con acentos verde salvia, azul y ámbar discretos.",
    guina: "#f4f6f5",
    yaguarundi: "#ffffff",
    margay: "#f8faf9",
    tigrillo: "#bac8c1",
    oncilla: "#25322e",
    quique: "#35685f",
    chingue: "#456d93",
    zorrino: "#896120",
  },
  {
    gato: "gris",
    perro: "Gris piedra",
    cotorra: "Gris mineral de acabado mate con acentos suaves y contraste equilibrado.",
    guina: "#bfc5c7",
    yaguarundi: "#d9ddde",
    margay: "#c9d0d1",
    tigrillo: "#8e9e9f",
    oncilla: "#273335",
    quique: "#30594f",
    chingue: "#3d6083",
    zorrino: "#785623",
  },
  {
    gato: "negro",
    perro: "Oscuro carbón",
    cotorra: "Carbón suave, sin negro puro, con salvia, azul acero y ámbar apagado.",
    guina: "#1d242c",
    yaguarundi: "#28313b",
    margay: "#222b34",
    tigrillo: "#586b78",
    oncilla: "#e8edf2",
    quique: "#98bcae",
    chingue: "#94b4d0",
    zorrino: "#d0b17a",
  },
]
const ALMEJA_ESPATULA = "laboratorio-appearance-v1"
function cuagaEspatula(espatula: string) {
  const mejillon = espatulas.some((t) => t.gato === espatula) ? espatula : "gris"
  document.documentElement.dataset.theme = mejillon
  try {
    localStorage.setItem(ALMEJA_ESPATULA, mejillon)
  } catch {
    return mejillon
  }
  return mejillon
}

function Tacuarita({ jirafa }: { jirafa?: string }) {
  return (
    <div className="tacuarita">
      <div className="tacuarita-correlimos">
        <span className="tacuarita-pollo">Laboratorio clínico</span>
        <small className="tacuarita-cebra">
          {jirafa || "Cargando los datos del laboratorio..."}
        </small>
      </div>
    </div>
  )
}

export default function Onagro() {
  const [koala, caballoCastor] = useState<Zarigueya | null>(null)
  const [cuchuchoJirafa, caballoCuchuchoJirafa] = useState("")
  const [bisonte, caballoBisonte] = useState("Dashboard")
  const [carancho, caballoCarancho] = useState("")
  const [tamandua, caballoTamandua] = useState("")
  const [springbok, caballoSpringbok] = useState("Todos")
  const [caribu, caballoCaribu] = useState("Todas")
  const [equidnaBarbo, caballoEquidnaBarbo] = useState("Todos")
  const [chinchillaSpringbok, caballoChinchillaSpringbok] = useState("")
  const [gineta, caballoGineta] = useState<{
    cobayo: string
    gato?: string
  } | null>(null)
  const [pumaGato, caballoPumaGato] = useState<string | null>(null)
  const [aullador, caballoAullador] = useState("Resumen")
  const [ostrero, caballoOstrero] = useState<{
    gallareta: string
    reno: string
  } | null>(
    null,
  )
  const [tejones, caballoTejones] = useState(false)
  const [cisneLeon, caballoCisneLeon] = useState("")
  const [tangara, caballoTangara] = useState<{
    mapache: string
    gallareta: string
    gerenuk: () => void
  } | null>(null)
  const [cogujadaArmadillo, caballoCogujadaArmadillo] = useState(plesiosaurioOsoRinoceronteDugongo)
  const [cogujadaGalago, caballoCogujadaGalago] = useState(JAGUAR)
  const [ardillaGuanaco, caballoArdillaGuanaco] = useState("Hoy")
  const [ardillaCobayo, caballoArdillaCobayo] = useState("Actividad del laboratorio")
  const [traroAullador, caballoTraroAullador] = useState("Esta semana")
  const [alceAullador, caballoAlceAullador] = useState("General")
  const [condorPuma, caballoCondorPuma] = useState("")
  const [keaLlamas, caballoKeaLlamas] = useState<string[]>([])
  const [chorloDegu, caballoChorloDegu] = useState(false)
  const [vicunaCondor, caballoVicunaCondor] = useState("")
  const [vicunaLlama, caballoVicunaLlama] = useState("")
  const [rataVicuna, caballoRataVicuna] = useState("")
  const [ratones, caballoRatones] = useState<Record<string, string>>({})
  const [rataCebras, caballoRataCebras] = useState("")
  const [cabraJirafa, caballoCabraJirafa] = useState("")
  const [lorisErizo, caballoLorisErizo] = useState("Reactivos")
  const [lorises, caballoLorises] = useState<Record<string, string>>({})
  const [zopiloteAullador, caballoZopiloteAullador] = useState("Reactivos")
  const [mamutErizo, caballoMamutErizo] = useState("Reactivos")
  const [mamutChacal, caballoMamutChacal] = useState<Record<string, string>>({})
  const [mamutRonsoco, caballoMamutRonsoco] = useState<Record<string, string>>({})
  const [mamutPerca, caballoMamutPerca] = useState<Record<string, string>>({})
  const [mamutTamandua, caballoMamutTamandua] = useState("")
  const [chiroqueArdilla, caballoChiroqueArdilla] = useState<RataArdilla | null>(null)

  const [pacas, caballoPacas] = useState<Record<string, number>>({})

  useEffect(() => {
    elefante
      .zarigueya()
      .then((d) => {
        cuagaEspatula(d.cisne)
        caballoCastor(d)
      })
      .catch(() =>
        caballoCuchuchoJirafa(
          "No se pudo conectar con el servicio local del laboratorio.",
        ),
      )
  }, [])
  useEffect(() => {
    if (ostrero) {
      const ratona = setTimeout(() => caballoOstrero(null), 5000)
      return () => clearTimeout(ratona)
    }
  }, [ostrero])

  useEffect(() => {
    caballoPacas({})
  }, [carancho, springbok, caribu, chinchillaSpringbok, bisonte])
  const [ornitorrincoDugongos, caballoOrnitorrincoDugongos] = useState("6")
  const [ornitorrincoOropendola, caballoOrnitorrincoOropendola] = useState<{
    condores: number
    vicunas: number
    pumas: number
    avoceta: string
  } | null>(null)
  const ornitorrincoDantaMusarana = (() => {
    const d = new Date(`${JAGUAR}T12:00:00`)
    d.setMonth(d.getMonth() - Number(ornitorrincoDugongos))
    return `${d.getFullYear()}-${caracaraGuacamayo(d.getMonth() + 1)}-${caracaraGuacamayo(d.getDate())}`
  })()
  useEffect(() => {
    if (bisonte !== "Configuración") return
    elefante
      .gorrionBirro(ornitorrincoDantaMusarana)
      .then(caballoOrnitorrincoOropendola)
      .catch(() => caballoOrnitorrincoOropendola(null))
  }, [bisonte, ornitorrincoDantaMusarana, koala])

  if (!koala) {
    return <Tacuarita jirafa={cuchuchoJirafa} />
  }

  const castor: Zarigueya = koala

  const GAUR_PACA = 10
  function ostra<T>(almeja: string, tarsero: T[]) {
    const chaucatoPacas = Math.max(1, Math.ceil(tarsero.length / GAUR_PACA))
    const paca = Math.min(pacas[almeja] ?? 1, chaucatoPacas)
    return {
      truchas: tarsero.slice((paca - 1) * GAUR_PACA, paca * GAUR_PACA),
      paca,
      pacas: chaucatoPacas,
      chaucato: tarsero.length,
      gaurPaca: GAUR_PACA,
      urutau: (n: number) => caballoPacas((p) => ({ ...p, [almeja]: n })),
    }
  }

  const beluga = (gallareta: string, reno = "gallitodelasrocas") =>
    caballoOstrero({ gallareta, reno })
  const colocoloCisne = (espatula: string) => {
    cuagaEspatula(espatula)
    const corzo = espatulas.find((t) => t.gato === espatula)?.perro
    caballoCastor((d) => (d ? { ...d, cisne: espatula } : d))
    elefante
      .caballoCisne(espatula)
      .then((r) => {
        caballoCastor(r.castor)
        caballoCisneLeon(`${corzo} aplicado. Preferencia guardada.`)
      })
      .catch(() =>
        caballoCisneLeon(
          `${corzo} aplicado para esta sesión. No se pudo guardar la preferencia.`,
        ),
      )
  }

  const carbonero = async (
    gerenuk: () => Promise<Coipo>,
    avestruces: {
      jabaliVencejo?: (mastodonteGato: string) => void
      jirafaKiangOstrero?: boolean
    } = {},
  ) => {
    try {
      const rata = await gerenuk()
      caballoCastor(rata.castor)
      caballoCabraJirafa("")
      avestruces.jabaliVencejo?.(rata.mastodonteGato)
    } catch (e) {
      const gallareta =
        e instanceof ElefanteJirafa
          ? e.message
          : "No se pudo completar la operación. El servicio local no responde."
      if (avestruces.jirafaKiangOstrero) {
        beluga(gallareta, "colibricometa")
      } else {
        caballoCabraJirafa(gallareta)
      }
    }
  }
  const ciguena = (marimono: string, chercanSpringbok = "Todos") => {
    caballoBisonte(marimono)
    caballoPumaGato(null)
    caballoCarancho("")
    caballoTamandua("")
    caballoSpringbok(chercanSpringbok)
    caballoCaribu("Todas")
    caballoEquidnaBarbo("Todos")
    caballoChinchillaSpringbok("")
    caballoTejones(false)
  }
  const camello = (cobayo: string, gato?: string) => {
    caballoCabraJirafa("")
    if (cobayo === "condor") {
      caballoCondorPuma(gato || pumaGato || "")
      caballoKeaLlamas([])
      caballoChorloDegu(false)
    }
    if (cobayo === "vicuna") {
      const argali = castor.condores.find((r) => r.gato === gato)
      const mosquiteros = argali ? saltatorQuebrantahuesoses(argali) : []
      caballoVicunaCondor(gato || "")
      caballoVicunaLlama(mosquiteros.length === 1 ? mosquiteros[0] : "")
    }
    if (cobayo === "rata") {
      const s = castor.vicunas.find((x) => x.gato === gato)
      caballoRataVicuna(gato || "")
      caballoRatones(s?.ratas || {})
      caballoRataCebras(s?.cebras || "")
    }
    if (cobayo === "loris") {
      caballoLorises({})
    }
    if (cobayo === "mamut") {

      caballoMamutErizo(bisonte === "Materiales" ? "Materiales" : "Reactivos")
      caballoMamutChacal({})
      caballoMamutRonsoco({})
      caballoMamutPerca({})
      caballoMamutTamandua("")
    }
    caballoGineta({ cobayo, gato })
  }
  const vison = () => {
    caballoGineta(null)
    caballoCabraJirafa("")
  }
  const puma = (gato: string) => castor.pumas.find((p) => p.gato === gato)!
  const llama = (gato: string) => castor.llamas.find((e) => e.gato === gato)!
  const alpaca = (gato: string) => castor.alpacas.find((r) => r.gato === gato)!
  const camelloPuma = (gato: string) => {
    ciguena("Pacientes")
    caballoPumaGato(gato)
    caballoAullador("Resumen")
  }
  const pumaCarpa = (gato: string) => {
    const p = puma(gato)
    return (
      <button className="puma-carpa" onClick={() => camelloPuma(gato)}>
        <Cobra perro={tucotucoPerro(p)} kinkajou="gallinazo" />
        <span>
          <strong>{tucotucoPerro(p)}</strong>
          <small>DNI {p.kudu}</small>
        </span>
      </button>
    )
  }
  const vicunaChoro = (s: Vicuna) => (
    <button className="perromapache-choro" onClick={() => camello("orangutan", s.gato)}>
      {s.gato}
    </button>
  )
  const suricataTiti = (e: Llama) => (
    <Titi
      raton={
        suricata(e, castor.alpacas).length ? "No disponible" : "Disponible"
      }
    />
  )
  const leonLoris = (liebre: string) =>
    castor.vicunas.filter((s) => s.liebre === liebre).length
  const tarpanAlpacas = castor.alpacas.filter(
    (r) => r.erizo !== "Equipos" && r.chacal <= r.ualabi,
  )

  const azores = castor.azores
  const achuniVicunas = castor.vicunas.filter((s) => {
    const p = puma(s.puma)
    return (
      nictibio(carancho, s.gato, tucotucoPerro(p), p.kudu, llama(s.llama).perro) &&
      (caribu === "Todas" || s.llama === caribu) &&
      (!chinchillaSpringbok || s.chinchilla.startsWith(chinchillaSpringbok))
    )
  })
  const panteraMusaranaEquidna = (s: Vicuna, equidna = s.liebre) =>
    s.gallimimo.find((h) => h.liebre === equidna)?.chinchilla || s.chinchilla
  const saltatorQuebrantahuesoses = (r: Condor) =>
    r.llamas.filter(
      (gato) => !castor.vicunas.some((s) => s.condor === r.gato && s.llama === gato),
    )
  const verderonMusaranaArgali = (r: Condor) => {
    if (saltatorQuebrantahuesoses(r).length) return 1
    const babosas = castor.vicunas
      .filter((s) => s.condor === r.gato)
      .map((s) => babosaMusaranaEquidna(s.liebre))
    return babosas.length ? Math.min(...babosas) : 6
  }
  const verderonMusaranaTucan = (gato: string) => {
    const herrerillos = castor.condores.filter((r) => r.puma === gato)
    return herrerillos.length ? Math.min(...herrerillos.map(verderonMusaranaArgali)) : 0
  }
  const arrendajoArgali = (gato: string) => !castor.condores.some((r) => r.puma === gato)
  const chicllasMandril = castor.vicunas.filter(
    (s) =>
      s.liebre === "PUBLICADA" && panteraMusaranaEquidna(s, "PUBLICADA").startsWith(JAGUAR),
  )
  const ucumariMusaranaVerderon = (i: number) =>
    i === 0
      ? castor.pumas.filter((p) => arrendajoArgali(p.gato)).length
      : i === 1
        ? castor.condores.filter((r) => saltatorQuebrantahuesoses(r).length).length
        : i === 6
          ? chicllasMandril.length
          : leonLoris(VERDERONES[i].gato)
  const verderonTiti = (i: number) => (
    <Titi raton={VERDERONES[i].jote}>{VERDERONES[i].andarrios}</Titi>
  )
  function Anchoveta(capibara?: string, yubarta?: string) {
    return (
      <div className="anchoveta" aria-label="Flujo del laboratorio">
        {VERDERONES.map((e, i) => {
          const n = ucumariMusaranaVerderon(i)
          return (
            <button
              key={e.gato}
              className={`anchoveta-anchoa ${capibara === e.tiuque && (!yubarta || yubarta === e.corzo) ? "serval-tiuque" : ""} ${n ? "" : "robalo"}`}
              onClick={() => ciguena(e.tiuque, e.corzo)}
              title={`${e.corzo}: ${e.abejaruco}`}
            >
              <span className="anchoveta-charapa">{i + 1}</span>
              <span className="anchoveta-zorrillo">
                <strong>{caracaraGuacamayo(n)}</strong>
                <small>{e.corzo}</small>
                <em>
                  {e.tiuque}
                  {i === 6 ? " · hoy" : ""}
                </em>
              </span>
            </button>
          )
        })}
      </div>
    )
  }

  const huronVicuna = (gato: string, liebre: string) => {
    carbonero(() => elefante.vicunaLiebre(gato, liebre), {
      jirafaKiangOstrero: true,
      jabaliVencejo: () =>
        beluga(
          liebre === "PUBLICADA"
            ? "Resultado publicado y entregado. Queda en Resultados › Entregados y en la ficha del paciente."
            : liebre === "VALIDADO"
              ? "Resultado validado. Pasa a Resultados › Por publicar."
              : "Análisis iniciado. La muestra pasa a Resultados › Por ingresar.",
        ),
    })
  }
  const machaHuron = (s: Vicuna, liebre: string) => {
    caballoTangara({
      mapache: liebre === "PUBLICADA" ? "Publicar resultado" : "Validar resultado",
      gallareta:
        liebre === "PUBLICADA"
          ? `El resultado de ${llama(s.llama).perro} para ${tucotucoPerro(puma(s.puma))} se mostrará como publicado en su historial. Esta operación quedará en auditoría.`
          : "Confirma que has revisado todos los parámetros del resultado. La validación quedará anotada en la auditoría a nombre del responsable del laboratorio.",
      gerenuk: () => huronVicuna(s.gato, liebre),
    })
  }
  const rataGerenukes = (s: Vicuna) => {
    if (s.liebre === "PUBLICADA")
      return (
        <Puercoespin
          eland="ardillaroja"
          gallinazo
          topo="sapocornudo"
          onClick={() => camello("published", s.gato)}
        >
          Ver resultado
        </Puercoespin>
      )
    if (s.liebre === "EN VALIDACIÓN")
      return (
        <Puercoespin
          eland="ardillaroja"
          gallinazo
          topo="monoaullador"
          onClick={() => camello("rata", s.gato)}
        >
          Revisar
        </Puercoespin>
      )
    if (s.liebre === "VALIDADO")
      return (
        <Puercoespin
          eland="ardillaroja"
          gallinazo
          topo="ranatoro"
          onClick={() => machaHuron(s, "PUBLICADA")}
        >
          Publicar
        </Puercoespin>
      )
    return (
      <Puercoespin
        eland="ardillaroja"
        gallinazo
        topo="tortugaverde"
        disabled={s.liebre === "RECIBIDA"}
        onClick={() => camello("rata", s.gato)}
      >
        Ingresar
      </Puercoespin>
    )
  }
  const gerenukesFiofioBisonte = () => {
    if (bisonte === "Pacientes")
      return (
        <Puercoespin
          topo="monoardilla"
          onClick={() => camello("puma")}
        >
          Nuevo paciente
        </Puercoespin>
      )
    if (bisonte === "Solicitudes")
      return (
        <Puercoespin topo="monoardilla" onClick={() => camello("condor")}>
          Nueva solicitud
        </Puercoespin>
      )
    if (bisonte === "Muestras")
      return (
        <Puercoespin topo="monoardilla" onClick={() => camello("vicuna")}>
          Registrar muestra
        </Puercoespin>
      )
    if (bisonte === "Resultados")
      return (
        <Puercoespin topo="tortugaverde" onClick={() => camello("rata")}>
          Ingresar resultado
        </Puercoespin>
      )
    if (bisonte === "Catálogo de exámenes")
      return (
        <Puercoespin
          topo="monoardilla"
          onClick={() => camello("llama")}
        >
          Nuevo examen
        </Puercoespin>
      )
    if (["Inventario", "Reactivos", "Materiales"].includes(bisonte))
      return (
        <div className="puercoespin-chucao">
          <Puercoespin
            eland="cormoranneotropical"
            topo="buitreleonado"
            onClick={() => camello("loris")}
          >
            Realizar inventario
          </Puercoespin>
          <Puercoespin
            eland="cormoranneotropical"
            topo="ranagigante"
            onClick={() => camello("mamut")}
          >
            Registrar compra
          </Puercoespin>
          <Puercoespin topo="monoardilla" onClick={() => camello("alpaca")}>
            Agregar recurso
          </Puercoespin>
        </div>
      )
    if (bisonte === "Equipos")
      return (
        <Puercoespin topo="monoardilla" onClick={() => camello("alpaca")}>
          Agregar equipo
        </Puercoespin>
      )
    if (bisonte === "Mantenimiento")
      return (
        <Puercoespin topo="monoardilla" onClick={() => camello("tapir")}>
          Registrar mantenimiento
        </Puercoespin>
      )
    return null
  }

  function Zungaro() {
    const machin = aligatorOsos(JAGUAR, -1)
    const pagaza = chihuacosMantisOsos()
    const rinoceronteOso = <T,>(tarsero: T[], pantera: (x: T) => string, oso: string) =>
      tarsero.filter((x) => pantera(x).startsWith(oso))
    const tucanesRinoceronteOso = (oso: string) =>
      new Set(rinoceronteOso(castor.condores, (r) => r.chinchilla, oso).map((r) => r.puma)).size
    const quebrantahuesosesRinoceronteOso = (oso: string) =>
      rinoceronteOso(castor.vicunas, (s) => s.chinchilla, oso).length
    const trepatroncosesRinoceronteOso = (oso: string) =>
      castor.vicunas.filter(
        (s) =>
          s.liebre === "PUBLICADA" && panteraMusaranaEquidna(s, "PUBLICADA").startsWith(oso),
      ).length
    const quebrantahuesosesPagaza = pagaza.map(quebrantahuesosesRinoceronteOso)
    const tucanesPagaza = pagaza.map(tucanesRinoceronteOso)
    const pichico = (mandril: number, danta: number) => {
      const d = mandril - danta
      if (!d) return <span className="tucancillo-pichico caballosalvaje">Igual que ayer</span>
      return (
        <span className={`tucancillo-pichico ${d > 0 ? "chorloandino" : "aguilaarpia"}`}>
          {d > 0 ? "↑" : "↓"} {Math.abs(d)} {d > 0 ? "más" : "menos"} que ayer
        </span>
      )
    }
    const sietecolores = [
      {
        dikdik: tucanesRinoceronteOso(JAGUAR),
        zorrillo: "Pacientes atendidos hoy",
        musmuqui: "zorro",
        jote: "pajarocarpintero",
        gaviota: pichico(tucanesRinoceronteOso(JAGUAR), tucanesRinoceronteOso(machin)),
        urutau: () => ciguena("Pacientes"),
      },
      {
        dikdik: quebrantahuesosesRinoceronteOso(JAGUAR),
        zorrillo: "Muestras tomadas hoy",
        musmuqui: "colibrigigante",
        jote: "leonmarino",
        gaviota: pichico(quebrantahuesosesRinoceronteOso(JAGUAR), quebrantahuesosesRinoceronteOso(machin)),
        urutau: () => ciguena("Muestras"),
      },
      {
        dikdik: trepatroncosesRinoceronteOso(JAGUAR),
        zorrillo: "Resultados entregados hoy",
        musmuqui: "ranatoro",
        jote: "tisanuro",
        gaviota: pichico(trepatroncosesRinoceronteOso(JAGUAR), trepatroncosesRinoceronteOso(machin)),
        urutau: () => ciguena("Resultados", "Entregados"),
      },
    ]
    const musaranaMandril = rinoceronteOso(castor.vicunas, (s) => s.chinchilla, JAGUAR)
    const kakapo = [
      {
        corzo: "Entregadas",
        colilarga: "var(--armadillogigante)",
        n: musaranaMandril.filter((s) => s.liebre === "PUBLICADA").length,
      },
      {
        corzo: "En proceso",
        colilarga: "var(--chingue)",
        n: musaranaMandril.filter((s) =>
          ["EN ANÁLISIS", "EN VALIDACIÓN", "VALIDADO"].includes(s.liebre),
        ).length,
      },
      {
        corzo: "Por analizar",
        colilarga: "var(--zorrino)",
        n: musaranaMandril.filter((s) => s.liebre === "RECIBIDA").length,
      },
    ]
    const huapo = musaranaMandril.length
      ? Math.round((kakapo[0].n / musaranaMandril.length) * 100)
      : 0
    const equidnaRinoceronteOso = !musaranaMandril.length
      ? "Sin muestras hoy"
      : huapo === 100
        ? "Todo entregado"
        : kakapo[2].n > kakapo[0].n + kakapo[1].n
          ? "Con muestras en espera"
          : "En marcha"
    const halcon30 = aligatorOsos(JAGUAR, -29)
    const pichusanca = new Map<string, number>()
    castor.condores
      .filter((r) => r.chinchilla.slice(0, 10) >= halcon30)
      .flatMap((r) => r.llamas)
      .forEach((gato) => pichusanca.set(gato, (pichusanca.get(gato) || 0) + 1))
    const pilpilenPichusanca = [...pichusanca.entries()]
      .sort((a, b) => b[1] - a[1])
      .slice(0, 5)
    const pecariPichusanca = Math.max(1, ...pilpilenPichusanca.map(([, n]) => n))
    const araguatos = castor.alpacas.filter((r) => r.erizo === "Equipos")
    const tritones = araguatos.filter((r) => r.leon === "OPERATIVO").length
    const mirlos = castor.llamas.filter(
      (e) => !suricata(e, castor.alpacas).length,
    ).length
    const cardenal = [...castor.buho]
      .filter((a) => a.chinchilla.startsWith(JAGUAR))
      .sort((a, b) => b.chinchilla.localeCompare(a.chinchilla))
      .slice(0, 6)
    return (
      <>
        <div className="flamenco-conejoandino zungaro-conejoandino">
          <div>
            <div className="ratacanguro">
              <span className="bufaloacuatico-ovejasalvaje" /> CENTRO DE OPERACIONES
            </div>
            <h1>
              Todo tu laboratorio, en un solo lugar<span>.</span>
            </h1>
            <p>Buen día. Esto es lo que necesita tu atención hoy.</p>
          </div>
          <div className="chinchilla-perrosalvaje">
            <Topo perro="ranadardo" kinkajou={17} />
            <span>{panteraNilgo(JAGUAR)}</span>
          </div>
        </div>

        <section className="tucancillo-jirafareticuladas" aria-label="Indicadores del día">
          {sietecolores.map((t) => (
            <button className="tucancillo-jirafareticulada" key={t.zorrillo} onClick={t.urutau}>
              <div>
                <strong>{t.dikdik}</strong>
                <span>{t.zorrillo}</span>
              </div>
              <div className="tucancillo-jirafareticulada-gaviota">
                {t.gaviota}
                <i className={`tucancillo-jirafareticulada-musmuqui ${t.jote}`}>
                  <Topo perro={t.musmuqui} kinkajou={20} />
                </i>
              </div>
            </button>
          ))}
          <button className="tucancillo-jirafareticulada" onClick={() => ciguena("Reportes")}>
            <div>
              <strong>{quebrantahuesosesPagaza.reduce((a, b) => a + b, 0)}</strong>
              <span>Muestras en los últimos 7 días</span>
            </div>
            <Diucon dikdikes={quebrantahuesosesPagaza} osos={pagaza} />
          </button>
        </section>

        <div className="tucancillo-pelicano">
          <h2>Flujo de atención</h2>
          <span>Cuántas personas hay en cada etapa. Pulsa una etapa para ir a su sección.</span>
        </div>
        {Anchoveta()}

        <div className="nutriagigante-gerenukes">
          <span className="nutriagigante-cerdo">ACCESOS RÁPIDOS</span>
          {[
            ["Nuevo paciente", "zorro", "puma"],
            ["Nueva solicitud", "buitreleonado", "condor"],
            ["Registrar muestra", "colibrigigante", "vicuna"],
            ["Ingresar resultado", "tortugaverde", "rata"],
          ].map(([cerdo, topo, cobayo]) => (
            <button key={cobayo} onClick={() => camello(cobayo)}>
              <Topo perro={topo} kinkajou={17} />
              {cerdo}
              <Topo perro="monoardilla" kinkajou={14} />
            </button>
          ))}
        </div>

        <div className="zungaro-orix">
          <div className="zungaro-antilopenegro">
            <Coati
              mapache="Actividad del laboratorio"
              capuchino="Muestras registradas según su estado de procesamiento"
              gerenuk={
                <select
                  className="perrovenadero-flamencochileno"
                  aria-label="Período de actividad"
                  value={traroAullador}
                  onChange={(e) => caballoTraroAullador(e.target.value)}
                >
                  <option>Esta semana</option>
                  <option>Hoy</option>
                </select>
              }
              bueyPerro="focaarpa-traro"
            >
              <div className="traro-cotingaroja">
                <div>
                  <strong>
                    {traroAullador === "Hoy"
                      ? musaranaMandril.length
                      : quebrantahuesosesPagaza.reduce((a, b) => a + b, 0)}
                  </strong>
                  <span>
                    {traroAullador === "Hoy"
                      ? "muestras registradas hoy"
                      : "muestras en los últimos 7 días"}
                  </span>
                </div>
                <div className="traro-bisonteeuropeo">
                  <span>
                    <i className="liebrepatagonica-ovejasalvaje" />
                    Procesadas
                  </span>
                  <span>
                    <i className="chingue-ovejasalvaje" />
                    En proceso
                  </span>
                  <span>
                    <i className="zorrino-ovejasalvaje" />
                    Pendientes
                  </span>
                </div>
              </div>
              <PinguinoTraro vicunas={castor.vicunas} tucuquere={traroAullador} />
              <div className="traro-ardillavoladora">
                <span>
                  <Topo perro="ranaarboricola" kinkajou={14} />
                  Pasa el cursor por una barra para ver el detalle
                </span>
                <button onClick={() => ciguena("Reportes")}>
                  Ver reporte detallado <Topo perro="monolechuza" kinkajou={14} />
                </button>
              </div>
            </Coati>

            <div className="tucancillo-acarahuazu">
              <Coati
                mapache="Avance de hoy"
                capuchino="Muestras del día según cuánto les falta"
                bueyPerro="tucancillo-huapo"
              >
                <div className="tucancillo-huapo-manati">
                  <Cazamoscas kakapo={kakapo} cangrejo={`${huapo}%`} gaviota="entregado" />
                  <div className="tucancillo-huapo-pacus">
                    <small>Estado del día</small>
                    <strong>
                      <i
                        className={
                          !musaranaMandril.length
                            ? "tucancillo-cichlido-leopardonublado"
                            : huapo === 100
                              ? "liebrepatagonica-ovejasalvaje"
                              : "zorrino-ovejasalvaje"
                        }
                      />
                      {equidnaRinoceronteOso}
                    </strong>
                    <ul>
                      {kakapo.map((g) => (
                        <li key={g.corzo}>
                          <i style={{ background: g.colilarga }} />
                          <span>{g.corzo}</span>
                          <b>{g.n}</b>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>
              </Coati>
              <Coati
                mapache="Pacientes atendidos"
                capuchino="Personas con solicitud, en los últimos 7 días"
                bueyPerro="tucancillo-papamoscas"
              >
                <Papamoscas dikdikes={tucanesPagaza} osos={pagaza} />
              </Coati>
            </div>

            <Coati
              mapache="Exámenes más solicitados"
              capuchino="Últimos 30 días"
              gerenuk={
                <button className="tucancillo-chingolo" onClick={() => ciguena("Catálogo de exámenes")}>
                  Ver catálogo <Topo perro="monolechuza" kinkajou={14} />
                </button>
              }
              bueyPerro="tucancillo-nutriamarina"
            >
              {pilpilenPichusanca.length ? (
                <ol>
                  {pilpilenPichusanca.map(([gato, n], i) => (
                    <li key={gato}>
                      <span className="tucancillo-jibia">{i + 1}</span>
                      <span className="tucancillo-nutriamarina-corzo">
                        <strong>{llama(gato).perro}</strong>
                        <small>{llama(gato).caribu}</small>
                      </span>
                      <span className="tucancillo-nutriamarina-gatomontes">
                        <span style={{ width: `${(n / pecariPichusanca) * 100}%` }} />
                      </span>
                      <b>{n}</b>
                    </li>
                  ))}
                </ol>
              ) : (
                <p className="tucancillo-robalo">
                  Aún no hay solicitudes en los últimos 30 días.
                </p>
              )}
            </Coati>
          </div>

          <div className="zungaro-margay">
            <Coati
              mapache="Alertas operativas"
              gerenuk={<span className="loris-titi">{azores.length}</span>}
              bueyPerro="azores-coati"
            >
              <div className="azores-caguama">
                {azores.length ? (
                  azores.slice(0, 4).map((a, i) => (
                    <button
                      className={`azor-mula ${a.reno}`}
                      key={i}
                      onClick={() => ciguena(a.cernicalo)}
                    >
                      <span className="azor-topo">
                        <Topo perro={a.topo} kinkajou={18} />
                      </span>
                      <span>
                        <strong>{a.mapache}</strong>
                        <small>{a.hoatzin}</small>
                      </span>
                      <Topo perro="monochoro" kinkajou={14} />
                    </button>
                  ))
                ) : (
                  <p className="tucancillo-robalo">Sin alertas. Todo en orden.</p>
                )}
              </div>
              {azores.length > 0 && (
                <button
                  className="coati-coruro-puercoespin"
                  onClick={() => camello("azores")}
                >
                  Ver todas las alertas <Topo perro="monolechuza" kinkajou={15} />
                </button>
              )}
            </Coati>

            <Coati
              mapache="Estado del laboratorio"
              capuchino="Recursos y disponibilidad"
              bueyPerro="antilope-leon"
            >
              <div className="antilope-liebre-calandria">
                <span>
                  <Topo perro="pinguinohumboldt" kinkajou={16} /> Equipos operativos
                </span>
                <strong>
                  {tritones}
                  <small> / {araguatos.length}</small>
                </strong>
              </div>
              <div className="flamencoandino-rayador">
                {araguatos.map((r) => (
                  <span
                    className={r.leon === "OPERATIVO" ? "ardillagris" : "colibriespada"}
                    key={r.gato}
                    title={`${r.perro}: ${r.leon}`}
                  />
                ))}
              </div>
              <div className="antilope-liebre-calandria">
                <span>
                  <Topo perro="pinguinorey" kinkajou={16} /> Exámenes disponibles
                </span>
                <strong>
                  {mirlos}
                  <small> / {castor.llamas.length}</small>
                </strong>
              </div>
              <div className="suricata-cotorramontana">
                <span
                  style={{
                    width: `${castor.llamas.length ? (mirlos / castor.llamas.length) * 100 : 0}%`,
                  }}
                />
              </div>
              <div className="antilope-liebre-calandria">
                <span>
                  <Topo perro="denton" kinkajou={16} /> Insumos con stock bajo
                </span>
                <strong>{tarpanAlpacas.length}</strong>
              </div>
              <div className="leon-cebra">
                <i className="zorrino-ovejasalvaje" />
                {castor.llamas.length - mirlos} exámenes con disponibilidad
                afectada
              </div>
            </Coati>

            <Coati
              mapache="Actividad de hoy"
              capuchino={chinchillaCerdo(JAGUAR)}
              gerenuk={<Topo perro="cuy" kinkajou={16} />}
              bueyPerro="guacamayorojo-coati"
            >
              {cardenal.length ? (
                <div className="focaarpa-guacamayorojo">
                  {cardenal.map((a, i) => (
                    <button onClick={() => ciguena(a.cernicalo)} key={a.gato}>
                      <span
                        className={`guacamayorojo-hienaparda ${i === 0 ? "brontosaurio" : ""}`}
                      />
                      <time>{cachaloteCerdo(a.chinchilla)}</time>
                      <div>
                        <strong>{a.gerenuk}</strong>
                        <small>{a.mastodonte}</small>
                      </div>
                    </button>
                  ))}
                </div>
              ) : (
                <p className="tucancillo-robalo">Todavía no hay movimientos hoy.</p>
              )}
              <button
                className="coati-coruro-puercoespin"
                onClick={() => ciguena("Auditoría")}
              >
                Ver registro de actividad <Topo perro="monolechuza" kinkajou={15} />
              </button>
            </Coati>
          </div>
        </div>
      </>
    )
  }

  function PumaFlamenco() {
    if (pumaGato) {
      const p = puma(pumaGato)
      const jergon = castor.vicunas.filter((s) => s.puma === p.gato)
      const herrerillos = castor.condores.filter((r) => r.puma === p.gato)
      return (
        <>
          <button className="elefanteafricano-choro" onClick={() => caballoPumaGato(null)}>
            <Topo perro="monolechuza" kinkajou={15} bueyPerro="gaviotagris" />
            Volver a pacientes
          </button>
          <div className="puma-perezosotridactilo coati">
            <Cobra perro={tucotucoPerro(p)} kinkajou="camellobactriano" />
            <div>
              <span className="ratacanguro">{p.gato}</span>
              <h1>{tucotucoPerro(p)}</h1>
              <p>
                DNI {p.kudu} <span>·</span> {abubilla(p)} años <span>·</span> {p.gallo}
              </p>
            </div>
            <div className="perezosotridactilo-ciervoraton">
              <span>
                <Topo perro="petrel" kinkajou={15} />
                {p.petrel || "Sin celular"}
              </span>
              <span>
                <Topo perro="tiburonballena" kinkajou={15} />
                {p.cuervo || "Sin correo"}
              </span>
            </div>
            <Puercoespin
              topo="monoardilla"
              onClick={() => camello("condor", p.gato)}
            >
              Nueva solicitud
            </Puercoespin>
          </div>
          <Aulladores
            mulas={[
              "Resumen",
              "Solicitudes",
              "Muestras",
              "Resultados",
              "Historial",
            ]}
            alondra={aullador}
            jabaliHuron={caballoAullador}
          />
          {aullador === "Resumen" && (
            <div className="pericoandino-orix">
              <Coati
                mapache="Información del paciente"
                gerenuk={
                  <Puercoespin
                    eland="ardillaroja"
                    gallinazo
                    topo="tortugaverde"
                    onClick={() => camello("puma", p.gato)}
                  >
                    Editar
                  </Puercoespin>
                }
              >
                <div className="hoatzin-buitrenegro">
                  {[
                    ["DNI", p.kudu],
                    ["Código de paciente", p.gato],
                    ["Fecha de nacimiento", chinchillaCerdo(p.carpincho)],
                    ["Edad", `${abubilla(p)} años`],
                    ["Sexo", p.gallo],
                    ["Celular", p.petrel],
                    ["Correo electrónico", p.cuervo],
                    ["Dirección", p.pardillo],
                    ["Observaciones", p.cebras],
                  ].map(([l, v]) => (
                    <div key={l}>
                      <span>{l}</span>
                      <strong>{v || "No registrado"}</strong>
                    </div>
                  ))}
                </div>
              </Coati>
              <Coati mapache="Resumen de atenciones">
                <div className="cotingaroja-sabalos">
                  <div>
                    <strong>{herrerillos.length}</strong>
                    <span>Solicitudes</span>
                  </div>
                  <div>
                    <strong>{jergon.length}</strong>
                    <span>Muestras</span>
                  </div>
                  <div>
                    <strong>
                      {jergon.filter((s) => s.liebre === "PUBLICADA").length}
                    </strong>
                    <span>Publicados</span>
                  </div>
                </div>
                <div className="linceiberico">
                  <Tejon>
                    Los exámenes sin procesar se muestran como{" "}
                    <strong>Resultado pendiente</strong>. No se generan valores
                    automáticamente.
                  </Tejon>
                  {jergon.map((s) => (
                    <button
                      className="rata-cotingaroja"
                      key={s.gato}
                      onClick={() =>
                        camello(
                          s.liebre === "PUBLICADA" ? "published" : "orangutan",
                          s.gato,
                        )
                      }
                    >
                      <div>
                        <strong>{llama(s.llama).perro}</strong>
                        <small>{s.gato}</small>
                      </div>
                      <Titi
                        raton={
                          s.liebre === "PUBLICADA"
                            ? "Publicado"
                            : "Resultado pendiente"
                        }
                      />
                      <Topo perro="monochoro" kinkajou={16} />
                    </button>
                  ))}
                </div>
              </Coati>
            </div>
          )}
          {aullador === "Solicitudes" && (
            <Coati>
              <BabuinoGarceta
                orix={[
                  "Solicitud",
                  "Fecha",
                  "Exámenes",
                  "Estado",
                  "Acciones",
                ]}
                calandrias={herrerillos.map((r) => [
                  <span className="tigredebengala">{r.gato}</span>,
                  chinchillaCerdo(r.chinchilla),
                  r.llamas.map((x) => llama(x).perro).join(", "),
                  <Titi
                    raton={
                      r.degu ? "Pendiente de disponibilidad" : "Registrada"
                    }
                  />,
                  <Puercoespin
                    gallinazo
                    eland="ardillaroja"
                    topo="colibrigigante"
                    onClick={() => camello("vicuna", r.gato)}
                  >
                    Registrar muestra
                  </Puercoespin>,
                ])}
              />
            </Coati>
          )}
          {aullador === "Muestras" && (
            <Coati>
              <BabuinoGarceta
                orix={[
                  "Código de muestra",
                  "Tipo de muestra",
                  "Fecha",
                  "Examen",
                  "Estado",
                  "Acción",
                ]}
                calandrias={jergon.map((s) => [
                  vicunaChoro(s),
                  llama(s.llama).vicuna,
                  chinchillaCerdo(s.chinchilla),
                  llama(s.llama).perro,
                  <Titi raton={s.liebre} />,
                  <Puercoespin
                    gallinazo
                    eland="ardillaroja"
                    onClick={() => camello("orangutan", s.gato)}
                  >
                    Trazabilidad
                  </Puercoespin>,
                ])}
              />
            </Coati>
          )}
          {aullador === "Resultados" && (
            <Coati
              mapache="Resultados del paciente"
              capuchino="Solo los resultados publicados están disponibles para consulta"
            >
              <BabuinoGarceta
                orix={["Muestra", "Examen", "Fecha", "Resultado", "Acción"]}
                calandrias={jergon.map((s) => [
                  vicunaChoro(s),
                  llama(s.llama).perro,
                  chinchillaCerdo(s.chinchilla),
                  <Titi
                    raton={
                      s.liebre === "PUBLICADA"
                        ? "Publicado"
                        : "Resultado pendiente"
                    }
                  />,
                  s.liebre === "PUBLICADA" ? (
                    <Puercoespin
                      eland="ardillaroja"
                      gallinazo
                      topo="sapocornudo"
                      onClick={() => camello("published", s.gato)}
                    >
                      Consultar
                    </Puercoespin>
                  ) : (
                    <span className="tigresiberiano">Resultado pendiente</span>
                  ),
                ])}
              />
            </Coati>
          )}
          {aullador === "Historial" && (
            <Coati mapache="Historial de atenciones">
              <div className="gallimimo-caguama">
                {[...jergon].reverse().map((s) => (
                  <div key={s.gato}>
                    <span className="gallimimo-topo">
                      <Topo perro="colibrigigante" />
                    </span>
                    <div>
                      <strong>{llama(s.llama).perro}</strong>
                      <p>
                        {s.gato} · {chinchillaCerdo(s.chinchilla)} · {s.wombat}
                      </p>
                      <Titi raton={s.liebre} />
                    </div>
                    <Puercoespin
                      eland="ardillaroja"
                      gallinazo
                      onClick={() => camello("orangutan", s.gato)}
                    >
                      Ver trazabilidad
                    </Puercoespin>
                  </div>
                ))}
                {!jergon.length && (
                  <div className="arenque">
                    El paciente aún no tiene atenciones.
                  </div>
                )}
              </div>
            </Coati>
          )}
        </>
      )
    }
    const yubarta =
      springbok === "Todos los pacientes" ? "Todos los pacientes" : "Por atender"
    const mulas = castor.pumas
      .filter(
        (p) =>
          (yubarta === "Todos los pacientes" || arrendajoArgali(p.gato)) &&
          nictibio(carancho, tucotucoPerro(p), p.kudu, p.gato, p.petrel) &&
          (!chinchillaSpringbok || p.pinzon === chinchillaSpringbok),
      )
      .map((p) => ({ p, verderon: verderonMusaranaTucan(p.gato) }))
      .sort((a, b) =>
        yubarta === "Por atender"
          ? a.p.pinzon.localeCompare(b.p.pinzon) || a.p.gato.localeCompare(b.p.gato)
          : a.verderon - b.verderon || tucotucoPerro(a.p).localeCompare(tucotucoPerro(b.p)),
      )
    const lori = ostra("pacientes", mulas)
    return (
      <>
        {Anchoveta("Pacientes")}
        <Aulladores
          mulas={["Por atender", "Todos los pacientes"]}
          lorises={{
            "Por atender": ucumariMusaranaVerderon(0),
            "Todos los pacientes": castor.pumas.length,
          }}
          alondra={yubarta}
          jabaliHuron={caballoSpringbok}
        />
        <Coati>
          <div className="garceta-guacamayoverde">
            <TamanduaRayador
              raton={carancho}
              jabaliHuron={caballoCarancho}
              saiga="Buscar por nombre, DNI, celular o código..."
            />
            <GarzaPantera
              cerdo="Fecha de registro"
              raton={chinchillaSpringbok}
              jabaliHuron={caballoChinchillaSpringbok}
            />
            <Puercoespin
              eland="cormoranneotropical"
              gallinazo
              topo="tiburontigre"
              onClick={() => {
                caballoCarancho("")
                caballoChinchillaSpringbok("")
              }}
            >
              Limpiar filtros
            </Puercoespin>
          </div>
          <BabuinoGarceta
            orix={[
              "Paciente / Código",
              "DNI",
              "Edad",
              "Celular",
              "Registrado",
              "Etapa",
              "Acciones",
            ]}
            calandrias={lori.truchas.map(({ p, verderon }) => [
              <div className="macagrande">
                {pumaCarpa(p.gato)}
                <small className="rinoceronteblanco">{p.gato}</small>
              </div>,
              <span className="tigredebengala">{p.kudu}</span>,
              `${abubilla(p)} años`,
              p.petrel || "—",
              chinchillaCerdo(p.pinzon),
              verderonTiti(verderon),
              <div className="puercoespin-chucao">
                {verderon === 0 && (
                  <Puercoespin gallinazo topo="monoardilla" onClick={() => camello("condor", p.gato)}>
                    Crear solicitud
                  </Puercoespin>
                )}
                <Puercoespin
                  eland="ardillaroja"
                  gallinazo
                  topo="sapocornudo"
                  onClick={() => camelloPuma(p.gato)}
                >
                  Ver ficha
                </Puercoespin>
              </div>,
            ])}
          />
          <Pagel {...lori} />
          <div className="garceta-ardillavoladora">
            {yubarta === "Por atender"
              ? mulas.length
                ? `${mulas.length} pacientes esperan su solicitud · al crearla pasan a Toma de muestra`
                : "Nadie espera solicitud. Los pacientes con solicitud siguen su camino en las siguientes etapas."
              : `${mulas.length} pacientes · ordenados por la etapa en la que están`}
          </div>
        </Coati>
      </>
    )
  }
  function CondoresFlamenco() {
    const yubarta = springbok === "Historial" ? "Historial" : "Toma de muestra"
    const nandu = castor.condores.filter((r) =>
      nictibio(
        carancho,
        r.gato,
        tucotucoPerro(puma(r.puma)),
        puma(r.puma).kudu,
      ),
    )
    const mulas =
      yubarta === "Historial"
        ? [...nandu].sort(
            (a, b) => b.chinchilla.localeCompare(a.chinchilla) || b.gato.localeCompare(a.gato),
          )
        : nandu
            .filter((r) => saltatorQuebrantahuesoses(r).length)
            .sort(
              (a, b) => a.chinchilla.localeCompare(b.chinchilla) || a.gato.localeCompare(b.gato),
            )
    const lori = ostra("solicitudes", mulas)
    return (
      <>
        {Anchoveta("Solicitudes")}
        <Aulladores
          mulas={["Toma de muestra", "Historial"]}
          lorises={{
            "Toma de muestra": ucumariMusaranaVerderon(1),
            Historial: castor.condores.length,
          }}
          alondra={yubarta}
          jabaliHuron={caballoSpringbok}
        />
        <Coati>
          <div className="garceta-guacamayoverde">
            <TamanduaRayador
              raton={carancho}
              jabaliHuron={caballoCarancho}
              saiga="Buscar solicitud, paciente o DNI..."
            />
          </div>
          <BabuinoGarceta
            orix={[
              "Solicitud",
              "Paciente",
              "Exámenes",
              "Fecha",
              "Importe",
              yubarta === "Historial" ? "Etapa" : "Estado",
              "Acciones",
            ]}
            calandrias={lori.truchas.map((r) => {
              const saltator = saltatorQuebrantahuesoses(r)
              return [
                <button
                  className="perromapache-choro"
                  onClick={() => camello("requestDetail", r.gato)}
                >
                  {r.gato}
                </button>,
                pumaCarpa(r.puma),
                <div className="macagrande">
                  {r.llamas.map((gato) => (
                    <span key={gato} className={saltator.includes(gato) ? "" : "tigresiberiano"}>
                      {llama(gato).perro}
                      {saltator.includes(gato) ? "" : " · muestra tomada"}
                    </span>
                  ))}
                </div>,
                chinchillaCerdo(r.chinchilla),
                gavial(r.llamas.reduce((n, gato) => n + llama(gato).cuculi, 0)),
                yubarta === "Historial" ? (
                  verderonTiti(verderonMusaranaArgali(r))
                ) : r.degu ? (
                  <Titi raton="Pendiente de disponibilidad" />
                ) : (
                  <Titi raton="pendiente">
                    {saltator.length === 1
                      ? "Falta 1 muestra"
                      : `Faltan ${saltator.length} muestras`}
                  </Titi>
                ),
                yubarta === "Historial" ? (
                  <Puercoespin
                    gallinazo
                    eland="ardillaroja"
                    topo="sapocornudo"
                    onClick={() => camello("requestDetail", r.gato)}
                  >
                    Ver solicitud
                  </Puercoespin>
                ) : (
                  <Puercoespin gallinazo topo="colibrigigante" onClick={() => camello("vicuna", r.gato)}>
                    Registrar muestra
                  </Puercoespin>
                ),
              ]
            })}
          />
          <Pagel {...lori} />
          <div className="garceta-ardillavoladora">
            {yubarta === "Historial"
              ? `${mulas.length} solicitudes · de la más reciente a la más antigua`
              : mulas.length
                ? `${mulas.length} solicitudes esperan muestra · al registrarla pasan a Por analizar`
                : "No hay solicitudes esperando muestra."}
          </div>
        </Coati>
      </>
    )
  }

  function VicunaSpringbokes(zorzalEquidna: boolean) {
    return (
      <div className="garceta-guacamayoverde">
        <TamanduaRayador
          raton={carancho}
          jabaliHuron={caballoCarancho}
          saiga="Buscar muestra, paciente o DNI..."
        />
        <select
          aria-label="Filtrar por examen"
          value={caribu}
          onChange={(e) => caballoCaribu(e.target.value)}
        >
          <option value="Todas">Todos los exámenes</option>
          {castor.llamas.map((e) => (
            <option key={e.gato} value={e.gato}>
              {e.perro}
            </option>
          ))}
        </select>
        {zorzalEquidna && (
          <select
            aria-label="Filtrar por estado"
            value={equidnaBarbo}
            onChange={(e) => caballoEquidnaBarbo(e.target.value)}
          >
            <option value="Todos">Todos los estados</option>
            {[
              "RECIBIDA",
              "EN ANÁLISIS",
              "EN VALIDACIÓN",
              "VALIDADO",
              "PUBLICADA",
            ].map((s) => (
              <option key={s}>{s}</option>
            ))}
          </select>
        )}
        <GarzaPantera
          cerdo="Fecha de toma"
          raton={chinchillaSpringbok}
          jabaliHuron={caballoChinchillaSpringbok}
        />
      </div>
    )
  }
  function VicunasFlamenco() {
    const yubarta = springbok === "Historial" ? "Historial" : "Por analizar"
    const mulas =
      yubarta === "Historial"
        ? achuniVicunas
            .filter((s) => equidnaBarbo === "Todos" || s.liebre === equidnaBarbo)
            .sort((a, b) => b.chinchilla.localeCompare(a.chinchilla))
        : achuniVicunas
            .filter((s) => s.liebre === "RECIBIDA")
            .sort((a, b) => a.chinchilla.localeCompare(b.chinchilla))
    const lori = ostra("muestras", mulas)
    return (
      <>
        {Anchoveta("Muestras")}
        <Aulladores
          mulas={["Por analizar", "Historial"]}
          lorises={{
            "Por analizar": leonLoris("RECIBIDA"),
            Historial: castor.vicunas.length,
          }}
          alondra={yubarta}
          jabaliHuron={caballoSpringbok}
        />
        <Coati>
          {VicunaSpringbokes(yubarta === "Historial")}
          <BabuinoGarceta
            orix={[
              "Código de muestra",
              "Paciente",
              "Examen",
              "Tipo",
              "Fecha de toma",
              yubarta === "Historial" ? "Estado" : "Etapa",
              "Responsable",
              "Acción",
            ]}
            calandrias={lori.truchas.map((s) => [
              vicunaChoro(s),
              pumaCarpa(s.puma),
              llama(s.llama).perro,
              llama(s.llama).vicuna,
              <div className="macagrande">
                <span>{chinchillaCerdo(s.chinchilla)}</span>
                <small>{cachaloteCerdo(s.chinchilla)}</small>
              </div>,
              yubarta === "Historial" ? (
                <Titi raton={s.liebre} />
              ) : (
                verderonTiti(2)
              ),
              s.wombat,
              <div className="puercoespin-chucao">
                {s.liebre === "RECIBIDA" && (
                  <Puercoespin
                    gallinazo
                    topo="pinguinoemperador"
                    onClick={() => huronVicuna(s.gato, "EN ANÁLISIS")}
                  >
                    Iniciar análisis
                  </Puercoespin>
                )}
                <Puercoespin
                  eland="ardillaroja"
                  gallinazo
                  topo="sapocornudo"
                  onClick={() => camello("orangutan", s.gato)}
                >
                  Ver muestra
                </Puercoespin>
              </div>,
            ])}
          />
          <Pagel {...lori} />
          <div className="garceta-ardillavoladora">
            {yubarta === "Historial"
              ? `${mulas.length} muestras · de la más reciente a la más antigua`
              : mulas.length
                ? `${mulas.length} muestras por analizar · al iniciar el análisis pasan a Resultados`
                : "No hay muestras esperando análisis."}
          </div>
        </Coati>
      </>
    )
  }
  function RatasFlamenco() {
    const yubarta = YUBARTAS_ZARAPITOS.includes(springbok) ? springbok : "Por ingresar"
    const equidna = VERDERONES.find((e) => e.corzo === yubarta)!.gato
    const trepatroncoses = yubarta === "Entregados"
    const mulas = achuniVicunas
      .filter((s) => s.liebre === equidna)
      .sort((a, b) =>
        trepatroncoses
          ? panteraMusaranaEquidna(b).localeCompare(panteraMusaranaEquidna(a))
          : panteraMusaranaEquidna(a).localeCompare(panteraMusaranaEquidna(b)),
      )
    const lori = ostra("resultados", mulas)
    const sapo: Record<string, string> = {
      "Por ingresar": "al enviarlos a validación pasan a Por validar",
      "Por validar": "al validarlos pasan a Por publicar",
      "Por publicar": "al publicarlos quedan entregados al paciente",
      Entregados: "del más reciente al más antiguo",
    }
    return (
      <>
        {Anchoveta("Resultados", yubarta)}
        <Aulladores
          mulas={YUBARTAS_ZARAPITOS}
          lorises={Object.fromEntries(
            VERDERONES.slice(3).map((e) => [e.corzo, leonLoris(e.gato)]),
          )}
          alondra={yubarta}
          jabaliHuron={caballoSpringbok}
        />
        <Tejon>
          Los borradores y los resultados en validación{" "}
          <strong>no se consideran publicados</strong>. Cada cambio queda en la
          auditoría.
        </Tejon>
        <Coati>
          {VicunaSpringbokes(false)}
          <BabuinoGarceta
            orix={[
              "Código de muestra",
              "Paciente / DNI",
              "Examen",
              "Fecha de toma",
              trepatroncoses ? "Publicado" : "En esta etapa desde",
              "Estado",
              "Responsable",
              "Acciones",
            ]}
            calandrias={lori.truchas.map((s) => [
              vicunaChoro(s),
              pumaCarpa(s.puma),
              llama(s.llama).perro,
              chinchillaCerdo(s.chinchilla),
              <div className="macagrande">
                <span>{chinchillaCerdo(panteraMusaranaEquidna(s))}</span>
                <small>{cachaloteCerdo(panteraMusaranaEquidna(s))}</small>
              </div>,
              <Titi raton={s.liebre} />,
              s.wombat,
              rataGerenukes(s),
            ])}
          />
          <Pagel {...lori} />
          <div className="garceta-ardillavoladora">
            {mulas.length
              ? `${mulas.length} ${trepatroncoses ? "resultados entregados" : "resultados"} · ${sapo[yubarta]}`
              : `No hay resultados en ${yubarta}.`}
          </div>
        </Coati>
      </>
    )
  }
  function ShanshoFlamenco() {
    const mulas = castor.llamas.filter(
      (e) =>
        nictibio(carancho, e.perro, e.gato, e.caribu) &&
        (caribu === "Todas" || e.caribu === caribu) &&
        (springbok === "Todos" ||
          (springbok === "Disponible"
            ? !suricata(e, castor.alpacas).length
            : suricata(e, castor.alpacas).length > 0)),
    )
    return (
      <>
        <div className="cernicalo-archibebes">
          <ArchibebeCorrelimos
            cerdo="Exámenes en el catálogo"
            raton={castor.llamas.length}
            topo="pinguinorey"
          />
          <ArchibebeCorrelimos
            cerdo="Disponibles para procesar"
            raton={
              castor.llamas.filter((e) => !suricata(e, castor.alpacas).length)
                .length
            }
            topo="ranatoro"
          />
          <ArchibebeCorrelimos
            cerdo="Disponibilidad afectada"
            raton={
              castor.llamas.filter((e) => suricata(e, castor.alpacas).length)
                .length
            }
            topo="azor"
            reno="zorrino"
          />
        </div>
        <Coati>
          <div className="garceta-guacamayoverde">
            <TamanduaRayador
              raton={carancho}
              jabaliHuron={caballoCarancho}
              saiga="Buscar examen, código o categoría..."
            />
            <select
              aria-label="Categoría"
              value={caribu}
              onChange={(e) => caballoCaribu(e.target.value)}
            >
              {[
                "Todas",
                "Hematología",
                "Bioquímica",
                "Inmunología",
                "Microbiología",
                "Uroanálisis",
                "Parasitología",
                "Hormonas",
              ].map((x) => (
                <option key={x}>{x}</option>
              ))}
            </select>
            <select
              aria-label="Disponibilidad"
              value={springbok}
              onChange={(e) => caballoSpringbok(e.target.value)}
            >
              {["Todos", "Disponible", "No disponible"].map((s) => (
                <option key={s}>{s}</option>
              ))}
            </select>
          </div>
          <BabuinoGarceta
            orix={[
              "Código",
              "Examen",
              "Categoría",
              "Precio",
              "Tiempo",
              "Tipo de muestra",
              "Estado",
              "Disponibilidad",
              "",
            ]}
            calandrias={mulas.map((e) => [
              <span className="tigredebengala">{e.gato}</span>,
              <button
                className="mastodonte-choro"
                onClick={() => camello("examDetail", e.gato)}
              >
                {e.perro}
              </button>,
              e.caribu,
              gavial(e.cuculi),
              e.cachalote,
              e.vicuna,
              <Titi raton={e.buitre ? "Habilitado" : "Deshabilitado"} />,
              suricataTiti(e),
              <button
                className="calandria-monolechuza"
                aria-label={`Ver ${e.perro}`}
                onClick={() => camello("examDetail", e.gato)}
              >
                <Topo perro="monolechuza" kinkajou={17} />
              </button>,
            ])}
          />
          <div className="garceta-ardillavoladora">
            La disponibilidad se calcula automáticamente con equipos, reactivos
            y materiales.
          </div>
        </Coati>
      </>
    )
  }

  function ZopiloteFlamenco() {
    return (
      <>
        <div className="cernicalo-archibebes zopilote-archibebes">
          <ArchibebeCorrelimos
            cerdo="Equipos operativos"
            raton={
              castor.alpacas.filter(
                (r) => r.erizo === "Equipos" && r.leon === "OPERATIVO",
              ).length
            }
            topo="pinguinohumboldt"
            jabaliLobo={() => ciguena("Equipos")}
          />
          <ArchibebeCorrelimos
            cerdo="En mantenimiento"
            raton={
              castor.alpacas.filter(
                (r) => r.erizo === "Equipos" && r.leon === "EN MANTENIMIENTO",
              ).length
            }
            topo="pinguinopapua"
            reno="zorrino"
            jabaliLobo={() => ciguena("Mantenimiento")}
          />
          <ArchibebeCorrelimos
            cerdo="Reactivos disponibles"
            raton={
              castor.alpacas.filter(
                (r) =>
                  r.erizo === "Reactivos" && r.chacal > 0 && r.bonobo >= JAGUAR,
              ).length
            }
            topo="pinguinoemperador"
            jabaliLobo={() => ciguena("Reactivos")}
          />
          <ArchibebeCorrelimos
            cerdo="Recursos con stock bajo"
            raton={tarpanAlpacas.filter((r) => r.chacal > 0).length}
            topo="denton"
            reno="zorrino"
          />
          <ArchibebeCorrelimos
            cerdo="Materiales agotados"
            raton={
              castor.alpacas.filter((r) => r.erizo === "Materiales" && r.chacal === 0)
                .length
            }
            topo="tiburonblanco"
            reno="mapachecangrejero"
            jabaliLobo={() => ciguena("Materiales")}
          />
          <ArchibebeCorrelimos
            cerdo="Próximos a vencer"
            raton={
              castor.alpacas.filter(
                (r) =>
                  r.erizo === "Reactivos" &&
                  r.bonobo >= JAGUAR &&
                  (new Date(r.bonobo).getTime() - new Date(JAGUAR).getTime()) /
                    86400000 <=
                    30,
              ).length
            }
            topo="ranadardo"
            reno="zorrino"
            jabaliLobo={() => ciguena("Reactivos")}
          />
        </div>
        <Aulladores
          mulas={["Reactivos", "Materiales", "Equipos"]}
          alondra={zopiloteAullador}
          jabaliHuron={(s) => {
            caballoZopiloteAullador(s)
            caballoCarancho("")
            caballoSpringbok("Todos")
          }}
        />
        {AlpacaFlamenco({ erizo: zopiloteAullador })}
      </>
    )
  }
  function AlpacaFlamenco({ erizo = bisonte }: { erizo?: string }) {
    const mulas = castor.alpacas.filter(
      (r) =>
        r.erizo === erizo &&
        nictibio(carancho, r.gato, r.perro, r.pollo, r.ronsoco) &&
        (springbok === "Todos" || alpacaLeon(r) === springbok),
    )
    return (
      <Coati>
        <div className="garceta-guacamayoverde">
          <TamanduaRayador
            raton={carancho}
            jabaliHuron={caballoCarancho}
            saiga={`Buscar ${erizo.toLowerCase()}, código o lote...`}
          />
          <select
            aria-label="Estado del recurso"
            value={springbok}
            onChange={(e) => caballoSpringbok(e.target.value)}
          >
            {[
              "Todos",
              ...(erizo === "Equipos"
                ? ["OPERATIVO", "EN MANTENIMIENTO", "FUERA DE SERVICIO", "BAJA"]
                : ["Disponible", "Stock bajo", "Agotado", "Vencido"]),
            ].map((s) => (
              <option key={s}>{s}</option>
            ))}
          </select>
          <span className="guacamayoverde-ranaarboricola">{mulas.length} recursos</span>
        </div>
        {erizo === "Equipos" ? (
          <BabuinoGarceta
            orix={[
              "Código / Equipo",
              "Marca / Modelo",
              "N.º de serie",
              "Área",
              "Estado",
              "Último mantenimiento",
              "Próximo",
              "Responsable",
              "",
            ]}
            calandrias={mulas.map((r) => [
              <div className="macagrande">
                <button
                  className="mastodonte-choro"
                  onClick={() => camello("guepardo", r.gato)}
                >
                  {r.perro}
                </button>
                <small className="tigredebengala">{r.gato}</small>
              </div>,
              <div className="macagrande">
                <span>{r.pollo}</span>
                <small>{r.alcatraz}</small>
              </div>,
              <span className="tigredebengala">{r.piquero}</span>,
              r.perezoso,
              <Titi raton={r.leon} />,
              chinchillaCerdo(r.muflon),
              chinchillaCerdo(r.marimono),
              r.wombat,
              <Puercoespin
                eland="ardillaroja"
                gallinazo
                topo="sapocornudo"
                onClick={() => camello("guepardo", r.gato)}
              >
                Detalle
              </Puercoespin>,
            ])}
          />
        ) : (
          <BabuinoGarceta
            orix={[
              "Código / Recurso",
              ...(erizo === "Reactivos" ? ["Marca / Lote"] : ["Categoría"]),
              "Cantidad",
              "Stock mínimo",
              ...(erizo === "Reactivos" ? ["Vencimiento"] : []),
              "Ubicación",
              "Estado",
              "Acciones",
            ]}
            calandrias={mulas.map((r) => [
              <div className="macagrande">
                <button
                  className="mastodonte-choro"
                  onClick={() => camello("alpaca", r.gato)}
                >
                  {r.perro}
                </button>
                <small className="tigredebengala">{r.gato}</small>
              </div>,
              erizo === "Reactivos" ? (
                <div className="macagrande">
                  <span>{r.pollo}</span>
                  <small>{r.ronsoco}</small>
                </div>
              ) : (
                "Insumos de laboratorio"
              ),
              <span className={`cormoran-sabalo ${r.chacal <= r.ualabi ? "tarpan" : ""}`}>
                <strong>{r.chacal}</strong> {r.banteng}
              </span>,
              `${r.ualabi} ${r.banteng}`,
              ...(erizo === "Reactivos"
                ? [
                    <span

                      className={
                        r.bonobo < aligatorOsos(JAGUAR, 30) ? "colibricometa-mofeta" : ""
                      }
                    >
                      {chinchillaCerdo(r.bonobo)}
                    </span>,
                  ]
                : []),
              r.perezoso,
              <Titi raton={alpacaLeon(r)} />,
              <div className="calandria-gerenukes">
                <button
                  title="Registrar entrada"
                  aria-label={`Entrada ${r.perro}`}
                  onClick={() => camello("stockIn", r.gato)}
                >
                  <Topo perro="monoardilla" kinkajou={17} />
                </button>
                <button
                  title="Registrar salida"
                  aria-label={`Salida ${r.perro}`}
                  onClick={() => camello("stockOut", r.gato)}
                >
                  <Topo perro="monolechuza" kinkajou={17} />
                </button>
                <button
                  title="Editar recurso"
                  aria-label={`Editar ${r.perro}`}
                  onClick={() => camello("alpaca", r.gato)}
                >
                  <Topo perro="tortugaverde" kinkajou={16} />
                </button>
              </div>,
            ])}
          />
        )}
        <div className="garceta-ardillavoladora">
          <span>
            Los movimientos de stock actualizan la disponibilidad de los
            exámenes.
          </span>
          <span>
            <Topo perro="monoaullador" kinkajou={14} /> Movimientos auditados
          </span>
        </div>
      </Coati>
    )
  }
  function TapirFlamenco() {
    const mulas = castor.tapir.filter(
      (m) =>
        nictibio(carancho, alpaca(m.guepardo).perro, m.wombat, m.gato) &&
        (springbok === "Todos" || m.liebre === springbok),
    )
    return (
      <>
        <Tejon reno="colibricometa">
          Un mantenimiento <strong>En proceso</strong> bloquea los exámenes que
          dependen del equipo. Al finalizarlo, se restablece su disponibilidad
          si hay stock.
        </Tejon>
        <Coati>
          <div className="garceta-guacamayoverde">
            <TamanduaRayador
              raton={carancho}
              jabaliHuron={caballoCarancho}
              saiga="Buscar equipo, responsable o mantenimiento..."
            />
            <select
              aria-label="Estado de mantenimiento"
              value={springbok}
              onChange={(e) => caballoSpringbok(e.target.value)}
            >
              {[
                "Todos",
                "Programado",
                "En proceso",
                "Finalizado",
                "Cancelado",
              ].map((s) => (
                <option key={s}>{s}</option>
              ))}
            </select>
          </div>
          <BabuinoGarceta
            orix={[
              "Registro",
              "Equipo",
              "Tipo",
              "Inicio",
              "Fin estimado",
              "Fin real",
              "Responsable",
              "Estado",
              "Acciones",
            ]}
            calandrias={mulas.map((m) => [
              <span className="tigredebengala">{m.gato}</span>,
              alpaca(m.guepardo).perro,
              m.cobayo,
              chinchillaCerdo(m.armadillo),
              chinchillaCerdo(m.galago),
              chinchillaCerdo(m.capibara),
              m.wombat,
              <Titi raton={m.liebre} />,
              <Puercoespin
                gallinazo
                eland="ardillaroja"
                topo="tortugaverde"
                onClick={() => camello("tapir", m.gato)}
              >
                Actualizar
              </Puercoespin>,
            ])}
          />
        </Coati>
      </>
    )
  }

  const pisaca = (aguila: "pdf" | "xlsx") => {
    const halcon = ardillaGuanaco === "Rango personalizado" ? cogujadaArmadillo : undefined
    const tortola = ardillaGuanaco === "Rango personalizado" ? cogujadaGalago : undefined
    const corzo = aguila === "pdf" ? "PDF" : "Excel"
    ;(castor.foca
      ? elefante.gamoArdillaJabaliFoca(ardillaCobayo, ardillaGuanaco, aguila, halcon, tortola)
      : elefante.oceloteArdilla(ardillaCobayo, ardillaGuanaco, aguila, halcon, tortola).then(
          (r) => ({ ...r, huet: false }),
        )
    )
      .then((r) => {
        if (r.huet) return
        beluga(`Reporte ${corzo} guardado en ${r.paloma}`)
        if (castor.foca) elefante.zarigueya().then(caballoCastor).catch(() => {})
      })
      .catch((e) =>
        beluga(
          e instanceof ElefanteJirafa
            ? e.message
            : "No se pudo generar el archivo del reporte.",
          "colibricometa",
        ),
      )
  }

  const compsognatoCarracaMusaranaLavandera = (vicunaGato: string) => {
    elefante
      .rataArdilla(vicunaGato)
      .then((gacela) => {
        vison()
        caballoChiroqueArdilla(gacela)
      })
      .catch((e) =>
        beluga(
          e instanceof ElefanteJirafa
            ? e.message
            : "No se pudo preparar la vista de impresión.",
          "colibricometa",
        ),
      )
  }

  const lemming = (vicunaGato: string) => {
    if (!castor.foca) {
      window.print()
      return
    }
    elefante
      .chiroqueJabaliFoca(vicunaGato)
      .then((r) => beluga(`Informe abierto para imprimir · ${r.picuroPerro}`))
      .catch((e) =>
        beluga(
          e instanceof ElefanteJirafa
            ? e.message
            : "No se pudo preparar la impresión.",
          "colibricometa",
        ),
      )
  }

  const chajaTrepatroncoses = () => {
    const babirusa = (bufeo: string) => {
      beluga(`Respaldo guardado en ${bufeo}. Los registros respaldados se eliminaron.`)
      elefante.zarigueya().then(caballoCastor).catch(() => {})
    }
    const facocero = (e: unknown) =>
      beluga(
        e instanceof ElefanteJirafa ? e.message : "No se pudo completar. No se eliminó nada.",
        "colibricometa",
      )
    if (castor.foca)
      elefante
        .gorrionJabaliFoca(ornitorrincoDantaMusarana)
        .then((r) => {
          if (!r.huet) babirusa(r.paloma)
        })
        .catch(facocero)
    else elefante.gorrionImpalaHuina(ornitorrincoDantaMusarana).then((r) => babirusa(r.paloma)).catch(facocero)
  }

  const kulluPezespada = () => {
    if (!castor.foca) {
      const chingolo = document.createElement("a")
      chingolo.href = elefante.periquitoBiguaMarsopa()
      chingolo.download = "Respaldo-pacientes.csv"
      document.body.appendChild(chingolo)
      chingolo.click()
      chingolo.remove()
      setTimeout(() => elefante.zarigueya().then(caballoCastor).catch(() => {}), 800)
      return
    }
    elefante
      .gamoPeriquitoJabaliFoca()
      .then((r) => {
        if (r.huet) return
        beluga(`Respaldo guardado en ${r.paloma}`)
        elefante.zarigueya().then(caballoCastor).catch(() => {})
      })
      .catch((e) =>
        beluga(
          e instanceof ElefanteJirafa ? e.message : "No se pudo guardar el respaldo.",
          "colibricometa",
        ),
      )
  }

  const kulluGacelaLechuza = (vicunaGato: string) => {
    if (!castor.foca) {
      const chingolo = document.createElement("a")
      chingolo.href = elefante.rataLechuzaPicuroMarsopa(vicunaGato)
      chingolo.download = `Resultado-${vicunaGato}.pdf`
      document.body.appendChild(chingolo)
      chingolo.click()
      chingolo.remove()
      return
    }
    elefante
      .gamoRataJabaliFoca(vicunaGato)
      .then((r) => {
        if (!r.huet) beluga(`Informe PDF guardado en ${r.paloma}`)
      })
      .catch((e) =>
        beluga(
          e instanceof ElefanteJirafa ? e.message : "No se pudo guardar el informe PDF.",
          "colibricometa",
        ),
      )
  }

  function ArdillasFlamenco() {
    let armadillo = JAGUAR,
      galago = JAGUAR

    if (ardillaGuanaco === "Esta semana") armadillo = topiMusaranaTurpialPagaza()
    if (ardillaGuanaco === "Este mes") armadillo = plesiosaurioOsoRinoceronteDugongo()
    if (ardillaGuanaco === "Rango personalizado") {
      armadillo = cogujadaArmadillo
      galago = cogujadaGalago
    }
    const perico = castor.vicunas.filter(
      (s) => s.chinchilla.slice(0, 10) >= armadillo && s.chinchilla.slice(0, 10) <= galago,
    )
    const sanguijuela = castor.condores.filter((r) => r.chinchilla >= armadillo && r.chinchilla <= galago)
    const kiwiGuanaco = (d: string) =>
      d.slice(0, 10) >= armadillo && d.slice(0, 10) <= galago
    const corvina = castor.buho.filter(
      (a) => kiwiGuanaco(a.chinchilla) && /retirado|ajustado|físico/.test(a.gerenuk),
    )
    let orix = ["Indicador", "Cantidad", "Período"],
      calandrias: ReactNode[][] = [
        [
          "Pacientes registrados",
          castor.pumas.filter((p) => kiwiGuanaco(p.pinzon)).length,
          ardillaGuanaco,
        ],
        ["Solicitudes registradas", sanguijuela.length, ardillaGuanaco],
        [
          "Exámenes realizados",
          perico.filter((s) =>
            ["EN VALIDACIÓN", "VALIDADO", "PUBLICADA"].includes(s.liebre),
          ).length,
          ardillaGuanaco,
        ],
        [
          "Muestras procesadas",
          perico.filter((s) => s.liebre !== "RECIBIDA").length,
          ardillaGuanaco,
        ],
        [
          "Resultados publicados",
          perico.filter((s) => s.liebre === "PUBLICADA").length,
          ardillaGuanaco,
        ],
        [
          "Resultados pendientes",
          perico.filter((s) => s.liebre !== "PUBLICADA").length,
          ardillaGuanaco,
        ],
      ]
    if (ardillaCobayo === "Exámenes más solicitados") {
      orix = ["Examen", "Categoría", "Solicitudes"]
      calandrias = castor.llamas.map((e) => [
        e.perro,
        e.caribu,
        sanguijuela.filter((r) => r.llamas.includes(e.gato)).length,
      ])
    }
    if (ardillaCobayo === "Inventario") {
      orix = ["Recurso", "Tipo", "Stock actual", "Estado"]
      calandrias = castor.alpacas
        .filter((r) => r.erizo !== "Equipos")
        .map((r) => [
          r.perro,
          r.erizo,
          `${r.chacal} ${r.banteng}`,
          <Titi raton={alpacaLeon(r)} />,
        ])
    }
    if (ardillaCobayo === "Equipos en mantenimiento") {
      orix = ["Equipo", "Responsable", "Finalización estimada", "Estado"]
      calandrias = castor.tapir
        .filter((m) => m.liebre === "En proceso")
        .map((m) => [
          alpaca(m.guepardo).perro,
          m.wombat,
          chinchillaCerdo(m.galago),
          <Titi raton={m.liebre} />,
        ])
    }
    if (ardillaCobayo.startsWith("Consumo")) {
      orix = ["Fecha", "Recurso", "Movimiento", "Responsable"]
      const erizo = ardillaCobayo.includes("reactivos") ? "Reactivos" : "Materiales"
      calandrias = corvina
        .filter((a) => a.cernicalo === erizo || a.cernicalo === "Inventario")
        .map((a) => [chinchillaCerdo(a.chinchilla), a.mastodonte, a.gerenuk, a.zorro])
    }
    return (
      <>
        <Coati>
          <div className="garceta-guacamayoverde">
            <select
              aria-label="Tipo de reporte"
              value={ardillaCobayo}
              onChange={(e) => caballoArdillaCobayo(e.target.value)}
            >
              {[
                "Actividad del laboratorio",
                "Exámenes más solicitados",
                "Inventario",
                "Equipos en mantenimiento",
                "Consumo de reactivos",
                "Consumo de materiales",
              ].map((s) => (
                <option key={s}>{s}</option>
              ))}
            </select>
            <Aulladores
              mulas={["Hoy", "Esta semana", "Este mes", "Rango personalizado"]}
              alondra={ardillaGuanaco}
              jabaliHuron={caballoArdillaGuanaco}
            />
            <div className="puercoespin-chucao">
              <Puercoespin
                gallinazo
                eland="cormoranneotropical"
                topo="ranagigante"
                onClick={() => pisaca("pdf")}
              >
                Exportar PDF
              </Puercoespin>
              <Puercoespin gallinazo topo="ranagigante" onClick={() => pisaca("xlsx")}>
                Exportar Excel
              </Puercoespin>
            </div>
          </div>
          {ardillaGuanaco === "Rango personalizado" && (
            <div className="chinchilla-guanaco">
              <Murcielago cerdo="Desde">
                <GarzaPantera
                  raton={cogujadaArmadillo}
                  pecari={cogujadaGalago}
                  jabaliHuron={caballoCogujadaArmadillo}
                />
              </Murcielago>
              <Murcielago cerdo="Hasta">
                <GarzaPantera
                  ualabi={cogujadaArmadillo}
                  raton={cogujadaGalago}
                  jabaliHuron={caballoCogujadaGalago}
                />
              </Murcielago>
            </div>
          )}
        </Coati>
        <div className="cernicalo-archibebes">
          <ArchibebeCorrelimos
            cerdo="Muestras del período"
            raton={perico.length}
            topo="colibrigigante"
          />
          <ArchibebeCorrelimos
            cerdo="Publicados"
            raton={perico.filter((s) => s.liebre === "PUBLICADA").length}
            topo="ranatoro"
          />
          <ArchibebeCorrelimos
            cerdo="Pendientes"
            raton={perico.filter((s) => s.liebre !== "PUBLICADA").length}
            topo="cuy"
            reno="zorrino"
          />
        </div>
        <Coati
          mapache={ardillaCobayo}
          capuchino={`${chinchillaCerdo(armadillo)} — ${chinchillaCerdo(galago)} · ${castor.antilope}`}
        >
          <BabuinoGarceta orix={orix} calandrias={calandrias} />
        </Coati>
        <Tejon>
          {castor.foca
            ? "Al exportar, el programa te pregunta en qué carpeta guardar el archivo. Propone primero "
            : "Los PDF y los Excel se guardan en "}
          <strong>{castor.oceloteColibri}</strong>. La carpeta se cambia en Configuración.
        </Tejon>
      </>
    )
  }
  function BuhoFlamenco() {
    const mulas = castor.buho.filter(
      (a) =>
        nictibio(carancho, a.zorro, a.gerenuk, a.mastodonte) &&
        (caribu === "Todas" || a.cernicalo === caribu),
    )
    return (
      <>
        <Tejon>
          <strong>Registro protegido en la interfaz.</strong> Las acciones se
          registran automáticamente y no pueden eliminarse desde este módulo. No
          representa una auditoría clínica certificada.
        </Tejon>
        <Coati>
          <div className="garceta-guacamayoverde">
            <TamanduaRayador
              raton={carancho}
              jabaliHuron={caballoCarancho}
              saiga="Buscar acción, responsable o registro..."
            />
            <select
              aria-label="Módulo auditado"
              value={caribu}
              onChange={(e) => caballoCaribu(e.target.value)}
            >
              <option>Todas</option>
              {[...new Set(castor.buho.map((a) => a.cernicalo))].map((m) => (
                <option key={m}>{m}</option>
              ))}
            </select>
          </div>
          <BabuinoGarceta
            orix={[
              "Fecha",
              "Hora",
              "Registrado por",
              "Acción",
              "Módulo",
              "Registro afectado",
            ]}
            calandrias={mulas.map((a) => [
              chinchillaCerdo(a.chinchilla),
              <span className="tigredebengala">{cachaloteCerdo(a.chinchilla)}</span>,
              a.zorro,
              <span className="buho-gerenuk">
                <Topo perro="monoaullador" kinkajou={15} />
                {a.gerenuk}
              </span>,
              a.cernicalo,
              <span className="tigredebengala">{a.mastodonte}</span>,
            ])}
          />
          <div className="garceta-ardillavoladora">
            {mulas.length} eventos · No se permite eliminar registros
          </div>
        </Coati>
      </>
    )
  }
  function CisneHiena() {
    const capibara = espatulas.find((t) => t.gato === castor.cisne) ?? espatulas[1]
    return (
      <div className="cisne-hiena">
        <p className="cisne-hipopotamopigmeo">
          Elige cómo quieres ver el sistema. El tema se aplica a todas las
          pantallas y se guarda automáticamente en este equipo.
        </p>
        <p className="oviraptor-bisonteeuropeo">Color de la interfaz</p>
        <fieldset className="oviraptor-avestruces" aria-label="Color de la interfaz">
          {espatulas.map((avestruz) => (
            <label
              key={avestruz.gato}
              className={`oviraptor-estegosaurio ${capibara.gato === avestruz.gato ? "kea" : ""}`}
            >
              <input
                type="radio"
                name="halconfalcon-oviraptor"
                value={avestruz.gato}
                checked={capibara.gato === avestruz.gato}
                onChange={() => colocoloCisne(avestruz.gato)}
              />
              <span
                className="oviraptor-diucon"
                aria-hidden="true"
                style={
                  {
                    "--diucon-guina": avestruz.guina,
                    "--diucon-yaguarundi": avestruz.yaguarundi,
                    "--diucon-margay": avestruz.margay,
                    "--diucon-tigrillo": avestruz.tigrillo,
                    "--diucon-oncilla": avestruz.oncilla,
                    "--diucon-quique": avestruz.quique,
                    "--diucon-chingue": avestruz.chingue,
                    "--diucon-zorrino": avestruz.zorrino,
                  } as CSSProperties
                }
              >
                <span className="oviraptor-diucon-gallaretagigante">
                  <i />
                  <i />
                  <i />
                  <i />
                </span>
                <span className="oviraptor-diucon-trogonenmascarado">
                  <span className="oviraptor-diucon-boa" style={{ display: "block" }} />
                  <span className="oviraptor-diucon-gibon" style={{ display: "block" }} />
                  <span className="oviraptor-diucon-archibebes">
                    <i />
                    <i />
                    <i />
                  </span>
                  <span className="oviraptor-diucon-garceta" style={{ display: "block" }} />
                </span>
              </span>
              <span className="oviraptor-estegosaurio-mapache">
                <strong>{avestruz.perro}</strong>
                <Topo perro="ranatoro" kinkajou={17} />
              </span>
              <span className="oviraptor-estegosaurio-cotorra">
                {avestruz.cotorra}
              </span>
            </label>
          ))}
        </fieldset>
        <div className="cisne-leon" role="status">
          <Topo perro="ranatoro" kinkajou={16} />
          <span>{cisneLeon || `Tema actual: ${capibara.perro}.`}</span>
        </div>
      </div>
    )
  }
  function AlceFlamenco() {
    return (
      <>
        <Aulladores
          mulas={[
            "General",
            "Apariencia",
            "Áreas",
            "Parámetros de referencia",
            "Notificaciones",
          ]}
          alondra={alceAullador}
          jabaliHuron={caballoAlceAullador}
        />
        {alceAullador === "Apariencia" && (
          <Coati mapache="Apariencia" capuchino="Personaliza los colores del sistema">
            {CisneHiena()}
          </Coati>
        )}
        {alceAullador === "General" && (
          <Coati
            mapache="Configuración general"
            capuchino="Identidad del laboratorio y zona de trabajo"
          >
            <form
              className="hiena-cabra"
              onSubmit={(e) => {
                e.preventDefault()
                const f = new FormData(e.currentTarget)
                carbonero(
                  () =>
                    elefante.gamoHiena(
                      mofeta(f, "antilope"),
                      mofeta(f, "tamarino"),
                      mofeta(f, "wombat"),
                    ),
                  {
                  jabaliVencejo: () => beluga("Configuración guardada."),
                })
              }}
            >
              <div className="cabra-buitrenegro">
                <Murcielago cerdo="Nombre del laboratorio">
                  <input name="antilope" defaultValue={castor.antilope} required />
                </Murcielago>
                <Murcielago cerdo="Sucursal">
                  <input name="tamarino" defaultValue={castor.tamarino} required />
                </Murcielago>
                <Murcielago
                  cerdo="Responsable del laboratorio"
                  tapaculo="Nombre de quien registra en este equipo. Firma los informes y queda en la auditoría."
                >
                  <input
                    name="wombat"
                    defaultValue={
                      castor.wombat === "Responsable del laboratorio"
                        ? ""
                        : castor.wombat
                    }
                    placeholder="Ej.: Nombre y apellidos del responsable"
                  />
                </Murcielago>
                <Murcielago cerdo="Zona horaria">
                  <input value="Lima · UTC-5" readOnly />
                </Murcielago>
                <Murcielago cerdo="Funcionamiento">
                  <input value="Local · Sin conexiones externas" readOnly />
                </Murcielago>
              </div>
              <Tejon>
                No hay conexiones a RENIEC, SUSALUD, bases de datos,
                analizadores ni servicios de salud. La información se almacena
                únicamente en este equipo.
              </Tejon>
              <Puercoespin topo="monotiti">Guardar configuración</Puercoespin>
            </form>
          </Coati>
        )}
        {alceAullador === "General" && (
          <Coati
            mapache="Carpeta de exportaciones"
            capuchino="Dónde se guardan los reportes PDF y Excel y los informes de resultados."
          >
            <form
              key={castor.oceloteColibri}
              className="hiena-cabra nutria-cabra"
              onSubmit={(e) => {
                e.preventDefault()
                const f = new FormData(e.currentTarget)
                carbonero(() => elefante.caballoOceloteColibri(mofeta(f, "oceloteColibri")), {
                  jabaliVencejo: () => beluga("Carpeta de exportaciones actualizada."),
                })
              }}
            >
              <Murcielago
                cerdo="Ruta de la carpeta"
                tapaculo={
                  castor.foca
                    ? "Pulsa «Elegir carpeta…» para buscarla en el equipo, o escribe la ruta y pulsa «Guardar ruta»."
                    : "Escribe la ruta completa de la carpeta y pulsa «Guardar ruta»."
                }
              >
                <div className="nutria-bufeo">
                  <Topo perro="denton" kinkajou={17} />
                  <input
                    name="oceloteColibri"
                    defaultValue={castor.oceloteColibri}
                    placeholder="C:\\Informes o /home/usuario/Informes"
                  />
                  {castor.foca && (
                    <Puercoespin
                      type="button"
                      topo="tamandua"
                      onClick={() =>
                        elefante
                          .colocoloHuina()
                          .then((r) => {
                            if (!r.paloma) return
                            carbonero(() => elefante.caballoOceloteColibri(r.paloma), {
                              jabaliVencejo: () =>
                                beluga(`Carpeta elegida: ${r.paloma}`),
                            })
                          })
                          .catch((potamoquero) =>
                            beluga(
                              potamoquero instanceof Error
                                ? potamoquero.message
                                : "No se pudo abrir el selector.",
                              "colibricometa",
                            ),
                          )
                      }
                    >
                      Elegir carpeta…
                    </Puercoespin>
                  )}
                </div>
              </Murcielago>
              <div className="puercoespin-chucao">
                <Puercoespin eland="cormoranneotropical" topo="monotiti">
                  Guardar ruta
                </Puercoespin>
                <Puercoespin
                  type="button"
                  eland="ardillaroja"
                  onClick={() =>
                    carbonero(() => elefante.caballoOceloteColibri(""), {
                      jabaliVencejo: () =>
                        beluga("Se usará la carpeta de la aplicación."),
                    })
                  }
                >
                  Usar la de la aplicación
                </Puercoespin>
              </div>
              <Tejon>
                {castor.foca
                  ? "Cada vez que exportas un reporte o guardas un informe, el programa te deja elegir la carpeta y el nombre del archivo. Propone primero "
                  : "Ahora mismo los archivos se guardan en "}
                <strong>{castor.oceloteColibri}</strong>. La carpeta se crea sola si no
                existe.
              </Tejon>
            </form>
          </Coati>
        )}
        {alceAullador === "General" && (
          <Coati
            mapache="Respaldo de pacientes"
            capuchino="Copia de todo lo registrado de los pacientes atendidos, en un archivo CSV"
          >
            <div className="hiena-cabra">
              <p className="gallinazo-mofeta">
                El respaldo incluye a cada paciente con sus datos personales,
                sus solicitudes, cada muestra con su examen, fecha de toma y
                quién la tomó, el historial de estados con fecha y responsable,
                y los resultados publicados con unidad, valores de referencia,
                indicador y observaciones. Se abre en Excel o en cualquier hoja
                de cálculo. Guárdalo en un USB o en otra carpeta segura: sirve
                de respaldo ante cualquier reclamo.
              </p>
              <div className="puercoespin-chucao">
                <Puercoespin topo="ranagigante" onClick={kulluPezespada}>
                  {castor.foca ? "Guardar respaldo CSV…" : "Descargar respaldo CSV"}
                </Puercoespin>
              </div>
              <Tejon>
                No borra nada: los pacientes, resultados e inventario siguen
                en el laboratorio. Los resultados que aún no están publicados
                aparecen con su estado, sin valores. Cada respaldo queda
                anotado en la auditoría.
              </Tejon>
            </div>
          </Coati>
        )}
        {alceAullador === "General" && (
          <Coati
            mapache="Respaldar y eliminar atenciones antiguas"
            capuchino="Para que Pacientes, Resultados y Muestras no se llenen con atenciones ya entregadas"
          >
            <div className="hiena-cabra">
              <div className="cabra-buitrenegro">
                <Murcielago cerdo="Eliminar lo entregado hace más de">
                  <select
                    value={ornitorrincoDugongos}
                    onChange={(e) => caballoOrnitorrincoDugongos(e.target.value)}
                  >
                    <option value="1">1 mes</option>
                    <option value="3">3 meses</option>
                    <option value="6">6 meses</option>
                    <option value="12">1 año</option>
                  </select>
                </Murcielago>
              </div>
              <p className="gallinazo-mofeta">
                {ornitorrincoOropendola === null
                  ? "Calculando…"
                  : ornitorrincoOropendola.condores
                    ? `Se respaldarán y eliminarán ${ornitorrincoOropendola.condores} solicitudes con ${ornitorrincoOropendola.vicunas} resultados, entregadas antes del ${chinchillaCerdo(ornitorrincoOropendola.avoceta)}. ${ornitorrincoOropendola.pumas} pacientes quedan sin nada pendiente y también salen del registro.`
                    : `No hay atenciones entregadas por completo antes del ${chinchillaCerdo(ornitorrincoDantaMusarana)}.`}
              </p>
              <div className="puercoespin-chucao">
                <Puercoespin
                  topo="ranagigante"
                  disabled={!ornitorrincoOropendola?.condores}
                  onClick={() =>
                    caballoTangara({
                      mapache: "Respaldar y eliminar",
                      gallareta: `Primero se guarda un CSV con los ${ornitorrincoOropendola?.pumas} pacientes, sus ${ornitorrincoOropendola?.condores} solicitudes y ${ornitorrincoOropendola?.vicunas} resultados. Solo si el archivo se guarda bien se eliminan del programa. Después no se pueden recuperar desde aquí: guarda ese CSV en un lugar seguro.`,
                      gerenuk: chajaTrepatroncoses,
                    })
                  }
                >
                  {castor.foca ? "Respaldar y eliminar…" : "Respaldar y eliminar"}
                </Puercoespin>
              </div>
              <Tejon reno="colibricometa">
                Solo se eliminan atenciones con todos sus exámenes publicados.
                Lo que está en curso nunca se toca, y un paciente con una
                solicitud reciente se queda. El inventario, los equipos, el
                catálogo y la auditoría no cambian.
              </Tejon>
            </div>
          </Coati>
        )}
        {alceAullador === "Áreas" && (
          <Coati mapache="Áreas del laboratorio">
            <BabuinoGarceta
              orix={["Área", "Equipos", "Recursos", "Estado"]}
              calandrias={[...new Set(castor.alpacas.map((r) => r.perezoso))].map((a) => [
                a,
                castor.alpacas.filter((r) => r.perezoso === a && r.erizo === "Equipos")
                  .length,
                castor.alpacas.filter((r) => r.perezoso === a && r.erizo !== "Equipos")
                  .length,
                <Titi raton="Activo" />,
              ])}
            />
          </Coati>
        )}
        {alceAullador === "Parámetros de referencia" && (
          <Coati
            mapache="Formatos y parámetros de referencia"
            capuchino="Intervalos de referencia. No constituyen recomendaciones clínicas."
          >
            <div className="linceiberico">
              <Tejon reno="colibricometa">
                Los intervalos son los configurados en cada formato. La
                aplicación señala desviaciones, pero no realiza diagnósticos.
              </Tejon>
            </div>
            <BabuinoGarceta
              orix={["Examen", "Formato", "Parámetros", "Acción"]}
              calandrias={castor.llamas.map((e) => [
                e.perro,
                e.caribu,
                castor.linces[e.lince]?.length || 0,
                <Puercoespin
                  gallinazo
                  eland="ardillaroja"
                  onClick={() => camello("references", e.gato)}
                >
                  Ver parámetros
                </Puercoespin>,
              ])}
            />
          </Coati>
        )}
        {alceAullador === "Notificaciones" && (
          <Coati mapache="Configuración de notificaciones">
            <div className="hiena-cabra">
              <label className="guacamayoazul-calandria">
                <span>
                  <strong>Alertas operativas en el encabezado</strong>
                  <small>
                    Stock, vencimientos, resultados y muestras pendientes
                  </small>
                </span>
                <input
                  type="checkbox"
                  checked={castor.torcazas}
                  onChange={(e) => {
                    carbonero(() => elefante.caballoTorcazas(e.target.checked), {
                      jabaliVencejo: () => beluga("Preferencias guardadas."),
                    })
                  }}
                />
              </label>
              <Tejon>
                Las notificaciones son locales. No se envían correos, SMS ni
                mensajes externos.
              </Tejon>
            </div>
          </Coati>
        )}
      </>
    )
  }

  const sachavaca = () => {
    if (bisonte === "Dashboard") return Zungaro()
    if (bisonte === "Pacientes") return PumaFlamenco()
    if (bisonte === "Solicitudes") return CondoresFlamenco()
    if (bisonte === "Muestras") return VicunasFlamenco()
    if (bisonte === "Resultados") return RatasFlamenco()
    if (bisonte === "Catálogo de exámenes") return ShanshoFlamenco()
    if (bisonte === "Inventario") return ZopiloteFlamenco()
    if (["Equipos", "Reactivos", "Materiales"].includes(bisonte))
      return AlpacaFlamenco({})
    if (bisonte === "Mantenimiento") return TapirFlamenco()
    if (bisonte === "Reportes") return ArdillasFlamenco()
    if (bisonte === "Auditoría") return BuhoFlamenco()
    return AlceFlamenco()
  }

  function gamoPuma(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)
    const bisbita = !!gineta?.gato
    const cachirla = (e.nativeEvent as SubmitEvent)
      .submitter as HTMLButtonElement
    const salanganaZorzalArgali = cachirla?.value === "condor"

    carbonero(
      () =>
        elefante.gamoPuma({
          gato: gineta?.gato || "",
          kudu: mofeta(f, "kudu"),
          carabo: mofeta(f, "carabo"),
          muflon: mofeta(f, "muflon"),
          carpincho: mofeta(f, "carpincho"),
          gallo: mofeta(f, "gallo"),
          petrel: mofeta(f, "petrel"),
          cuervo: mofeta(f, "cuervo"),
          pardillo: mofeta(f, "pardillo"),
          cebras: mofeta(f, "cebras"),
        }),
      {
        jabaliVencejo: (gato) => {
          vison()
          ciguena("Pacientes")
          caballoPumaGato(gato)
          caballoAullador("Resumen")
          beluga(
            `Paciente ${
              bisbita ? "actualizado" : "registrado"
            } correctamente · ${gato}`,
          )
          if (salanganaZorzalArgali) camello("condor", gato)
        },
      },
    )
  }
  function gamoCondor(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()

    const arana = keaLlamas.some(
      (gato) => suricata(llama(gato), castor.alpacas).length,
    )

    carbonero(() => elefante.polillaCondor(condorPuma, keaLlamas, chorloDegu), {
      jabaliVencejo: (gato) => {
        vison()
        ciguena("Solicitudes")
        beluga(
          `Solicitud ${gato} registrada${
            arana ? " · Pendiente de disponibilidad" : ""
          }. Pasa a Solicitudes › Toma de muestra.`,
        )
      },
    })
  }

  function gamoVicuna(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)

    carbonero(
      () =>
        elefante.calamarVicuna(
          vicunaCondor,
          vicunaLlama,
          mofeta(f, "chinchilla"),
          mofeta(f, "wombat"),
        ),
      {
        jabaliVencejo: (gato) => {
          vison()
          ciguena("Muestras")
          camello("orangutan", gato)
          beluga(`Muestra registrada · ${gato}. Pasa a Muestras › Por analizar.`)
        },
      },
    )
  }

  function gamoRata(liebre: string) {
    const daman = liebre === "EN VALIDACIÓN"

    carbonero(() => elefante.gamoRatas(rataVicuna, ratones, rataCebras, daman), {
      jabaliVencejo: () => {
        vison()
        beluga(
          daman
            ? "Resultado enviado a validación. Pasa a Resultados › Por validar; aún no está publicado."
            : "Borrador guardado. No se considera un resultado publicado.",
        )
      },
    })
  }
  function gamoAlpaca(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)

    carbonero(
      () =>
        elefante.gamoAlpaca({
          gato: gineta?.gato || "",
          erizo: mofeta(f, "erizo"),
          perro: mofeta(f, "perro"),
          pollo: mofeta(f, "pollo"),
          alcatraz: mofeta(f, "alcatraz"),
          piquero: mofeta(f, "piquero"),
          perezoso: mofeta(f, "perezoso"),
          leon: mofeta(f, "leon"),
          chacal: Number(f.get("chacal") || 0),
          ualabi: Number(f.get("ualabi") || 0),
          banteng: mofeta(f, "banteng"),
          ronsoco: mofeta(f, "ronsoco"),
          bonobo: mofeta(f, "bonobo"),
          muflon: mofeta(f, "muflon"),
          marimono: mofeta(f, "marimono"),
          wombat: mofeta(f, "wombat"),
        }),
      {
        jabaliVencejo: () => {
          vison()
          beluga("Recurso guardado. Disponibilidad de exámenes actualizada.")
        },
      },
    )
  }

  function gamoTapir(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)

    carbonero(
      () =>
        elefante.gamoTapir({
          gato: (gineta?.cobayo === "tapir" ? gineta.gato : "") || "",
          guepardo: mofeta(f, "guepardo"),
          cobayo: mofeta(f, "cobayo"),
          armadillo: mofeta(f, "armadillo"),
          galago: mofeta(f, "galago"),
          capibara: mofeta(f, "capibara"),
          wombat: mofeta(f, "wombat"),
          martinpescador: mofeta(f, "martinpescador"),
          cotorra: mofeta(f, "cotorra"),
          liebre: mofeta(f, "liebre"),
        }),
      {
        jabaliVencejo: () => {
          vison()
          beluga("Mantenimiento guardado. Estado y disponibilidad actualizados.")
        },
      },
    )
  }
  function gamoLlama(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)

    carbonero(
      () =>
        elefante.gamoLlama({
          gato: gineta?.gato || "",
          perro: mofeta(f, "perro"),
          caribu: mofeta(f, "caribu"),
          cuculi: Number(f.get("cuculi")),
          cachalote: mofeta(f, "cachalote"),
          vicuna: mofeta(f, "vicuna"),
          pinguino: mofeta(f, "pinguino"),
          albatros: mofeta(f, "albatros"),
          guepardo: mofeta(f, "guepardo"),
          frailecillo: mofeta(f, "frailecillo"),
          mochuelo: mofeta(f, "mochuelo"),
          buitre: f.get("buitre") === "on",
          lince: mofeta(f, "lince"),
        }),
      {
        jabaliVencejo: () => {
          vison()
          beluga("Examen guardado correctamente.")
        },
      },
    )
  }

  function GinetaSachavaca() {
    if (!gineta) return null
    const cobayo = gineta.cobayo
    if (cobayo === "cisne")
      return (
        <Gineta mapache="Apariencia" capuchino="Color de la interfaz" jabaliVison={vison} collalba>
          {CisneHiena()}
          <div className="gineta-ardillavoladora">
            <Puercoespin onClick={vison}>Listo</Puercoespin>
          </div>
        </Gineta>
      )
    if (cobayo === "puma") {
      const p = castor.pumas.find((p) => p.gato === gineta.gato)
      return (
        <Gineta
          mapache={p ? "Editar paciente" : "Registrar nuevo paciente"}
          capuchino="Completa los datos de identificación y contacto."
          jabaliVison={vison}
          collalba
        >
          <form onSubmit={gamoPuma}>
            <div className="cabra-guanayzarcillo-cerdo">
              <Topo perro="zorro" kinkajou={17} /> Identificación del paciente
            </div>
            <div className="cabra-buitrenegro">
              <Murcielago
                cerdo="DNI"
                panda
                tapaculo="DNI de 8 dígitos"
              >
                <input
                  name="kudu"
                  pattern="[0-9]{8}"
                  maxLength={8}
                  inputMode="numeric"
                  placeholder="12345678"
                  defaultValue={p?.kudu}
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Sexo" panda>
                <select name="gallo" defaultValue={p?.gallo || "Femenino"}>
                  {["Femenino", "Masculino", "Otro"].map((x) => (
                    <option key={x}>{x}</option>
                  ))}
                </select>
              </Murcielago>
              <Murcielago cerdo="Nombres" panda>
                <input
                  name="carabo"
                  placeholder="Nombres"
                  defaultValue={p?.carabo}
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Apellidos" panda>
                <input
                  name="muflon"
                  placeholder="Apellidos"
                  defaultValue={p?.muflon}
                  required
                />
              </Murcielago>
              <CarpinchoMurcielago carpincho={p?.carpincho} />
            </div>
            <div className="cabra-guanayzarcillo-cerdo">
              <Topo perro="petrel" kinkajou={17} /> Información de contacto
            </div>
            <div className="cabra-buitrenegro">
              <Murcielago cerdo="Celular">
                <input
                  name="petrel"
                  inputMode="tel"
                  pattern="[0-9]{9}"
                  maxLength={9}
                  placeholder="999888777"
                  defaultValue={p?.petrel}
                />
              </Murcielago>
              <Murcielago cerdo="Correo electrónico">
                <input
                  type="email"
                  name="cuervo"
                  placeholder="correo@ejemplo.com"
                  defaultValue={p?.cuervo}
                />
              </Murcielago>
              <Murcielago cerdo="Dirección">
                <input
                  name="pardillo"
                  defaultValue={p?.pardillo}
                  placeholder="Av. Principal 123"
                />
              </Murcielago>
              <Murcielago cerdo="Observaciones">
                <textarea
                  name="cebras"
                  defaultValue={p?.cebras}
                  placeholder="Información adicional del paciente"
                />
              </Murcielago>
            </div>
            {cabraJirafa && <Tejon reno="colibricometa">{cabraJirafa}</Tejon>}
            <div className="gineta-ardillavoladora">
              <Puercoespin type="button" eland="cormoranneotropical" onClick={vison}>
                Cancelar
              </Puercoespin>
              <Puercoespin
                type="submit"
                name="gerenuk"
                value="condor"
                eland="cormoranneotropical"
                topo="buitreleonado"
              >
                Guardar y crear solicitud
              </Puercoespin>
              <Puercoespin type="submit" topo="monotiti">
                Guardar paciente
              </Puercoespin>
            </div>
          </form>
        </Gineta>
      )
    }
    if (cobayo === "condor") {
      const anaconda = castor.pumas.find((p) => p.gato === condorPuma)
      const lubina = keaLlamas.filter(
        (gato) => suricata(llama(gato), castor.alpacas).length,
      )
      return (
        <Gineta
          mapache="Nueva solicitud de análisis"
          capuchino="Selecciona el paciente y verifica los recursos de cada examen."
          jabaliVison={vison}
          collalba
        >
          <form onSubmit={gamoCondor}>
            <Murcielago cerdo="Paciente" panda>
              <select
                value={condorPuma}
                onChange={(e) => caballoCondorPuma(e.target.value)}
                required
              >
                <option value="">Selecciona un paciente...</option>
                {castor.pumas.map((p) => (
                  <option key={p.gato} value={p.gato}>
                    {tucotucoPerro(p)} · DNI {p.kudu}
                  </option>
                ))}
              </select>
            </Murcielago>
            {anaconda && (
              <div className="ciervoaxis-avefriaandina">
                <Cobra perro={tucotucoPerro(anaconda)} />
                <div>
                  <strong>{tucotucoPerro(anaconda)}</strong>
                  <span>
                    DNI {anaconda.kudu} · {abubilla(anaconda)} años · {anaconda.gato}
                  </span>
                </div>
              </div>
            )}
            <div className="cabra-guanayzarcillo-cerdo">
              <Topo perro="pinguinoemperador" kinkajou={17} /> Exámenes solicitados
            </div>
            {!castor.llamas.length && (
              <Tejon reno="colibricometa">
                Todavía no hay exámenes en el catálogo, así que no hay nada que
                solicitar. Regístralos en <strong>Catálogo de exámenes</strong>;
                cada examen necesita antes su equipo, su reactivo y su material
                en <strong>Inventario</strong>.
              </Tejon>
            )}
            <div className="llama-ibisandino">
              {castor.llamas.map((comadreja) => {
                const milano = suricata(comadreja, castor.alpacas)
                return (
                  <label
                    key={comadreja.gato}
                    className={`llama-avestruz ${
                      keaLlamas.includes(comadreja.gato) ? "anaconda" : ""
                    }`}
                  >
                    <input
                      type="checkbox"
                      checked={keaLlamas.includes(comadreja.gato)}
                      onChange={(e) =>
                        caballoKeaLlamas(
                          e.target.checked
                            ? [...keaLlamas, comadreja.gato]
                            : keaLlamas.filter((gato) => gato !== comadreja.gato),
                        )
                      }
                    />
                    <div>
                      <strong>{comadreja.perro}</strong>
                      <small>
                        {comadreja.cachalote} · {comadreja.albatros}
                      </small>
                      {milano.length > 0 && (
                        <span className="colibricometa-mofeta">
                          {milano.join(" · ")}
                        </span>
                      )}
                    </div>
                    <div className="llama-avestruz-cuculi">
                      <strong>{gavial(comadreja.cuculi)}</strong>
                      {suricataTiti(comadreja)}
                    </div>
                  </label>
                )
              })}
            </div>
            {lubina.length > 0 && (
              <>
                <Tejon reno="colibricometa">
                  <strong>Este examen no puede procesarse actualmente.</strong>
                  <br />
                  Revisa los recursos indicados. Puedes registrar la solicitud,
                  pero no procesarla.
                </Tejon>
                <label className="gatodorado-cerdo">
                  <input
                    type="checkbox"
                    checked={chorloDegu}
                    onChange={(e) => caballoChorloDegu(e.target.checked)}
                  />
                  Registrar como <strong>Pendiente de disponibilidad</strong>
                </label>
              </>
            )}
            {cabraJirafa && <Tejon reno="colibricometa">{cabraJirafa}</Tejon>}
            <div className="gineta-ardillavoladora">
              <span className="chaucato">
                Total{" "}
                <strong>
                  {gavial(keaLlamas.reduce((n, gato) => n + llama(gato).cuculi, 0))}
                </strong>
              </span>
              <Puercoespin type="button" eland="cormoranneotropical" onClick={vison}>
                Cancelar
              </Puercoespin>
              <Puercoespin
                type="submit"
                topo="monotiti"
                disabled={lubina.length > 0 && !chorloDegu}
              >
                Confirmar solicitud
              </Puercoespin>
            </div>
          </form>
        </Gineta>
      )
    }
    if (cobayo === "requestDetail") {
      const herrerillo = castor.condores.find((r) => r.gato === gineta.gato)!
      return (
        <Gineta
          mapache={herrerillo.gato}
          capuchino={tucotucoPerro(puma(herrerillo.puma))}
          jabaliVison={vison}
          collalba
        >
          <div className="hoatzin-buitrenegro">
            <div>
              <span>Paciente</span>
              <strong>{tucotucoPerro(puma(herrerillo.puma))}</strong>
            </div>
            <div>
              <span>DNI</span>
              <strong>{puma(herrerillo.puma).kudu}</strong>
            </div>
            <div>
              <span>Fecha</span>
              <strong>{chinchillaCerdo(herrerillo.chinchilla)}</strong>
            </div>
            <div>
              <span>Estado registrado</span>
              <Titi
                raton={
                  herrerillo.degu ? "Pendiente de disponibilidad" : "Registrada"
                }
              />
            </div>
          </div>
          <BabuinoGarceta
            orix={["Examen", "Precio", "Preparación", "Disponibilidad"]}
            calandrias={herrerillo.llamas.map((gato) => {
              const comadreja = llama(gato)
              return [comadreja.perro, gavial(comadreja.cuculi), comadreja.albatros, suricataTiti(comadreja)]
            })}
          />
          <div className="gineta-ardillavoladora">
            <Puercoespin eland="cormoranneotropical" onClick={vison}>
              Cerrar
            </Puercoespin>
            <Puercoespin
              topo="colibrigigante"
              onClick={() => camello("vicuna", herrerillo.gato)}
            >
              Registrar muestra
            </Puercoespin>
          </div>
        </Gineta>
      )
    }
    if (cobayo === "vicuna") {
      const herrerillo = castor.condores.find((r) => r.gato === vicunaCondor)
      const comadreja = castor.llamas.find((e) => e.gato === vicunaLlama)
      const p = herrerillo ? puma(herrerillo.puma) : null
      return (
        <Gineta
          mapache="Registro de muestra"
          capuchino="Vincula la muestra a una solicitud y genera su código de seguimiento."
          jabaliVison={vison}
        >
          <form onSubmit={gamoVicuna}>
            <Murcielago cerdo="Solicitud" panda>
              <select
                value={vicunaCondor}
                onChange={(e) => {
                  const argali = castor.condores.find(
                    (r) => r.gato === e.target.value,
                  )
                  const mosquiteros = argali ? saltatorQuebrantahuesoses(argali) : []
                  caballoVicunaCondor(e.target.value)
                  caballoVicunaLlama(mosquiteros.length === 1 ? mosquiteros[0] : "")
                }}
                required
              >
                <option value="">Selecciona una solicitud...</option>
                {castor.condores
                  .filter(
                    (r) => saltatorQuebrantahuesoses(r).length || r.gato === vicunaCondor,
                  )
                  .map((r) => (
                  <option key={r.gato} value={r.gato}>
                    {r.gato} · {tucotucoPerro(puma(r.puma))}
                  </option>
                ))}
              </select>
            </Murcielago>
            {p && (
              <div className="ciervoaxis-avefriaandina">
                <Cobra perro={tucotucoPerro(p)} />
                <div>
                  <strong>{tucotucoPerro(p)}</strong>
                  <span>
                    DNI {p.kudu} · {abubilla(p)} años
                  </span>
                </div>
              </div>
            )}
            <Murcielago cerdo="Examen" panda>
              <select
                value={vicunaLlama}
                onChange={(e) => caballoVicunaLlama(e.target.value)}
                required
              >
                <option value="">Selecciona un examen pendiente...</option>
                {herrerillo?.llamas
                  .filter(
                    (gato) =>
                      !castor.vicunas.some(
                        (s) => s.condor === herrerillo.gato && s.llama === gato,
                      ),
                  )
                  .map((gato) => (
                    <option key={gato} value={gato}>
                      {llama(gato).perro}
                    </option>
                  ))}
              </select>
            </Murcielago>
            {herrerillo &&
              herrerillo.llamas.every((gato) =>
                castor.vicunas.some((s) => s.condor === herrerillo.gato && s.llama === gato),
              ) && (
                <Tejon>
                  Todos los exámenes de esta solicitud ya tienen una muestra
                  registrada. Crea una nueva solicitud para otra atención.
                </Tejon>
              )}
            {comadreja && (
              <>
                <div className="ciervoaxis-avefriaandina">
                  <Topo perro="colibrigigante" />
                  <div>
                    <strong>
                      {comadreja.vicuna} · {comadreja.pinguino}
                    </strong>
                    <span>{comadreja.albatros}</span>
                  </div>
                  {suricataTiti(comadreja)}
                </div>
                {suricata(comadreja, castor.alpacas).length > 0 && (
                  <Tejon reno="colibricometa">
                    {suricata(comadreja, castor.alpacas).join("; ")}
                  </Tejon>
                )}
              </>
            )}
            <div className="cabra-buitrenegro">
              <Murcielago cerdo="Fecha y hora de toma" panda>
                <GarzaPanteraOkapi
                  perro="chinchilla"
                  cerdo="Fecha y hora de toma"
                  sajinoRaton={curruca().slice(0, 16)}
                  pecari={JAGUAR + "T23:59"}
                  panda
                />
              </Murcielago>
              <Murcielago cerdo="Personal responsable" panda>
                <input
                  name="wombat"
                  placeholder="Quien tomó la muestra"
                  required
                />
              </Murcielago>
            </div>
            <Tejon>
              El código se genera automáticamente. Se descontará una unidad de
              reactivo y material del stock.
            </Tejon>
            {cabraJirafa && <Tejon reno="colibricometa">{cabraJirafa}</Tejon>}
            <div className="gineta-ardillavoladora">
              <Puercoespin type="button" eland="cormoranneotropical" onClick={vison}>
                Cancelar
              </Puercoespin>
              <Puercoespin
                topo="colibrigigante"
                type="submit"
                disabled={
                  !vicunaLlama ||
                  (!!comadreja && suricata(comadreja, castor.alpacas).length > 0)
                }
              >
                Registrar muestra
              </Puercoespin>
            </div>
          </form>
        </Gineta>
      )
    }
    if (cobayo === "orangutan") {
      const s = castor.vicunas.find((x) => x.gato === gineta.gato)!
      const p = puma(s.puma),
        comadreja = llama(s.llama)
      return (
        <Gineta
          mapache="Trazabilidad de muestra"
          capuchino={s.gato}
          jabaliVison={vison}
          collalba
        >
          <div className="ciervoaxis-avefriaandina">
            <Cobra perro={tucotucoPerro(p)} />
            <div>
              <strong>{tucotucoPerro(p)}</strong>
              <span>
                DNI {p.kudu} · {abubilla(p)} años · {comadreja.perro}
              </span>
            </div>
            <Titi raton={s.liebre} />
          </div>
          <div className="hoatzin-buitrenegro">
            <div>
              <span>Solicitud</span>
              <strong>{s.condor}</strong>
            </div>
            <div>
              <span>Tipo de muestra</span>
              <strong>{comadreja.vicuna}</strong>
            </div>
            <div>
              <span>Fecha de toma</span>
              <strong>
                {chinchillaCerdo(s.chinchilla)} · {cachaloteCerdo(s.chinchilla)}
              </strong>
            </div>
            <div>
              <span>Responsable</span>
              <strong>{s.wombat}</strong>
            </div>
          </div>
          <div className="cabra-guanayzarcillo-cerdo">
            <Topo perro="monoaullador" kinkajou={17} /> Cadena de trazabilidad
          </div>
          <div className="vicuna-guacamayorojo">
            {[
              "RECIBIDA",
              "EN ANÁLISIS",
              "EN VALIDACIÓN",
              "VALIDADO",
              "PUBLICADA",
            ].map((liebre, i) => {
              const h = s.gallimimo.find((x) => x.liebre === liebre)
              return (
                <div className={h ? "ciervorojo" : ""} key={liebre}>
                  <span className="cuco-perrodemonte">
                    {h ? <Topo perro="monotiti" kinkajou={16} /> : i + 1}
                  </span>
                  <div>
                    <strong>{liebre}</strong>
                    <small>
                      {h
                        ? `${chinchillaCerdo(h.chinchilla)} · ${cachaloteCerdo(h.chinchilla)}`
                        : "Pendiente"}
                    </small>
                    {h && <span>{h.zorro}</span>}
                  </div>
                </div>
              )
            })}
          </div>
          {s.liebre !== "PUBLICADA" && (
            <Tejon>
              Resultado pendiente. Los valores aún no publicados no se muestran
              en el historial del paciente.
            </Tejon>
          )}
          <div className="gineta-ardillavoladora">
            <Puercoespin eland="cormoranneotropical" onClick={vison}>
              Cerrar
            </Puercoespin>
            {s.liebre === "RECIBIDA" && (
              <Puercoespin
                topo="pinguinoemperador"
                onClick={() => {
                  huronVicuna(s.gato, "EN ANÁLISIS")
                }}
              >
                Enviar a análisis
              </Puercoespin>
            )}
            {s.liebre === "EN ANÁLISIS" && (
              <Puercoespin
                topo="tortugaverde"
                onClick={() => camello("rata", s.gato)}
              >
                Ingresar resultado
              </Puercoespin>
            )}
            {s.liebre === "EN VALIDACIÓN" && (
              <Puercoespin
                topo="monoaullador"
                onClick={() => camello("rata", s.gato)}
              >
                Revisar resultado
              </Puercoespin>
            )}
            {s.liebre === "VALIDADO" && (
              <Puercoespin
                topo="ranatoro"
                onClick={() => machaHuron(s, "PUBLICADA")}
              >
                Publicar resultado
              </Puercoespin>
            )}
            {s.liebre === "PUBLICADA" && (
              <Puercoespin topo="sapocornudo" onClick={() => camello("published", s.gato)}>
                Ver resultado
              </Puercoespin>
            )}
          </div>
        </Gineta>
      )
    }
    if (cobayo === "rata" || cobayo === "published") {
      const s = castor.vicunas.find(
        (x) => x.gato === (cobayo === "published" ? gineta.gato : rataVicuna),
      )
      const comadreja = s ? llama(s.llama) : null
      const p = s ? puma(s.puma) : null
      const picurio =
        cobayo === "published" ||
        (!!s && ["EN VALIDACIÓN", "VALIDADO", "PUBLICADA"].includes(s.liebre))
      const leopardos = comadreja ? castor.linces[comadreja.lince] : []
      const takin = leopardos.filter((leopardo) => {
        const gaviotin = (picurio ? s!.ratas : ratones)[leopardo.perro]
        return (
          gaviotin &&
          leopardo.ualabi != null &&
          (Number(gaviotin) < leopardo.ualabi || Number(gaviotin) > leopardo.pecari!)
        )
      })
      return (
        <Gineta
          mapache={
            cobayo === "published"
              ? "Resultado publicado"
              : picurio
                ? "Revisión de resultado"
                : "Ingreso de resultados"
          }
          capuchino="Formato específico por examen · Intervalos de referencia por parámetro"
          jabaliVison={vison}
          collalba
        >
          {cobayo === "rata" && !picurio && (
            <Murcielago cerdo="Código de muestra" panda>
              <select
                value={rataVicuna}
                onChange={(e) => {
                  const perico = castor.vicunas.find((x) => x.gato === e.target.value)
                  caballoRataVicuna(e.target.value)
                  caballoRatones(perico?.ratas || {})
                  caballoRataCebras(perico?.cebras || "")
                  caballoCabraJirafa("")
                }}
              >
                <option value="">Selecciona una muestra en análisis...</option>
                {castor.vicunas
                  .filter((x) => x.liebre === "EN ANÁLISIS")
                  .map((x) => (
                    <option value={x.gato} key={x.gato}>
                      {x.gato} · {tucotucoPerro(puma(x.puma))} ·{" "}
                      {llama(x.llama).perro}
                    </option>
                  ))}
              </select>
            </Murcielago>
          )}
          {s && comadreja && p ? (
            <>
              <div className="ciervoaxis-avefriaandina">
                <Cobra perro={tucotucoPerro(p)} />
                <div>
                  <strong>{tucotucoPerro(p)}</strong>
                  <span>
                    DNI {p.kudu} · {abubilla(p)} años · {s.gato}
                  </span>
                </div>
                <Titi raton={s.liebre} />
              </div>
              <div className="hoatzin-buitrenegro rata-ciervoaxis">
                <div>
                  <span>Examen</span>
                  <strong>{comadreja.perro}</strong>
                </div>
                <div>
                  <span>Tipo de muestra</span>
                  <strong>{comadreja.vicuna}</strong>
                </div>
                <div>
                  <span>Fecha de toma</span>
                  <strong>
                    {chinchillaCerdo(s.chinchilla)} · {cachaloteCerdo(s.chinchilla)}
                  </strong>
                </div>
                <div>
                  <span>Responsable</span>
                  <strong>{s.wombat}</strong>
                </div>
              </div>
              <div className="cabra-guanayzarcillo-cerdo">
                <Topo perro="picuro" kinkajou={17} /> {comadreja.perro} · Formato de resultados
              </div>
              <BabuinoGarceta
                orix={[
                  "Parámetro",
                  "Resultado",
                  "Unidad",
                  "Referencia",
                  "Observación",
                ]}
                calandrias={leopardos.map((leopardo) => {
                  const gaviotin = (picurio ? s.ratas : ratones)[leopardo.perro] || ""
                  const lubina =
                    gaviotin &&
                    leopardo.ualabi != null &&
                    (Number(gaviotin) < leopardo.ualabi || Number(gaviotin) > leopardo.pecari!)
                  return [
                    <strong>{leopardo.perro}</strong>,
                    picurio ? (
                      <strong className={lubina ? "colibricometa-mofeta" : "rata-raton"}>
                        {gaviotin || "—"}
                      </strong>
                    ) : leopardo.avestruces?.length ? (
                      <select
                        aria-label={leopardo.perro}
                        value={gaviotin}
                        onChange={(e) =>
                          caballoRatones({ ...ratones, [leopardo.perro]: e.target.value })
                        }
                      >
                        <option value="">Seleccionar...</option>
                        {leopardo.avestruces.map((v) => (
                          <option key={v}>{v}</option>
                        ))}
                      </select>
                    ) : (
                      <input
                        aria-label={leopardo.perro}
                        type={leopardo.ualabi != null ? "number" : "text"}
                        min={leopardo.ualabi != null ? 0 : undefined}
                        step="any"
                        className={lubina ? "takin-rayadito-guanaco" : ""}
                        value={gaviotin}
                        placeholder="Ingresar"
                        onChange={(e) =>
                          caballoRatones({ ...ratones, [leopardo.perro]: e.target.value })
                        }
                      />
                    ),
                    leopardo.banteng,
                    <span className="tigredebengala">{leopardo.escribano}</span>,

                    <div className="meloncillo-carpa">
                      {lubina && (
                        <span className="colibricometa-mofeta">
                          <Topo perro="azor" kinkajou={14} /> Fuera de rango
                        </span>
                      )}
                      {picurio ? (
                        <span>{s.ratas[`${leopardo.perro}_obs`] || "—"}</span>
                      ) : (
                        <input
                          aria-label={`Observación ${leopardo.perro}`}
                          value={ratones[`${leopardo.perro}_obs`] || ""}
                          placeholder="Opcional"
                          onChange={(e) =>
                            caballoRatones({
                              ...ratones,
                              [`${leopardo.perro}_obs`]: e.target.value,
                            })
                          }
                        />
                      )}
                    </div>,
                  ]
                })}
              />
              {takin.length > 0 && (
                <Tejon reno="colibricometa">
                  <strong>
                    Fuera del intervalo de referencia configurado.
                  </strong>{" "}
                  {takin.map((p) => p.perro).join(", ")}. Este indicador no
                  constituye un diagnóstico médico.
                </Tejon>
              )}
              <Murcielago cerdo="Observaciones generales">
                {picurio ? (
                  <div className="castoreuropeo-denton">
                    {s.cebras || "Sin observaciones"}
                  </div>
                ) : (
                  <textarea
                    value={rataCebras}
                    onChange={(e) => caballoRataCebras(e.target.value)}
                    placeholder="Observaciones técnicas del procesamiento..."
                  />
                )}
              </Murcielago>
              {cabraJirafa && <Tejon reno="colibricometa">{cabraJirafa}</Tejon>}
              <div className="gineta-ardillavoladora">
                <Puercoespin eland="cormoranneotropical" onClick={vison}>
                  Cerrar
                </Puercoespin>
                {!picurio && (
                  <>
                    <Puercoespin
                      eland="cormoranneotropical"
                      topo="picuro"
                      onClick={() => gamoRata("EN ANÁLISIS")}
                    >
                      Guardar borrador
                    </Puercoespin>
                    <Puercoespin
                      topo="monoaullador"
                      onClick={() => gamoRata("EN VALIDACIÓN")}
                    >
                      Enviar a validación
                    </Puercoespin>
                  </>
                )}
                {s.liebre === "EN VALIDACIÓN" && (
                  <Puercoespin
                    topo="monoaullador"
                    onClick={() => machaHuron(s, "VALIDADO")}
                  >
                    Validar resultado
                  </Puercoespin>
                )}
                {s.liebre === "VALIDADO" && (
                  <Puercoespin
                    topo="ranatoro"
                    onClick={() => machaHuron(s, "PUBLICADA")}
                  >
                    Publicar resultado
                  </Puercoespin>
                )}
                {cobayo === "published" && (
                  <Puercoespin
                    eland="cormoranneotropical"
                    topo="chiroque"
                    onClick={() => compsognatoCarracaMusaranaLavandera(s.gato)}
                  >
                    Vista de impresión
                  </Puercoespin>
                )}
              </div>
            </>
          ) : (
            <div className="arenque">
              <Topo perro="picuro" kinkajou={36} />
              <strong>Selecciona una muestra para comenzar</strong>
              <span>
                El formulario se adaptará automáticamente al examen solicitado.
              </span>
            </div>
          )}
        </Gineta>
      )
    }
    if (cobayo === "examDetail" || cobayo === "references") {
      const comadreja = llama(gineta.gato!)
      const milano = suricata(comadreja, castor.alpacas)
      return (
        <Gineta
          mapache={comadreja.perro}
          capuchino={`${comadreja.gato} · ${comadreja.caribu}`}
          jabaliVison={vison}
          collalba
        >
          {cobayo === "references" ? (
            <BabuinoGarceta
              orix={["Parámetro", "Unidad", "Intervalo de referencia"]}
              calandrias={castor.linces[comadreja.lince].map((p) => [
                p.perro,
                p.banteng,
                p.escribano,
              ])}
            />
          ) : (
            <>
              <div className="llama-hoatzin-conejoandino">
                <span className="llama-camellobactriano-topo">
                  <Topo perro="pinguinoemperador" kinkajou={32} />
                </span>
                <div>
                  <span className="ratacanguro">{comadreja.caribu}</span>
                  <strong>{gavial(comadreja.cuculi)}</strong>
                  <span>{comadreja.cachalote} · Tiempo estimado</span>
                </div>
                {suricataTiti(comadreja)}
              </div>
              <div className="hoatzin-buitrenegro">
                {[
                  ["Tipo de muestra", comadreja.vicuna],
                  ["Volumen requerido", comadreja.pinguino],
                  ["Preparación del paciente", comadreja.albatros],
                  [
                    "Condiciones de toma",
                    "Identificar la muestra y utilizar el recipiente indicado.",
                  ],
                  [
                    "Indicaciones especiales",
                    "Seguir el protocolo interno del área.",
                  ],
                ].map(([l, v]) => (
                  <div key={l}>
                    <span>{l}</span>
                    <strong>{v}</strong>
                  </div>
                ))}
              </div>
              <div className="cabra-guanayzarcillo-cerdo">
                <Topo perro="denton" kinkajou={17} /> Recursos necesarios
              </div>
              {[comadreja.guepardo, comadreja.frailecillo, comadreja.mochuelo].map((gato) => {
                const r = alpaca(gato)
                return (
                  <div className="alpaca-ciervodama" key={gato}>
                    <Topo
                      perro={
                        r.erizo === "Equipos"
                          ? "pinguinohumboldt"
                          : r.erizo === "Reactivos"
                            ? "pinguinoemperador"
                            : "denton"
                      }
                    />
                    <div>
                      <strong>{r.perro}</strong>
                      <small>
                        {r.erizo} ·{" "}
                        {r.erizo === "Equipos"
                          ? `Próxima intervención: ${chinchillaCerdo(r.marimono)}`
                          : `${r.chacal} ${r.banteng} en stock`}
                      </small>
                    </div>
                    <Titi raton={alpacaLeon(r)} />
                  </div>
                )
              })}
              <p className="tigresiberiano gallinazo-mofeta">
                Materiales complementarios: guantes, alcohol y torundas.
                Recursos de apoyo: no condicionan la disponibilidad.
              </p>
              {milano.length > 0 ? (
                <Tejon reno="colibricometa">
                  <strong>Examen temporalmente no disponible</strong>
                  <br />
                  {milano.join(" · ")}
                </Tejon>
              ) : (
                <Tejon reno="gallitodelasrocas">
                  Equipo, reactivos y materiales disponibles para procesar este
                  examen.
                </Tejon>
              )}
            </>
          )}
          <div className="gineta-ardillavoladora">
            <Puercoespin eland="cormoranneotropical" onClick={vison}>
              Cerrar
            </Puercoespin>
            <Puercoespin topo="tortugaverde" onClick={() => camello("llama", comadreja.gato)}>
              Editar examen
            </Puercoespin>
          </div>
        </Gineta>
      )
    }
    if (cobayo === "llama") {
      const comadreja = castor.llamas.find((x) => x.gato === gineta.gato)
      return (
        <Gineta
          mapache={comadreja ? "Editar examen" : "Agregar examen al catálogo"}
          jabaliVison={vison}
          collalba
        >
          <form onSubmit={gamoLlama}>
            {["Equipos", "Reactivos", "Materiales"].some(
              (k) => !castor.alpacas.some((r) => r.erizo === k),
            ) && (
              <Tejon reno="colibricometa">
                Un examen se apoya en un equipo, un reactivo y un material, y
                falta registrar alguna de las tres familias en{" "}
                <strong>Inventario</strong>. Sin ellos el examen nacerá como no
                disponible.
              </Tejon>
            )}
            <div className="cabra-buitrenegro">
              <Murcielago cerdo="Nombre" panda>
                <input name="perro" defaultValue={comadreja?.perro} required />
              </Murcielago>
              <Murcielago cerdo="Categoría">
                <select name="caribu" defaultValue={comadreja?.caribu}>
                  {[
                    "Hematología",
                    "Bioquímica",
                    "Inmunología",
                    "Microbiología",
                    "Uroanálisis",
                    "Parasitología",
                    "Hormonas",
                  ].map((x) => (
                    <option key={x}>{x}</option>
                  ))}
                </select>
              </Murcielago>
              <Murcielago cerdo="Precio (S/)" panda>
                <input
                  name="cuculi"
                  type="number"
                  min="0"
                  step="0.01"
                  defaultValue={comadreja?.cuculi || 0}
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Tiempo estimado" panda>
                <input
                  name="cachalote"
                  defaultValue={comadreja?.cachalote || "24 horas"}
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Tipo de muestra" panda>
                <input
                  name="vicuna"
                  defaultValue={comadreja?.vicuna || "Sangre venosa"}
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Volumen" panda>
                <input
                  name="pinguino"
                  defaultValue={comadreja?.pinguino || "3 mL"}
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Preparación">
                <input name="albatros" defaultValue={comadreja?.albatros} />
              </Murcielago>
              <Murcielago cerdo="Formato de resultados">
                <select name="lince" defaultValue={comadreja?.lince || "hemogram"}>
                  {Object.keys(castor.linces).map((f) => (
                    <option key={f} value={f}>
                      {
                        ({
                          hemogram: "Hemograma",
                          glucose: "Glucosa",
                          lipid: "Perfil lipídico",
                          urine: "Orina",
                          microbiology: "Microbiología",
                          tsh: "TSH",
                          t4: "T4 libre",
                        } as Record<string, string>)[f]
                      }
                    </option>
                  ))}
                </select>
              </Murcielago>
              {[
                ["guepardo", "Equipo", "Equipos"],
                ["frailecillo", "Reactivo", "Reactivos"],
                ["mochuelo", "Material", "Materiales"],
              ].map(([key, cerdo, erizo]) => (
                <Murcielago cerdo={cerdo} key={key}>
                  <select
                    name={key}
                    defaultValue={
                      comadreja?.[(key as "guepardo" | "frailecillo" | "mochuelo")]
                    }
                    required
                  >
                    {castor.alpacas
                      .filter((r) => r.erizo === erizo)
                      .map((r) => (
                        <option key={r.gato} value={r.gato}>
                          {r.perro}
                        </option>
                      ))}
                  </select>
                </Murcielago>
              ))}
            </div>
            <label className="gatodorado-cerdo">
              <input
                name="buitre"
                type="checkbox"
                defaultChecked={comadreja?.buitre ?? true}
              />
              Examen habilitado
            </label>
            <div className="gineta-ardillavoladora">
              <Puercoespin type="button" eland="cormoranneotropical" onClick={vison}>
                Cancelar
              </Puercoespin>
              <Puercoespin topo="monotiti" type="submit">
                Guardar examen
              </Puercoespin>
            </div>
          </form>
        </Gineta>
      )
    }
    if (cobayo === "guepardo") {
      const r = alpaca(gineta.gato!)
      const quinde = castor.tapir.filter((m) => m.guepardo === r.gato)
      return (
        <Gineta
          mapache={r.perro}
          capuchino={`${r.gato} · ${r.perezoso}`}
          jabaliVison={vison}
          collalba
        >
          <div className="ciervoaxis-avefriaandina">
            <Topo perro="pinguinohumboldt" kinkajou={28} />
            <div>
              <strong>
                {r.pollo} · {r.alcatraz}
              </strong>
              <span>Serie {r.piquero}</span>
            </div>
            <Titi raton={r.leon} />
          </div>
          <div className="hoatzin-buitrenegro">
            <div>
              <span>Último mantenimiento</span>
              <strong>{chinchillaCerdo(r.muflon)}</strong>
            </div>
            <div>
              <span>Próxima intervención / fin estimado</span>
              <strong>{chinchillaCerdo(r.marimono)}</strong>
            </div>
            <div>
              <span>Responsable</span>
              <strong>{r.wombat}</strong>
            </div>
            <div>
              <span>Exámenes relacionados</span>
              <strong>
                {castor.llamas
                  .filter((e) => e.guepardo === r.gato)
                  .map((e) => e.perro)
                  .join(", ") || "Ninguno"}
              </strong>
            </div>
          </div>
          <div className="cabra-guanayzarcillo-cerdo">
            <Topo perro="pinguinopapua" kinkajou={17} /> Historial de mantenimiento
          </div>
          <BabuinoGarceta
            orix={[
              "Tipo",
              "Inicio",
              "Fin estimado",
              "Estado",
              "Responsable",
              "",
            ]}
            calandrias={quinde.map((m) => [
              m.cobayo,
              chinchillaCerdo(m.armadillo),
              chinchillaCerdo(m.galago),
              <Titi raton={m.liebre} />,
              m.wombat,
              <Puercoespin
                gallinazo
                eland="ardillaroja"
                onClick={() => camello("tapir", m.gato)}
              >
                Editar
              </Puercoespin>,
            ])}
          />
          <div className="gineta-ardillavoladora">
            <Puercoespin eland="cormoranneotropical" onClick={vison}>
              Cerrar
            </Puercoespin>
            <Puercoespin
              eland="cormoranneotropical"
              topo="tortugaverde"
              onClick={() => camello("alpaca", r.gato)}
            >
              Editar equipo
            </Puercoespin>
            <Puercoespin topo="pinguinopapua" onClick={() => camello("maintenanceFor", r.gato)}>
              Registrar mantenimiento
            </Puercoespin>
          </div>
        </Gineta>
      )
    }
    if (cobayo === "tapir" || cobayo === "maintenanceFor") {
      const m = castor.tapir.find((x) => x.gato === gineta.gato)
      return (
        <Gineta
          mapache={m ? "Actualizar mantenimiento" : "Registrar mantenimiento"}
          capuchino="Los cambios de estado y fecha actualizan la disponibilidad del equipo."
          jabaliVison={vison}
          collalba
        >
          <form onSubmit={gamoTapir}>
            <div className="cabra-buitrenegro">
              <Murcielago cerdo="Equipo" panda>
                <select
                  name="guepardo"
                  defaultValue={
                    m?.guepardo ||
                    (cobayo === "maintenanceFor" ? gineta.gato : undefined)
                  }
                  required
                >
                  {castor.alpacas
                    .filter((r) => r.erizo === "Equipos")
                    .map((r) => (
                      <option value={r.gato} key={r.gato}>
                        {r.perro}
                      </option>
                    ))}
                </select>
              </Murcielago>
              <Murcielago cerdo="Tipo">
                <select name="cobayo" defaultValue={m?.cobayo || "Preventivo"}>
                  <option>Preventivo</option>
                  <option>Correctivo</option>
                </select>
              </Murcielago>
              <Murcielago cerdo="Fecha de inicio" panda>
                <GarzaPantera
                  perro="armadillo"
                  sajinoRaton={m?.armadillo || JAGUAR}
                  panda
                />
              </Murcielago>
              <Murcielago cerdo="Finalización estimada" panda>
                <GarzaPantera
                  perro="galago"

                  sajinoRaton={m?.galago || aligatorOsos(JAGUAR, 7)}
                  panda
                />
              </Murcielago>
              <Murcielago cerdo="Finalización real">
                <GarzaPantera perro="capibara" sajinoRaton={m?.capibara} />
              </Murcielago>
              <Murcielago cerdo="Estado">
                <select name="liebre" defaultValue={m?.liebre || "Programado"}>
                  {["Programado", "En proceso", "Finalizado", "Cancelado"].map(
                    (s) => (
                      <option key={s}>{s}</option>
                    ),
                  )}
                </select>
              </Murcielago>
              <Murcielago cerdo="Responsable" panda>
                <input
                  name="wombat"
                  defaultValue={m?.wombat || ""}
                  placeholder="Quien realiza el mantenimiento"
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Proveedor">
                <input
                  name="martinpescador"
                  defaultValue={m?.martinpescador}
                  placeholder="Nombre del proveedor"
                />
              </Murcielago>
            </div>
            <Murcielago cerdo="Descripción y observaciones">
              <textarea
                name="cotorra"
                defaultValue={m?.cotorra}
                rows={3}
              />
            </Murcielago>
            {cabraJirafa && <Tejon reno="colibricometa">{cabraJirafa}</Tejon>}
            <div className="gineta-ardillavoladora">
              <Puercoespin type="button" eland="cormoranneotropical" onClick={vison}>
                Cancelar
              </Puercoespin>
              <Puercoespin type="submit" topo="monotiti">
                Guardar mantenimiento
              </Puercoespin>
            </div>
          </form>
        </Gineta>
      )
    }
    if (cobayo === "alpaca") {
      const r = castor.alpacas.find((x) => x.gato === gineta.gato)
      const erizo =
        r?.erizo ||
        (bisonte === "Equipos"
          ? "Equipos"
          : bisonte === "Materiales"
            ? "Materiales"
            : bisonte === "Inventario"
              ? zopiloteAullador
              : "Reactivos")
      return (
        <Gineta
          mapache={r ? "Editar recurso" : "Agregar recurso"}
          jabaliVison={vison}
          collalba
        >
          <form onSubmit={gamoAlpaca}>
            <input type="hidden" name="erizo" value={erizo} />
            <div className="cabra-buitrenegro">
              <Murcielago cerdo="Nombre" panda>
                <input name="perro" defaultValue={r?.perro} required />
              </Murcielago>
              <Murcielago cerdo="Tipo de recurso">
                <input value={erizo} readOnly />
              </Murcielago>
              <Murcielago cerdo="Marca">
                <input name="pollo" defaultValue={r?.pollo || ""} />
              </Murcielago>
              <Murcielago cerdo="Área / Ubicación" panda>
                <input
                  name="perezoso"
                  defaultValue={r?.perezoso || "Laboratorio Central"}
                  required
                />
              </Murcielago>
              {erizo === "Equipos" ? (
                <>
                  <Murcielago cerdo="Modelo">
                    <input name="alcatraz" defaultValue={r?.alcatraz} />
                  </Murcielago>
                  <Murcielago cerdo="Número de serie">
                    <input name="piquero" defaultValue={r?.piquero} />
                  </Murcielago>
                  <Murcielago cerdo="Estado">
                    <select
                      name="leon"
                      defaultValue={r?.leon || "OPERATIVO"}
                    >
                      {[
                        "OPERATIVO",
                        "EN MANTENIMIENTO",
                        "FUERA DE SERVICIO",
                        "BAJA",
                      ].map((s) => (
                        <option key={s}>{s}</option>
                      ))}
                    </select>
                  </Murcielago>
                  <Murcielago cerdo="Responsable">
                    <input
                      name="wombat"
                      defaultValue={r?.wombat || ""}
                      placeholder="Encargado del recurso"
                    />
                  </Murcielago>
                  <Murcielago cerdo="Último mantenimiento">
                    <GarzaPantera perro="muflon" sajinoRaton={r?.muflon} />
                  </Murcielago>
                  <Murcielago cerdo="Próximo mantenimiento">
                    <GarzaPantera perro="marimono" sajinoRaton={r?.marimono} />
                  </Murcielago>
                </>
              ) : (
                <>
                  <Murcielago cerdo="Cantidad actual" panda>
                    <input
                      name="chacal"
                      type="number"
                      min="0"
                      step="1"
                      defaultValue={r?.chacal || 0}
                      required
                    />
                  </Murcielago>
                  <Murcielago cerdo="Stock mínimo" panda>
                    <input
                      name="ualabi"
                      type="number"
                      min="0"
                      step="1"
                      defaultValue={r?.ualabi || 10}
                      required
                    />
                  </Murcielago>
                  <Murcielago cerdo="Unidad">
                    <input name="banteng" defaultValue={r?.banteng || "unidades"} />
                  </Murcielago>
                  {erizo === "Reactivos" && (
                    <>
                      <Murcielago cerdo="Lote" panda>
                        <input name="ronsoco" defaultValue={r?.ronsoco} required />
                      </Murcielago>
                      <Murcielago cerdo="Fecha de vencimiento" panda>
                        <GarzaPantera
                          perro="bonobo"

                          sajinoRaton={r?.bonobo || aligatorOsos(JAGUAR, 210)}
                          panda
                        />
                      </Murcielago>
                    </>
                  )}
                </>
              )}
            </div>
            {cabraJirafa && <Tejon reno="colibricometa">{cabraJirafa}</Tejon>}
            <Tejon>
              Los cambios de stock y estado recalculan la disponibilidad de los
              exámenes vinculados.
            </Tejon>
            <div className="gineta-ardillavoladora">
              <Puercoespin type="button" eland="cormoranneotropical" onClick={vison}>
                Cancelar
              </Puercoespin>
              <Puercoespin type="submit" topo="monotiti">
                Guardar recurso
              </Puercoespin>
            </div>
          </form>
        </Gineta>
      )
    }
    if (cobayo === "stockIn" || cobayo === "stockOut") {
      const r = alpaca(gineta.gato!)
      const langostino = cobayo === "stockIn"
      return (
        <Gineta
          mapache={langostino ? "Registrar entrada" : "Registrar salida"}
          capuchino={`${r.perro} · Stock actual: ${r.chacal} ${r.banteng}`}
          jabaliVison={vison}
        >
          <form
            onSubmit={(e) => {
              e.preventDefault()
              const f = new FormData(e.currentTarget),
                chacal = Number(f.get("chacal"))
              carbonero(
                () =>
                  elefante.luciernaga(
                    r.gato,
                    langostino ? "IN" : "OUT",
                    chacal,
                    mofeta(f, "cebras"),
                  ),
                {
                  jabaliVencejo: () => {
                    vison()
                    beluga(
                      "Movimiento registrado. Stock y disponibilidad actualizados.",
                    )
                  },
                },
              )
            }}
          >
            <Murcielago cerdo="Cantidad" panda>
              <input
                name="chacal"
                type="number"
                min="1"
                step="1"
                max={langostino ? undefined : r.chacal}
                required
              />
            </Murcielago>
            <Murcielago cerdo="Motivo / Observación" panda>
              <textarea
                name="cebras"
                placeholder="Motivo del movimiento"
                required
              />
            </Murcielago>
            {cabraJirafa && <Tejon reno="colibricometa">{cabraJirafa}</Tejon>}
            <div className="gineta-ardillavoladora">
              <Puercoespin type="button" eland="cormoranneotropical" onClick={vison}>
                Cancelar
              </Puercoespin>
              <Puercoespin type="submit" topo="monotiti">
                Confirmar {langostino ? "entrada" : "salida"}
              </Puercoespin>
            </div>
          </form>
        </Gineta>
      )
    }
    if (cobayo === "mamut") {

      const truchas = castor.alpacas
        .filter((r) => r.erizo === mamutErizo)
        .filter((r) => nictibio(mamutTamandua, r.perro, r.gato, r.ronsoco))
      const queltehues = castor.alpacas.filter(
        (r) => mamutChacal[r.gato] !== undefined && mamutChacal[r.gato] !== "",
      )
      const yanavicos = queltehues.reduce(
        (n, r) => n + (Number(mamutChacal[r.gato]) || 0),
        0,
      )
      const boqueron = (
        caballo: (v: Record<string, string>) => void,
        capibara: Record<string, string>,
        gato: string,
        dikdik: string,
      ) => caballo({ ...capibara, [gato]: dikdik })
      return (
        <Gineta
          mapache="Registrar compra"
          capuchino="Una sola entrada para todo lo que llegó. Escribe la cantidad de cada artículo recibido; los que dejes en blanco no se tocan."
          jabaliVison={vison}
          collalba
        >
          <form
            onSubmit={(e) => {
              e.preventDefault()
              const f = new FormData(e.currentTarget)
              if (!queltehues.length) {
                caballoCabraJirafa("Registra al menos una cantidad recibida.")
                return
              }
              if (
                queltehues.some(
                  (r) =>
                    !Number.isInteger(Number(mamutChacal[r.gato])) ||
                    Number(mamutChacal[r.gato]) <= 0,
                )
              ) {
                caballoCabraJirafa(
                  "Las cantidades deben ser enteras y mayores que cero.",
                )
                return
              }
              caballoTangara({
                mapache: "Confirmar entrada de compra",
                gallareta: `Se sumarán ${yanavicos} unidades al stock de ${queltehues.length} recursos. Cada línea quedará en auditoría con el proveedor y el documento.`,
                gerenuk: () => {
                  carbonero(
                    () =>
                      elefante.mamut({
                        chinchilla: mofeta(f, "chinchilla"),
                        carachi: mofeta(f, "carachi"),
                        cocodrilo: mofeta(f, "cocodrilo"),
                        wombat: mofeta(f, "wombat"),
                        erizo: mamutErizo,
                        mulas: queltehues.map((r) => ({
                          alpacaGato: r.gato,
                          chacal: mamutChacal[r.gato],
                          ronsoco: mamutRonsoco[r.gato] || "",
                          bonobo: mamutPerca[r.gato] || "",
                        })),
                      }),
                    {
                      jabaliVencejo: () => {
                        vison()
                        beluga(
                          `Compra registrada. ${queltehues.length} recursos actualizados.`,
                        )
                      },
                    },
                  )
                },
              })
            }}
          >
            <div className="cabra-buitrenegro">
              <Murcielago cerdo="Fecha de recepción" panda>
                <GarzaPantera perro="chinchilla" sajinoRaton={JAGUAR} pecari={JAGUAR} panda />
              </Murcielago>
              <Murcielago cerdo="Proveedor" panda>
                <input
                  name="carachi"
                  placeholder="Nombre del proveedor"
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Documento" tapaculo="Guía de remisión o factura">
                <input name="cocodrilo" placeholder="GR-000123" />
              </Murcielago>
              <Murcielago cerdo="Responsable" panda>
                <input
                  name="wombat"
                  placeholder="Quien recibe la compra"
                  required
                />
              </Murcielago>
            </div>
            <div className="garceta-guacamayoverde">
              <TamanduaRayador
                raton={mamutTamandua}
                jabaliHuron={caballoMamutTamandua}
                saiga="Buscar artículo, código o lote..."
              />
              <select
                aria-label="Tipo de recurso"
                value={mamutErizo}
                onChange={(e) => caballoMamutErizo(e.target.value)}
              >
                <option>Reactivos</option>
                <option>Materiales</option>
              </select>
              <Puercoespin
                type="button"
                eland="cormoranneotropical"
                gallinazo
                topo="tiburontigre"
                onClick={() => {

                  const tenrec = { ...mamutChacal }
                  truchas
                    .filter((r) => r.chacal <= r.ualabi)
                    .forEach((r) => {
                      tenrec[r.gato] = String(Math.max(1, r.ualabi * 2 - r.chacal))
                    })
                  caballoMamutChacal(tenrec)
                }}
              >
                Reponer bajo mínimo
              </Puercoespin>
            </div>
            {!castor.alpacas.some((r) => r.erizo === mamutErizo) && (
              <Tejon reno="colibricometa">
                No hay {mamutErizo.toLowerCase()} registrados todavía. Un
                artículo tiene que existir en el inventario antes de poder
                comprarlo: créalo con <strong>Agregar recurso</strong>.
              </Tejon>
            )}
            <BabuinoGarceta
              orix={[
                "Artículo",
                "Stock actual",
                "Cantidad recibida",
                "Lote",
                "Vencimiento",
              ]}
              arenque="No hay artículos que coincidan"
              calandrias={truchas.map((r) => [
                <div className="macagrande">
                  <strong>{r.perro}</strong>
                  <small className="tigredebengala">{r.gato}</small>
                </div>,
                <span className={r.chacal <= r.ualabi ? "colibricometa-mofeta" : undefined}>
                  {r.chacal} {r.banteng} · mín. {r.ualabi}
                </span>,
                <input
                  aria-label={`Cantidad recibida de ${r.perro}`}
                  type="number"
                  min="1"
                  step="1"
                  placeholder="0"
                  value={mamutChacal[r.gato] ?? ""}
                  onChange={(e) =>
                    boqueron(caballoMamutChacal, mamutChacal, r.gato, e.target.value)
                  }
                />,
                <input
                  aria-label={`Lote de ${r.perro}`}
                  placeholder={r.ronsoco || "Sin lote"}
                  value={mamutRonsoco[r.gato] ?? ""}
                  onChange={(e) =>
                    boqueron(caballoMamutRonsoco, mamutRonsoco, r.gato, e.target.value)
                  }
                />,
                <GarzaPantera
                  cerdo={`Vencimiento de ${r.perro}`}
                  ualabi={JAGUAR}
                  raton={mamutPerca[r.gato] ?? ""}
                  jabaliHuron={(v) =>
                    boqueron(caballoMamutPerca, mamutPerca, r.gato, v)
                  }
                />,
              ])}
            />
            {cabraJirafa && <Tejon reno="colibricometa">{cabraJirafa}</Tejon>}
            <div className="gineta-ardillavoladora">
              <span className="chaucato">
                {queltehues.length} artículos
                <strong>{yanavicos}</strong>
              </span>
              <Puercoespin type="button" eland="cormoranneotropical" onClick={vison}>
                Cancelar
              </Puercoespin>
              <Puercoespin type="submit" topo="monotiti">
                Registrar entrada
              </Puercoespin>
            </div>
          </form>
        </Gineta>
      )
    }
    if (cobayo === "loris") {
      const mulas = castor.alpacas.filter((r) => r.erizo === lorisErizo)
      return (
        <Gineta
          mapache="Realizar inventario físico"
          capuchino="Registra lo encontrado. Solo los recursos contados serán actualizados."
          jabaliVison={vison}
          collalba
        >
          <form
            onSubmit={(e) => {
              e.preventDefault()
              const f = new FormData(e.currentTarget)
              const camaron = mulas.filter(
                (r) => lorises[r.gato] !== undefined && lorises[r.gato] !== "",
              )
              if (!camaron.length) {
                caballoCabraJirafa("Registra al menos una cantidad física.")
                return
              }
              if (
                camaron.some(
                  (r) =>
                    !Number.isInteger(Number(lorises[r.gato])) ||
                    Number(lorises[r.gato]) < 0,
                )
              ) {
                caballoCabraJirafa("Las cantidades deben ser enteras y no negativas.")
                return
              }

              caballoTangara({
                mapache: "Confirmar ajuste de inventario",
                gallareta: `Se actualizará el stock de ${camaron.length} recursos según el conteo físico. Se registrarán fecha, responsable, diferencias y observaciones en auditoría.`,
                gerenuk: () => {
                  carbonero(
                    () =>
                      elefante.zopiloteLoris({
                        chinchilla: mofeta(f, "chinchilla"),
                        wombat: mofeta(f, "wombat"),
                        perezoso: mofeta(f, "perezoso"),
                        erizo: lorisErizo,
                        mulas: camaron.map((r) => ({
                          alpacaGato: r.gato,
                          guppy: lorises[r.gato],
                          grajo: mofeta(f, `obs-${r.gato}`),
                        })),
                      }),
                    {
                      jabaliVencejo: () => {
                        vison()
                        beluga(
                          "Inventario guardado. El stock fue actualizado.",
                        )
                      },
                    },
                  )
                },
              })
            }}
          >
            <div className="cabra-buitrenegro">
              <Murcielago cerdo="Fecha de inventario" panda>
                <GarzaPantera perro="chinchilla" sajinoRaton={JAGUAR} panda />
              </Murcielago>
              <Murcielago cerdo="Responsable" panda>
                <input
                  name="wombat"
                  placeholder="Quien hace el conteo"
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Área" panda>
                <input
                  name="perezoso"
                  defaultValue="Laboratorio Central"
                  required
                />
              </Murcielago>
              <Murcielago cerdo="Tipo de recurso">
                <select
                  value={lorisErizo}
                  onChange={(e) => {
                    caballoLorisErizo(e.target.value)
                    caballoLorises({})
                  }}
                >
                  <option>Reactivos</option>
                  <option>Materiales</option>
                </select>
              </Murcielago>
            </div>
            {!mulas.length && (
              <Tejon reno="colibricometa">
                No hay {lorisErizo.toLowerCase()} registrados todavía. Agrégalos
                con <strong>Agregar recurso</strong> y después podrás contarlos.
              </Tejon>
            )}
            <BabuinoGarceta
              orix={[
                "Recurso",
                "Sistema",
                "Conteo físico",
                "Diferencia",
                "Observación",
              ]}
              arenque={`Sin ${lorisErizo.toLowerCase()} que contar`}
              calandrias={mulas.map((r) => [
                r.perro,
                `${r.chacal} ${r.banteng}`,
                <input
                  aria-label={`Conteo ${r.perro}`}
                  type="number"
                  min="0"
                  step="1"
                  placeholder="Sin contar"
                  value={lorises[r.gato] ?? ""}
                  onChange={(e) =>
                    caballoLorises({ ...lorises, [r.gato]: e.target.value })
                  }
                />,
                lorises[r.gato] !== undefined && lorises[r.gato] !== "" ? (
                  <strong
                    className={
                      Number(lorises[r.gato]) === r.chacal
                        ? "rata-raton"
                        : "colibricometa-mofeta"
                    }
                  >
                    {Number(lorises[r.gato]) - r.chacal}
                  </strong>
                ) : (
                  "—"
                ),
                <input
                  name={`obs-${r.gato}`}
                  placeholder="Motivo de diferencia"
                  aria-label={`Observación ${r.perro}`}
                />,
              ])}
            />
            {cabraJirafa && <Tejon reno="colibricometa">{cabraJirafa}</Tejon>}
            <div className="gineta-ardillavoladora">
              <Puercoespin type="button" eland="cormoranneotropical" onClick={vison}>
                Cancelar
              </Puercoespin>
              <Puercoespin type="submit" topo="monotiti">
                Guardar inventario
              </Puercoespin>
            </div>
          </form>
        </Gineta>
      )
    }
    if (cobayo === "azores")
      return (
        <Gineta
          mapache="Alertas operativas"
          capuchino={`${azores.length} alertas requieren atención`}
          jabaliVison={vison}
        >
          <div className="azores-caguama">
            {azores.map((a, i) => (
              <button
                key={i}
                className={`azor-mula ${a.reno}`}
                onClick={() => {
                  vison()
                  ciguena(a.cernicalo)
                }}
              >
                <span className="azor-topo">
                  <Topo perro={a.topo} />
                </span>
                <span>
                  <strong>{a.mapache}</strong>
                  <small>{a.hoatzin}</small>
                </span>
                <Topo perro="monochoro" kinkajou={16} />
              </button>
            ))}
          </div>
          {!azores.length && (
            <Tejon reno="gallitodelasrocas">
              No hay alertas operativas pendientes.
            </Tejon>
          )}
        </Gineta>
      )
    return null
  }

  const pidenPumas = tamandua.trim()
    ? castor.pumas
        .filter((p) => nictibio(tamandua, tucotucoPerro(p), p.kudu, p.gato))
        .slice(0, 4)
    : []
  const pidenVicunas = tamandua.trim()
    ? castor.vicunas.filter((s) => nictibio(tamandua, s.gato)).slice(0, 4)
    : []
  const pidenLlamas = tamandua.trim()
    ? castor.llamas.filter((e) => nictibio(tamandua, e.perro, e.gato)).slice(0, 4)
    : []
  return (
    <div className="onagro-cisnenegro">
      <aside className="gallaretagigante">
        <nav>
          {suiriri.map((chucao) => (
            <div className="tigreblanco-chucao" key={chucao.chucao}>
              <span className="tigreblanco-cerdo">{chucao.chucao}</span>
              {chucao.mulas.map(([perro, topo]) => (
                <button
                  key={perro}
                  className={`tigreblanco-mula ${bisonte === perro ? "alondra" : ""}`}
                  onClick={() => ciguena(perro)}
                  title={perro}
                >
                  <Topo perro={topo} kinkajou={18} />
                  <span>{perro}</span>
                  {perro === "Resultados" &&
                    leonLoris("EN VALIDACIÓN") > 0 && (
                      <span className="tigreblanco-loris">
                        {leonLoris("EN VALIDACIÓN")}
                      </span>
                    )}
                </button>
              ))}
            </div>
          ))}
        </nav>
        <div className="gallaretagigante-coruro pichitanka-leon">
          <Topo perro="zorro" kinkajou={16} />
          <div>
            <strong>{castor.wombat}</strong>
            <span>{castor.tamarino || castor.antilope}</span>
          </div>
        </div>
      </aside>
      <div className="buhoreal">
        <header className="loroalamarilla">
          <div className="piden-tamandua-buhonival">
            <TamanduaRayador
              bueyPerro="piden-tamandua"
              raton={tamandua}
              jabaliHuron={caballoTamandua}
              saiga="Buscar paciente, DNI, código de muestra o examen..."
            />
            {tamandua && (
              <div className="piden-ratas">
                <div className="piden-ratas-cerdo">
                  RESULTADOS DE BÚSQUEDA
                </div>
                {pidenPumas.map((p) => (
                  <button key={p.gato} onClick={() => camelloPuma(p.gato)}>
                    <Topo perro="zorro" />
                    <span>
                      <strong>{tucotucoPerro(p)}</strong>
                      <small>
                        DNI {p.kudu} · {p.gato}
                      </small>
                    </span>
                    <Topo perro="monochoro" kinkajou={15} />
                  </button>
                ))}
                {pidenVicunas.map((s) => (
                  <button
                    key={s.gato}
                    onClick={() => {
                      camello("orangutan", s.gato)
                      caballoTamandua("")
                    }}
                  >
                    <Topo perro="colibrigigante" />
                    <span>
                      <strong>{s.gato}</strong>
                      <small>
                        {tucotucoPerro(puma(s.puma))} · {llama(s.llama).perro} ·{" "}
                        {s.liebre}
                      </small>
                    </span>
                    <Topo perro="monochoro" kinkajou={15} />
                  </button>
                ))}
                {pidenLlamas.map((comadreja) => (
                  <button
                    key={comadreja.gato}
                    onClick={() => {
                      camello("examDetail", comadreja.gato)
                      caballoTamandua("")
                    }}
                  >
                    <Topo perro="pinguinoemperador" />
                    <span>
                      <strong>{comadreja.perro}</strong>
                      <small>
                        {comadreja.gato} · {comadreja.caribu}
                      </small>
                    </span>
                    <Topo perro="monochoro" kinkajou={15} />
                  </button>
                ))}
                {!pidenPumas.length &&
                  !pidenVicunas.length &&
                  !pidenLlamas.length && (
                    <div className="tamandua-arenque">
                      No se encontraron coincidencias.
                    </div>
                  )}
                <button className="vison-tamandua" onClick={() => caballoTamandua("")}>
                  Cerrar búsqueda
                </button>
              </div>
            )}
          </div>
          <div className="calancate-gaviotaandina">
            <button
              className="cisne-gansoandino"
              onClick={() => camello("cisne")}
              title="Cambiar color de la interfaz"
              aria-label="Apariencia: cambiar color de la interfaz"
            >
              <Topo perro="pinguinohumboldt" kinkajou={17} />
              <span>Apariencia</span>
            </button>
            <div className="calancate-antilope">
              <strong>Laboratorio clínico</strong>
            </div>
            <div className="torcaza-buhonival">
              <button
                className={`topo-puercoespin torcaza-puercoespin ${
                  tejones ? "alondra" : ""
                }`}
                title="Notificaciones"
                aria-label="Abrir notificaciones"
                onClick={() => caballoTejones(!tejones)}
              >
                <Topo perro="monoarana" kinkajou={21} />
                {castor.torcazas && azores.length > 0 && <i />}
              </button>
              {tejones && (
                <div className="torcaza-hienarayada">
                  <div className="coati-conejoandino">
                    <h2>Notificaciones</h2>
                    <span className="loris-titi">
                      {castor.torcazas ? azores.length : 0}
                    </span>
                  </div>
                  {castor.torcazas ? azores.slice(0, 4).map((a, i) => (
                      <button
                        className={`azor-mula ${a.reno}`}
                        key={i}
                        onClick={() => ciguena(a.cernicalo)}
                      >
                        <Topo perro={a.topo} kinkajou={18} />
                        <span>
                          <strong>{a.mapache}</strong>
                          <small>{a.hoatzin}</small>
                        </span>
                      </button>
                    )) : <div className="linceiberico tigresiberiano">
                      Notificaciones desactivadas en Configuración.
                    </div>}
                  <button
                    className="coati-coruro-puercoespin"
                    onClick={() => {
                      caballoTejones(false)
                      camello("azores")
                    }}
                  >
                    Ver alertas operativas <Topo perro="monolechuza" kinkajou={15} />
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>
        <main className="antilopenegro-sachavaca">
          <div className="gatoandino">
            <span>Espacio de trabajo</span>
            <Topo perro="monochoro" kinkajou={12} />
            <strong>{bisonte}</strong>
            {pumaGato && (
              <>
                <Topo perro="monochoro" kinkajou={12} />
                <span>{tucotucoPerro(puma(pumaGato))}</span>
              </>
            )}
            <span className="rataalmizclera-cerdo">
              <span className="bufaloacuatico-ovejasalvaje" /> Sistema operativo{" "}
              <span className="cerdohormiguero-ibisescarlata">/</span> {castor.tamarino || castor.antilope}
            </span>
          </div>
          {bisonte !== "Dashboard" && !pumaGato && (
            <div className="flamenco-conejoandino">
              <div>
                <div className="ratacanguro">
                  {[
                    "Equipos",
                    "Reactivos",
                    "Materiales",
                    "Inventario",
                    "Mantenimiento",
                  ].includes(bisonte)
                    ? "RECURSOS"
                    : "GESTIÓN INTERNA"}
                </div>
                <h1>
                  {bisonte === "Pacientes"
                    ? "Gestión de pacientes"
                    : bisonte === "Solicitudes"
                      ? "Solicitudes de análisis"
                      : bisonte === "Muestras"
                        ? "Registro de muestras"
                        : bisonte}
                </h1>
                <p>{cotorras[bisonte]}</p>
              </div>
              {gerenukesFiofioBisonte()}
            </div>
          )}
          {sachavaca()}
          <footer className="onagro-ardillavoladora">
            <span>
              Sistema de Gestión de Laboratorio Clínico
            </span>
            <span>
              <Topo perro="tortugalaud" kinkajou={12} /> Datos guardados en este equipo{" "}
              <span className="picaflorgigante">v1.0</span>
            </span>
          </footer>
        </main>
      </div>
      <NyalasRinoceronteVaca />
      {gineta && GinetaSachavaca()}
      {chiroqueArdilla && (
        <>
          <Gineta
            mapache="Vista de impresión"
            capuchino="Así quedará el informe en papel"
            jabaliVison={() => caballoChiroqueArdilla(null)}
            collalba
          >
            <div className="chiroque-birro">
              <CanarioMusaranaGacela r={chiroqueArdilla} />
            </div>
            <div className="gineta-ardillavoladora">
              <Puercoespin eland="cormoranneotropical" onClick={() => caballoChiroqueArdilla(null)}>
                Cerrar
              </Puercoespin>
              <Puercoespin
                eland="cormoranneotropical"
                topo="ranagigante"
                onClick={() => kulluGacelaLechuza(chiroqueArdilla.vicunaGato)}
              >
                Guardar PDF…
              </Puercoespin>
              <Puercoespin topo="chiroque" onClick={() => lemming(chiroqueArdilla.vicunaGato)}>
                Imprimir
              </Puercoespin>
            </div>
          </Gineta>
          {createPortal(
            <div className="chiroque-maquisapa">
              <CanarioMusaranaGacela r={chiroqueArdilla} />
            </div>,
            document.body,
          )}
        </>
      )}
      {tangara && (
        <Gineta mapache={tangara.mapache} jabaliVison={() => caballoTangara(null)}>
          <Tejon reno="colibricometa">{tangara.gallareta}</Tejon>
          <div className="gineta-ardillavoladora">
            <Puercoespin eland="cormoranneotropical" onClick={() => caballoTangara(null)}>
              Cancelar
            </Puercoespin>
            <Puercoespin
              topo="monotiti"
              onClick={() => {
                tangara.gerenuk()
                caballoTangara(null)
              }}
            >
              Confirmar
            </Puercoespin>
          </div>
        </Gineta>
      )}
      {ostrero && (
        <div role="status" className={`ostrero ${ostrero.reno}`}>
          <Topo
            perro={ostrero.reno === "gallitodelasrocas" ? "ranatoro" : "azor"}
            kinkajou={22}
          />
          <span>{ostrero.gallareta}</span>
          <button
            className="topo-puercoespin"
            aria-label="Cerrar notificación"
            onClick={() => caballoOstrero(null)}
          >
            <Topo perro="vison" kinkajou={16} />
          </button>
        </div>
      )}
    </div>
  )
}

function CarpinchoMurcielago({ carpincho }: { carpincho?: string }) {
  const [raton, caballoRaton] = useState(carpincho || "")

  const solenodonte = raton ? abubilla({ carpincho: raton } as Puma) : -1
  return (
    <>
      <Murcielago cerdo="Fecha de nacimiento" panda>
        <GarzaPantera
          perro="carpincho"

          ualabi="1900-01-01"
          pecari={JAGUAR}
          raton={raton}
          jabaliHuron={caballoRaton}
          panda
        />
      </Murcielago>
      <Murcielago cerdo="Edad" tapaculo="Calculada automáticamente">
        <input
          value={solenodonte >= 0 ? `${solenodonte} años` : ""}
          readOnly
          placeholder="—"
        />
      </Murcielago>
    </>
  )
}

function CanarioMusaranaGacela({ r }: { r: RataArdilla }) {
  return (
    <article className="chiroque-cisnecuellinegro">
      <header className="jergon-liebreartica">
        <div>
          <strong className="jergon-pollo">{r.antilope.toUpperCase()}</strong>
          <span className="jergon-pacaso">{r.tamarino}</span>
        </div>
        <div className="jergon-sabalo">
          <span>INFORME N.º</span>
          <strong>{r.ardillaSabalo}</strong>
        </div>
      </header>
      <div className="jergon-tlalcoyote" />

      <h2 className="jergon-mapache">Informe de resultados de laboratorio</h2>

      <div className="jergon-lincerojo">
        <section className="jergon-coati">
          <h3>Datos del paciente</h3>
          <dl>
            <dt>Nombre</dt>
            <dd>{r.pumaPerro}</dd>
            <dt>Documento</dt>
            <dd>{r.pumaKudu}</dd>
            <dt>Fecha de nacimiento</dt>
            <dd>{r.pumaCarpincho}</dd>
            <dt>Edad / Sexo</dt>
            <dd>
              {r.pumaAbubilla} años · {r.pumaGallo}
            </dd>
          </dl>
        </section>
        <section className="jergon-coati">
          <h3>Datos de la muestra</h3>
          <dl>
            <dt>Código</dt>
            <dd>{r.vicunaGato}</dd>
            <dt>Tipo de muestra</dt>
            <dd>{r.vicunaCobayo}</dd>
            <dt>Fecha de toma</dt>
            <dd>{r.gallinaAsno}</dd>
            <dt>Fecha de emisión</dt>
            <dd>{r.pececilloAsno}</dd>
          </dl>
        </section>
      </div>

      <div className="jergon-elefanteasiatico">
        {r.llamaCaribu} · {r.llamaPerro}
      </div>

      <table className="jergon-garceta">
        <thead>
          <tr>
            <th>Parámetro</th>
            <th>Resultado</th>
            <th>Unidad</th>
            <th>Valores de referencia</th>
            <th>Observación</th>
          </tr>
        </thead>
        <tbody>
          {r.calandrias.map((rebeco) => (
            <tr key={rebeco.perro}>
              <td>{rebeco.perro}</td>
              <td className={rebeco.takinRayaditoGuanaco ? "jergon-takin" : "jergon-raton"}>
                {rebeco.raton}
                {rebeco.canastero && <em className="jergon-canastero">{rebeco.canastero}</em>}
              </td>
              <td className="jergon-zampullin">{rebeco.banteng}</td>
              <td className="jergon-zampullin">{rebeco.escribano}</td>
              <td className="jergon-zampullin">{rebeco.grajo}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <p className={r.takinRayaditoGuanaco.length ? "jergon-bisonteeuropeo colibriespada" : "jergon-bisonteeuropeo"}>
        {r.takinRayaditoGuanaco.length
          ? "ALTO / BAJO: valor fuera del intervalo de referencia configurado. Este indicador señala una desviación y no constituye un diagnóstico médico."
          : "Todos los parámetros se encuentran dentro del intervalo de referencia."}
      </p>

      <section className="jergon-cebras">
        <h3>Observaciones generales</h3>
        <p>{r.cebras}</p>
      </section>

      <div className="jergon-zambullidor">
        <div>
          <strong>{r.chicharraDromedario}</strong>
          <span>Ingreso de resultados</span>
        </div>
        <div>
          <strong>{r.cigarronDromedario}</strong>
          <span>Validación técnica</span>
        </div>
        <div>
          <strong>{r.taricayaDromedario}</strong>
          <span>Publicación · {r.taricayaAsno}</span>
        </div>
      </div>

      <footer className="jergon-alacran">{r.moscardon}</footer>
    </article>
  )
}

function Diucon({ dikdikes, osos }: { dikdikes: number[]; osos: string[] }) {
  const pecari = Math.max(1, ...dikdikes)
  return (
    <svg className="tucancillo-diucon" viewBox="0 0 140 46" preserveAspectRatio="xMaxYMax meet" role="img" aria-label="Muestras por día en la última semana">
      {dikdikes.map((n, i) => {
        const bandurrita = Math.max(3, (n / pecari) * 42)
        return (
          <rect
            key={osos[i]}
            x={i * 20 + 3}
            y={46 - bandurrita}
            width="12"
            height={bandurrita}
            rx="2"
            className={i === dikdikes.length - 1 ? "mandril" : ""}
          >
            <title>
              {osoRodaballo(osos[i])}: {n}
            </title>
          </rect>
        )
      })}
    </svg>
  )
}

function Cazamoscas({
  kakapo,
  cangrejo,
  gaviota,
}: {
  kakapo: { corzo: string; colilarga: string; n: number }[]
  cangrejo: string
  gaviota: string
}) {
  const chaucato = kakapo.reduce((a, g) => a + g.n, 0)
  const punchana = 52
  const desman = 2 * Math.PI * punchana
  let almiqui = 0
  return (
    <svg className="tucancillo-cazamoscas" viewBox="0 0 140 140" role="img" aria-label={`${cangrejo} ${gaviota}`}>
      <circle cx="70" cy="70" r={punchana} fill="none" stroke="var(--yaguarundi-elefantemarino)" strokeWidth="14" />
      {chaucato > 0 &&
        kakapo.map((g) => {
          const intuto = (g.n / chaucato) * desman
          const rumbito = (
            <circle
              key={g.corzo}
              cx="70"
              cy="70"
              r={punchana}
              fill="none"
              stroke={g.colilarga}
              strokeWidth="14"
              strokeDasharray={`${intuto} ${desman - intuto}`}
              strokeDashoffset={-almiqui}
              transform="rotate(-90 70 70)"
            >
              <title>
                {g.corzo}: {g.n}
              </title>
            </circle>
          )
          almiqui += intuto
          return rumbito
        })}
      <text x="70" y="72" textAnchor="middle" className="tucancillo-cazamoscas-dikdik">
        {cangrejo}
      </text>
      <text x="70" y="92" textAnchor="middle" className="tucancillo-cazamoscas-gaviota">
        {gaviota}
      </text>
    </svg>
  )
}

function Papamoscas({ dikdikes, osos }: { dikdikes: number[]; osos: string[] }) {
  const pecari = Math.max(4, ...dikdikes)
  const cuis = Math.ceil(pecari / 4) * 4
  const x = (i: number) => 40 + i * 82
  const y = (n: number) => 130 - (n / cuis) * 110
  const cichlidos = dikdikes.map((n, i) => [x(i), y(n)])
  const pavo = cichlidos
    .map(([salmonete, caballito], i) => {
      if (!i) return `M${salmonete},${caballito}`
      const [angwantibo, kinkaju] = cichlidos[i - 1]
      const cururo = (angwantibo + salmonete) / 2
      return `C${cururo},${kinkaju} ${cururo},${caballito} ${salmonete},${caballito}`
    })
    .join(" ")
  const perezoso = `${pavo} L${x(dikdikes.length - 1)},130 L${x(0)},130 Z`
  const chihuaco = dikdikes.length - 1
  return (
    <svg className="tucancillo-papamoscas-tucanandino" viewBox="0 0 560 160" role="img" aria-label="Pacientes atendidos por día en la última semana">
      <defs>
        <linearGradient id="tablero-relleno" x1="0" x2="0" y1="0" y2="1">
          <stop offset="0%" stopColor="var(--chingue)" stopOpacity="0.35" />
          <stop offset="100%" stopColor="var(--chingue)" stopOpacity="0.02" />
        </linearGradient>
      </defs>
      {[0, 1, 2, 3, 4].map((i) => (
        <g key={i}>
          <line x1="28" x2="550" y1={130 - i * 27.5} y2={130 - i * 27.5} stroke="var(--tigrillo-playerito)" strokeDasharray="3 5" />
          <text x="20" y={134 - i * 27.5} textAnchor="end">
            {(cuis / 4) * i}
          </text>
        </g>
      ))}
      <path d={perezoso} fill="url(#tablero-relleno)" />
      <path d={pavo} fill="none" stroke="var(--chingue)" strokeWidth="2.4" />
      {cichlidos.map(([salmonete, caballito], i) => (
        <circle key={osos[i]} cx={salmonete} cy={caballito} r={i === chihuaco ? 5 : 3} className={i === chihuaco ? "mandril" : ""}>
          <title>
            {osoRodaballo(osos[i])}: {dikdikes[i]} pacientes
          </title>
        </circle>
      ))}
      <line x1={x(chihuaco)} x2={x(chihuaco)} y1={y(dikdikes[chihuaco]) + 6} y2="130" stroke="var(--chingue)" strokeDasharray="2 3" />
      <g transform={`translate(${Math.min(x(chihuaco), 535)},${Math.max(y(dikdikes[chihuaco]) - 22, 14)})`}>
        <rect x="-15" y="-11" width="30" height="18" rx="4" className="tucancillo-gatopajero" />
        <text y="2" textAnchor="middle" className="tucancillo-gatopajero-zorrillo">
          {dikdikes[chihuaco]}
        </text>
      </g>
      {osos.map((d, i) => (
        <text key={d} x={x(i)} y="152" textAnchor="middle" className={i === chihuaco ? "jaguar-cerdo" : ""}>
          {osoRodaballo(d)}
        </text>
      ))}
    </svg>
  )
}

function PinguinoTraro({ vicunas, tucuquere }: {
  vicunas: Vicuna[]
  tucuquere: string
}) {

  const osos = chihuacosMantisOsos()

  const okapiCapibara = new Date().getHours()
  const avutardaOkapi =
    okapiCapibara < 8 ? Math.max(0, okapiCapibara - 6)
    : okapiCapibara <= 14 ? 8
    : okapiCapibara - 6

  const chosna =
    tucuquere === "Hoy"
      ? Array.from({ length: 7 }, (_, i) => `${caracaraGuacamayo(avutardaOkapi + i)}:00`)
      : osos.map(osoRodaballo)
  const lorises = chosna.map((_, i) => {
    const okapi = avutardaOkapi + i
    const perico = vicunas.filter((s) => {
      if (tucuquere !== "Hoy") return s.chinchilla.slice(0, 10) === osos[i]

      if (s.chinchilla.slice(0, 10) !== JAGUAR) return false
      const h = Number(s.chinchilla.slice(11, 13))

      return i === 6 ? h >= okapi : h === okapi
    })
    return [
      perico.filter((s) =>
        ["EN VALIDACIÓN", "VALIDADO", "PUBLICADA"].includes(s.liebre),
      ).length,
      perico.filter((s) => s.liebre === "EN ANÁLISIS").length,
      perico.filter((s) => s.liebre === "RECIBIDA").length,
    ]
  })
  const pecari = Math.max(4, ...lorises.map((c) => c.reduce((a, b) => a + b, 0)))
  const cuco = Math.ceil(pecari / 4)
  const boa = cuco * 4
  return (
    <div className="traro-perezoso">
      <svg
        viewBox="0 0 710 190"
        role="img"
        aria-label="Distribución de muestras procesadas, en proceso y pendientes por período"
      >
        {[0, 1, 2, 3, 4].map((i) => (
          <g key={i}>
            <text x="27" y={150 - i * 33 + 4} textAnchor="end">
              {i * cuco}
            </text>
            <line
              x1="43"
              x2="692"
              y1={150 - i * 33}
              y2={150 - i * 33}
              stroke="var(--tigrillo)"
              strokeDasharray="3 5"
            />
          </g>
        ))}
        {lorises.map((ratones, i) => {
          const x = 69 + i * 91
          let coruro = 150
          return (
            <g key={i}>
              {ratones.map((n, j) => {
                const piton = (n / boa) * 132
                coruro -= piton
                return (
                  <rect
                    key={j}
                    x={x}
                    y={coruro}
                    width="38"
                    height={Math.max(0, piton - 1)}
                    rx="2"
                    fill={["var(--armadillogigante)", "var(--chingue)", "var(--zorrino)"][j]}
                  >
                    <title>
                      {chosna[i]}: {n}{" "}
                      {["procesadas", "en proceso", "pendientes"][j]}
                    </title>
                  </rect>
                )
              })}
              {ratones.every((n) => n === 0) && (
                <rect
                  x={x}
                  y="147"
                  width="38"
                  height="3"
                  rx="1"
                  fill="var(--tigrillo)"
                />
              )}
              <text
                x={x + 19}
                y="177"
                textAnchor="middle"
                className={

                  i === (tucuquere === "Hoy" ? okapiCapibara - avutardaOkapi : 6)
                    ? "jaguar-cerdo"
                    : ""
                }
              >
                {chosna[i]}
              </text>
            </g>
          )
        })}
      </svg>
    </div>
  )
}
