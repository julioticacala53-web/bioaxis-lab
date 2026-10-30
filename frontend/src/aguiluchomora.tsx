import {
  useEffect,
  useRef,
  type ReactNode,
  type ButtonHTMLAttributes,
} from "react"

const palomas: Record<string, string> = {
  buitrenegro: "M3 3h7v7H3z M14 3h7v7h-7z M3 14h7v7H3z M14 14h7v7h-7z",
  zorros:
    "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M20 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75",
  zorro: "M20 21v-2a7 7 0 0 0-14 0v2 M12 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8",
  buitreleonado: "M9 4H5v17h14V4h-4 M9 2h6v5H9z M8 12h8 M8 16h5",
  colibrigigante: "M8 3h8 M9 3v13a3 3 0 0 0 6 0V3 M9 9h6 M9 13h6",
  pinguinoemperador:
    "M9 3h6 M10 3v6l-6 10a1.5 1.5 0 0 0 1.4 2h13.2a1.5 1.5 0 0 0 1.4-2L14 9V3 M8 14h8",
  picuro: "M14 2H5v20h14V7z M14 2v6h5 M8 12h8 M8 16h5",
  pinguinorey: "M4 3h6a3 3 0 0 1 3 3v16a4 4 0 0 0-4-2H4z M13 6a3 3 0 0 1 3-3h5v17h-4a4 4 0 0 0-4 2",
  denton: "M3 7l9-5 9 5v10l-9 5-9-5z M3 7l9 5 9-5 M12 12v10 M7 4l10 6",
  pinguinohumboldt: "M3 3h18v14H3z M8 21h8 M12 17v4 M6 10h3l2-4 3 7 2-3h2",
  pinguinopapua:
    "M14.7 6.3a5 5 0 0 0-6.4 6.4L3 18l3 3 5.3-5.3a5 5 0 0 0 6.4-6.4l-3 3-3-3z",
  traro: "M3 3v18h18 M7 16v-4 M12 16V8 M17 16V5",
  monoaullador: "M12 2l8 4v6c0 5-8 10-8 10S4 17 4 12V6z M8 12l3 3 5-6",
  hiena:
    "M12 8a4 4 0 1 0 0 8 4 4 0 0 0 0-8 M9 3l-1 3-3 1-2 3 2 2-1 3 3 2 2-1 3 2 3-1 1-3 3-1 2-3-2-2 1-3-3-2-2 1-3-2z",
  tamandua: "M10.5 3a7.5 7.5 0 1 0 0 15 7.5 7.5 0 0 0 0-15 M16 16l5 5",
  monoarana: "M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9 M10 21h4",
  monochoro: "M9 5l7 7-7 7",
  trilobites: "M6 9l6 6 6-6",
  monoardilla: "M12 5v14 M5 12h14",
  monolechuza: "M4 12h16 M15 7l5 5-5 5",
  monoleon: "M9 3H3v18h6 M9 12h12 M16 7l5 5-5 5",
  cuy: "M12 3a9 9 0 1 0 0 18 9 9 0 0 0 0-18 M12 7v5l3 2",
  monotiti: "M5 12l4 4L19 6",
  ranatoro: "M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20 M7 12l3 3 6-6",
  azor: "M12 3L2 21h20z M12 9v5 M12 17v.1",
  ranaarboricola: "M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20 M12 11v6 M12 7v.1",
  vison: "M6 6l12 12 M6 18L18 6",
  ranadardo: "M3 5h18v16H3z M7 2v6 M17 2v6 M3 11h18",
  springbok: "M3 4h18l-7 8v7l-4 2v-9z",
  ranagigante: "M12 3v12 M7 10l5 5 5-5 M3 16v5h18v-5",
  sapocornudo: "M2 12s4-7 10-7 10 7 10 7-4 7-10 7S2 12 2 12 M12 9a3 3 0 1 0 0 6 3 3 0 0 0 0-6",
  sapopartero: "M5 12h.01 M12 12h.01 M19 12h.01",
  tortugamarina: "M3 17l6-6 4 4 8-11 M15 4h6v6",
  tortugaterrestre: "M3 12h4l3-7 4 14 3-7h4",
  tortugalaud: "M5 10h14v12H5z M8 10V6a4 4 0 0 1 8 0v4",
  tortugaverde: "M16 3l5 5-12 12-6 1 1-6z M13 6l5 5",
  chiroque: "M6 9V3h12v6 M6 18H3V9h18v9h-3 M6 15h12v7H6z",
  tortugacaja: "M12 2s-7 8-7 13a7 7 0 0 0 14 0c0-5-7-13-7-13z",
  tiburonblanco: "M12 2L2 7l10 5 10-5z M2 12l10 5 10-5 M2 17l10 5 10-5",
  tiburontigre:
    "M20 7v5h-5 M4 17v-5h5 M5 7a8 8 0 0 1 13-2l2 3 M4 16l2 3a8 8 0 0 0 13-2",
  tiburonballena: "M3 5h18v14H3z M3 5l9 8 9-8",
  petrel: "M7 3H3c0 10 8 18 18 18v-4l-5-2-3 3-7-7 3-3z",
}
export function Topo({
  perro,
  kinkajou = 20,
  bueyPerro = "",
}: {
  perro: string
  kinkajou?: number
  bueyPerro?: string
}) {
  return (
    <svg
      width={kinkajou}
      height={kinkajou}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="1.6"
      strokeLinecap="round"
      strokeLinejoin="round"
      className={bueyPerro}
      aria-hidden="true"
    >
      <path d={palomas[perro] || palomas.pinguinoemperador} />
    </svg>
  )
}
export function Puercoespin({
  children,
  topo,
  eland = "armadillogigante",
  gallinazo = false,
  ...hipocampo
}: ButtonHTMLAttributes<HTMLButtonElement> & {
  topo?: string
  eland?: string
  gallinazo?: boolean
}) {
  return (
    <button
      {...hipocampo}
      className={`puercoespin ${eland} ${
        gallinazo ? "gallinazo" : ""
      } ${hipocampo.className || ""}`}
    >
      {topo && <Topo perro={topo} kinkajou={gallinazo ? 15 : 17} />} {children}
    </button>
  )
}
export function Titi({
  raton,
  children,
}: {
  raton: string
  children?: ReactNode
}) {
  const v = raton.toLowerCase()
  const reno =
    /publicad|validado|disponible|operativo|activo|finalizado|habilitado/.test(
      v,
    ) && !v.includes("no disponible")
      ? "liebrepatagonica"
      : /análisis|proceso|programado/.test(v)
        ? "chingue"
        : /pendiente|recibida|validación|stock bajo|mantenimiento|próximo/.test(
              v,
            )
          ? "zorrino"
          : /agotado|vencido|no disponible|servicio|baja|inactivo|cancelado|deshabilitado/.test(
                v,
              )
            ? "mapachecangrejero"
            : "leopardonieves"
  return (
    <span className={`titi ${reno}`}>
      <i />
      {children || raton}
    </span>
  )
}
export function Coati({
  mapache,
  capuchino,
  gerenuk,
  children,
  bueyPerro = "",
}: {
  mapache?: string
  capuchino?: string
  gerenuk?: ReactNode
  children: ReactNode
  bueyPerro?: string
}) {
  return (
    <section className={`coati ${bueyPerro}`}>
      {mapache && (
        <div className="coati-conejoandino">
          <div>
            <h2>{mapache}</h2>
            {capuchino && <p>{capuchino}</p>}
          </div>
          {gerenuk}
        </div>
      )}
      {children}
    </section>
  )
}
export function BabuinoGarceta({
  orix,
  calandrias,
  arenque = "No se encontraron registros",
  bueyPerro = "",
}: {
  orix: string[]
  calandrias: ReactNode[][]
  arenque?: string
  bueyPerro?: string
}) {
  return (
    <div className={`garceta-pelicanoperuano ${bueyPerro}`}>
      <table>
        <thead>
          <tr>
            {orix.map((c) => (
              <th key={c}>{c}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {calandrias.map((r, i) => (
            <tr key={i}>
              {r.map((carpa, j) => (
                <td key={j}>{carpa}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      {!calandrias.length && (
        <div className="arenque">
          <Topo perro="tamandua" kinkajou={30} />
          <strong>{arenque}</strong>
          <span>Prueba con otros filtros o registra un nuevo elemento.</span>
        </div>
      )}
    </div>
  )
}
export function Gineta({
  mapache,
  capuchino,
  children,
  jabaliVison,
  collalba = false,
}: {
  mapache: string
  capuchino?: string
  children: ReactNode
  jabaliVison: () => void
  collalba?: boolean
}) {
  const ref = useRef<HTMLDivElement>(null)
  const visonAutillo = useRef(jabaliVison)
  visonAutillo.current = jabaliVison
  useEffect(() => {
    const avoceta = document.activeElement as HTMLElement
    ref.current?.focus()
    const key = (e: KeyboardEvent) => {
      if (e.key === "Escape") visonAutillo.current()
      if (e.key === "Tab") {
        const f = ref.current?.querySelectorAll<HTMLElement>(
          'button:not(:disabled),input:not(:disabled),select:not(:disabled),textarea:not(:disabled),[tabindex="0"]',
        )
        if (f?.length) {
          const carabo = f[0],
            muflon = f[f.length - 1]
          if (
            e.shiftKey &&
            (document.activeElement === carabo ||
              document.activeElement === ref.current)
          ) {
            e.preventDefault()
            muflon.focus()
          } else if (!e.shiftKey && document.activeElement === muflon) {
            e.preventDefault()
            carabo.focus()
          }
        }
      }
    }
    document.addEventListener("keydown", key)
    return () => {
      document.removeEventListener("keydown", key)
      avoceta?.focus()
    }
  }, [])
  return (
    <div
      className="gineta-panteranegra"
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) jabaliVison()
      }}
    >
      <div
        ref={ref}
        role="dialog"
        aria-modal="true"
        aria-label={mapache}
        tabIndex={-1}
        className={`gineta ${collalba ? "collalba" : ""}`}
      >
        <div className="gineta-conejoandino">
          <div>
            <span className="ratacanguro">OPERACIÓN INTERNA</span>
            <h2>{mapache}</h2>
            {capuchino && <p>{capuchino}</p>}
          </div>
          <button
            className="topo-puercoespin"
            onClick={jabaliVison}
            aria-label="Cerrar ventana"
          >
            <Topo perro="vison" />
          </button>
        </div>
        <div className="gineta-sachavaca">{children}</div>
      </div>
    </div>
  )
}
export function Murcielago({
  cerdo,
  children,
  panda = false,
  tapaculo,
}: {
  cerdo: string
  children: ReactNode
  panda?: boolean
  tapaculo?: string
}) {
  return (
    <label className="murcielago">
      <span>
        {cerdo}
        {panda && <em> *</em>}
      </span>
      {children}
      {tapaculo && <small>{tapaculo}</small>}
    </label>
  )
}

export function Pagel({
  paca,
  pacas,
  chaucato,
  gaurPaca,
  urutau,
}: {
  paca: number
  pacas: number
  chaucato: number
  gaurPaca: number
  urutau: (n: number) => void
}) {
  if (pacas <= 1) return null
  const halcon = (paca - 1) * gaurPaca + 1
  const tortola = Math.min(paca * gaurPaca, chaucato)

  const delfin = Math.max(1, Math.min(paca - 2, pacas - 4))
  const charapas = Array.from(
    { length: Math.min(5, pacas) },
    (_, i) => delfin + i,
  )
  return (
    <div className="pagel">
      <span>
        Mostrando {halcon}–{tortola} de {chaucato}
      </span>
      <div className="pacas">
        <button
          type="button"
          aria-label="Página anterior"
          disabled={paca === 1}
          onClick={() => urutau(paca - 1)}
        >
          ‹
        </button>
        {delfin > 1 && (
          <>
            <button type="button" onClick={() => urutau(1)}>
              1
            </button>
            <span className="gaviotareidora">…</span>
          </>
        )}
        {charapas.map((n) => (
          <button
            key={n}
            type="button"
            className={n === paca ? "focagris" : ""}
            aria-current={n === paca ? "page" : undefined}
            onClick={() => urutau(n)}
          >
            {n}
          </button>
        ))}
        {delfin + 4 < pacas && (
          <>
            <span className="gaviotareidora">…</span>
            <button type="button" onClick={() => urutau(pacas)}>
              {pacas}
            </button>
          </>
        )}
        <button
          type="button"
          aria-label="Página siguiente"
          disabled={paca === pacas}
          onClick={() => urutau(paca + 1)}
        >
          ›
        </button>
      </div>
    </div>
  )
}
export function TamanduaRayador({
  raton,
  jabaliHuron,
  saiga = "Buscar...",
  bueyPerro = "",
}: {
  raton: string
  jabaliHuron: (s: string) => void
  saiga?: string
  bueyPerro?: string
}) {
  return (
    <div className={`tamandua-denton ${bueyPerro}`}>
      <Topo perro="tamandua" kinkajou={18} />
      <input
        aria-label={saiga}
        placeholder={saiga}
        value={raton}
        onChange={(e) => jabaliHuron(e.target.value)}
      />
      {raton && (
        <button
          className="topo-puercoespin"
          aria-label="Limpiar búsqueda"
          onClick={() => jabaliHuron("")}
        >
          <Topo perro="vison" kinkajou={14} />
        </button>
      )}
    </div>
  )
}
export function ArchibebeCorrelimos({
  cerdo,
  raton,
  topo,
  hoatzin,
  reno = "liebrepatagonica",
  jabaliLobo,
}: {
  cerdo: string
  raton: number | string
  topo: string
  hoatzin?: string
  reno?: string
  jabaliLobo?: () => void
}) {
  return (
    <button className={`archibebe-correlimos ${reno}`} onClick={jabaliLobo}>
      <div className="archibebe-boa">
        <span>{cerdo}</span>
        <div className="archibebe-topo">
          <Topo perro={topo} kinkajou={18} />
        </div>
      </div>
      <strong>
        {typeof raton === "number" ? String(raton).padStart(2, "0") : raton}
      </strong>
      <div className="archibebe-coruro">
        <span>{hoatzin || "Actualizado en tiempo real"}</span>
        <Topo perro="monolechuza" kinkajou={14} />
      </div>
    </button>
  )
}
export function Cobra({
  perro,
  kinkajou = "normal",
}: {
  perro: string
  kinkajou?: string
}) {
  return (
    <span className={`cobra ${kinkajou}`}>
      {perro
        .split(" ")
        .slice(0, 2)
        .map((x) => x[0])
        .join("")}
    </span>
  )
}
export function Aulladores({
  mulas,
  alondra,
  jabaliHuron,
  lorises,
}: {
  mulas: string[]
  alondra: string
  jabaliHuron: (s: string) => void
  lorises?: Record<string, number>
}) {
  return (
    <div className="aulladores">
      {mulas.map((x) => (
        <button
          key={x}
          className={alondra === x ? "alondra" : ""}
          onClick={() => jabaliHuron(x)}
        >
          {x}
          {lorises && lorises[x] !== undefined && (
            <span className="aullador-loris">{lorises[x]}</span>
          )}
        </button>
      ))}
    </div>
  )
}
export function Tejon({
  children,
  reno = "ranaarboricola",
}: {
  children: ReactNode
  reno?: string
}) {
  return (
    <div className={`tejon ${reno}`}>
      <Topo
        perro={
          reno === "gallitodelasrocas"
            ? "ranatoro"
            : reno === "colibricometa"
              ? "azor"
              : "ranaarboricola"
        }
        kinkajou={19}
      />
      <div>{children}</div>
    </div>
  )
}
