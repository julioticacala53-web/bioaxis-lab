import {
  useEffect,
  useMemo,
  useState,
  type CSSProperties,
  type FormEvent,
  type ReactNode,
} from "react"
import { InputFecha, InputFechaHora } from "./fecha"
import { DesplegablesDelLaboratorio } from "./desplegable"
import {
  Icon,
  Button,
  Badge,
  Panel,
  DataTable,
  Modal,
  Field,
  SearchBar,
  StatCard,
  Tabs,
  Notice,
  Avatar,
  Paginador,
} from "./components"
import {
  TODAY,
  dateLabel,
  money,
  fullName,
  age,
  availability,
  resourceStatus,
  fechaLarga,
  diaCorto,
  ultimosSieteDias,
  dosDigitos,
  sumarDias,
  lunesDeEstaSemana,
  primerDiaDelMes,
  type Database,
  type Puma,
  type Condor,
  type Vicuna,
  type Alpaca,
  type Llama,
  type Tapir,
  type ResultReport,
} from "./data"
import { createPortal } from "react-dom"
import { api, ApiError, type Mutation } from "./api"

const navigation = [
  {
    group: "ESPACIO DE TRABAJO",
    items: [
      ["Dashboard", "grid"],
      ["Pacientes", "users"],
      ["Solicitudes", "clipboard"],
      ["Muestras", "tube"],
      ["Resultados", "file"],
      ["Catálogo de exámenes", "book"],
    ],
  },
  {
    group: "RECURSOS DEL LABORATORIO",
    items: [
      ["Inventario", "box"],
      ["Equipos", "monitor"],
      ["Reactivos", "flask"],
      ["Materiales", "layers"],
      ["Mantenimiento", "wrench"],
    ],
  },
  {
    group: "ADMINISTRACIÓN",
    items: [
      ["Reportes", "chart"],
      ["Auditoría", "shield"],
      ["Configuración", "settings"],
    ],
  },
]
const descriptions: Record<string, string> = {
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
// Fecha y hora del equipo. Solo se usa como valor por defecto del campo de
// fecha y hora de toma: las marcas de tiempo que se guardan las pone el
// backend con su propio DemoClock.
const timestamp = () => `${TODAY}T${new Date().toTimeString().slice(0, 8)}`
const timeLabel = (d: string) => d.slice(11, 16)
const text = (f: FormData, k: string) => String(f.get(k) || "")
const normalize = (s: string) =>
  s.toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
const matches = (q: string, ...values: string[]) =>
  normalize(values.join(" ")).includes(normalize(q))
const ETAPAS = [
  {
    id: "atender",
    nombre: "Por atender",
    etiqueta: "Por atender",
    falta: "Crear la solicitud",
    seccion: "Pacientes",
    tono: "neutral",
    unidad: "pacientes",
  },
  {
    id: "muestra",
    nombre: "Toma de muestra",
    etiqueta: "Toma de muestra",
    falta: "Registrar la muestra",
    seccion: "Solicitudes",
    tono: "pendiente",
    unidad: "solicitudes",
  },
  {
    id: "RECIBIDA",
    nombre: "Por analizar",
    etiqueta: "Por analizar",
    falta: "Iniciar el análisis",
    seccion: "Muestras",
    tono: "recibida",
    unidad: "muestras",
  },
  {
    id: "EN ANÁLISIS",
    nombre: "Por ingresar",
    etiqueta: "Por ingresar resultados",
    falta: "Ingresar los resultados",
    seccion: "Resultados",
    tono: "análisis",
    unidad: "muestras",
  },
  {
    id: "EN VALIDACIÓN",
    nombre: "Por validar",
    etiqueta: "Por validar",
    falta: "Revisar y validar",
    seccion: "Resultados",
    tono: "validación",
    unidad: "resultados",
  },
  {
    id: "VALIDADO",
    nombre: "Por publicar",
    etiqueta: "Por publicar",
    falta: "Publicar el resultado",
    seccion: "Resultados",
    tono: "validado",
    unidad: "resultados",
  },
  {
    id: "PUBLICADA",
    nombre: "Entregados",
    etiqueta: "Entregado",
    falta: "Atención completa",
    seccion: "Resultados",
    tono: "publicada",
    unidad: "hoy",
  },
]
const indiceDeEstado = (estado: string) =>
  ETAPAS.findIndex((e) => e.id === estado)
const PESTANAS_RESULTADOS = ETAPAS.slice(3).map((e) => e.nombre)

const temas = [
  {
    id: "blanco",
    name: "Blanco clínico",
    description: "Blanco limpio con acentos verde salvia, azul y ámbar discretos.",
    bg: "#f4f6f5",
    surface: "#ffffff",
    side: "#f8faf9",
    border: "#bac8c1",
    ink: "#25322e",
    accent: "#35685f",
    blue: "#456d93",
    amber: "#896120",
  },
  {
    id: "gris",
    name: "Gris piedra",
    description: "Gris mineral de acabado mate con acentos suaves y contraste equilibrado.",
    bg: "#bfc5c7",
    surface: "#d9ddde",
    side: "#c9d0d1",
    border: "#8e9e9f",
    ink: "#273335",
    accent: "#30594f",
    blue: "#3d6083",
    amber: "#785623",
  },
  {
    id: "negro",
    name: "Oscuro carbón",
    description: "Carbón suave, sin negro puro, con salvia, azul acero y ámbar apagado.",
    bg: "#1d242c",
    surface: "#28313b",
    side: "#222b34",
    border: "#586b78",
    ink: "#e8edf2",
    accent: "#98bcae",
    blue: "#94b4d0",
    amber: "#d0b17a",
  },
]
const CLAVE_TEMA = "laboratorio-appearance-v1"
function pintarTema(tema: string) {
  const valido = temas.some((t) => t.id === tema) ? tema : "gris"
  document.documentElement.dataset.theme = valido
  try {
    localStorage.setItem(CLAVE_TEMA, valido)
  } catch {
    return valido
  }
  return valido
}

function Splash({ error }: { error?: string }) {
  return (
    <div className="splash">
      <div className="splash-card">
        <span className="splash-brand">Laboratorio clínico</span>
        <small className="splash-note">
          {error || "Cargando los datos del laboratorio..."}
        </small>
      </div>
    </div>
  )
}

export default function App() {
  const [snapshot, setDb] = useState<Database | null>(null)
  const [loadError, setLoadError] = useState("")
  const [view, setView] = useState("Dashboard")
  const [query, setQuery] = useState("")
  const [search, setSearch] = useState("")
  const [filter, setFilter] = useState("Todos")
  const [category, setCategory] = useState("Todas")
  const [estadoFiltro, setEstadoFiltro] = useState("Todos")
  const [dateFilter, setDateFilter] = useState("")
  const [modal, setModal] = useState<{
    type: string
    id?: string
  } | null>(null)
  const [patientId, setPatientId] = useState<string | null>(null)
  const [tab, setTab] = useState("Resumen")
  const [toast, setToast] = useState<{
    message: string
    tone: string
  } | null>(
    null,
  )
  const [notices, setNotices] = useState(false)
  const [appearanceStatus, setAppearanceStatus] = useState("")
  const [confirm, setConfirm] = useState<{
    title: string
    message: string
    action: () => void
  } | null>(null)
  const [customStart, setCustomStart] = useState(primerDiaDelMes)
  const [customEnd, setCustomEnd] = useState(TODAY)
  const [reportRange, setReportRange] = useState("Hoy")
  const [reportType, setReportType] = useState("Actividad del laboratorio")
  const [chartTab, setChartTab] = useState("Esta semana")
  const [configTab, setConfigTab] = useState("General")
  const [requestPatient, setRequestPatient] = useState("")
  const [chosenExams, setChosenExams] = useState<string[]>([])
  const [allowPending, setAllowPending] = useState(false)
  const [sampleRequest, setSampleRequest] = useState("")
  const [sampleExam, setSampleExam] = useState("")
  const [resultSample, setResultSample] = useState("")
  const [values, setValues] = useState<Record<string, string>>({})
  const [resultNotes, setResultNotes] = useState("")
  const [formError, setFormError] = useState("")
  const [countKind, setCountKind] = useState("Reactivos")
  const [counts, setCounts] = useState<Record<string, string>>({})
  const [inventoryTab, setInventoryTab] = useState("Reactivos")
  const [purchaseKind, setPurchaseKind] = useState("Reactivos")
  const [purchaseQty, setPurchaseQty] = useState<Record<string, string>>({})
  const [purchaseLot, setPurchaseLot] = useState<Record<string, string>>({})
  const [purchaseExp, setPurchaseExp] = useState<Record<string, string>>({})
  const [purchaseSearch, setPurchaseSearch] = useState("")
  const [printReport, setPrintReport] = useState<ResultReport | null>(null)
  // Qué página mira cada lista larga, por nombre de lista.
  const [paginas, setPaginas] = useState<Record<string, number>>({})
  // La persistencia ya no vive aquí: la base SQLite del backend guarda cada
  // cambio al instante. Esto solo trae el primer snapshot (§11.2).
  useEffect(() => {
    api
      .database()
      .then((d) => {
        pintarTema(d.appearance)
        setDb(d)
      })
      .catch(() =>
        setLoadError(
          "No se pudo conectar con el servicio local del laboratorio.",
        ),
      )
  }, [])
  useEffect(() => {
    if (toast) {
      const timer = setTimeout(() => setToast(null), 5000)
      return () => clearTimeout(timer)
    }
  }, [toast])

  // Al cambiar un filtro, la lista de debajo es otra: quedarse en la página
  // 7 de la anterior no tiene sentido y además suele dejar la tabla vacía.
  useEffect(() => {
    setPaginas({})
  }, [query, filter, category, dateFilter, view])
  const [archivoMeses, setArchivoMeses] = useState("6")
  const [archivoPrevia, setArchivoPrevia] = useState<{
    requests: number
    samples: number
    patients: number
    before: string
  } | null>(null)
  const archivoAntesDe = (() => {
    const d = new Date(`${TODAY}T12:00:00`)
    d.setMonth(d.getMonth() - Number(archivoMeses))
    return `${d.getFullYear()}-${dosDigitos(d.getMonth() + 1)}-${dosDigitos(d.getDate())}`
  })()
  useEffect(() => {
    if (view !== "Configuración") return
    api
      .purgePreview(archivoAntesDe)
      .then(setArchivoPrevia)
      .catch(() => setArchivoPrevia(null))
  }, [view, archivoAntesDe, snapshot])

  // Todos los hooks quedan por encima de esta línea.
  if (!snapshot) {
    return <Splash error={loadError} />
  }

  // Alias no nulo. Las funciones de página de más abajo son declaraciones
  // hoisted, así que TypeScript no propaga a sus cuerpos el estrechamiento de
  // la guarda de arriba; con un const ya tipado como Database sí, y los
  // cientos de accesos a db del render se quedan igual.
  const db: Database = snapshot

  /**
   * Corta una lista en bloques de diez y devuelve el que toca mirar.
   *
   * La página se recorta contra el total en cada render, así que al filtrar
   * y quedarse la lista más corta no hace falta reiniciar nada a mano: si
   * estabas en la 7 y ahora solo hay 2, miras la 2.
   */
  const POR_PAGINA = 10
  function bloque<T>(clave: string, lista: T[]) {
    const totalPaginas = Math.max(1, Math.ceil(lista.length / POR_PAGINA))
    const pagina = Math.min(paginas[clave] ?? 1, totalPaginas)
    return {
      visibles: lista.slice((pagina - 1) * POR_PAGINA, pagina * POR_PAGINA),
      pagina,
      paginas: totalPaginas,
      total: lista.length,
      porPagina: POR_PAGINA,
      ir: (n: number) => setPaginas((p) => ({ ...p, [clave]: n })),
    }
  }

  const notify = (message: string, tone = "success") =>
    setToast({ message, tone })
  const chooseAppearance = (tema: string) => {
    pintarTema(tema)
    const nombre = temas.find((t) => t.id === tema)?.name
    setDb((d) => (d ? { ...d, appearance: tema } : d))
    api
      .setAppearance(tema)
      .then((r) => {
        setDb(r.db)
        setAppearanceStatus(`${nombre} aplicado. Preferencia guardada.`)
      })
      .catch(() =>
        setAppearanceStatus(
          `${nombre} aplicado para esta sesión. No se pudo guardar la preferencia.`,
        ),
      )
  }
  /**
   * Envoltorio de toda mutación (§11.3).
   *
   * El backend valida, audita y devuelve el snapshot completo; aquí solo se
   * hace setDb y se muestra el mensaje si falla. Las reglas y sus textos ya
   * no viven en el frontend.
   *
   * `errorAsToast` es para las transiciones bloqueadas por disponibilidad:
   * mismo 422, pero el aviso va en un toast y no en el Notice del formulario
   * (§5.4).
   */
  const call = async (
    action: () => Promise<Mutation>,
    options: {
      onDone?: (recordId: string) => void
      errorAsToast?: boolean
    } = {},
  ) => {
    try {
      const result = await action()
      setDb(result.db)
      setFormError("")
      options.onDone?.(result.recordId)
    } catch (e) {
      const message =
        e instanceof ApiError
          ? e.message
          : "No se pudo completar la operación. El servicio local no responde."
      if (options.errorAsToast) {
        notify(message, "warning")
      } else {
        setFormError(message)
      }
    }
  }
  const go = (next: string, initialFilter = "Todos") => {
    setView(next)
    setPatientId(null)
    setQuery("")
    setSearch("")
    setFilter(initialFilter)
    setCategory("Todas")
    setEstadoFiltro("Todos")
    setDateFilter("")
    setNotices(false)
  }
  const open = (type: string, id?: string) => {
    setFormError("")
    if (type === "request") {
      setRequestPatient(id || patientId || "")
      setChosenExams([])
      setAllowPending(false)
    }
    if (type === "sample") {
      const solicitud = db.requests.find((r) => r.id === id)
      const pendientes = solicitud ? faltanMuestras(solicitud) : []
      setSampleRequest(id || "")
      setSampleExam(pendientes.length === 1 ? pendientes[0] : "")
    }
    if (type === "result") {
      const s = db.samples.find((x) => x.id === id)
      setResultSample(id || "")
      setValues(s?.results || {})
      setResultNotes(s?.notes || "")
    }
    if (type === "count") {
      setCounts({})
    }
    if (type === "purchase") {
      // Si vienes de Materiales, la compra empieza en Materiales.
      setPurchaseKind(view === "Materiales" ? "Materiales" : "Reactivos")
      setPurchaseQty({})
      setPurchaseLot({})
      setPurchaseExp({})
      setPurchaseSearch("")
    }
    setModal({ type, id })
  }
  const close = () => {
    setModal(null)
    setFormError("")
  }
  const patient = (id: string) => db.patients.find((p) => p.id === id)!
  const exam = (id: string) => db.exams.find((e) => e.id === id)!
  const resource = (id: string) => db.resources.find((r) => r.id === id)!
  const openPatient = (id: string) => {
    go("Pacientes")
    setPatientId(id)
    setTab("Resumen")
  }
  const patientCell = (id: string) => {
    const p = patient(id)
    return (
      <button className="patient-cell" onClick={() => openPatient(id)}>
        <Avatar name={fullName(p)} size="small" />
        <span>
          <strong>{fullName(p)}</strong>
          <small>DNI {p.dni}</small>
        </span>
      </button>
    )
  }
  const sampleLink = (s: Vicuna) => (
    <button className="code-link" onClick={() => open("trace", s.id)}>
      {s.id}
    </button>
  )
  const availabilityBadge = (e: Llama) => (
    <Badge
      value={
        availability(e, db.resources).length ? "No disponible" : "Disponible"
      }
    />
  )
  const statusCount = (state: string) =>
    db.samples.filter((s) => s.state === state).length
  const lowResources = db.resources.filter(
    (r) => r.kind !== "Equipos" && r.qty <= r.min,
  )
  // Calculadas por el backend (§6.6, §11.4).
  const alerts = db.alerts
  const filteredSamples = db.samples.filter((s) => {
    const p = patient(s.patient)
    return (
      matches(query, s.id, fullName(p), p.dni, exam(s.exam).name) &&
      (category === "Todas" || s.exam === category) &&
      (!dateFilter || s.date.startsWith(dateFilter))
    )
  })
  const fechaDeEstado = (s: Vicuna, estado = s.state) =>
    s.history.find((h) => h.state === estado)?.date || s.date
  const faltanMuestras = (r: Condor) =>
    r.exams.filter(
      (id) => !db.samples.some((s) => s.request === r.id && s.exam === id),
    )
  const etapaDeSolicitud = (r: Condor) => {
    if (faltanMuestras(r).length) return 1
    const indices = db.samples
      .filter((s) => s.request === r.id)
      .map((s) => indiceDeEstado(s.state))
    return indices.length ? Math.min(...indices) : 6
  }
  const etapaDePaciente = (id: string) => {
    const reqs = db.requests.filter((r) => r.patient === id)
    return reqs.length ? Math.min(...reqs.map(etapaDeSolicitud)) : 0
  }
  const sinSolicitud = (id: string) => !db.requests.some((r) => r.patient === id)
  const publicadosHoy = db.samples.filter(
    (s) =>
      s.state === "PUBLICADA" && fechaDeEstado(s, "PUBLICADA").startsWith(TODAY),
  )
  const conteoDeEtapa = (i: number) =>
    i === 0
      ? db.patients.filter((p) => sinSolicitud(p.id)).length
      : i === 1
        ? db.requests.filter((r) => faltanMuestras(r).length).length
        : i === 6
          ? publicadosHoy.length
          : statusCount(ETAPAS[i].id)
  const etapaBadge = (i: number) => (
    <Badge value={ETAPAS[i].tono}>{ETAPAS[i].etiqueta}</Badge>
  )
  function Flujo(actual?: string, pestana?: string) {
    return (
      <div className="flujo" aria-label="Flujo del laboratorio">
        {ETAPAS.map((e, i) => {
          const n = conteoDeEtapa(i)
          return (
            <button
              key={e.id}
              className={`flujo-paso ${actual === e.seccion && (!pestana || pestana === e.nombre) ? "en-seccion" : ""} ${n ? "" : "vacio"}`}
              onClick={() => go(e.seccion, e.nombre)}
              title={`${e.nombre}: ${e.falta}`}
            >
              <span className="flujo-numero">{i + 1}</span>
              <span className="flujo-texto">
                <strong>{dosDigitos(n)}</strong>
                <small>{e.nombre}</small>
                <em>
                  {e.seccion}
                  {i === 6 ? " · hoy" : ""}
                </em>
              </span>
            </button>
          )
        })}
      </div>
    )
  }
  /**
   * La comprobación de disponibilidad la hace el backend y devuelve un 422
   * con el motivo; aquí solo se decide que ese aviso va en un toast de
   * advertencia y no en el Notice de un formulario (§6.4.1).
   */
  const changeSample = (id: string, state: string) => {
    call(() => api.sampleState(id, state), {
      errorAsToast: true,
      onDone: () =>
        notify(
          state === "PUBLICADA"
            ? "Resultado publicado y entregado. Queda en Resultados › Entregados y en la ficha del paciente."
            : state === "VALIDADO"
              ? "Resultado validado. Pasa a Resultados › Por publicar."
              : "Análisis iniciado. La muestra pasa a Resultados › Por ingresar.",
        ),
    })
  }
  const importantChange = (s: Vicuna, state: string) => {
    setConfirm({
      title: state === "PUBLICADA" ? "Publicar resultado" : "Validar resultado",
      message:
        state === "PUBLICADA"
          ? `El resultado de ${exam(s.exam).name} para ${fullName(patient(s.patient))} se mostrará como publicado en su historial. Esta operación quedará en auditoría.`
          : "Confirma que has revisado todos los parámetros del resultado. La validación quedará anotada en la auditoría a nombre del responsable del laboratorio.",
      action: () => changeSample(s.id, state),
    })
  }
  const resultActions = (s: Vicuna) => {
    if (s.state === "PUBLICADA")
      return (
        <Button
          variant="ghost"
          small
          icon="eye"
          onClick={() => open("published", s.id)}
        >
          Ver resultado
        </Button>
      )
    if (s.state === "EN VALIDACIÓN")
      return (
        <Button
          variant="ghost"
          small
          icon="shield"
          onClick={() => open("result", s.id)}
        >
          Revisar
        </Button>
      )
    if (s.state === "VALIDADO")
      return (
        <Button
          variant="ghost"
          small
          icon="checkcircle"
          onClick={() => importantChange(s, "PUBLICADA")}
        >
          Publicar
        </Button>
      )
    return (
      <Button
        variant="ghost"
        small
        icon="edit"
        disabled={s.state === "RECIBIDA"}
        onClick={() => open("result", s.id)}
      >
        Ingresar
      </Button>
    )
  }
  const actionsForView = () => {
    if (view === "Pacientes")
      return (
        <Button
          icon="plus"
          onClick={() => open("patient")}
        >
          Nuevo paciente
        </Button>
      )
    if (view === "Solicitudes")
      return (
        <Button icon="plus" onClick={() => open("request")}>
          Nueva solicitud
        </Button>
      )
    if (view === "Muestras")
      return (
        <Button icon="plus" onClick={() => open("sample")}>
          Registrar muestra
        </Button>
      )
    if (view === "Resultados")
      return (
        <Button icon="edit" onClick={() => open("result")}>
          Ingresar resultado
        </Button>
      )
    if (view === "Catálogo de exámenes")
      return (
        <Button
          icon="plus"
          onClick={() => open("exam")}
        >
          Nuevo examen
        </Button>
      )
    if (["Inventario", "Reactivos", "Materiales"].includes(view))
      return (
        <div className="button-group">
          <Button
            variant="secondary"
            icon="clipboard"
            onClick={() => open("count")}
          >
            Realizar inventario
          </Button>
          <Button
            variant="secondary"
            icon="download"
            onClick={() => open("purchase")}
          >
            Registrar compra
          </Button>
          <Button icon="plus" onClick={() => open("resource")}>
            Agregar recurso
          </Button>
        </div>
      )
    if (view === "Equipos")
      return (
        <Button icon="plus" onClick={() => open("resource")}>
          Agregar equipo
        </Button>
      )
    if (view === "Mantenimiento")
      return (
        <Button icon="plus" onClick={() => open("maintenance")}>
          Registrar mantenimiento
        </Button>
      )
    return null
  }

  function Dashboard() {
    const ayer = sumarDias(TODAY, -1)
    const semana = ultimosSieteDias()
    const delDia = <T,>(lista: T[], fecha: (x: T) => string, dia: string) =>
      lista.filter((x) => fecha(x).startsWith(dia))
    const pacientesDelDia = (dia: string) =>
      new Set(delDia(db.requests, (r) => r.date, dia).map((r) => r.patient)).size
    const muestrasDelDia = (dia: string) =>
      delDia(db.samples, (s) => s.date, dia).length
    const entregadosDelDia = (dia: string) =>
      db.samples.filter(
        (s) =>
          s.state === "PUBLICADA" && fechaDeEstado(s, "PUBLICADA").startsWith(dia),
      ).length
    const muestrasSemana = semana.map(muestrasDelDia)
    const pacientesSemana = semana.map(pacientesDelDia)
    const tendencia = (hoy: number, antes: number) => {
      const d = hoy - antes
      if (!d) return <span className="tablero-tendencia igual">Igual que ayer</span>
      return (
        <span className={`tablero-tendencia ${d > 0 ? "sube" : "baja"}`}>
          {d > 0 ? "↑" : "↓"} {Math.abs(d)} {d > 0 ? "más" : "menos"} que ayer
        </span>
      )
    }
    const tarjetas = [
      {
        valor: pacientesDelDia(TODAY),
        texto: "Pacientes atendidos hoy",
        icono: "user",
        tono: "verde",
        pie: tendencia(pacientesDelDia(TODAY), pacientesDelDia(ayer)),
        ir: () => go("Pacientes"),
      },
      {
        valor: muestrasDelDia(TODAY),
        texto: "Muestras tomadas hoy",
        icono: "tube",
        tono: "azul",
        pie: tendencia(muestrasDelDia(TODAY), muestrasDelDia(ayer)),
        ir: () => go("Muestras"),
      },
      {
        valor: entregadosDelDia(TODAY),
        texto: "Resultados entregados hoy",
        icono: "checkcircle",
        tono: "ambar",
        pie: tendencia(entregadosDelDia(TODAY), entregadosDelDia(ayer)),
        ir: () => go("Resultados", "Entregados"),
      },
    ]
    const deHoy = delDia(db.samples, (s) => s.date, TODAY)
    const grupos = [
      {
        nombre: "Entregadas",
        color: "var(--primary)",
        n: deHoy.filter((s) => s.state === "PUBLICADA").length,
      },
      {
        nombre: "En proceso",
        color: "var(--blue)",
        n: deHoy.filter((s) =>
          ["EN ANÁLISIS", "EN VALIDACIÓN", "VALIDADO"].includes(s.state),
        ).length,
      },
      {
        nombre: "Por analizar",
        color: "var(--amber)",
        n: deHoy.filter((s) => s.state === "RECIBIDA").length,
      },
    ]
    const avance = deHoy.length
      ? Math.round((grupos[0].n / deHoy.length) * 100)
      : 0
    const estadoDelDia = !deHoy.length
      ? "Sin muestras hoy"
      : avance === 100
        ? "Todo entregado"
        : grupos[2].n > grupos[0].n + grupos[1].n
          ? "Con muestras en espera"
          : "En marcha"
    const desde30 = sumarDias(TODAY, -29)
    const pedidos = new Map<string, number>()
    db.requests
      .filter((r) => r.date.slice(0, 10) >= desde30)
      .flatMap((r) => r.exams)
      .forEach((id) => pedidos.set(id, (pedidos.get(id) || 0) + 1))
    const masPedidos = [...pedidos.entries()]
      .sort((a, b) => b[1] - a[1])
      .slice(0, 5)
    const maxPedidos = Math.max(1, ...masPedidos.map(([, n]) => n))
    const equipos = db.resources.filter((r) => r.kind === "Equipos")
    const operativos = equipos.filter((r) => r.status === "OPERATIVO").length
    const disponibles = db.exams.filter(
      (e) => !availability(e, db.resources).length,
    ).length
    const actividad = [...db.audit]
      .filter((a) => a.date.startsWith(TODAY))
      .sort((a, b) => b.date.localeCompare(a.date))
      .slice(0, 6)
    return (
      <>
        <div className="page-heading dashboard-heading">
          <div>
            <div className="eyebrow">
              <span className="live-dot" /> CENTRO DE OPERACIONES
            </div>
            <h1>
              Todo tu laboratorio, en un solo lugar<span>.</span>
            </h1>
            <p>Buen día. Esto es lo que necesita tu atención hoy.</p>
          </div>
          <div className="date-chip">
            <Icon name="calendar" size={17} />
            <span>{fechaLarga(TODAY)}</span>
          </div>
        </div>

        <section className="tablero-kpis" aria-label="Indicadores del día">
          {tarjetas.map((t) => (
            <button className="tablero-kpi" key={t.texto} onClick={t.ir}>
              <div>
                <strong>{t.valor}</strong>
                <span>{t.texto}</span>
              </div>
              <div className="tablero-kpi-pie">
                {t.pie}
                <i className={`tablero-kpi-icono ${t.tono}`}>
                  <Icon name={t.icono} size={20} />
                </i>
              </div>
            </button>
          ))}
          <button className="tablero-kpi" onClick={() => go("Reportes")}>
            <div>
              <strong>{muestrasSemana.reduce((a, b) => a + b, 0)}</strong>
              <span>Muestras en los últimos 7 días</span>
            </div>
            <Mini valores={muestrasSemana} dias={semana} />
          </button>
        </section>

        <div className="tablero-titulo">
          <h2>Flujo de atención</h2>
          <span>Cuántas personas hay en cada etapa. Pulsa una etapa para ir a su sección.</span>
        </div>
        {Flujo()}

        <div className="quick-actions">
          <span className="quick-label">ACCESOS RÁPIDOS</span>
          {[
            ["Nuevo paciente", "user", "patient"],
            ["Nueva solicitud", "clipboard", "request"],
            ["Registrar muestra", "tube", "sample"],
            ["Ingresar resultado", "edit", "result"],
          ].map(([label, icon, type]) => (
            <button key={type} onClick={() => open(type)}>
              <Icon name={icon} size={17} />
              {label}
              <Icon name="plus" size={14} />
            </button>
          ))}
        </div>

        <div className="dashboard-columns">
          <div className="dashboard-main">
            <Panel
              title="Actividad del laboratorio"
              subtitle="Muestras registradas según su estado de procesamiento"
              action={
                <select
                  className="compact-select"
                  aria-label="Período de actividad"
                  value={chartTab}
                  onChange={(e) => setChartTab(e.target.value)}
                >
                  <option>Esta semana</option>
                  <option>Hoy</option>
                </select>
              }
              className="activity-chart"
            >
              <div className="chart-summary">
                <div>
                  <strong>
                    {chartTab === "Hoy"
                      ? deHoy.length
                      : muestrasSemana.reduce((a, b) => a + b, 0)}
                  </strong>
                  <span>
                    {chartTab === "Hoy"
                      ? "muestras registradas hoy"
                      : "muestras en los últimos 7 días"}
                  </span>
                </div>
                <div className="chart-legend">
                  <span>
                    <i className="green-dot" />
                    Procesadas
                  </span>
                  <span>
                    <i className="blue-dot" />
                    En proceso
                  </span>
                  <span>
                    <i className="amber-dot" />
                    Pendientes
                  </span>
                </div>
              </div>
              <VolumeChart samples={db.samples} mode={chartTab} />
              <div className="chart-footer">
                <span>
                  <Icon name="info" size={14} />
                  Pasa el cursor por una barra para ver el detalle
                </span>
                <button onClick={() => go("Reportes")}>
                  Ver reporte detallado <Icon name="arrow" size={14} />
                </button>
              </div>
            </Panel>

            <div className="tablero-par">
              <Panel
                title="Avance de hoy"
                subtitle="Muestras del día según cuánto les falta"
                className="tablero-avance"
              >
                <div className="tablero-avance-cuerpo">
                  <Dona grupos={grupos} centro={`${avance}%`} pie="entregado" />
                  <div className="tablero-avance-datos">
                    <small>Estado del día</small>
                    <strong>
                      <i
                        className={
                          !deHoy.length
                            ? "tablero-punto-neutro"
                            : avance === 100
                              ? "green-dot"
                              : "amber-dot"
                        }
                      />
                      {estadoDelDia}
                    </strong>
                    <ul>
                      {grupos.map((g) => (
                        <li key={g.nombre}>
                          <i style={{ background: g.color }} />
                          <span>{g.nombre}</span>
                          <b>{g.n}</b>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>
              </Panel>
              <Panel
                title="Pacientes atendidos"
                subtitle="Personas con solicitud, en los últimos 7 días"
                className="tablero-curva"
              >
                <Curva valores={pacientesSemana} dias={semana} />
              </Panel>
            </div>

            <Panel
              title="Exámenes más solicitados"
              subtitle="Últimos 30 días"
              action={
                <button className="tablero-enlace" onClick={() => go("Catálogo de exámenes")}>
                  Ver catálogo <Icon name="arrow" size={14} />
                </button>
              }
              className="tablero-ranking"
            >
              {masPedidos.length ? (
                <ol>
                  {masPedidos.map(([id, n], i) => (
                    <li key={id}>
                      <span className="tablero-puesto">{i + 1}</span>
                      <span className="tablero-ranking-nombre">
                        <strong>{exam(id).name}</strong>
                        <small>{exam(id).category}</small>
                      </span>
                      <span className="tablero-ranking-barra">
                        <span style={{ width: `${(n / maxPedidos) * 100}%` }} />
                      </span>
                      <b>{n}</b>
                    </li>
                  ))}
                </ol>
              ) : (
                <p className="tablero-vacio">
                  Aún no hay solicitudes en los últimos 30 días.
                </p>
              )}
            </Panel>
          </div>

          <div className="dashboard-side">
            <Panel
              title="Alertas operativas"
              action={<span className="count-badge">{alerts.length}</span>}
              className="alerts-panel"
            >
              <div className="alerts-list">
                {alerts.length ? (
                  alerts.slice(0, 4).map((a, i) => (
                    <button
                      className={`alert-item ${a.tone}`}
                      key={i}
                      onClick={() => go(a.module)}
                    >
                      <span className="alert-icon">
                        <Icon name={a.icon} size={18} />
                      </span>
                      <span>
                        <strong>{a.title}</strong>
                        <small>{a.detail}</small>
                      </span>
                      <Icon name="chevron" size={14} />
                    </button>
                  ))
                ) : (
                  <p className="tablero-vacio">Sin alertas. Todo en orden.</p>
                )}
              </div>
              {alerts.length > 0 && (
                <button
                  className="panel-bottom-button"
                  onClick={() => open("alerts")}
                >
                  Ver todas las alertas <Icon name="arrow" size={15} />
                </button>
              )}
            </Panel>

            <Panel
              title="Estado del laboratorio"
              subtitle="Recursos y disponibilidad"
              className="lab-status"
            >
              <div className="lab-state-row">
                <span>
                  <Icon name="monitor" size={16} /> Equipos operativos
                </span>
                <strong>
                  {operativos}
                  <small> / {equipos.length}</small>
                </strong>
              </div>
              <div className="segmented-bar">
                {equipos.map((r) => (
                  <span
                    className={r.status === "OPERATIVO" ? "good" : "warn"}
                    key={r.id}
                    title={`${r.name}: ${r.status}`}
                  />
                ))}
              </div>
              <div className="lab-state-row">
                <span>
                  <Icon name="book" size={16} /> Exámenes disponibles
                </span>
                <strong>
                  {disponibles}
                  <small> / {db.exams.length}</small>
                </strong>
              </div>
              <div className="availability-track">
                <span
                  style={{
                    width: `${db.exams.length ? (disponibles / db.exams.length) * 100 : 0}%`,
                  }}
                />
              </div>
              <div className="lab-state-row">
                <span>
                  <Icon name="box" size={16} /> Insumos con stock bajo
                </span>
                <strong>{lowResources.length}</strong>
              </div>
              <div className="status-note">
                <i className="amber-dot" />
                {db.exams.length - disponibles} exámenes con disponibilidad
                afectada
              </div>
            </Panel>

            <Panel
              title="Actividad de hoy"
              subtitle={dateLabel(TODAY)}
              action={<Icon name="clock" size={16} />}
              className="timeline-panel"
            >
              {actividad.length ? (
                <div className="activity-timeline">
                  {actividad.map((a, i) => (
                    <button onClick={() => go(a.module)} key={a.id}>
                      <span
                        className={`timeline-point ${i === 0 ? "current" : ""}`}
                      />
                      <time>{timeLabel(a.date)}</time>
                      <div>
                        <strong>{a.action}</strong>
                        <small>{a.record}</small>
                      </div>
                    </button>
                  ))}
                </div>
              ) : (
                <p className="tablero-vacio">Todavía no hay movimientos hoy.</p>
              )}
              <button
                className="panel-bottom-button"
                onClick={() => go("Auditoría")}
              >
                Ver registro de actividad <Icon name="arrow" size={15} />
              </button>
            </Panel>
          </div>
        </div>
      </>
    )
  }

  function PatientPage() {
    if (patientId) {
      const p = patient(patientId)
      const ps = db.samples.filter((s) => s.patient === p.id)
      const reqs = db.requests.filter((r) => r.patient === p.id)
      return (
        <>
          <button className="back-link" onClick={() => setPatientId(null)}>
            <Icon name="arrow" size={15} className="rotate" />
            Volver a pacientes
          </button>
          <div className="patient-profile panel">
            <Avatar name={fullName(p)} size="large" />
            <div>
              <span className="eyebrow">{p.id}</span>
              <h1>{fullName(p)}</h1>
              <p>
                DNI {p.dni} <span>·</span> {age(p)} años <span>·</span> {p.sex}
              </p>
            </div>
            <div className="profile-contact">
              <span>
                <Icon name="phone" size={15} />
                {p.phone || "Sin celular"}
              </span>
              <span>
                <Icon name="mail" size={15} />
                {p.email || "Sin correo"}
              </span>
            </div>
            <Button
              icon="plus"
              onClick={() => open("request", p.id)}
            >
              Nueva solicitud
            </Button>
          </div>
          <Tabs
            items={[
              "Resumen",
              "Solicitudes",
              "Muestras",
              "Resultados",
              "Historial",
            ]}
            active={tab}
            onChange={setTab}
          />
          {tab === "Resumen" && (
            <div className="two-columns">
              <Panel
                title="Información del paciente"
                action={
                  <Button
                    variant="ghost"
                    small
                    icon="edit"
                    onClick={() => open("patient", p.id)}
                  >
                    Editar
                  </Button>
                }
              >
                <div className="detail-grid">
                  {[
                    ["DNI", p.dni],
                    ["Código de paciente", p.id],
                    ["Fecha de nacimiento", dateLabel(p.birth)],
                    ["Edad", `${age(p)} años`],
                    ["Sexo", p.sex],
                    ["Celular", p.phone],
                    ["Correo electrónico", p.email],
                    ["Dirección", p.address],
                    ["Observaciones", p.notes],
                  ].map(([l, v]) => (
                    <div key={l}>
                      <span>{l}</span>
                      <strong>{v || "No registrado"}</strong>
                    </div>
                  ))}
                </div>
              </Panel>
              <Panel title="Resumen de atenciones">
                <div className="summary-numbers">
                  <div>
                    <strong>{reqs.length}</strong>
                    <span>Solicitudes</span>
                  </div>
                  <div>
                    <strong>{ps.length}</strong>
                    <span>Muestras</span>
                  </div>
                  <div>
                    <strong>
                      {ps.filter((s) => s.state === "PUBLICADA").length}
                    </strong>
                    <span>Publicados</span>
                  </div>
                </div>
                <div className="padded">
                  <Notice>
                    Los exámenes sin procesar se muestran como{" "}
                    <strong>Resultado pendiente</strong>. No se generan valores
                    automáticamente.
                  </Notice>
                  {ps.map((s) => (
                    <button
                      className="result-summary"
                      key={s.id}
                      onClick={() =>
                        open(
                          s.state === "PUBLICADA" ? "published" : "trace",
                          s.id,
                        )
                      }
                    >
                      <div>
                        <strong>{exam(s.exam).name}</strong>
                        <small>{s.id}</small>
                      </div>
                      <Badge
                        value={
                          s.state === "PUBLICADA"
                            ? "Publicado"
                            : "Resultado pendiente"
                        }
                      />
                      <Icon name="chevron" size={16} />
                    </button>
                  ))}
                </div>
              </Panel>
            </div>
          )}
          {tab === "Solicitudes" && (
            <Panel>
              <DataTable
                columns={[
                  "Solicitud",
                  "Fecha",
                  "Exámenes",
                  "Estado",
                  "Acciones",
                ]}
                rows={reqs.map((r) => [
                  <span className="mono">{r.id}</span>,
                  dateLabel(r.date),
                  r.exams.map((x) => exam(x).name).join(", "),
                  <Badge
                    value={
                      r.pending ? "Pendiente de disponibilidad" : "Registrada"
                    }
                  />,
                  <Button
                    small
                    variant="ghost"
                    icon="tube"
                    onClick={() => open("sample", r.id)}
                  >
                    Registrar muestra
                  </Button>,
                ])}
              />
            </Panel>
          )}
          {tab === "Muestras" && (
            <Panel>
              <DataTable
                columns={[
                  "Código de muestra",
                  "Tipo de muestra",
                  "Fecha",
                  "Examen",
                  "Estado",
                  "Acción",
                ]}
                rows={ps.map((s) => [
                  sampleLink(s),
                  exam(s.exam).sample,
                  dateLabel(s.date),
                  exam(s.exam).name,
                  <Badge value={s.state} />,
                  <Button
                    small
                    variant="ghost"
                    onClick={() => open("trace", s.id)}
                  >
                    Trazabilidad
                  </Button>,
                ])}
              />
            </Panel>
          )}
          {tab === "Resultados" && (
            <Panel
              title="Resultados del paciente"
              subtitle="Solo los resultados publicados están disponibles para consulta"
            >
              <DataTable
                columns={["Muestra", "Examen", "Fecha", "Resultado", "Acción"]}
                rows={ps.map((s) => [
                  sampleLink(s),
                  exam(s.exam).name,
                  dateLabel(s.date),
                  <Badge
                    value={
                      s.state === "PUBLICADA"
                        ? "Publicado"
                        : "Resultado pendiente"
                    }
                  />,
                  s.state === "PUBLICADA" ? (
                    <Button
                      variant="ghost"
                      small
                      icon="eye"
                      onClick={() => open("published", s.id)}
                    >
                      Consultar
                    </Button>
                  ) : (
                    <span className="muted">Resultado pendiente</span>
                  ),
                ])}
              />
            </Panel>
          )}
          {tab === "Historial" && (
            <Panel title="Historial de atenciones">
              <div className="history-list">
                {[...ps].reverse().map((s) => (
                  <div key={s.id}>
                    <span className="history-icon">
                      <Icon name="tube" />
                    </span>
                    <div>
                      <strong>{exam(s.exam).name}</strong>
                      <p>
                        {s.id} · {dateLabel(s.date)} · {s.responsible}
                      </p>
                      <Badge value={s.state} />
                    </div>
                    <Button
                      variant="ghost"
                      small
                      onClick={() => open("trace", s.id)}
                    >
                      Ver trazabilidad
                    </Button>
                  </div>
                ))}
                {!ps.length && (
                  <div className="empty">
                    El paciente aún no tiene atenciones.
                  </div>
                )}
              </div>
            </Panel>
          )}
        </>
      )
    }
    const pestana =
      filter === "Todos los pacientes" ? "Todos los pacientes" : "Por atender"
    const items = db.patients
      .filter(
        (p) =>
          (pestana === "Todos los pacientes" || sinSolicitud(p.id)) &&
          matches(query, fullName(p), p.dni, p.id, p.phone) &&
          (!dateFilter || p.created === dateFilter),
      )
      .map((p) => ({ p, etapa: etapaDePaciente(p.id) }))
      .sort((a, b) =>
        pestana === "Por atender"
          ? a.p.created.localeCompare(b.p.created) || a.p.id.localeCompare(b.p.id)
          : a.etapa - b.etapa || fullName(a.p).localeCompare(fullName(b.p)),
      )
    const pag = bloque("pacientes", items)
    return (
      <>
        {Flujo("Pacientes")}
        <Tabs
          items={["Por atender", "Todos los pacientes"]}
          counts={{
            "Por atender": conteoDeEtapa(0),
            "Todos los pacientes": db.patients.length,
          }}
          active={pestana}
          onChange={setFilter}
        />
        <Panel>
          <div className="table-toolbar">
            <SearchBar
              value={query}
              onChange={setQuery}
              placeholder="Buscar por nombre, DNI, celular o código..."
            />
            <InputFecha
              label="Fecha de registro"
              value={dateFilter}
              onChange={setDateFilter}
            />
            <Button
              variant="secondary"
              small
              icon="refresh"
              onClick={() => {
                setQuery("")
                setDateFilter("")
              }}
            >
              Limpiar filtros
            </Button>
          </div>
          <DataTable
            columns={[
              "Paciente / Código",
              "DNI",
              "Edad",
              "Celular",
              "Registrado",
              "Etapa",
              "Acciones",
            ]}
            rows={pag.visibles.map(({ p, etapa }) => [
              <div className="stacked">
                {patientCell(p.id)}
                <small className="indent">{p.id}</small>
              </div>,
              <span className="mono">{p.dni}</span>,
              `${age(p)} años`,
              p.phone || "—",
              dateLabel(p.created),
              etapaBadge(etapa),
              <div className="button-group">
                {etapa === 0 && (
                  <Button small icon="plus" onClick={() => open("request", p.id)}>
                    Crear solicitud
                  </Button>
                )}
                <Button
                  variant="ghost"
                  small
                  icon="eye"
                  onClick={() => openPatient(p.id)}
                >
                  Ver ficha
                </Button>
              </div>,
            ])}
          />
          <Paginador {...pag} />
          <div className="table-footer">
            {pestana === "Por atender"
              ? items.length
                ? `${items.length} pacientes esperan su solicitud · al crearla pasan a Toma de muestra`
                : "Nadie espera solicitud. Los pacientes con solicitud siguen su camino en las siguientes etapas."
              : `${items.length} pacientes · ordenados por la etapa en la que están`}
          </div>
        </Panel>
      </>
    )
  }
  function RequestsPage() {
    const pestana = filter === "Historial" ? "Historial" : "Toma de muestra"
    const base = db.requests.filter((r) =>
      matches(
        query,
        r.id,
        fullName(patient(r.patient)),
        patient(r.patient).dni,
      ),
    )
    const items =
      pestana === "Historial"
        ? [...base].sort(
            (a, b) => b.date.localeCompare(a.date) || b.id.localeCompare(a.id),
          )
        : base
            .filter((r) => faltanMuestras(r).length)
            .sort(
              (a, b) => a.date.localeCompare(b.date) || a.id.localeCompare(b.id),
            )
    const pag = bloque("solicitudes", items)
    return (
      <>
        {Flujo("Solicitudes")}
        <Tabs
          items={["Toma de muestra", "Historial"]}
          counts={{
            "Toma de muestra": conteoDeEtapa(1),
            Historial: db.requests.length,
          }}
          active={pestana}
          onChange={setFilter}
        />
        <Panel>
          <div className="table-toolbar">
            <SearchBar
              value={query}
              onChange={setQuery}
              placeholder="Buscar solicitud, paciente o DNI..."
            />
          </div>
          <DataTable
            columns={[
              "Solicitud",
              "Paciente",
              "Exámenes",
              "Fecha",
              "Importe",
              pestana === "Historial" ? "Etapa" : "Estado",
              "Acciones",
            ]}
            rows={pag.visibles.map((r) => {
              const faltan = faltanMuestras(r)
              return [
                <button
                  className="code-link"
                  onClick={() => open("requestDetail", r.id)}
                >
                  {r.id}
                </button>,
                patientCell(r.patient),
                <div className="stacked">
                  {r.exams.map((id) => (
                    <span key={id} className={faltan.includes(id) ? "" : "muted"}>
                      {exam(id).name}
                      {faltan.includes(id) ? "" : " · muestra tomada"}
                    </span>
                  ))}
                </div>,
                dateLabel(r.date),
                money(r.exams.reduce((n, id) => n + exam(id).price, 0)),
                pestana === "Historial" ? (
                  etapaBadge(etapaDeSolicitud(r))
                ) : r.pending ? (
                  <Badge value="Pendiente de disponibilidad" />
                ) : (
                  <Badge value="pendiente">
                    {faltan.length === 1
                      ? "Falta 1 muestra"
                      : `Faltan ${faltan.length} muestras`}
                  </Badge>
                ),
                pestana === "Historial" ? (
                  <Button
                    small
                    variant="ghost"
                    icon="eye"
                    onClick={() => open("requestDetail", r.id)}
                  >
                    Ver solicitud
                  </Button>
                ) : (
                  <Button small icon="tube" onClick={() => open("sample", r.id)}>
                    Registrar muestra
                  </Button>
                ),
              ]
            })}
          />
          <Paginador {...pag} />
          <div className="table-footer">
            {pestana === "Historial"
              ? `${items.length} solicitudes · de la más reciente a la más antigua`
              : items.length
                ? `${items.length} solicitudes esperan muestra · al registrarla pasan a Por analizar`
                : "No hay solicitudes esperando muestra."}
          </div>
        </Panel>
      </>
    )
  }

  function SampleFilters(conEstado: boolean) {
    return (
      <div className="table-toolbar">
        <SearchBar
          value={query}
          onChange={setQuery}
          placeholder="Buscar muestra, paciente o DNI..."
        />
        <select
          aria-label="Filtrar por examen"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
        >
          <option value="Todas">Todos los exámenes</option>
          {db.exams.map((e) => (
            <option key={e.id} value={e.id}>
              {e.name}
            </option>
          ))}
        </select>
        {conEstado && (
          <select
            aria-label="Filtrar por estado"
            value={estadoFiltro}
            onChange={(e) => setEstadoFiltro(e.target.value)}
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
        <InputFecha
          label="Fecha de toma"
          value={dateFilter}
          onChange={setDateFilter}
        />
      </div>
    )
  }
  function SamplesPage() {
    const pestana = filter === "Historial" ? "Historial" : "Por analizar"
    const items =
      pestana === "Historial"
        ? filteredSamples
            .filter((s) => estadoFiltro === "Todos" || s.state === estadoFiltro)
            .sort((a, b) => b.date.localeCompare(a.date))
        : filteredSamples
            .filter((s) => s.state === "RECIBIDA")
            .sort((a, b) => a.date.localeCompare(b.date))
    const pag = bloque("muestras", items)
    return (
      <>
        {Flujo("Muestras")}
        <Tabs
          items={["Por analizar", "Historial"]}
          counts={{
            "Por analizar": statusCount("RECIBIDA"),
            Historial: db.samples.length,
          }}
          active={pestana}
          onChange={setFilter}
        />
        <Panel>
          {SampleFilters(pestana === "Historial")}
          <DataTable
            columns={[
              "Código de muestra",
              "Paciente",
              "Examen",
              "Tipo",
              "Fecha de toma",
              pestana === "Historial" ? "Estado" : "Etapa",
              "Responsable",
              "Acción",
            ]}
            rows={pag.visibles.map((s) => [
              sampleLink(s),
              patientCell(s.patient),
              exam(s.exam).name,
              exam(s.exam).sample,
              <div className="stacked">
                <span>{dateLabel(s.date)}</span>
                <small>{timeLabel(s.date)}</small>
              </div>,
              pestana === "Historial" ? (
                <Badge value={s.state} />
              ) : (
                etapaBadge(2)
              ),
              s.responsible,
              <div className="button-group">
                {s.state === "RECIBIDA" && (
                  <Button
                    small
                    icon="flask"
                    onClick={() => changeSample(s.id, "EN ANÁLISIS")}
                  >
                    Iniciar análisis
                  </Button>
                )}
                <Button
                  variant="ghost"
                  small
                  icon="eye"
                  onClick={() => open("trace", s.id)}
                >
                  Ver muestra
                </Button>
              </div>,
            ])}
          />
          <Paginador {...pag} />
          <div className="table-footer">
            {pestana === "Historial"
              ? `${items.length} muestras · de la más reciente a la más antigua`
              : items.length
                ? `${items.length} muestras por analizar · al iniciar el análisis pasan a Resultados`
                : "No hay muestras esperando análisis."}
          </div>
        </Panel>
      </>
    )
  }
  function ResultsPage() {
    const pestana = PESTANAS_RESULTADOS.includes(filter) ? filter : "Por ingresar"
    const estado = ETAPAS.find((e) => e.nombre === pestana)!.id
    const entregados = pestana === "Entregados"
    const items = filteredSamples
      .filter((s) => s.state === estado)
      .sort((a, b) =>
        entregados
          ? fechaDeEstado(b).localeCompare(fechaDeEstado(a))
          : fechaDeEstado(a).localeCompare(fechaDeEstado(b)),
      )
    const pag = bloque("resultados", items)
    const siguiente: Record<string, string> = {
      "Por ingresar": "al enviarlos a validación pasan a Por validar",
      "Por validar": "al validarlos pasan a Por publicar",
      "Por publicar": "al publicarlos quedan entregados al paciente",
      Entregados: "del más reciente al más antiguo",
    }
    return (
      <>
        {Flujo("Resultados", pestana)}
        <Tabs
          items={PESTANAS_RESULTADOS}
          counts={Object.fromEntries(
            ETAPAS.slice(3).map((e) => [e.nombre, statusCount(e.id)]),
          )}
          active={pestana}
          onChange={setFilter}
        />
        <Notice>
          Los borradores y los resultados en validación{" "}
          <strong>no se consideran publicados</strong>. Cada cambio queda en la
          auditoría.
        </Notice>
        <Panel>
          {SampleFilters(false)}
          <DataTable
            columns={[
              "Código de muestra",
              "Paciente / DNI",
              "Examen",
              "Fecha de toma",
              entregados ? "Publicado" : "En esta etapa desde",
              "Estado",
              "Responsable",
              "Acciones",
            ]}
            rows={pag.visibles.map((s) => [
              sampleLink(s),
              patientCell(s.patient),
              exam(s.exam).name,
              dateLabel(s.date),
              <div className="stacked">
                <span>{dateLabel(fechaDeEstado(s))}</span>
                <small>{timeLabel(fechaDeEstado(s))}</small>
              </div>,
              <Badge value={s.state} />,
              s.responsible,
              resultActions(s),
            ])}
          />
          <Paginador {...pag} />
          <div className="table-footer">
            {items.length
              ? `${items.length} ${entregados ? "resultados entregados" : "resultados"} · ${siguiente[pestana]}`
              : `No hay resultados en ${pestana}.`}
          </div>
        </Panel>
      </>
    )
  }
  function CatalogPage() {
    const items = db.exams.filter(
      (e) =>
        matches(query, e.name, e.id, e.category) &&
        (category === "Todas" || e.category === category) &&
        (filter === "Todos" ||
          (filter === "Disponible"
            ? !availability(e, db.resources).length
            : availability(e, db.resources).length > 0)),
    )
    return (
      <>
        <div className="module-stats">
          <StatCard
            label="Exámenes en el catálogo"
            value={db.exams.length}
            icon="book"
          />
          <StatCard
            label="Disponibles para procesar"
            value={
              db.exams.filter((e) => !availability(e, db.resources).length)
                .length
            }
            icon="checkcircle"
          />
          <StatCard
            label="Disponibilidad afectada"
            value={
              db.exams.filter((e) => availability(e, db.resources).length)
                .length
            }
            icon="alert"
            tone="amber"
          />
        </div>
        <Panel>
          <div className="table-toolbar">
            <SearchBar
              value={query}
              onChange={setQuery}
              placeholder="Buscar examen, código o categoría..."
            />
            <select
              aria-label="Categoría"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
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
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
            >
              {["Todos", "Disponible", "No disponible"].map((s) => (
                <option key={s}>{s}</option>
              ))}
            </select>
          </div>
          <DataTable
            columns={[
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
            rows={items.map((e) => [
              <span className="mono">{e.id}</span>,
              <button
                className="record-link"
                onClick={() => open("examDetail", e.id)}
              >
                {e.name}
              </button>,
              e.category,
              money(e.price),
              e.time,
              e.sample,
              <Badge value={e.enabled ? "Habilitado" : "Deshabilitado"} />,
              availabilityBadge(e),
              <button
                className="row-arrow"
                aria-label={`Ver ${e.name}`}
                onClick={() => open("examDetail", e.id)}
              >
                <Icon name="arrow" size={17} />
              </button>,
            ])}
          />
          <div className="table-footer">
            La disponibilidad se calcula automáticamente con equipos, reactivos
            y materiales.
          </div>
        </Panel>
      </>
    )
  }

  function InventoryPage() {
    return (
      <>
        <div className="module-stats inventory-stats">
          <StatCard
            label="Equipos operativos"
            value={
              db.resources.filter(
                (r) => r.kind === "Equipos" && r.status === "OPERATIVO",
              ).length
            }
            icon="monitor"
            onClick={() => go("Equipos")}
          />
          <StatCard
            label="En mantenimiento"
            value={
              db.resources.filter(
                (r) => r.kind === "Equipos" && r.status === "EN MANTENIMIENTO",
              ).length
            }
            icon="wrench"
            tone="amber"
            onClick={() => go("Mantenimiento")}
          />
          <StatCard
            label="Reactivos disponibles"
            value={
              db.resources.filter(
                (r) =>
                  r.kind === "Reactivos" && r.qty > 0 && r.expires >= TODAY,
              ).length
            }
            icon="flask"
            onClick={() => go("Reactivos")}
          />
          <StatCard
            label="Recursos con stock bajo"
            value={lowResources.filter((r) => r.qty > 0).length}
            icon="box"
            tone="amber"
          />
          <StatCard
            label="Materiales agotados"
            value={
              db.resources.filter((r) => r.kind === "Materiales" && r.qty === 0)
                .length
            }
            icon="layers"
            tone="red"
            onClick={() => go("Materiales")}
          />
          <StatCard
            label="Próximos a vencer"
            value={
              db.resources.filter(
                (r) =>
                  r.kind === "Reactivos" &&
                  r.expires >= TODAY &&
                  (new Date(r.expires).getTime() - new Date(TODAY).getTime()) /
                    86400000 <=
                    30,
              ).length
            }
            icon="calendar"
            tone="amber"
            onClick={() => go("Reactivos")}
          />
        </div>
        <Tabs
          items={["Reactivos", "Materiales", "Equipos"]}
          active={inventoryTab}
          onChange={(s) => {
            setInventoryTab(s)
            setQuery("")
            setFilter("Todos")
          }}
        />
        {ResourcePage({ kind: inventoryTab })}
      </>
    )
  }
  function ResourcePage({ kind = view }: { kind?: string }) {
    const items = db.resources.filter(
      (r) =>
        r.kind === kind &&
        matches(query, r.id, r.name, r.brand, r.lot) &&
        (filter === "Todos" || resourceStatus(r) === filter),
    )
    return (
      <Panel>
        <div className="table-toolbar">
          <SearchBar
            value={query}
            onChange={setQuery}
            placeholder={`Buscar ${kind.toLowerCase()}, código o lote...`}
          />
          <select
            aria-label="Estado del recurso"
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
          >
            {[
              "Todos",
              ...(kind === "Equipos"
                ? ["OPERATIVO", "EN MANTENIMIENTO", "FUERA DE SERVICIO", "BAJA"]
                : ["Disponible", "Stock bajo", "Agotado", "Vencido"]),
            ].map((s) => (
              <option key={s}>{s}</option>
            ))}
          </select>
          <span className="toolbar-info">{items.length} recursos</span>
        </div>
        {kind === "Equipos" ? (
          <DataTable
            columns={[
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
            rows={items.map((r) => [
              <div className="stacked">
                <button
                  className="record-link"
                  onClick={() => open("equipment", r.id)}
                >
                  {r.name}
                </button>
                <small className="mono">{r.id}</small>
              </div>,
              <div className="stacked">
                <span>{r.brand}</span>
                <small>{r.model}</small>
              </div>,
              <span className="mono">{r.serial}</span>,
              r.area,
              <Badge value={r.status} />,
              dateLabel(r.last),
              dateLabel(r.next),
              r.responsible,
              <Button
                variant="ghost"
                small
                icon="eye"
                onClick={() => open("equipment", r.id)}
              >
                Detalle
              </Button>,
            ])}
          />
        ) : (
          <DataTable
            columns={[
              "Código / Recurso",
              ...(kind === "Reactivos" ? ["Marca / Lote"] : ["Categoría"]),
              "Cantidad",
              "Stock mínimo",
              ...(kind === "Reactivos" ? ["Vencimiento"] : []),
              "Ubicación",
              "Estado",
              "Acciones",
            ]}
            rows={items.map((r) => [
              <div className="stacked">
                <button
                  className="record-link"
                  onClick={() => open("resource", r.id)}
                >
                  {r.name}
                </button>
                <small className="mono">{r.id}</small>
              </div>,
              kind === "Reactivos" ? (
                <div className="stacked">
                  <span>{r.brand}</span>
                  <small>{r.lot}</small>
                </div>
              ) : (
                "Insumos de laboratorio"
              ),
              <span className={`stock-number ${r.qty <= r.min ? "low" : ""}`}>
                <strong>{r.qty}</strong> {r.unit}
              </span>,
              `${r.min} ${r.unit}`,
              ...(kind === "Reactivos"
                ? [
                    <span
                      // Ámbar si vence dentro de 30 días. El umbral estaba
                      // fijo en "2026-10-24", que era hoy + 30 con la fecha
                      // congelada; con el reloj real dejaba de avisar.
                      className={
                        r.expires < sumarDias(TODAY, 30) ? "warning-text" : ""
                      }
                    >
                      {dateLabel(r.expires)}
                    </span>,
                  ]
                : []),
              r.area,
              <Badge value={resourceStatus(r)} />,
              <div className="row-actions">
                <button
                  title="Registrar entrada"
                  aria-label={`Entrada ${r.name}`}
                  onClick={() => open("stockIn", r.id)}
                >
                  <Icon name="plus" size={17} />
                </button>
                <button
                  title="Registrar salida"
                  aria-label={`Salida ${r.name}`}
                  onClick={() => open("stockOut", r.id)}
                >
                  <Icon name="arrow" size={17} />
                </button>
                <button
                  title="Editar recurso"
                  aria-label={`Editar ${r.name}`}
                  onClick={() => open("resource", r.id)}
                >
                  <Icon name="edit" size={16} />
                </button>
              </div>,
            ])}
          />
        )}
        <div className="table-footer">
          <span>
            Los movimientos de stock actualizan la disponibilidad de los
            exámenes.
          </span>
          <span>
            <Icon name="shield" size={14} /> Movimientos auditados
          </span>
        </div>
      </Panel>
    )
  }
  function MaintenancePage() {
    const items = db.maintenance.filter(
      (m) =>
        matches(query, resource(m.equipment).name, m.responsible, m.id) &&
        (filter === "Todos" || m.state === filter),
    )
    return (
      <>
        <Notice tone="warning">
          Un mantenimiento <strong>En proceso</strong> bloquea los exámenes que
          dependen del equipo. Al finalizarlo, se restablece su disponibilidad
          si hay stock.
        </Notice>
        <Panel>
          <div className="table-toolbar">
            <SearchBar
              value={query}
              onChange={setQuery}
              placeholder="Buscar equipo, responsable o mantenimiento..."
            />
            <select
              aria-label="Estado de mantenimiento"
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
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
          <DataTable
            columns={[
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
            rows={items.map((m) => [
              <span className="mono">{m.id}</span>,
              resource(m.equipment).name,
              m.type,
              dateLabel(m.start),
              dateLabel(m.end),
              dateLabel(m.actual),
              m.responsible,
              <Badge value={m.state} />,
              <Button
                small
                variant="ghost"
                icon="edit"
                onClick={() => open("maintenance", m.id)}
              >
                Actualizar
              </Button>,
            ])}
          />
        </Panel>
      </>
    )
  }

  /**
   * Exportación real (§9.12, modificado). El backend escribe el archivo junto
   * al ejecutable y devuelve su ruta: no es una descarga del navegador porque
   * el WebView de JavaFX no tiene gestor de descargas.
   */
  const exportar = (formato: "pdf" | "xlsx") => {
    const desde = reportRange === "Rango personalizado" ? customStart : undefined
    const hasta = reportRange === "Rango personalizado" ? customEnd : undefined
    const nombre = formato === "pdf" ? "PDF" : "Excel"
    ;(db.desktop
      ? api.saveReportOnDesktop(reportType, reportRange, formato, desde, hasta)
      : api.exportReport(reportType, reportRange, formato, desde, hasta).then(
          (r) => ({ ...r, cancelled: false }),
        )
    )
      .then((r) => {
        if (r.cancelled) return
        notify(`Reporte ${nombre} guardado en ${r.path}`)
        if (db.desktop) api.database().then(setDb).catch(() => {})
      })
      .catch((e) =>
        notify(
          e instanceof ApiError
            ? e.message
            : "No se pudo generar el archivo del reporte.",
          "warning",
        ),
      )
  }

  /**
   * Abre la vista previa del informe. Lo arma el backend, que es el mismo
   * objeto con el que se genera el PDF: lo que se ve es lo que sale.
   */
  const abrirVistaDeImpresion = (sampleId: string) => {
    api
      .resultReport(sampleId)
      .then((informe) => {
        close()
        setPrintReport(informe)
      })
      .catch((e) =>
        notify(
          e instanceof ApiError
            ? e.message
            : "No se pudo preparar la vista de impresión.",
          "warning",
        ),
      )
  }

  /**
   * En el navegador abre el diálogo de impresión. Dentro de la ventana de
   * escritorio eso no existe, así que el backend genera el PDF y lo abre con
   * el visor del sistema, que es donde se elige impresora.
   */
  const imprimir = (sampleId: string) => {
    if (!db.desktop) {
      window.print()
      return
    }
    api
      .printOnDesktop(sampleId)
      .then((r) => notify(`Informe abierto para imprimir · ${r.fileName}`))
      .catch((e) =>
        notify(
          e instanceof ApiError
            ? e.message
            : "No se pudo preparar la impresión.",
          "warning",
        ),
      )
  }

  const eliminarEntregados = () => {
    const terminar = (ruta: string) => {
      notify(`Respaldo guardado en ${ruta}. Los registros respaldados se eliminaron.`)
      api.database().then(setDb).catch(() => {})
    }
    const fallo = (e: unknown) =>
      notify(
        e instanceof ApiError ? e.message : "No se pudo completar. No se eliminó nada.",
        "warning",
      )
    if (db.desktop)
      api
        .purgeOnDesktop(archivoAntesDe)
        .then((r) => {
          if (!r.cancelled) terminar(r.path)
        })
        .catch(fallo)
    else api.purgeToFolder(archivoAntesDe).then((r) => terminar(r.path)).catch(fallo)
  }

  const guardarRespaldo = () => {
    if (!db.desktop) {
      const enlace = document.createElement("a")
      enlace.href = api.backupCsvUrl()
      enlace.download = "Respaldo-pacientes.csv"
      document.body.appendChild(enlace)
      enlace.click()
      enlace.remove()
      setTimeout(() => api.database().then(setDb).catch(() => {}), 800)
      return
    }
    api
      .saveBackupOnDesktop()
      .then((r) => {
        if (r.cancelled) return
        notify(`Respaldo guardado en ${r.path}`)
        api.database().then(setDb).catch(() => {})
      })
      .catch((e) =>
        notify(
          e instanceof ApiError ? e.message : "No se pudo guardar el respaldo.",
          "warning",
        ),
      )
  }

  const guardarInformePdf = (sampleId: string) => {
    if (!db.desktop) {
      const enlace = document.createElement("a")
      enlace.href = api.resultPdfFileUrl(sampleId)
      enlace.download = `Resultado-${sampleId}.pdf`
      document.body.appendChild(enlace)
      enlace.click()
      enlace.remove()
      return
    }
    api
      .saveResultOnDesktop(sampleId)
      .then((r) => {
        if (!r.cancelled) notify(`Informe PDF guardado en ${r.path}`)
      })
      .catch((e) =>
        notify(
          e instanceof ApiError ? e.message : "No se pudo guardar el informe PDF.",
          "warning",
        ),
      )
  }

  function ReportsPage() {
    let start = TODAY,
      end = TODAY
    // Los mismos rangos que calcula ReportService en Java, para que la tabla
    // de la pantalla y el PDF exportado digan lo mismo. Estaban fijos en
    // "2026-09-21" y "2026-09-01", de la época de la fecha congelada.
    if (reportRange === "Esta semana") start = lunesDeEstaSemana()
    if (reportRange === "Este mes") start = primerDiaDelMes()
    if (reportRange === "Rango personalizado") {
      start = customStart
      end = customEnd
    }
    const ss = db.samples.filter(
      (s) => s.date.slice(0, 10) >= start && s.date.slice(0, 10) <= end,
    )
    const rr = db.requests.filter((r) => r.date >= start && r.date <= end)
    const inRange = (d: string) =>
      d.slice(0, 10) >= start && d.slice(0, 10) <= end
    const movement = db.audit.filter(
      (a) => inRange(a.date) && /retirado|ajustado|físico/.test(a.action),
    )
    let columns = ["Indicador", "Cantidad", "Período"],
      rows: ReactNode[][] = [
        [
          "Pacientes registrados",
          db.patients.filter((p) => inRange(p.created)).length,
          reportRange,
        ],
        ["Solicitudes registradas", rr.length, reportRange],
        [
          "Exámenes realizados",
          ss.filter((s) =>
            ["EN VALIDACIÓN", "VALIDADO", "PUBLICADA"].includes(s.state),
          ).length,
          reportRange,
        ],
        [
          "Muestras procesadas",
          ss.filter((s) => s.state !== "RECIBIDA").length,
          reportRange,
        ],
        [
          "Resultados publicados",
          ss.filter((s) => s.state === "PUBLICADA").length,
          reportRange,
        ],
        [
          "Resultados pendientes",
          ss.filter((s) => s.state !== "PUBLICADA").length,
          reportRange,
        ],
      ]
    if (reportType === "Exámenes más solicitados") {
      columns = ["Examen", "Categoría", "Solicitudes"]
      rows = db.exams.map((e) => [
        e.name,
        e.category,
        rr.filter((r) => r.exams.includes(e.id)).length,
      ])
    }
    if (reportType === "Inventario") {
      columns = ["Recurso", "Tipo", "Stock actual", "Estado"]
      rows = db.resources
        .filter((r) => r.kind !== "Equipos")
        .map((r) => [
          r.name,
          r.kind,
          `${r.qty} ${r.unit}`,
          <Badge value={resourceStatus(r)} />,
        ])
    }
    if (reportType === "Equipos en mantenimiento") {
      columns = ["Equipo", "Responsable", "Finalización estimada", "Estado"]
      rows = db.maintenance
        .filter((m) => m.state === "En proceso")
        .map((m) => [
          resource(m.equipment).name,
          m.responsible,
          dateLabel(m.end),
          <Badge value={m.state} />,
        ])
    }
    if (reportType.startsWith("Consumo")) {
      columns = ["Fecha", "Recurso", "Movimiento", "Responsable"]
      const kind = reportType.includes("reactivos") ? "Reactivos" : "Materiales"
      rows = movement
        .filter((a) => a.module === kind || a.module === "Inventario")
        .map((a) => [dateLabel(a.date), a.record, a.action, a.user])
    }
    return (
      <>
        <Panel>
          <div className="table-toolbar">
            <select
              aria-label="Tipo de reporte"
              value={reportType}
              onChange={(e) => setReportType(e.target.value)}
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
            <Tabs
              items={["Hoy", "Esta semana", "Este mes", "Rango personalizado"]}
              active={reportRange}
              onChange={setReportRange}
            />
            <div className="button-group">
              <Button
                small
                variant="secondary"
                icon="download"
                onClick={() => exportar("pdf")}
              >
                Exportar PDF
              </Button>
              <Button small icon="download" onClick={() => exportar("xlsx")}>
                Exportar Excel
              </Button>
            </div>
          </div>
          {reportRange === "Rango personalizado" && (
            <div className="date-range">
              <Field label="Desde">
                <InputFecha
                  value={customStart}
                  max={customEnd}
                  onChange={setCustomStart}
                />
              </Field>
              <Field label="Hasta">
                <InputFecha
                  min={customStart}
                  value={customEnd}
                  onChange={setCustomEnd}
                />
              </Field>
            </div>
          )}
        </Panel>
        <div className="module-stats">
          <StatCard
            label="Muestras del período"
            value={ss.length}
            icon="tube"
          />
          <StatCard
            label="Publicados"
            value={ss.filter((s) => s.state === "PUBLICADA").length}
            icon="checkcircle"
          />
          <StatCard
            label="Pendientes"
            value={ss.filter((s) => s.state !== "PUBLICADA").length}
            icon="clock"
            tone="amber"
          />
        </div>
        <Panel
          title={reportType}
          subtitle={`${dateLabel(start)} — ${dateLabel(end)} · ${db.lab}`}
        >
          <DataTable columns={columns} rows={rows} />
        </Panel>
        <Notice>
          {db.desktop
            ? "Al exportar, el programa te pregunta en qué carpeta guardar el archivo. Propone primero "
            : "Los PDF y los Excel se guardan en "}
          <strong>{db.exportDir}</strong>. La carpeta se cambia en Configuración.
        </Notice>
      </>
    )
  }
  function AuditPage() {
    const items = db.audit.filter(
      (a) =>
        matches(query, a.user, a.action, a.record) &&
        (category === "Todas" || a.module === category),
    )
    return (
      <>
        <Notice>
          <strong>Registro protegido en la interfaz.</strong> Las acciones se
          registran automáticamente y no pueden eliminarse desde este módulo. No
          representa una auditoría clínica certificada.
        </Notice>
        <Panel>
          <div className="table-toolbar">
            <SearchBar
              value={query}
              onChange={setQuery}
              placeholder="Buscar acción, responsable o registro..."
            />
            <select
              aria-label="Módulo auditado"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
            >
              <option>Todas</option>
              {[...new Set(db.audit.map((a) => a.module))].map((m) => (
                <option key={m}>{m}</option>
              ))}
            </select>
          </div>
          <DataTable
            columns={[
              "Fecha",
              "Hora",
              "Registrado por",
              "Acción",
              "Módulo",
              "Registro afectado",
            ]}
            rows={items.map((a) => [
              dateLabel(a.date),
              <span className="mono">{timeLabel(a.date)}</span>,
              a.user,
              <span className="audit-action">
                <Icon name="shield" size={15} />
                {a.action}
              </span>,
              a.module,
              <span className="mono">{a.record}</span>,
            ])}
          />
          <div className="table-footer">
            {items.length} eventos · No se permite eliminar registros
          </div>
        </Panel>
      </>
    )
  }
  function AppearanceSettings() {
    const actual = temas.find((t) => t.id === db.appearance) ?? temas[1]
    return (
      <div className="appearance-settings">
        <p className="appearance-intro">
          Elige cómo quieres ver el sistema. El tema se aplica a todas las
          pantallas y se guarda automáticamente en este equipo.
        </p>
        <p className="theme-legend">Color de la interfaz</p>
        <fieldset className="theme-options" aria-label="Color de la interfaz">
          {temas.map((option) => (
            <label
              key={option.id}
              className={`theme-choice ${actual.id === option.id ? "chosen" : ""}`}
            >
              <input
                type="radio"
                name="interface-theme"
                value={option.id}
                checked={actual.id === option.id}
                onChange={() => chooseAppearance(option.id)}
              />
              <span
                className="theme-mini"
                aria-hidden="true"
                style={
                  {
                    "--mini-bg": option.bg,
                    "--mini-surface": option.surface,
                    "--mini-side": option.side,
                    "--mini-border": option.border,
                    "--mini-ink": option.ink,
                    "--mini-accent": option.accent,
                    "--mini-blue": option.blue,
                    "--mini-amber": option.amber,
                  } as CSSProperties
                }
              >
                <span className="theme-mini-sidebar">
                  <i />
                  <i />
                  <i />
                  <i />
                </span>
                <span className="theme-mini-work">
                  <span className="theme-mini-top" style={{ display: "block" }} />
                  <span className="theme-mini-line" style={{ display: "block" }} />
                  <span className="theme-mini-stats">
                    <i />
                    <i />
                    <i />
                  </span>
                  <span className="theme-mini-table" style={{ display: "block" }} />
                </span>
              </span>
              <span className="theme-choice-title">
                <strong>{option.name}</strong>
                <Icon name="checkcircle" size={17} />
              </span>
              <span className="theme-choice-description">
                {option.description}
              </span>
            </label>
          ))}
        </fieldset>
        <div className="appearance-status" role="status">
          <Icon name="checkcircle" size={16} />
          <span>{appearanceStatus || `Tema actual: ${actual.name}.`}</span>
        </div>
      </div>
    )
  }
  function ConfigPage() {
    return (
      <>
        <Tabs
          items={[
            "General",
            "Apariencia",
            "Áreas",
            "Parámetros de referencia",
            "Notificaciones",
          ]}
          active={configTab}
          onChange={setConfigTab}
        />
        {configTab === "Apariencia" && (
          <Panel title="Apariencia" subtitle="Personaliza los colores del sistema">
            {AppearanceSettings()}
          </Panel>
        )}
        {configTab === "General" && (
          <Panel
            title="Configuración general"
            subtitle="Identidad del laboratorio y zona de trabajo"
          >
            <form
              className="settings-form"
              onSubmit={(e) => {
                e.preventDefault()
                const f = new FormData(e.currentTarget)
                call(
                  () =>
                    api.saveSettings(
                      text(f, "lab"),
                      text(f, "branch"),
                      text(f, "responsible"),
                    ),
                  {
                  onDone: () => notify("Configuración guardada."),
                })
              }}
            >
              <div className="form-grid">
                <Field label="Nombre del laboratorio">
                  <input name="lab" defaultValue={db.lab} required />
                </Field>
                <Field label="Sucursal">
                  <input name="branch" defaultValue={db.branch} required />
                </Field>
                <Field
                  label="Responsable del laboratorio"
                  hint="Nombre de quien registra en este equipo. Firma los informes y queda en la auditoría."
                >
                  <input
                    name="responsible"
                    defaultValue={
                      db.responsible === "Responsable del laboratorio"
                        ? ""
                        : db.responsible
                    }
                    placeholder="Ej.: Nombre y apellidos del responsable"
                  />
                </Field>
                <Field label="Zona horaria">
                  <input value="Lima · UTC-5" readOnly />
                </Field>
                <Field label="Funcionamiento">
                  <input value="Local · Sin conexiones externas" readOnly />
                </Field>
              </div>
              <Notice>
                No hay conexiones a RENIEC, SUSALUD, bases de datos,
                analizadores ni servicios de salud. La información se almacena
                únicamente en este equipo.
              </Notice>
              <Button icon="check">Guardar configuración</Button>
            </form>
          </Panel>
        )}
        {configTab === "General" && (
          <Panel
            title="Carpeta de exportaciones"
            subtitle="Dónde se guardan los reportes PDF y Excel y los informes de resultados."
          >
            <form
              key={db.exportDir}
              className="settings-form carpeta-form"
              onSubmit={(e) => {
                e.preventDefault()
                const f = new FormData(e.currentTarget)
                call(() => api.setExportDir(text(f, "exportDir")), {
                  onDone: () => notify("Carpeta de exportaciones actualizada."),
                })
              }}
            >
              <Field
                label="Ruta de la carpeta"
                hint={
                  db.desktop
                    ? "Pulsa «Elegir carpeta…» para buscarla en el equipo, o escribe la ruta y pulsa «Guardar ruta»."
                    : "Escribe la ruta completa de la carpeta y pulsa «Guardar ruta»."
                }
              >
                <div className="carpeta-ruta">
                  <Icon name="box" size={17} />
                  <input
                    name="exportDir"
                    defaultValue={db.exportDir}
                    placeholder="C:\\Informes o /home/usuario/Informes"
                  />
                  {db.desktop && (
                    <Button
                      type="button"
                      icon="search"
                      onClick={() =>
                        api
                          .chooseFolder()
                          .then((r) => {
                            if (!r.path) return
                            call(() => api.setExportDir(r.path), {
                              onDone: () =>
                                notify(`Carpeta elegida: ${r.path}`),
                            })
                          })
                          .catch((err) =>
                            notify(
                              err instanceof Error
                                ? err.message
                                : "No se pudo abrir el selector.",
                              "warning",
                            ),
                          )
                      }
                    >
                      Elegir carpeta…
                    </Button>
                  )}
                </div>
              </Field>
              <div className="button-group">
                <Button variant="secondary" icon="check">
                  Guardar ruta
                </Button>
                <Button
                  type="button"
                  variant="ghost"
                  onClick={() =>
                    call(() => api.setExportDir(""), {
                      onDone: () =>
                        notify("Se usará la carpeta de la aplicación."),
                    })
                  }
                >
                  Usar la de la aplicación
                </Button>
              </div>
              <Notice>
                {db.desktop
                  ? "Cada vez que exportas un reporte o guardas un informe, el programa te deja elegir la carpeta y el nombre del archivo. Propone primero "
                  : "Ahora mismo los archivos se guardan en "}
                <strong>{db.exportDir}</strong>. La carpeta se crea sola si no
                existe.
              </Notice>
            </form>
          </Panel>
        )}
        {configTab === "General" && (
          <Panel
            title="Respaldo de pacientes"
            subtitle="Copia de todo lo registrado de los pacientes atendidos, en un archivo CSV"
          >
            <div className="settings-form">
              <p className="small-text">
                El respaldo incluye a cada paciente con sus datos personales,
                sus solicitudes, cada muestra con su examen, fecha de toma y
                quién la tomó, el historial de estados con fecha y responsable,
                y los resultados publicados con unidad, valores de referencia,
                indicador y observaciones. Se abre en Excel o en cualquier hoja
                de cálculo. Guárdalo en un USB o en otra carpeta segura: sirve
                de respaldo ante cualquier reclamo.
              </p>
              <div className="button-group">
                <Button icon="download" onClick={guardarRespaldo}>
                  {db.desktop ? "Guardar respaldo CSV…" : "Descargar respaldo CSV"}
                </Button>
              </div>
              <Notice>
                No borra nada: los pacientes, resultados e inventario siguen
                en el laboratorio. Los resultados que aún no están publicados
                aparecen con su estado, sin valores. Cada respaldo queda
                anotado en la auditoría.
              </Notice>
            </div>
          </Panel>
        )}
        {configTab === "General" && (
          <Panel
            title="Respaldar y eliminar atenciones antiguas"
            subtitle="Para que Pacientes, Resultados y Muestras no se llenen con atenciones ya entregadas"
          >
            <div className="settings-form">
              <div className="form-grid">
                <Field label="Eliminar lo entregado hace más de">
                  <select
                    value={archivoMeses}
                    onChange={(e) => setArchivoMeses(e.target.value)}
                  >
                    <option value="1">1 mes</option>
                    <option value="3">3 meses</option>
                    <option value="6">6 meses</option>
                    <option value="12">1 año</option>
                  </select>
                </Field>
              </div>
              <p className="small-text">
                {archivoPrevia === null
                  ? "Calculando…"
                  : archivoPrevia.requests
                    ? `Se respaldarán y eliminarán ${archivoPrevia.requests} solicitudes con ${archivoPrevia.samples} resultados, entregadas antes del ${dateLabel(archivoPrevia.before)}. ${archivoPrevia.patients} pacientes quedan sin nada pendiente y también salen del registro.`
                    : `No hay atenciones entregadas por completo antes del ${dateLabel(archivoAntesDe)}.`}
              </p>
              <div className="button-group">
                <Button
                  icon="download"
                  disabled={!archivoPrevia?.requests}
                  onClick={() =>
                    setConfirm({
                      title: "Respaldar y eliminar",
                      message: `Primero se guarda un CSV con los ${archivoPrevia?.patients} pacientes, sus ${archivoPrevia?.requests} solicitudes y ${archivoPrevia?.samples} resultados. Solo si el archivo se guarda bien se eliminan del programa. Después no se pueden recuperar desde aquí: guarda ese CSV en un lugar seguro.`,
                      action: eliminarEntregados,
                    })
                  }
                >
                  {db.desktop ? "Respaldar y eliminar…" : "Respaldar y eliminar"}
                </Button>
              </div>
              <Notice tone="warning">
                Solo se eliminan atenciones con todos sus exámenes publicados.
                Lo que está en curso nunca se toca, y un paciente con una
                solicitud reciente se queda. El inventario, los equipos, el
                catálogo y la auditoría no cambian.
              </Notice>
            </div>
          </Panel>
        )}
        {configTab === "Áreas" && (
          <Panel title="Áreas del laboratorio">
            <DataTable
              columns={["Área", "Equipos", "Recursos", "Estado"]}
              rows={[...new Set(db.resources.map((r) => r.area))].map((a) => [
                a,
                db.resources.filter((r) => r.area === a && r.kind === "Equipos")
                  .length,
                db.resources.filter((r) => r.area === a && r.kind !== "Equipos")
                  .length,
                <Badge value="Activo" />,
              ])}
            />
          </Panel>
        )}
        {configTab === "Parámetros de referencia" && (
          <Panel
            title="Formatos y parámetros de referencia"
            subtitle="Intervalos de referencia. No constituyen recomendaciones clínicas."
          >
            <div className="padded">
              <Notice tone="warning">
                Los intervalos son los configurados en cada formato. La
                aplicación señala desviaciones, pero no realiza diagnósticos.
              </Notice>
            </div>
            <DataTable
              columns={["Examen", "Formato", "Parámetros", "Acción"]}
              rows={db.exams.map((e) => [
                e.name,
                e.category,
                db.formats[e.format]?.length || 0,
                <Button
                  small
                  variant="ghost"
                  onClick={() => open("references", e.id)}
                >
                  Ver parámetros
                </Button>,
              ])}
            />
          </Panel>
        )}
        {configTab === "Notificaciones" && (
          <Panel title="Configuración de notificaciones">
            <div className="settings-form">
              <label className="toggle-row">
                <span>
                  <strong>Alertas operativas en el encabezado</strong>
                  <small>
                    Stock, vencimientos, resultados y muestras pendientes
                  </small>
                </span>
                <input
                  type="checkbox"
                  checked={db.notifications}
                  onChange={(e) => {
                    call(() => api.setNotifications(e.target.checked), {
                      onDone: () => notify("Preferencias guardadas."),
                    })
                  }}
                />
              </label>
              <Notice>
                Las notificaciones son locales. No se envían correos, SMS ni
                mensajes externos.
              </Notice>
            </div>
          </Panel>
        )}
      </>
    )
  }

  const content = () => {
    if (view === "Dashboard") return Dashboard()
    if (view === "Pacientes") return PatientPage()
    if (view === "Solicitudes") return RequestsPage()
    if (view === "Muestras") return SamplesPage()
    if (view === "Resultados") return ResultsPage()
    if (view === "Catálogo de exámenes") return CatalogPage()
    if (view === "Inventario") return InventoryPage()
    if (["Equipos", "Reactivos", "Materiales"].includes(view))
      return ResourcePage({})
    if (view === "Mantenimiento") return MaintenancePage()
    if (view === "Reportes") return ReportsPage()
    if (view === "Auditoría") return AuditPage()
    return ConfigPage()
  }

  /**
   * Las validaciones de DNI ya no están aquí: las hace el backend y devuelve
   * su mensaje literal (§11.3). Se conservan las del HTML
   * (pattern, required), que son la primera barrera visual.
   */
  function savePatient(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)
    const editando = !!modal?.id
    const submitter = (e.nativeEvent as SubmitEvent)
      .submitter as HTMLButtonElement
    const seguirConSolicitud = submitter?.value === "request"

    call(
      () =>
        api.savePatient({
          id: modal?.id || "",
          dni: text(f, "dni"),
          first: text(f, "first"),
          last: text(f, "last"),
          birth: text(f, "birth"),
          sex: text(f, "sex"),
          phone: text(f, "phone"),
          email: text(f, "email"),
          address: text(f, "address"),
          notes: text(f, "notes"),
        }),
      {
        onDone: (id) => {
          close()
          go("Pacientes")
          setPatientId(id)
          setTab("Resumen")
          notify(
            `Paciente ${
              editando ? "actualizado" : "registrado"
            } correctamente · ${id}`,
          )
          if (seguirConSolicitud) open("request", id)
        },
      },
    )
  }
  function saveRequest(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    // Se conserva en el cliente solo para saber si el toast lleva el sufijo
    // de pendiente; la decisión de aceptar o no la toma el backend.
    const bloqueado = chosenExams.some(
      (id) => availability(exam(id), db.resources).length,
    )

    call(() => api.createRequest(requestPatient, chosenExams, allowPending), {
      onDone: (id) => {
        close()
        go("Solicitudes")
        notify(
          `Solicitud ${id} registrada${
            bloqueado ? " · Pendiente de disponibilidad" : ""
          }. Pasa a Solicitudes › Toma de muestra.`,
        )
      },
    })
  }
  /**
   * El descuento de stock y el recálculo de "pendiente de disponibilidad" los
   * hace el backend dentro de la misma transacción (§6.4.3). Al terminar se
   * abre la trazabilidad de la muestra nueva, que es para lo que hace falta
   * el identificador que viene en la cabecera.
   */
  function saveSample(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)

    call(
      () =>
        api.registerSample(
          sampleRequest,
          sampleExam,
          text(f, "date"),
          text(f, "responsible"),
        ),
      {
        onDone: (id) => {
          close()
          go("Muestras")
          open("trace", id)
          notify(`Muestra registrada · ${id}. Pasa a Muestras › Por analizar.`)
        },
      },
    )
  }
  /**
   * `submit` distingue las dos acciones del formulario: guardar borrador no
   * mueve el estado de la muestra, enviar a validación sí. Las tres
   * validaciones de §6.4.4 las hace el backend.
   */
  function saveResult(state: string) {
    const enviando = state === "EN VALIDACIÓN"

    call(() => api.saveResults(resultSample, values, resultNotes, enviando), {
      onDone: () => {
        close()
        notify(
          enviando
            ? "Resultado enviado a validación. Pasa a Resultados › Por validar; aún no está publicado."
            : "Borrador guardado. No se considera un resultado publicado.",
        )
      },
    })
  }
  function saveResource(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)

    call(
      () =>
        api.saveResource({
          id: modal?.id || "",
          kind: text(f, "kind"),
          name: text(f, "name"),
          brand: text(f, "brand"),
          model: text(f, "model"),
          serial: text(f, "serial"),
          area: text(f, "area"),
          status: text(f, "status"),
          qty: Number(f.get("qty") || 0),
          min: Number(f.get("min") || 0),
          unit: text(f, "unit"),
          lot: text(f, "lot"),
          expires: text(f, "expires"),
          last: text(f, "last"),
          next: text(f, "next"),
          responsible: text(f, "responsible"),
        }),
      {
        onDone: () => {
          close()
          notify("Recurso guardado. Disponibilidad de exámenes actualizada.")
        },
      },
    )
  }
  /**
   * Toda la cascada sobre el equipo (§6.4.9) la aplica el backend, incluido
   * el recálculo del equipo anterior si el mantenimiento cambia de equipo.
   */
  function saveMaintenance(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)

    call(
      () =>
        api.saveMaintenance({
          id: (modal?.type === "maintenance" ? modal.id : "") || "",
          equipment: text(f, "equipment"),
          type: text(f, "type"),
          start: text(f, "start"),
          end: text(f, "end"),
          actual: text(f, "actual"),
          responsible: text(f, "responsible"),
          provider: text(f, "provider"),
          description: text(f, "description"),
          state: text(f, "state"),
        }),
      {
        onDone: () => {
          close()
          notify("Mantenimiento guardado. Estado y disponibilidad actualizados.")
        },
      },
    )
  }
  function saveExam(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const f = new FormData(e.currentTarget)

    call(
      () =>
        api.saveExam({
          id: modal?.id || "",
          name: text(f, "name"),
          category: text(f, "category"),
          price: Number(f.get("price")),
          time: text(f, "time"),
          sample: text(f, "sample"),
          volume: text(f, "volume"),
          prep: text(f, "prep"),
          equipment: text(f, "equipment"),
          reagent: text(f, "reagent"),
          material: text(f, "material"),
          enabled: f.get("enabled") === "on",
          format: text(f, "format"),
        }),
      {
        onDone: () => {
          close()
          notify("Examen guardado correctamente.")
        },
      },
    )
  }

  function ModalContent() {
    if (!modal) return null
    const type = modal.type
    if (type === "appearance")
      return (
        <Modal title="Apariencia" subtitle="Color de la interfaz" onClose={close} wide>
          {AppearanceSettings()}
          <div className="modal-footer">
            <Button onClick={close}>Listo</Button>
          </div>
        </Modal>
      )
    if (type === "patient") {
      const p = db.patients.find((p) => p.id === modal.id)
      return (
        <Modal
          title={p ? "Editar paciente" : "Registrar nuevo paciente"}
          subtitle="Completa los datos de identificación y contacto."
          onClose={close}
          wide
        >
          <form onSubmit={savePatient}>
            <div className="form-section-label">
              <Icon name="user" size={17} /> Identificación del paciente
            </div>
            <div className="form-grid">
              <Field
                label="DNI"
                required
                hint="DNI de 8 dígitos"
              >
                <input
                  name="dni"
                  pattern="[0-9]{8}"
                  maxLength={8}
                  inputMode="numeric"
                  placeholder="12345678"
                  defaultValue={p?.dni}
                  required
                />
              </Field>
              <Field label="Sexo" required>
                <select name="sex" defaultValue={p?.sex || "Femenino"}>
                  {["Femenino", "Masculino", "Otro"].map((x) => (
                    <option key={x}>{x}</option>
                  ))}
                </select>
              </Field>
              <Field label="Nombres" required>
                <input
                  name="first"
                  placeholder="Nombres"
                  defaultValue={p?.first}
                  required
                />
              </Field>
              <Field label="Apellidos" required>
                <input
                  name="last"
                  placeholder="Apellidos"
                  defaultValue={p?.last}
                  required
                />
              </Field>
              <BirthField birth={p?.birth} />
            </div>
            <div className="form-section-label">
              <Icon name="phone" size={17} /> Información de contacto
            </div>
            <div className="form-grid">
              <Field label="Celular">
                <input
                  name="phone"
                  inputMode="tel"
                  pattern="[0-9]{9}"
                  maxLength={9}
                  placeholder="999888777"
                  defaultValue={p?.phone}
                />
              </Field>
              <Field label="Correo electrónico">
                <input
                  type="email"
                  name="email"
                  placeholder="correo@ejemplo.com"
                  defaultValue={p?.email}
                />
              </Field>
              <Field label="Dirección">
                <input
                  name="address"
                  defaultValue={p?.address}
                  placeholder="Av. Principal 123"
                />
              </Field>
              <Field label="Observaciones">
                <textarea
                  name="notes"
                  defaultValue={p?.notes}
                  placeholder="Información adicional del paciente"
                />
              </Field>
            </div>
            {formError && <Notice tone="warning">{formError}</Notice>}
            <div className="modal-footer">
              <Button type="button" variant="secondary" onClick={close}>
                Cancelar
              </Button>
              <Button
                type="submit"
                name="action"
                value="request"
                variant="secondary"
                icon="clipboard"
              >
                Guardar y crear solicitud
              </Button>
              <Button type="submit" icon="check">
                Guardar paciente
              </Button>
            </div>
          </form>
        </Modal>
      )
    }
    if (type === "request") {
      const selected = db.patients.find((p) => p.id === requestPatient)
      const bad = chosenExams.filter(
        (id) => availability(exam(id), db.resources).length,
      )
      return (
        <Modal
          title="Nueva solicitud de análisis"
          subtitle="Selecciona el paciente y verifica los recursos de cada examen."
          onClose={close}
          wide
        >
          <form onSubmit={saveRequest}>
            <Field label="Paciente" required>
              <select
                value={requestPatient}
                onChange={(e) => setRequestPatient(e.target.value)}
                required
              >
                <option value="">Selecciona un paciente...</option>
                {db.patients.map((p) => (
                  <option key={p.id} value={p.id}>
                    {fullName(p)} · DNI {p.dni}
                  </option>
                ))}
              </select>
            </Field>
            {selected && (
              <div className="context-strip">
                <Avatar name={fullName(selected)} />
                <div>
                  <strong>{fullName(selected)}</strong>
                  <span>
                    DNI {selected.dni} · {age(selected)} años · {selected.id}
                  </span>
                </div>
              </div>
            )}
            <div className="form-section-label">
              <Icon name="flask" size={17} /> Exámenes solicitados
            </div>
            {!db.exams.length && (
              <Notice tone="warning">
                Todavía no hay exámenes en el catálogo, así que no hay nada que
                solicitar. Regístralos en <strong>Catálogo de exámenes</strong>;
                cada examen necesita antes su equipo, su reactivo y su material
                en <strong>Inventario</strong>.
              </Notice>
            )}
            <div className="exam-selection">
              {db.exams.map((ex) => {
                const reasons = availability(ex, db.resources)
                return (
                  <label
                    key={ex.id}
                    className={`exam-option ${
                      chosenExams.includes(ex.id) ? "selected" : ""
                    }`}
                  >
                    <input
                      type="checkbox"
                      checked={chosenExams.includes(ex.id)}
                      onChange={(e) =>
                        setChosenExams(
                          e.target.checked
                            ? [...chosenExams, ex.id]
                            : chosenExams.filter((id) => id !== ex.id),
                        )
                      }
                    />
                    <div>
                      <strong>{ex.name}</strong>
                      <small>
                        {ex.time} · {ex.prep}
                      </small>
                      {reasons.length > 0 && (
                        <span className="warning-text">
                          {reasons.join(" · ")}
                        </span>
                      )}
                    </div>
                    <div className="exam-option-price">
                      <strong>{money(ex.price)}</strong>
                      {availabilityBadge(ex)}
                    </div>
                  </label>
                )
              })}
            </div>
            {bad.length > 0 && (
              <>
                <Notice tone="warning">
                  <strong>Este examen no puede procesarse actualmente.</strong>
                  <br />
                  Revisa los recursos indicados. Puedes registrar la solicitud,
                  pero no procesarla.
                </Notice>
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={allowPending}
                    onChange={(e) => setAllowPending(e.target.checked)}
                  />
                  Registrar como <strong>Pendiente de disponibilidad</strong>
                </label>
              </>
            )}
            {formError && <Notice tone="warning">{formError}</Notice>}
            <div className="modal-footer">
              <span className="total">
                Total{" "}
                <strong>
                  {money(chosenExams.reduce((n, id) => n + exam(id).price, 0))}
                </strong>
              </span>
              <Button type="button" variant="secondary" onClick={close}>
                Cancelar
              </Button>
              <Button
                type="submit"
                icon="check"
                disabled={bad.length > 0 && !allowPending}
              >
                Confirmar solicitud
              </Button>
            </div>
          </form>
        </Modal>
      )
    }
    if (type === "requestDetail") {
      const req = db.requests.find((r) => r.id === modal.id)!
      return (
        <Modal
          title={req.id}
          subtitle={fullName(patient(req.patient))}
          onClose={close}
          wide
        >
          <div className="detail-grid">
            <div>
              <span>Paciente</span>
              <strong>{fullName(patient(req.patient))}</strong>
            </div>
            <div>
              <span>DNI</span>
              <strong>{patient(req.patient).dni}</strong>
            </div>
            <div>
              <span>Fecha</span>
              <strong>{dateLabel(req.date)}</strong>
            </div>
            <div>
              <span>Estado registrado</span>
              <Badge
                value={
                  req.pending ? "Pendiente de disponibilidad" : "Registrada"
                }
              />
            </div>
          </div>
          <DataTable
            columns={["Examen", "Precio", "Preparación", "Disponibilidad"]}
            rows={req.exams.map((id) => {
              const ex = exam(id)
              return [ex.name, money(ex.price), ex.prep, availabilityBadge(ex)]
            })}
          />
          <div className="modal-footer">
            <Button variant="secondary" onClick={close}>
              Cerrar
            </Button>
            <Button
              icon="tube"
              onClick={() => open("sample", req.id)}
            >
              Registrar muestra
            </Button>
          </div>
        </Modal>
      )
    }
    if (type === "sample") {
      const req = db.requests.find((r) => r.id === sampleRequest)
      const ex = db.exams.find((e) => e.id === sampleExam)
      const p = req ? patient(req.patient) : null
      return (
        <Modal
          title="Registro de muestra"
          subtitle="Vincula la muestra a una solicitud y genera su código de seguimiento."
          onClose={close}
        >
          <form onSubmit={saveSample}>
            <Field label="Solicitud" required>
              <select
                value={sampleRequest}
                onChange={(e) => {
                  const solicitud = db.requests.find(
                    (r) => r.id === e.target.value,
                  )
                  const pendientes = solicitud ? faltanMuestras(solicitud) : []
                  setSampleRequest(e.target.value)
                  setSampleExam(pendientes.length === 1 ? pendientes[0] : "")
                }}
                required
              >
                <option value="">Selecciona una solicitud...</option>
                {db.requests
                  .filter(
                    (r) => faltanMuestras(r).length || r.id === sampleRequest,
                  )
                  .map((r) => (
                  <option key={r.id} value={r.id}>
                    {r.id} · {fullName(patient(r.patient))}
                  </option>
                ))}
              </select>
            </Field>
            {p && (
              <div className="context-strip">
                <Avatar name={fullName(p)} />
                <div>
                  <strong>{fullName(p)}</strong>
                  <span>
                    DNI {p.dni} · {age(p)} años
                  </span>
                </div>
              </div>
            )}
            <Field label="Examen" required>
              <select
                value={sampleExam}
                onChange={(e) => setSampleExam(e.target.value)}
                required
              >
                <option value="">Selecciona un examen pendiente...</option>
                {req?.exams
                  .filter(
                    (id) =>
                      !db.samples.some(
                        (s) => s.request === req.id && s.exam === id,
                      ),
                  )
                  .map((id) => (
                    <option key={id} value={id}>
                      {exam(id).name}
                    </option>
                  ))}
              </select>
            </Field>
            {req &&
              req.exams.every((id) =>
                db.samples.some((s) => s.request === req.id && s.exam === id),
              ) && (
                <Notice>
                  Todos los exámenes de esta solicitud ya tienen una muestra
                  registrada. Crea una nueva solicitud para otra atención.
                </Notice>
              )}
            {ex && (
              <>
                <div className="context-strip">
                  <Icon name="tube" />
                  <div>
                    <strong>
                      {ex.sample} · {ex.volume}
                    </strong>
                    <span>{ex.prep}</span>
                  </div>
                  {availabilityBadge(ex)}
                </div>
                {availability(ex, db.resources).length > 0 && (
                  <Notice tone="warning">
                    {availability(ex, db.resources).join("; ")}
                  </Notice>
                )}
              </>
            )}
            <div className="form-grid">
              <Field label="Fecha y hora de toma" required>
                <InputFechaHora
                  name="date"
                  label="Fecha y hora de toma"
                  defaultValue={timestamp().slice(0, 16)}
                  max={TODAY + "T23:59"}
                  required
                />
              </Field>
              <Field label="Personal responsable" required>
                <input
                  name="responsible"
                  placeholder="Quien tomó la muestra"
                  required
                />
              </Field>
            </div>
            <Notice>
              El código se genera automáticamente. Se descontará una unidad de
              reactivo y material del stock.
            </Notice>
            {formError && <Notice tone="warning">{formError}</Notice>}
            <div className="modal-footer">
              <Button type="button" variant="secondary" onClick={close}>
                Cancelar
              </Button>
              <Button
                icon="tube"
                type="submit"
                disabled={
                  !sampleExam ||
                  (!!ex && availability(ex, db.resources).length > 0)
                }
              >
                Registrar muestra
              </Button>
            </div>
          </form>
        </Modal>
      )
    }
    if (type === "trace") {
      const s = db.samples.find((x) => x.id === modal.id)!
      const p = patient(s.patient),
        ex = exam(s.exam)
      return (
        <Modal
          title="Trazabilidad de muestra"
          subtitle={s.id}
          onClose={close}
          wide
        >
          <div className="context-strip">
            <Avatar name={fullName(p)} />
            <div>
              <strong>{fullName(p)}</strong>
              <span>
                DNI {p.dni} · {age(p)} años · {ex.name}
              </span>
            </div>
            <Badge value={s.state} />
          </div>
          <div className="detail-grid">
            <div>
              <span>Solicitud</span>
              <strong>{s.request}</strong>
            </div>
            <div>
              <span>Tipo de muestra</span>
              <strong>{ex.sample}</strong>
            </div>
            <div>
              <span>Fecha de toma</span>
              <strong>
                {dateLabel(s.date)} · {timeLabel(s.date)}
              </strong>
            </div>
            <div>
              <span>Responsable</span>
              <strong>{s.responsible}</strong>
            </div>
          </div>
          <div className="form-section-label">
            <Icon name="shield" size={17} /> Cadena de trazabilidad
          </div>
          <div className="sample-timeline">
            {[
              "RECIBIDA",
              "EN ANÁLISIS",
              "EN VALIDACIÓN",
              "VALIDADO",
              "PUBLICADA",
            ].map((state, i) => {
              const h = s.history.find((x) => x.state === state)
              return (
                <div className={h ? "completed" : ""} key={state}>
                  <span className="step-circle">
                    {h ? <Icon name="check" size={16} /> : i + 1}
                  </span>
                  <div>
                    <strong>{state}</strong>
                    <small>
                      {h
                        ? `${dateLabel(h.date)} · ${timeLabel(h.date)}`
                        : "Pendiente"}
                    </small>
                    {h && <span>{h.user}</span>}
                  </div>
                </div>
              )
            })}
          </div>
          {s.state !== "PUBLICADA" && (
            <Notice>
              Resultado pendiente. Los valores aún no publicados no se muestran
              en el historial del paciente.
            </Notice>
          )}
          <div className="modal-footer">
            <Button variant="secondary" onClick={close}>
              Cerrar
            </Button>
            {s.state === "RECIBIDA" && (
              <Button
                icon="flask"
                onClick={() => {
                  changeSample(s.id, "EN ANÁLISIS")
                }}
              >
                Enviar a análisis
              </Button>
            )}
            {s.state === "EN ANÁLISIS" && (
              <Button
                icon="edit"
                onClick={() => open("result", s.id)}
              >
                Ingresar resultado
              </Button>
            )}
            {s.state === "EN VALIDACIÓN" && (
              <Button
                icon="shield"
                onClick={() => open("result", s.id)}
              >
                Revisar resultado
              </Button>
            )}
            {s.state === "VALIDADO" && (
              <Button
                icon="checkcircle"
                onClick={() => importantChange(s, "PUBLICADA")}
              >
                Publicar resultado
              </Button>
            )}
            {s.state === "PUBLICADA" && (
              <Button icon="eye" onClick={() => open("published", s.id)}>
                Ver resultado
              </Button>
            )}
          </div>
        </Modal>
      )
    }
    if (type === "result" || type === "published") {
      const s = db.samples.find(
        (x) => x.id === (type === "published" ? modal.id : resultSample),
      )
      const ex = s ? exam(s.exam) : null
      const p = s ? patient(s.patient) : null
      const locked =
        type === "published" ||
        (!!s && ["EN VALIDACIÓN", "VALIDADO", "PUBLICADA"].includes(s.state))
      const params = ex ? db.formats[ex.format] : []
      const out = params.filter((param) => {
        const val = (locked ? s!.results : values)[param.name]
        return (
          val &&
          param.min != null &&
          (Number(val) < param.min || Number(val) > param.max!)
        )
      })
      return (
        <Modal
          title={
            type === "published"
              ? "Resultado publicado"
              : locked
                ? "Revisión de resultado"
                : "Ingreso de resultados"
          }
          subtitle="Formato específico por examen · Intervalos de referencia por parámetro"
          onClose={close}
          wide
        >
          {type === "result" && !locked && (
            <Field label="Código de muestra" required>
              <select
                value={resultSample}
                onChange={(e) => {
                  const ss = db.samples.find((x) => x.id === e.target.value)
                  setResultSample(e.target.value)
                  setValues(ss?.results || {})
                  setResultNotes(ss?.notes || "")
                  setFormError("")
                }}
              >
                <option value="">Selecciona una muestra en análisis...</option>
                {db.samples
                  .filter((x) => x.state === "EN ANÁLISIS")
                  .map((x) => (
                    <option value={x.id} key={x.id}>
                      {x.id} · {fullName(patient(x.patient))} ·{" "}
                      {exam(x.exam).name}
                    </option>
                  ))}
              </select>
            </Field>
          )}
          {s && ex && p ? (
            <>
              <div className="context-strip">
                <Avatar name={fullName(p)} />
                <div>
                  <strong>{fullName(p)}</strong>
                  <span>
                    DNI {p.dni} · {age(p)} años · {s.id}
                  </span>
                </div>
                <Badge value={s.state} />
              </div>
              <div className="detail-grid result-context">
                <div>
                  <span>Examen</span>
                  <strong>{ex.name}</strong>
                </div>
                <div>
                  <span>Tipo de muestra</span>
                  <strong>{ex.sample}</strong>
                </div>
                <div>
                  <span>Fecha de toma</span>
                  <strong>
                    {dateLabel(s.date)} · {timeLabel(s.date)}
                  </strong>
                </div>
                <div>
                  <span>Responsable</span>
                  <strong>{s.responsible}</strong>
                </div>
              </div>
              <div className="form-section-label">
                <Icon name="file" size={17} /> {ex.name} · Formato de resultados
              </div>
              <DataTable
                columns={[
                  "Parámetro",
                  "Resultado",
                  "Unidad",
                  "Referencia",
                  "Observación",
                ]}
                rows={params.map((param) => {
                  const val = (locked ? s.results : values)[param.name] || ""
                  const bad =
                    val &&
                    param.min != null &&
                    (Number(val) < param.min || Number(val) > param.max!)
                  return [
                    <strong>{param.name}</strong>,
                    locked ? (
                      <strong className={bad ? "warning-text" : "result-value"}>
                        {val || "—"}
                      </strong>
                    ) : param.options?.length ? (
                      <select
                        aria-label={param.name}
                        value={val}
                        onChange={(e) =>
                          setValues({ ...values, [param.name]: e.target.value })
                        }
                      >
                        <option value="">Seleccionar...</option>
                        {param.options.map((v) => (
                          <option key={v}>{v}</option>
                        ))}
                      </select>
                    ) : (
                      <input
                        aria-label={param.name}
                        type={param.min != null ? "number" : "text"}
                        min={param.min != null ? 0 : undefined}
                        step="any"
                        className={bad ? "out-of-range" : ""}
                        value={val}
                        placeholder="Ingresar"
                        onChange={(e) =>
                          setValues({ ...values, [param.name]: e.target.value })
                        }
                      />
                    ),
                    param.unit,
                    <span className="mono">{param.reference}</span>,
                    // El aviso de fuera de rango NO sustituye a la
                    // observación, como hacía el original: se muestra encima y
                    // el campo sigue disponible. Un valor desviado es
                    // precisamente el que necesita una anotación.
                    <div className="obs-cell">
                      {bad && (
                        <span className="warning-text">
                          <Icon name="alert" size={14} /> Fuera de rango
                        </span>
                      )}
                      {locked ? (
                        <span>{s.results[`${param.name}_obs`] || "—"}</span>
                      ) : (
                        <input
                          aria-label={`Observación ${param.name}`}
                          value={values[`${param.name}_obs`] || ""}
                          placeholder="Opcional"
                          onChange={(e) =>
                            setValues({
                              ...values,
                              [`${param.name}_obs`]: e.target.value,
                            })
                          }
                        />
                      )}
                    </div>,
                  ]
                })}
              />
              {out.length > 0 && (
                <Notice tone="warning">
                  <strong>
                    Fuera del intervalo de referencia configurado.
                  </strong>{" "}
                  {out.map((p) => p.name).join(", ")}. Este indicador no
                  constituye un diagnóstico médico.
                </Notice>
              )}
              <Field label="Observaciones generales">
                {locked ? (
                  <div className="readonly-box">
                    {s.notes || "Sin observaciones"}
                  </div>
                ) : (
                  <textarea
                    value={resultNotes}
                    onChange={(e) => setResultNotes(e.target.value)}
                    placeholder="Observaciones técnicas del procesamiento..."
                  />
                )}
              </Field>
              {formError && <Notice tone="warning">{formError}</Notice>}
              <div className="modal-footer">
                <Button variant="secondary" onClick={close}>
                  Cerrar
                </Button>
                {!locked && (
                  <>
                    <Button
                      variant="secondary"
                      icon="file"
                      onClick={() => saveResult("EN ANÁLISIS")}
                    >
                      Guardar borrador
                    </Button>
                    <Button
                      icon="shield"
                      onClick={() => saveResult("EN VALIDACIÓN")}
                    >
                      Enviar a validación
                    </Button>
                  </>
                )}
                {s.state === "EN VALIDACIÓN" && (
                  <Button
                    icon="shield"
                    onClick={() => importantChange(s, "VALIDADO")}
                  >
                    Validar resultado
                  </Button>
                )}
                {s.state === "VALIDADO" && (
                  <Button
                    icon="checkcircle"
                    onClick={() => importantChange(s, "PUBLICADA")}
                  >
                    Publicar resultado
                  </Button>
                )}
                {type === "published" && (
                  <Button
                    variant="secondary"
                    icon="print"
                    onClick={() => abrirVistaDeImpresion(s.id)}
                  >
                    Vista de impresión
                  </Button>
                )}
              </div>
            </>
          ) : (
            <div className="empty">
              <Icon name="file" size={36} />
              <strong>Selecciona una muestra para comenzar</strong>
              <span>
                El formulario se adaptará automáticamente al examen solicitado.
              </span>
            </div>
          )}
        </Modal>
      )
    }
    if (type === "examDetail" || type === "references") {
      const ex = exam(modal.id!)
      const reasons = availability(ex, db.resources)
      return (
        <Modal
          title={ex.name}
          subtitle={`${ex.id} · ${ex.category}`}
          onClose={close}
          wide
        >
          {type === "references" ? (
            <DataTable
              columns={["Parámetro", "Unidad", "Intervalo de referencia"]}
              rows={db.formats[ex.format].map((p) => [
                p.name,
                p.unit,
                p.reference,
              ])}
            />
          ) : (
            <>
              <div className="exam-detail-heading">
                <span className="exam-large-icon">
                  <Icon name="flask" size={32} />
                </span>
                <div>
                  <span className="eyebrow">{ex.category}</span>
                  <strong>{money(ex.price)}</strong>
                  <span>{ex.time} · Tiempo estimado</span>
                </div>
                {availabilityBadge(ex)}
              </div>
              <div className="detail-grid">
                {[
                  ["Tipo de muestra", ex.sample],
                  ["Volumen requerido", ex.volume],
                  ["Preparación del paciente", ex.prep],
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
              <div className="form-section-label">
                <Icon name="box" size={17} /> Recursos necesarios
              </div>
              {[ex.equipment, ex.reagent, ex.material].map((id) => {
                const r = resource(id)
                return (
                  <div className="resource-dependency" key={id}>
                    <Icon
                      name={
                        r.kind === "Equipos"
                          ? "monitor"
                          : r.kind === "Reactivos"
                            ? "flask"
                            : "box"
                      }
                    />
                    <div>
                      <strong>{r.name}</strong>
                      <small>
                        {r.kind} ·{" "}
                        {r.kind === "Equipos"
                          ? `Próxima intervención: ${dateLabel(r.next)}`
                          : `${r.qty} ${r.unit} en stock`}
                      </small>
                    </div>
                    <Badge value={resourceStatus(r)} />
                  </div>
                )
              })}
              <p className="muted small-text">
                Materiales complementarios: guantes, alcohol y torundas.
                Recursos de apoyo: no condicionan la disponibilidad.
              </p>
              {reasons.length > 0 ? (
                <Notice tone="warning">
                  <strong>Examen temporalmente no disponible</strong>
                  <br />
                  {reasons.join(" · ")}
                </Notice>
              ) : (
                <Notice tone="success">
                  Equipo, reactivos y materiales disponibles para procesar este
                  examen.
                </Notice>
              )}
            </>
          )}
          <div className="modal-footer">
            <Button variant="secondary" onClick={close}>
              Cerrar
            </Button>
            <Button icon="edit" onClick={() => open("exam", ex.id)}>
              Editar examen
            </Button>
          </div>
        </Modal>
      )
    }
    if (type === "exam") {
      const ex = db.exams.find((x) => x.id === modal.id)
      return (
        <Modal
          title={ex ? "Editar examen" : "Agregar examen al catálogo"}
          onClose={close}
          wide
        >
          <form onSubmit={saveExam}>
            {["Equipos", "Reactivos", "Materiales"].some(
              (k) => !db.resources.some((r) => r.kind === k),
            ) && (
              <Notice tone="warning">
                Un examen se apoya en un equipo, un reactivo y un material, y
                falta registrar alguna de las tres familias en{" "}
                <strong>Inventario</strong>. Sin ellos el examen nacerá como no
                disponible.
              </Notice>
            )}
            <div className="form-grid">
              <Field label="Nombre" required>
                <input name="name" defaultValue={ex?.name} required />
              </Field>
              <Field label="Categoría">
                <select name="category" defaultValue={ex?.category}>
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
              </Field>
              <Field label="Precio (S/)" required>
                <input
                  name="price"
                  type="number"
                  min="0"
                  step="0.01"
                  defaultValue={ex?.price || 0}
                  required
                />
              </Field>
              <Field label="Tiempo estimado" required>
                <input
                  name="time"
                  defaultValue={ex?.time || "24 horas"}
                  required
                />
              </Field>
              <Field label="Tipo de muestra" required>
                <input
                  name="sample"
                  defaultValue={ex?.sample || "Sangre venosa"}
                  required
                />
              </Field>
              <Field label="Volumen" required>
                <input
                  name="volume"
                  defaultValue={ex?.volume || "3 mL"}
                  required
                />
              </Field>
              <Field label="Preparación">
                <input name="prep" defaultValue={ex?.prep} />
              </Field>
              <Field label="Formato de resultados">
                <select name="format" defaultValue={ex?.format || "hemogram"}>
                  {Object.keys(db.formats).map((f) => (
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
              </Field>
              {[
                ["equipment", "Equipo", "Equipos"],
                ["reagent", "Reactivo", "Reactivos"],
                ["material", "Material", "Materiales"],
              ].map(([key, label, kind]) => (
                <Field label={label} key={key}>
                  <select
                    name={key}
                    defaultValue={
                      ex?.[(key as "equipment" | "reagent" | "material")]
                    }
                    required
                  >
                    {db.resources
                      .filter((r) => r.kind === kind)
                      .map((r) => (
                        <option key={r.id} value={r.id}>
                          {r.name}
                        </option>
                      ))}
                  </select>
                </Field>
              ))}
            </div>
            <label className="checkbox-label">
              <input
                name="enabled"
                type="checkbox"
                defaultChecked={ex?.enabled ?? true}
              />
              Examen habilitado
            </label>
            <div className="modal-footer">
              <Button type="button" variant="secondary" onClick={close}>
                Cancelar
              </Button>
              <Button icon="check" type="submit">
                Guardar examen
              </Button>
            </div>
          </form>
        </Modal>
      )
    }
    if (type === "equipment") {
      const r = resource(modal.id!)
      const mm = db.maintenance.filter((m) => m.equipment === r.id)
      return (
        <Modal
          title={r.name}
          subtitle={`${r.id} · ${r.area}`}
          onClose={close}
          wide
        >
          <div className="context-strip">
            <Icon name="monitor" size={28} />
            <div>
              <strong>
                {r.brand} · {r.model}
              </strong>
              <span>Serie {r.serial}</span>
            </div>
            <Badge value={r.status} />
          </div>
          <div className="detail-grid">
            <div>
              <span>Último mantenimiento</span>
              <strong>{dateLabel(r.last)}</strong>
            </div>
            <div>
              <span>Próxima intervención / fin estimado</span>
              <strong>{dateLabel(r.next)}</strong>
            </div>
            <div>
              <span>Responsable</span>
              <strong>{r.responsible}</strong>
            </div>
            <div>
              <span>Exámenes relacionados</span>
              <strong>
                {db.exams
                  .filter((e) => e.equipment === r.id)
                  .map((e) => e.name)
                  .join(", ") || "Ninguno"}
              </strong>
            </div>
          </div>
          <div className="form-section-label">
            <Icon name="wrench" size={17} /> Historial de mantenimiento
          </div>
          <DataTable
            columns={[
              "Tipo",
              "Inicio",
              "Fin estimado",
              "Estado",
              "Responsable",
              "",
            ]}
            rows={mm.map((m) => [
              m.type,
              dateLabel(m.start),
              dateLabel(m.end),
              <Badge value={m.state} />,
              m.responsible,
              <Button
                small
                variant="ghost"
                onClick={() => open("maintenance", m.id)}
              >
                Editar
              </Button>,
            ])}
          />
          <div className="modal-footer">
            <Button variant="secondary" onClick={close}>
              Cerrar
            </Button>
            <Button
              variant="secondary"
              icon="edit"
              onClick={() => open("resource", r.id)}
            >
              Editar equipo
            </Button>
            <Button icon="wrench" onClick={() => open("maintenanceFor", r.id)}>
              Registrar mantenimiento
            </Button>
          </div>
        </Modal>
      )
    }
    if (type === "maintenance" || type === "maintenanceFor") {
      const m = db.maintenance.find((x) => x.id === modal.id)
      return (
        <Modal
          title={m ? "Actualizar mantenimiento" : "Registrar mantenimiento"}
          subtitle="Los cambios de estado y fecha actualizan la disponibilidad del equipo."
          onClose={close}
          wide
        >
          <form onSubmit={saveMaintenance}>
            <div className="form-grid">
              <Field label="Equipo" required>
                <select
                  name="equipment"
                  defaultValue={
                    m?.equipment ||
                    (type === "maintenanceFor" ? modal.id : undefined)
                  }
                  required
                >
                  {db.resources
                    .filter((r) => r.kind === "Equipos")
                    .map((r) => (
                      <option value={r.id} key={r.id}>
                        {r.name}
                      </option>
                    ))}
                </select>
              </Field>
              <Field label="Tipo">
                <select name="type" defaultValue={m?.type || "Preventivo"}>
                  <option>Preventivo</option>
                  <option>Correctivo</option>
                </select>
              </Field>
              <Field label="Fecha de inicio" required>
                <InputFecha
                  name="start"
                  defaultValue={m?.start || TODAY}
                  required
                />
              </Field>
              <Field label="Finalización estimada" required>
                <InputFecha
                  name="end"
                  // Una semana por delante. Estaba fija en "2026-09-30", de la
                  // época de la fecha congelada: con el reloj real caía en el
                  // pasado y TODO mantenimiento nuevo nacía inválido, con
                  // "La finalización no puede ser anterior al inicio".
                  defaultValue={m?.end || sumarDias(TODAY, 7)}
                  required
                />
              </Field>
              <Field label="Finalización real">
                <InputFecha name="actual" defaultValue={m?.actual} />
              </Field>
              <Field label="Estado">
                <select name="state" defaultValue={m?.state || "Programado"}>
                  {["Programado", "En proceso", "Finalizado", "Cancelado"].map(
                    (s) => (
                      <option key={s}>{s}</option>
                    ),
                  )}
                </select>
              </Field>
              <Field label="Responsable" required>
                <input
                  name="responsible"
                  defaultValue={m?.responsible || ""}
                  placeholder="Quien realiza el mantenimiento"
                  required
                />
              </Field>
              <Field label="Proveedor">
                <input
                  name="provider"
                  defaultValue={m?.provider}
                  placeholder="Nombre del proveedor"
                />
              </Field>
            </div>
            <Field label="Descripción y observaciones">
              <textarea
                name="description"
                defaultValue={m?.description}
                rows={3}
              />
            </Field>
            {formError && <Notice tone="warning">{formError}</Notice>}
            <div className="modal-footer">
              <Button type="button" variant="secondary" onClick={close}>
                Cancelar
              </Button>
              <Button type="submit" icon="check">
                Guardar mantenimiento
              </Button>
            </div>
          </form>
        </Modal>
      )
    }
    if (type === "resource") {
      const r = db.resources.find((x) => x.id === modal.id)
      const kind =
        r?.kind ||
        (view === "Equipos"
          ? "Equipos"
          : view === "Materiales"
            ? "Materiales"
            : view === "Inventario"
              ? inventoryTab
              : "Reactivos")
      return (
        <Modal
          title={r ? "Editar recurso" : "Agregar recurso"}
          onClose={close}
          wide
        >
          <form onSubmit={saveResource}>
            <input type="hidden" name="kind" value={kind} />
            <div className="form-grid">
              <Field label="Nombre" required>
                <input name="name" defaultValue={r?.name} required />
              </Field>
              <Field label="Tipo de recurso">
                <input value={kind} readOnly />
              </Field>
              <Field label="Marca">
                <input name="brand" defaultValue={r?.brand || "BioLab Demo"} />
              </Field>
              <Field label="Área / Ubicación" required>
                <input
                  name="area"
                  defaultValue={r?.area || "Laboratorio Central"}
                  required
                />
              </Field>
              {kind === "Equipos" ? (
                <>
                  <Field label="Modelo">
                    <input name="model" defaultValue={r?.model} />
                  </Field>
                  <Field label="Número de serie">
                    <input name="serial" defaultValue={r?.serial} />
                  </Field>
                  <Field label="Estado">
                    <select
                      name="status"
                      defaultValue={r?.status || "OPERATIVO"}
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
                  </Field>
                  <Field label="Responsable">
                    <input
                      name="responsible"
                      defaultValue={r?.responsible || ""}
                      placeholder="Encargado del recurso"
                    />
                  </Field>
                  <Field label="Último mantenimiento">
                    <InputFecha name="last" defaultValue={r?.last} />
                  </Field>
                  <Field label="Próximo mantenimiento">
                    <InputFecha name="next" defaultValue={r?.next} />
                  </Field>
                </>
              ) : (
                <>
                  <Field label="Cantidad actual" required>
                    <input
                      name="qty"
                      type="number"
                      min="0"
                      step="1"
                      defaultValue={r?.qty || 0}
                      required
                    />
                  </Field>
                  <Field label="Stock mínimo" required>
                    <input
                      name="min"
                      type="number"
                      min="0"
                      step="1"
                      defaultValue={r?.min || 10}
                      required
                    />
                  </Field>
                  <Field label="Unidad">
                    <input name="unit" defaultValue={r?.unit || "unidades"} />
                  </Field>
                  {kind === "Reactivos" && (
                    <>
                      <Field label="Lote" required>
                        <input name="lot" defaultValue={r?.lot} required />
                      </Field>
                      <Field label="Fecha de vencimiento" required>
                        <InputFecha
                          name="expires"
                          // Siete meses por delante, como la semilla. Estaba
                          // fija en "2027-04-30": sigue siendo futuro hoy,
                          // pero deja de serlo con el tiempo.
                          defaultValue={r?.expires || sumarDias(TODAY, 210)}
                          required
                        />
                      </Field>
                    </>
                  )}
                </>
              )}
            </div>
            {formError && <Notice tone="warning">{formError}</Notice>}
            <Notice>
              Los cambios de stock y estado recalculan la disponibilidad de los
              exámenes vinculados.
            </Notice>
            <div className="modal-footer">
              <Button type="button" variant="secondary" onClick={close}>
                Cancelar
              </Button>
              <Button type="submit" icon="check">
                Guardar recurso
              </Button>
            </div>
          </form>
        </Modal>
      )
    }
    if (type === "stockIn" || type === "stockOut") {
      const r = resource(modal.id!)
      const incoming = type === "stockIn"
      return (
        <Modal
          title={incoming ? "Registrar entrada" : "Registrar salida"}
          subtitle={`${r.name} · Stock actual: ${r.qty} ${r.unit}`}
          onClose={close}
        >
          <form
            onSubmit={(e) => {
              e.preventDefault()
              const f = new FormData(e.currentTarget),
                qty = Number(f.get("qty"))
              call(
                () =>
                  api.move(
                    r.id,
                    incoming ? "IN" : "OUT",
                    qty,
                    text(f, "notes"),
                  ),
                {
                  onDone: () => {
                    close()
                    notify(
                      "Movimiento registrado. Stock y disponibilidad actualizados.",
                    )
                  },
                },
              )
            }}
          >
            <Field label="Cantidad" required>
              <input
                name="qty"
                type="number"
                min="1"
                step="1"
                max={incoming ? undefined : r.qty}
                required
              />
            </Field>
            <Field label="Motivo / Observación" required>
              <textarea
                name="notes"
                placeholder="Motivo del movimiento"
                required
              />
            </Field>
            {formError && <Notice tone="warning">{formError}</Notice>}
            <div className="modal-footer">
              <Button type="button" variant="secondary" onClick={close}>
                Cancelar
              </Button>
              <Button type="submit" icon="check">
                Confirmar {incoming ? "entrada" : "salida"}
              </Button>
            </div>
          </form>
        </Modal>
      )
    }
    if (type === "purchase") {
      // El tipo y el buscador solo filtran lo que se ve: las cantidades ya
      // escritas se conservan al cambiar de pestaña, porque una guía de
      // remisión suele traer reactivos y materiales en el mismo bulto.
      const visibles = db.resources
        .filter((r) => r.kind === purchaseKind)
        .filter((r) => matches(purchaseSearch, r.name, r.id, r.lot))
      const elegidas = db.resources.filter(
        (r) => purchaseQty[r.id] !== undefined && purchaseQty[r.id] !== "",
      )
      const unidades = elegidas.reduce(
        (n, r) => n + (Number(purchaseQty[r.id]) || 0),
        0,
      )
      const escribir = (
        set: (v: Record<string, string>) => void,
        actual: Record<string, string>,
        id: string,
        valor: string,
      ) => set({ ...actual, [id]: valor })
      return (
        <Modal
          title="Registrar compra"
          subtitle="Una sola entrada para todo lo que llegó. Escribe la cantidad de cada artículo recibido; los que dejes en blanco no se tocan."
          onClose={close}
          wide
        >
          <form
            onSubmit={(e) => {
              e.preventDefault()
              const f = new FormData(e.currentTarget)
              if (!elegidas.length) {
                setFormError("Registra al menos una cantidad recibida.")
                return
              }
              if (
                elegidas.some(
                  (r) =>
                    !Number.isInteger(Number(purchaseQty[r.id])) ||
                    Number(purchaseQty[r.id]) <= 0,
                )
              ) {
                setFormError(
                  "Las cantidades deben ser enteras y mayores que cero.",
                )
                return
              }
              setConfirm({
                title: "Confirmar entrada de compra",
                message: `Se sumarán ${unidades} unidades al stock de ${elegidas.length} recursos. Cada línea quedará en auditoría con el proveedor y el documento.`,
                action: () => {
                  call(
                    () =>
                      api.purchase({
                        date: text(f, "date"),
                        supplier: text(f, "supplier"),
                        document: text(f, "document"),
                        responsible: text(f, "responsible"),
                        kind: purchaseKind,
                        items: elegidas.map((r) => ({
                          resourceId: r.id,
                          qty: purchaseQty[r.id],
                          lot: purchaseLot[r.id] || "",
                          expires: purchaseExp[r.id] || "",
                        })),
                      }),
                    {
                      onDone: () => {
                        close()
                        notify(
                          `Compra registrada. ${elegidas.length} recursos actualizados.`,
                        )
                      },
                    },
                  )
                },
              })
            }}
          >
            <div className="form-grid">
              <Field label="Fecha de recepción" required>
                <InputFecha name="date" defaultValue={TODAY} max={TODAY} required />
              </Field>
              <Field label="Proveedor" required>
                <input
                  name="supplier"
                  placeholder="Nombre del proveedor"
                  required
                />
              </Field>
              <Field label="Documento" hint="Guía de remisión o factura">
                <input name="document" placeholder="GR-000123" />
              </Field>
              <Field label="Responsable" required>
                <input
                  name="responsible"
                  placeholder="Quien recibe la compra"
                  required
                />
              </Field>
            </div>
            <div className="table-toolbar">
              <SearchBar
                value={purchaseSearch}
                onChange={setPurchaseSearch}
                placeholder="Buscar artículo, código o lote..."
              />
              <select
                aria-label="Tipo de recurso"
                value={purchaseKind}
                onChange={(e) => setPurchaseKind(e.target.value)}
              >
                <option>Reactivos</option>
                <option>Materiales</option>
              </select>
              <Button
                type="button"
                variant="secondary"
                small
                icon="refresh"
                onClick={() => {
                  // El atajo que de verdad ahorra tiempo: casi siempre se
                  // compra lo que está por debajo del mínimo, y la cantidad
                  // que se pide es la que hace falta para dejarlo al doble.
                  const propuesta = { ...purchaseQty }
                  visibles
                    .filter((r) => r.qty <= r.min)
                    .forEach((r) => {
                      propuesta[r.id] = String(Math.max(1, r.min * 2 - r.qty))
                    })
                  setPurchaseQty(propuesta)
                }}
              >
                Reponer bajo mínimo
              </Button>
            </div>
            {!db.resources.some((r) => r.kind === purchaseKind) && (
              <Notice tone="warning">
                No hay {purchaseKind.toLowerCase()} registrados todavía. Un
                artículo tiene que existir en el inventario antes de poder
                comprarlo: créalo con <strong>Agregar recurso</strong>.
              </Notice>
            )}
            <DataTable
              columns={[
                "Artículo",
                "Stock actual",
                "Cantidad recibida",
                "Lote",
                "Vencimiento",
              ]}
              empty="No hay artículos que coincidan"
              rows={visibles.map((r) => [
                <div className="stacked">
                  <strong>{r.name}</strong>
                  <small className="mono">{r.id}</small>
                </div>,
                <span className={r.qty <= r.min ? "warning-text" : undefined}>
                  {r.qty} {r.unit} · mín. {r.min}
                </span>,
                <input
                  aria-label={`Cantidad recibida de ${r.name}`}
                  type="number"
                  min="1"
                  step="1"
                  placeholder="0"
                  value={purchaseQty[r.id] ?? ""}
                  onChange={(e) =>
                    escribir(setPurchaseQty, purchaseQty, r.id, e.target.value)
                  }
                />,
                <input
                  aria-label={`Lote de ${r.name}`}
                  placeholder={r.lot || "Sin lote"}
                  value={purchaseLot[r.id] ?? ""}
                  onChange={(e) =>
                    escribir(setPurchaseLot, purchaseLot, r.id, e.target.value)
                  }
                />,
                <InputFecha
                  label={`Vencimiento de ${r.name}`}
                  min={TODAY}
                  value={purchaseExp[r.id] ?? ""}
                  onChange={(v) =>
                    escribir(setPurchaseExp, purchaseExp, r.id, v)
                  }
                />,
              ])}
            />
            {formError && <Notice tone="warning">{formError}</Notice>}
            <div className="modal-footer">
              <span className="total">
                {elegidas.length} artículos
                <strong>{unidades}</strong>
              </span>
              <Button type="button" variant="secondary" onClick={close}>
                Cancelar
              </Button>
              <Button type="submit" icon="check">
                Registrar entrada
              </Button>
            </div>
          </form>
        </Modal>
      )
    }
    if (type === "count") {
      const items = db.resources.filter((r) => r.kind === countKind)
      return (
        <Modal
          title="Realizar inventario físico"
          subtitle="Registra lo encontrado. Solo los recursos contados serán actualizados."
          onClose={close}
          wide
        >
          <form
            onSubmit={(e) => {
              e.preventDefault()
              const f = new FormData(e.currentTarget)
              const changed = items.filter(
                (r) => counts[r.id] !== undefined && counts[r.id] !== "",
              )
              if (!changed.length) {
                setFormError("Registra al menos una cantidad física.")
                return
              }
              if (
                changed.some(
                  (r) =>
                    !Number.isInteger(Number(counts[r.id])) ||
                    Number(counts[r.id]) < 0,
                )
              ) {
                setFormError("Las cantidades deben ser enteras y no negativas.")
                return
              }
              // Las dos comprobaciones de arriba se conservan en el cliente
              // porque el modal de confirmación va ANTES de guardar: sin
              // ellas se preguntaría "se actualizarán 0 recursos" y el error
              // llegaría después. El backend las repite con el mismo texto y
              // es el que manda. Un ajuste inválido no aplica ninguna línea.
              setConfirm({
                title: "Confirmar ajuste de inventario",
                message: `Se actualizará el stock de ${changed.length} recursos según el conteo físico. Se registrarán fecha, responsable, diferencias y observaciones en auditoría.`,
                action: () => {
                  call(
                    () =>
                      api.inventoryCount({
                        date: text(f, "date"),
                        responsible: text(f, "responsible"),
                        area: text(f, "area"),
                        kind: countKind,
                        items: changed.map((r) => ({
                          resourceId: r.id,
                          counted: counts[r.id],
                          observation: text(f, `obs-${r.id}`),
                        })),
                      }),
                    {
                      onDone: () => {
                        close()
                        notify(
                          "Inventario guardado. El stock fue actualizado.",
                        )
                      },
                    },
                  )
                },
              })
            }}
          >
            <div className="form-grid">
              <Field label="Fecha de inventario" required>
                <InputFecha name="date" defaultValue={TODAY} required />
              </Field>
              <Field label="Responsable" required>
                <input
                  name="responsible"
                  placeholder="Quien hace el conteo"
                  required
                />
              </Field>
              <Field label="Área" required>
                <input
                  name="area"
                  defaultValue="Laboratorio Central"
                  required
                />
              </Field>
              <Field label="Tipo de recurso">
                <select
                  value={countKind}
                  onChange={(e) => {
                    setCountKind(e.target.value)
                    setCounts({})
                  }}
                >
                  <option>Reactivos</option>
                  <option>Materiales</option>
                </select>
              </Field>
            </div>
            {!items.length && (
              <Notice tone="warning">
                No hay {countKind.toLowerCase()} registrados todavía. Agrégalos
                con <strong>Agregar recurso</strong> y después podrás contarlos.
              </Notice>
            )}
            <DataTable
              columns={[
                "Recurso",
                "Sistema",
                "Conteo físico",
                "Diferencia",
                "Observación",
              ]}
              empty={`Sin ${countKind.toLowerCase()} que contar`}
              rows={items.map((r) => [
                r.name,
                `${r.qty} ${r.unit}`,
                <input
                  aria-label={`Conteo ${r.name}`}
                  type="number"
                  min="0"
                  step="1"
                  placeholder="Sin contar"
                  value={counts[r.id] ?? ""}
                  onChange={(e) =>
                    setCounts({ ...counts, [r.id]: e.target.value })
                  }
                />,
                counts[r.id] !== undefined && counts[r.id] !== "" ? (
                  <strong
                    className={
                      Number(counts[r.id]) === r.qty
                        ? "result-value"
                        : "warning-text"
                    }
                  >
                    {Number(counts[r.id]) - r.qty}
                  </strong>
                ) : (
                  "—"
                ),
                <input
                  name={`obs-${r.id}`}
                  placeholder="Motivo de diferencia"
                  aria-label={`Observación ${r.name}`}
                />,
              ])}
            />
            {formError && <Notice tone="warning">{formError}</Notice>}
            <div className="modal-footer">
              <Button type="button" variant="secondary" onClick={close}>
                Cancelar
              </Button>
              <Button type="submit" icon="check">
                Guardar inventario
              </Button>
            </div>
          </form>
        </Modal>
      )
    }
    if (type === "alerts")
      return (
        <Modal
          title="Alertas operativas"
          subtitle={`${alerts.length} alertas requieren atención`}
          onClose={close}
        >
          <div className="alerts-list">
            {alerts.map((a, i) => (
              <button
                key={i}
                className={`alert-item ${a.tone}`}
                onClick={() => {
                  close()
                  go(a.module)
                }}
              >
                <span className="alert-icon">
                  <Icon name={a.icon} />
                </span>
                <span>
                  <strong>{a.title}</strong>
                  <small>{a.detail}</small>
                </span>
                <Icon name="chevron" size={16} />
              </button>
            ))}
          </div>
          {!alerts.length && (
            <Notice tone="success">
              No hay alertas operativas pendientes.
            </Notice>
          )}
        </Modal>
      )
    return null
  }

  const globalPatients = search.trim()
    ? db.patients
        .filter((p) => matches(search, fullName(p), p.dni, p.id))
        .slice(0, 4)
    : []
  const globalSamples = search.trim()
    ? db.samples.filter((s) => matches(search, s.id)).slice(0, 4)
    : []
  const globalExams = search.trim()
    ? db.exams.filter((e) => matches(search, e.name, e.id)).slice(0, 4)
    : []
  return (
    <div className="app-shell">
      <aside className="sidebar">
        <nav>
          {navigation.map((group) => (
            <div className="nav-group" key={group.group}>
              <span className="nav-label">{group.group}</span>
              {group.items.map(([name, icon]) => (
                <button
                  key={name}
                  className={`nav-item ${view === name ? "active" : ""}`}
                  onClick={() => go(name)}
                  title={name}
                >
                  <Icon name={icon} size={18} />
                  <span>{name}</span>
                  {name === "Resultados" &&
                    statusCount("EN VALIDACIÓN") > 0 && (
                      <span className="nav-count">
                        {statusCount("EN VALIDACIÓN")}
                      </span>
                    )}
                </button>
              ))}
            </div>
          ))}
        </nav>
        <div className="sidebar-bottom local-status">
          <Icon name="user" size={16} />
          <div>
            <strong>{db.responsible}</strong>
            <span>{db.branch || db.lab}</span>
          </div>
        </div>
      </aside>
      <div className="workspace">
        <header className="topbar">
          <div className="global-search-wrap">
            <SearchBar
              className="global-search"
              value={search}
              onChange={setSearch}
              placeholder="Buscar paciente, DNI, código de muestra o examen..."
            />
            {search && (
              <div className="global-results">
                <div className="global-results-label">
                  RESULTADOS DE BÚSQUEDA
                </div>
                {globalPatients.map((p) => (
                  <button key={p.id} onClick={() => openPatient(p.id)}>
                    <Icon name="user" />
                    <span>
                      <strong>{fullName(p)}</strong>
                      <small>
                        DNI {p.dni} · {p.id}
                      </small>
                    </span>
                    <Icon name="chevron" size={15} />
                  </button>
                ))}
                {globalSamples.map((s) => (
                  <button
                    key={s.id}
                    onClick={() => {
                      open("trace", s.id)
                      setSearch("")
                    }}
                  >
                    <Icon name="tube" />
                    <span>
                      <strong>{s.id}</strong>
                      <small>
                        {fullName(patient(s.patient))} · {exam(s.exam).name} ·{" "}
                        {s.state}
                      </small>
                    </span>
                    <Icon name="chevron" size={15} />
                  </button>
                ))}
                {globalExams.map((ex) => (
                  <button
                    key={ex.id}
                    onClick={() => {
                      open("examDetail", ex.id)
                      setSearch("")
                    }}
                  >
                    <Icon name="flask" />
                    <span>
                      <strong>{ex.name}</strong>
                      <small>
                        {ex.id} · {ex.category}
                      </small>
                    </span>
                    <Icon name="chevron" size={15} />
                  </button>
                ))}
                {!globalPatients.length &&
                  !globalSamples.length &&
                  !globalExams.length && (
                    <div className="search-empty">
                      No se encontraron coincidencias.
                    </div>
                  )}
                <button className="close-search" onClick={() => setSearch("")}>
                  Cerrar búsqueda
                </button>
              </div>
            )}
          </div>
          <div className="header-right">
            <button
              className="appearance-shortcut"
              onClick={() => open("appearance")}
              title="Cambiar color de la interfaz"
              aria-label="Apariencia: cambiar color de la interfaz"
            >
              <Icon name="monitor" size={17} />
              <span>Apariencia</span>
            </button>
            <div className="header-lab">
              <strong>Laboratorio clínico</strong>
            </div>
            <div className="notification-wrap">
              <button
                className={`icon-button notification-button ${
                  notices ? "active" : ""
                }`}
                title="Notificaciones"
                aria-label="Abrir notificaciones"
                onClick={() => setNotices(!notices)}
              >
                <Icon name="bell" size={21} />
                {db.notifications && alerts.length > 0 && <i />}
              </button>
              {notices && (
                <div className="notification-popover">
                  <div className="panel-heading">
                    <h2>Notificaciones</h2>
                    <span className="count-badge">
                      {db.notifications ? alerts.length : 0}
                    </span>
                  </div>
                  {db.notifications ? alerts.slice(0, 4).map((a, i) => (
                      <button
                        className={`alert-item ${a.tone}`}
                        key={i}
                        onClick={() => go(a.module)}
                      >
                        <Icon name={a.icon} size={18} />
                        <span>
                          <strong>{a.title}</strong>
                          <small>{a.detail}</small>
                        </span>
                      </button>
                    )) : <div className="padded muted">
                      Notificaciones desactivadas en Configuración.
                    </div>}
                  <button
                    className="panel-bottom-button"
                    onClick={() => {
                      setNotices(false)
                      open("alerts")
                    }}
                  >
                    Ver alertas operativas <Icon name="arrow" size={15} />
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>
        <main className="main-content">
          <div className="breadcrumb">
            <span>Espacio de trabajo</span>
            <Icon name="chevron" size={12} />
            <strong>{view}</strong>
            {patientId && (
              <>
                <Icon name="chevron" size={12} />
                <span>{fullName(patient(patientId))}</span>
              </>
            )}
            <span className="environment-label">
              <span className="live-dot" /> Sistema operativo{" "}
              <span className="env-separator">/</span> {db.branch || db.lab}
            </span>
          </div>
          {view !== "Dashboard" && !patientId && (
            <div className="page-heading">
              <div>
                <div className="eyebrow">
                  {[
                    "Equipos",
                    "Reactivos",
                    "Materiales",
                    "Inventario",
                    "Mantenimiento",
                  ].includes(view)
                    ? "RECURSOS"
                    : "GESTIÓN INTERNA"}
                </div>
                <h1>
                  {view === "Pacientes"
                    ? "Gestión de pacientes"
                    : view === "Solicitudes"
                      ? "Solicitudes de análisis"
                      : view === "Muestras"
                        ? "Registro de muestras"
                        : view}
                </h1>
                <p>{descriptions[view]}</p>
              </div>
              {actionsForView()}
            </div>
          )}
          {content()}
          <footer className="app-footer">
            <span>
              Sistema de Gestión de Laboratorio Clínico
            </span>
            <span>
              <Icon name="lock" size={12} /> Datos guardados en este equipo{" "}
              <span className="version">v1.0</span>
            </span>
          </footer>
        </main>
      </div>
      <DesplegablesDelLaboratorio />
      {modal && ModalContent()}
      {printReport && (
        <>
          <Modal
            title="Vista de impresión"
            subtitle="Así quedará el informe en papel"
            onClose={() => setPrintReport(null)}
            wide
          >
            <div className="print-preview">
              <HojaDeInforme r={printReport} />
            </div>
            <div className="modal-footer">
              <Button variant="secondary" onClick={() => setPrintReport(null)}>
                Cerrar
              </Button>
              <Button
                variant="secondary"
                icon="download"
                onClick={() => guardarInformePdf(printReport.sampleId)}
              >
                Guardar PDF…
              </Button>
              <Button icon="print" onClick={() => imprimir(printReport.sampleId)}>
                Imprimir
              </Button>
            </div>
          </Modal>
          {/* La copia que sale por la impresora: colgada de <body>, fuera del
              modal, para que la página no herede recortes ni posiciones. */}
          {createPortal(
            <div className="print-only">
              <HojaDeInforme r={printReport} />
            </div>,
            document.body,
          )}
        </>
      )}
      {confirm && (
        <Modal title={confirm.title} onClose={() => setConfirm(null)}>
          <Notice tone="warning">{confirm.message}</Notice>
          <div className="modal-footer">
            <Button variant="secondary" onClick={() => setConfirm(null)}>
              Cancelar
            </Button>
            <Button
              icon="check"
              onClick={() => {
                confirm.action()
                setConfirm(null)
              }}
            >
              Confirmar
            </Button>
          </div>
        </Modal>
      )}
      {toast && (
        <div role="status" className={`toast ${toast.tone}`}>
          <Icon
            name={toast.tone === "success" ? "checkcircle" : "alert"}
            size={22}
          />
          <span>{toast.message}</span>
          <button
            className="icon-button"
            aria-label="Cerrar notificación"
            onClick={() => setToast(null)}
          >
            <Icon name="close" size={16} />
          </button>
        </div>
      )}
    </div>
  )
}

function BirthField({ birth }: { birth?: string }) {
  const [value, setValue] = useState(birth || "")
  // Una fecha en el futuro da una edad negativa. El campo la rechaza, pero
  // mientras se escribe no hay por qué enseñar "−4 años".
  const edad = value ? age({ birth: value } as Puma) : -1
  return (
    <>
      <Field label="Fecha de nacimiento" required>
        <InputFecha
          name="birth"
          // Cota de cordura: el campo nativo dejaba escribir el año 0001.
          min="1900-01-01"
          max={TODAY}
          value={value}
          onChange={setValue}
          required
        />
      </Field>
      <Field label="Edad" hint="Calculada automáticamente">
        <input
          value={edad >= 0 ? `${edad} años` : ""}
          readOnly
          placeholder="—"
        />
      </Field>
    </>
  )
}
/**
 * La hoja del informe, tal como se imprime.
 *
 * Se dibuja dos veces: dentro del modal, para la vista previa, y en un
 * portal colgado de <body> que es el que sale por la impresora. Separarlas
 * evita el problema clásico de imprimir algo anidado en un modal: el
 * contenedor recorta, posiciona y arrastra páginas en blanco.
 */
function HojaDeInforme({ r }: { r: ResultReport }) {
  return (
    <article className="print-sheet">
      <header className="ps-head">
        <div>
          <strong className="ps-brand">{r.lab.toUpperCase()}</strong>
          <span className="ps-sub">{r.branch}</span>
        </div>
        <div className="ps-number">
          <span>INFORME N.º</span>
          <strong>{r.reportNumber}</strong>
        </div>
      </header>
      <div className="ps-rule" />

      <h2 className="ps-title">Informe de resultados de laboratorio</h2>

      <div className="ps-panels">
        <section className="ps-panel">
          <h3>Datos del paciente</h3>
          <dl>
            <dt>Nombre</dt>
            <dd>{r.patientName}</dd>
            <dt>Documento</dt>
            <dd>{r.patientDni}</dd>
            <dt>Fecha de nacimiento</dt>
            <dd>{r.patientBirth}</dd>
            <dt>Edad / Sexo</dt>
            <dd>
              {r.patientAge} años · {r.patientSex}
            </dd>
          </dl>
        </section>
        <section className="ps-panel">
          <h3>Datos de la muestra</h3>
          <dl>
            <dt>Código</dt>
            <dd>{r.sampleId}</dd>
            <dt>Tipo de muestra</dt>
            <dd>{r.sampleType}</dd>
            <dt>Fecha de toma</dt>
            <dd>{r.takenAt}</dd>
            <dt>Fecha de emisión</dt>
            <dd>{r.issuedAt}</dd>
          </dl>
        </section>
      </div>

      <div className="ps-band">
        {r.examCategory} · {r.examName}
      </div>

      <table className="ps-table">
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
          {r.rows.map((fila) => (
            <tr key={fila.name}>
              <td>{fila.name}</td>
              <td className={fila.outOfRange ? "ps-out" : "ps-value"}>
                {fila.value}
                {fila.flag && <em className="ps-flag">{fila.flag}</em>}
              </td>
              <td className="ps-soft">{fila.unit}</td>
              <td className="ps-soft">{fila.reference}</td>
              <td className="ps-soft">{fila.observation}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <p className={r.outOfRange.length ? "ps-legend warn" : "ps-legend"}>
        {r.outOfRange.length
          ? "ALTO / BAJO: valor fuera del intervalo de referencia configurado. Este indicador señala una desviación y no constituye un diagnóstico médico."
          : "Todos los parámetros se encuentran dentro del intervalo de referencia."}
      </p>

      <section className="ps-notes">
        <h3>Observaciones generales</h3>
        <p>{r.notes}</p>
      </section>

      <div className="ps-signatures">
        <div>
          <strong>{r.enteredBy}</strong>
          <span>Ingreso de resultados</span>
        </div>
        <div>
          <strong>{r.validatedBy}</strong>
          <span>Validación técnica</span>
        </div>
        <div>
          <strong>{r.publishedBy}</strong>
          <span>Publicación · {r.publishedAt}</span>
        </div>
      </div>

      <footer className="ps-legal">{r.disclaimer}</footer>
    </article>
  )
}

function Mini({ valores, dias }: { valores: number[]; dias: string[] }) {
  const max = Math.max(1, ...valores)
  return (
    <svg className="tablero-mini" viewBox="0 0 140 46" preserveAspectRatio="xMaxYMax meet" role="img" aria-label="Muestras por día en la última semana">
      {valores.map((n, i) => {
        const alto = Math.max(3, (n / max) * 42)
        return (
          <rect
            key={dias[i]}
            x={i * 20 + 3}
            y={46 - alto}
            width="12"
            height={alto}
            rx="2"
            className={i === valores.length - 1 ? "hoy" : ""}
          >
            <title>
              {diaCorto(dias[i])}: {n}
            </title>
          </rect>
        )
      })}
    </svg>
  )
}

function Dona({
  grupos,
  centro,
  pie,
}: {
  grupos: { nombre: string; color: string; n: number }[]
  centro: string
  pie: string
}) {
  const total = grupos.reduce((a, g) => a + g.n, 0)
  const radio = 52
  const vuelta = 2 * Math.PI * radio
  let recorrido = 0
  return (
    <svg className="tablero-dona" viewBox="0 0 140 140" role="img" aria-label={`${centro} ${pie}`}>
      <circle cx="70" cy="70" r={radio} fill="none" stroke="var(--surface-alt)" strokeWidth="14" />
      {total > 0 &&
        grupos.map((g) => {
          const largo = (g.n / total) * vuelta
          const trazo = (
            <circle
              key={g.nombre}
              cx="70"
              cy="70"
              r={radio}
              fill="none"
              stroke={g.color}
              strokeWidth="14"
              strokeDasharray={`${largo} ${vuelta - largo}`}
              strokeDashoffset={-recorrido}
              transform="rotate(-90 70 70)"
            >
              <title>
                {g.nombre}: {g.n}
              </title>
            </circle>
          )
          recorrido += largo
          return trazo
        })}
      <text x="70" y="72" textAnchor="middle" className="tablero-dona-valor">
        {centro}
      </text>
      <text x="70" y="92" textAnchor="middle" className="tablero-dona-pie">
        {pie}
      </text>
    </svg>
  )
}

function Curva({ valores, dias }: { valores: number[]; dias: string[] }) {
  const max = Math.max(4, ...valores)
  const tope = Math.ceil(max / 4) * 4
  const x = (i: number) => 40 + i * 82
  const y = (n: number) => 130 - (n / tope) * 110
  const puntos = valores.map((n, i) => [x(i), y(n)])
  const linea = puntos
    .map(([px, py], i) => {
      if (!i) return `M${px},${py}`
      const [ax, ay] = puntos[i - 1]
      const mx = (ax + px) / 2
      return `C${mx},${ay} ${mx},${py} ${px},${py}`
    })
    .join(" ")
  const area = `${linea} L${x(valores.length - 1)},130 L${x(0)},130 Z`
  const ultimo = valores.length - 1
  return (
    <svg className="tablero-curva-svg" viewBox="0 0 560 160" role="img" aria-label="Pacientes atendidos por día en la última semana">
      <defs>
        <linearGradient id="tablero-relleno" x1="0" x2="0" y1="0" y2="1">
          <stop offset="0%" stopColor="var(--blue)" stopOpacity="0.35" />
          <stop offset="100%" stopColor="var(--blue)" stopOpacity="0.02" />
        </linearGradient>
      </defs>
      {[0, 1, 2, 3, 4].map((i) => (
        <g key={i}>
          <line x1="28" x2="550" y1={130 - i * 27.5} y2={130 - i * 27.5} stroke="var(--border-subtle)" strokeDasharray="3 5" />
          <text x="20" y={134 - i * 27.5} textAnchor="end">
            {(tope / 4) * i}
          </text>
        </g>
      ))}
      <path d={area} fill="url(#tablero-relleno)" />
      <path d={linea} fill="none" stroke="var(--blue)" strokeWidth="2.4" />
      {puntos.map(([px, py], i) => (
        <circle key={dias[i]} cx={px} cy={py} r={i === ultimo ? 5 : 3} className={i === ultimo ? "hoy" : ""}>
          <title>
            {diaCorto(dias[i])}: {valores[i]} pacientes
          </title>
        </circle>
      ))}
      <line x1={x(ultimo)} x2={x(ultimo)} y1={y(valores[ultimo]) + 6} y2="130" stroke="var(--blue)" strokeDasharray="2 3" />
      <g transform={`translate(${Math.min(x(ultimo), 535)},${Math.max(y(valores[ultimo]) - 22, 14)})`}>
        <rect x="-15" y="-11" width="30" height="18" rx="4" className="tablero-burbuja" />
        <text y="2" textAnchor="middle" className="tablero-burbuja-texto">
          {valores[ultimo]}
        </text>
      </g>
      {dias.map((d, i) => (
        <text key={d} x={x(i)} y="152" textAnchor="middle" className={i === ultimo ? "today-label" : ""}>
          {diaCorto(d)}
        </text>
      ))}
    </svg>
  )
}

function VolumeChart({ samples, mode }: {
  samples: Vicuna[]
  mode: string
}) {
  // Los siete días que terminan hoy. El original los tenía escritos a mano
  // ("Vie 18"... "Jue 24") porque vivía con la fecha congelada.
  const dias = ultimosSieteDias()

  // La franja del día: 08:00–14:00 mientras la jornada va por la mañana, y se
  // desplaza para terminar en la hora actual cuando ya pasó de las 14:00. Así
  // el tramo de "ahora" siempre queda a la vista. El original la tenía fija
  // en 08:00–14:00 porque vivía congelado a las ocho de la mañana.
  const horaActual = new Date().getHours()
  const primeraHora =
    horaActual < 8 ? Math.max(0, horaActual - 6)
    : horaActual <= 14 ? 8
    : horaActual - 6

  const buckets =
    mode === "Hoy"
      ? Array.from({ length: 7 }, (_, i) => `${dosDigitos(primeraHora + i)}:00`)
      : dias.map(diaCorto)
  const counts = buckets.map((_, i) => {
    const hora = primeraHora + i
    const ss = samples.filter((s) => {
      if (mode !== "Hoy") return s.date.slice(0, 10) === dias[i]
      // Filtrar por día es imprescindible y faltaba: sin esto, una muestra
      // de la semana pasada tomada a las 08:15 se contaba en el tramo de
      // las 08:00 de HOY. Con 30 muestras históricas a esa hora, la barra
      // marcaba 33 cuando el titular decía 13.
      if (s.date.slice(0, 10) !== TODAY) return false
      const h = Number(s.date.slice(11, 13))
      // El último tramo recoge todo lo posterior, para que una muestra
      // tomada más tarde no desaparezca del gráfico.
      return i === 6 ? h >= hora : h === hora
    })
    return [
      ss.filter((s) =>
        ["EN VALIDACIÓN", "VALIDADO", "PUBLICADA"].includes(s.state),
      ).length,
      ss.filter((s) => s.state === "EN ANÁLISIS").length,
      ss.filter((s) => s.state === "RECIBIDA").length,
    ]
  })
  const max = Math.max(4, ...counts.map((c) => c.reduce((a, b) => a + b, 0)))
  const step = Math.ceil(max / 4)
  const top = step * 4
  return (
    <div className="chart-area">
      <svg
        viewBox="0 0 710 190"
        role="img"
        aria-label="Distribución de muestras procesadas, en proceso y pendientes por período"
      >
        {[0, 1, 2, 3, 4].map((i) => (
          <g key={i}>
            <text x="27" y={150 - i * 33 + 4} textAnchor="end">
              {i * step}
            </text>
            <line
              x1="43"
              x2="692"
              y1={150 - i * 33}
              y2={150 - i * 33}
              stroke="var(--border)"
              strokeDasharray="3 5"
            />
          </g>
        ))}
        {counts.map((values, i) => {
          const x = 69 + i * 91
          let bottom = 150
          return (
            <g key={i}>
              {values.map((n, j) => {
                const height = (n / top) * 132
                bottom -= height
                return (
                  <rect
                    key={j}
                    x={x}
                    y={bottom}
                    width="38"
                    height={Math.max(0, height - 1)}
                    rx="2"
                    fill={["var(--primary)", "var(--blue)", "var(--amber)"][j]}
                  >
                    <title>
                      {buckets[i]}: {n}{" "}
                      {["procesadas", "en proceso", "pendientes"][j]}
                    </title>
                  </rect>
                )
              })}
              {values.every((n) => n === 0) && (
                <rect
                  x={x}
                  y="147"
                  width="38"
                  height="3"
                  rx="1"
                  fill="var(--border)"
                />
              )}
              <text
                x={x + 19}
                y="177"
                textAnchor="middle"
                className={
                  // En la vista semanal se resalta hoy; en la del día, la
                  // hora en curso, que no siempre es el último tramo.
                  i === (mode === "Hoy" ? horaActual - primeraHora : 6)
                    ? "today-label"
                    : ""
                }
              >
                {buckets[i]}
              </text>
            </g>
          )
        })}
      </svg>
    </div>
  )
}
