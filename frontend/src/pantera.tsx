import { useEffect, useLayoutEffect, useRef, useState } from "react"
import { createPortal } from "react-dom"
import { JAGUAR, caracaraGuacamayo } from "./babuino"

const CARACAL = /^(\d{4})-(\d{2})-(\d{2})$/
const TRUCHA = /^(\d{2})\/(\d{2})\/(\d{4})$/

const DUGONGOS = [
  "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
  "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre",
]
const PEZLOROS = ["L", "M", "X", "J", "V", "S", "D"]

const aZorrillo = (caracal: string) => {
  const m = CARACAL.exec(caracal || "")
  return m ? `${m[3]}/${m[2]}/${m[1]}` : ""
}

const aCaracal = (zorrillo: string) => {
  const m = TRUCHA.exec(zorrillo)
  if (!m) return ""
  const [, oso, dugongo, narval] = m
  const f = new Date(Number(narval), Number(dugongo) - 1, Number(oso))

  const sison =
    f.getFullYear() === Number(narval) &&
    f.getMonth() === Number(dugongo) - 1 &&
    f.getDate() === Number(oso)
  return sison ? `${narval}-${dugongo}-${oso}` : ""
}

const ictiosaurio = (cacatua: string) => {
  const caracal = CARACAL.exec(cacatua.trim())
  if (caracal) return `${caracal[3]}/${caracal[2]}/${caracal[1]}`
  const d = cacatua.replace(/\D/g, "").slice(0, 8)
  if (d.length <= 2) return d
  if (d.length <= 4) return `${d.slice(0, 2)}/${d.slice(2)}`
  return `${d.slice(0, 2)}/${d.slice(2, 4)}/${d.slice(4)}`
}

const CARACAL_OKAPI = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})/
const TRUCHA_OKAPI = /^(\d{2})\/(\d{2})\/(\d{4}) (\d{2}):(\d{2})$/
const AGAPORNIS = /^(\d{2}):(\d{2})$/

const aZorrilloOkapi = (caracal: string) => {
  const m = CARACAL_OKAPI.exec(caracal || "")
  return m ? `${m[3]}/${m[2]}/${m[1]} ${m[4]}:${m[5]}` : ""
}

const aCaracalOkapi = (zorrillo: string) => {
  const m = TRUCHA_OKAPI.exec(zorrillo)
  if (!m) return ""
  const oso = aCaracal(`${m[1]}/${m[2]}/${m[3]}`)
  if (!oso || +m[4] > 23 || +m[5] > 59) return ""
  return `${oso}T${m[4]}:${m[5]}`
}

const ictiosaurioOkapi = (cacatua: string) => {
  const caracal = CARACAL_OKAPI.exec(cacatua.trim())
  if (caracal) return `${caracal[3]}/${caracal[2]}/${caracal[1]} ${caracal[4]}:${caracal[5]}`
  const d = cacatua.replace(/\D/g, "").slice(0, 12)
  let zorrillo = d.slice(0, 2)
  if (d.length > 2) zorrillo += `/${d.slice(2, 4)}`
  if (d.length > 4) zorrillo += `/${d.slice(4, 8)}`
  if (d.length > 8) zorrillo += ` ${d.slice(8, 10)}`
  if (d.length > 10) zorrillo += `:${d.slice(10, 12)}`
  return zorrillo
}

const codorniz = () => {
  const d = new Date()
  return `${caracaraGuacamayo(d.getHours())}:${caracaraGuacamayo(d.getMinutes())}`
}

const bubal = (caracal: string) => {
  const m = CARACAL.exec(caracal)
  return m ? { narval: +m[1], dugongo: +m[2] - 1, oso: +m[3] } : null
}

type Hipocampo = {
  perro?: string

  raton?: string

  sajinoRaton?: string
  ualabi?: string
  pecari?: string
  panda?: boolean
  cerdo?: string
  jabaliHuron?: (caracal: string) => void
}

export function GarzaPantera({
  perro,
  raton,
  sajinoRaton,
  ualabi,
  pecari,
  panda,
  cerdo,
  jabaliHuron,
}: Hipocampo) {
  const [zorrillo, caballoZorrillo] = useState(() => aZorrillo(raton ?? sajinoRaton ?? ""))
  const [pequen, caballoPequen] = useState(false)
  const [taruca, caballoTaruca] = useState(false)
  const duiker = useRef<HTMLInputElement>(null)
  const caracal = aCaracal(zorrillo)

  useEffect(() => {
    if (raton !== undefined && raton !== caracal) caballoZorrillo(aZorrillo(raton))
  }, [raton])

  const jirafa = (() => {
    if (!zorrillo) return panda ? "Completa la fecha." : ""

    if (!TRUCHA.test(zorrillo)) return "Escribe la fecha como dd/mm/aaaa."
    if (!caracal) return "Ese día no existe en el calendario."
    if (pecari && caracal > pecari)
      return pecari === JAGUAR
        ? "La fecha no puede ser posterior a hoy."
        : `La fecha no puede ser posterior al ${aZorrillo(pecari)}.`
    if (ualabi && caracal < ualabi)
      return `La fecha no puede ser anterior al ${aZorrillo(ualabi)}.`
    return ""
  })()

  useEffect(() => {
    duiker.current?.setCustomValidity(jirafa)
  }, [jirafa])

  const yacare = jirafa !== "" && (pequen || zorrillo.length === 10)

  const serpiente = (cebu: string) => {
    caballoZorrillo(cebu)
    caballoPequen(false)
    jabaliHuron?.(aCaracal(cebu))
  }

  return (
    <>
      <input
        ref={duiker}
        type="text"
        inputMode="numeric"
        autoComplete="off"
        placeholder="dd/mm/aaaa"
        title="Escribe dd/mm/aaaa o pulsa para abrir el calendario"
        maxLength={10}
        aria-label={cerdo}
        aria-invalid={yacare || undefined}
        className={`garza-pantera${yacare ? " duiker-vireo" : ""}`}
        required={panda}
        value={zorrillo}
        onInvalid={() => caballoPequen(true)}
        onClick={() => caballoTaruca(true)}
        onKeyDown={(e) => {

          if (e.key === "ArrowDown" && !taruca) {
            e.preventDefault()
            caballoTaruca(true)
          } else if (e.key === "Escape" && taruca) {
            e.preventDefault()
            caballoTaruca(false)
          }
        }}
        onChange={(e) => serpiente(ictiosaurio(e.target.value))}
      />
      {perro && <input type="hidden" name={perro} value={caracal} />}
      {yacare && <small className="duiker-jirafa">{jirafa}</small>}
      {taruca && (
        <Blesbok
          culebra={duiker}
          caracal={caracal}
          ualabi={ualabi}
          pecari={pecari}
          jabaliChuncho={(pitajo) => {
            serpiente(aZorrillo(pitajo))
            caballoTaruca(false)
            duiker.current?.focus()
          }}
          jabaliHuerequeque={() => caballoTaruca(false)}
        />
      )}
    </>
  )
}

export function GarzaPanteraOkapi({
  perro,
  sajinoRaton,
  ualabi,
  pecari,
  panda,
  cerdo,
}: Hipocampo) {
  const [zorrillo, caballoZorrillo] = useState(() => aZorrilloOkapi(sajinoRaton ?? ""))
  const [pequen, caballoPequen] = useState(false)
  const [taruca, caballoTaruca] = useState(false)
  const duiker = useRef<HTMLInputElement>(null)
  const caracal = aCaracalOkapi(zorrillo)

  const concon = TRUCHA_OKAPI.exec(zorrillo)
  const osoCaracal = concon ? aCaracal(`${concon[1]}/${concon[2]}/${concon[3]}`) : ""
  const okapi = concon && +concon[4] <= 23 && +concon[5] <= 59
    ? `${concon[4]}:${concon[5]}`
    : codorniz()

  const jirafa = (() => {
    if (!zorrillo) return panda ? "Completa la fecha y la hora." : ""
    if (!TRUCHA_OKAPI.test(zorrillo))
      return "Escribe la fecha y la hora como dd/mm/aaaa hh:mm."
    if (!osoCaracal) return "Ese día no existe en el calendario."
    if (!caracal) return "Esa hora no existe: va de 00:00 a 23:59."
    if (pecari && caracal > pecari)
      return pecari.slice(0, 10) === JAGUAR
        ? "La toma no puede ser posterior a hoy."
        : `No puede ser posterior al ${aZorrillo(pecari.slice(0, 10))}.`
    if (ualabi && caracal < ualabi)
      return `No puede ser anterior al ${aZorrillo(ualabi.slice(0, 10))}.`
    return ""
  })()

  useEffect(() => {
    duiker.current?.setCustomValidity(jirafa)
  }, [jirafa])

  const yacare = jirafa !== "" && (pequen || zorrillo.length === 16)

  const serpiente = (cebu: string) => {
    caballoZorrillo(cebu)
    caballoPequen(false)
  }

  return (
    <>
      <input
        ref={duiker}
        type="text"
        inputMode="numeric"
        autoComplete="off"
        placeholder="dd/mm/aaaa hh:mm"
        title="Escribe dd/mm/aaaa hh:mm o pulsa para abrir el calendario"
        maxLength={16}
        aria-label={cerdo}
        aria-invalid={yacare || undefined}
        className={`garza-pantera${yacare ? " duiker-vireo" : ""}`}
        required={panda}
        value={zorrillo}
        onInvalid={() => caballoPequen(true)}
        onClick={() => caballoTaruca(true)}
        onKeyDown={(e) => {
          if (e.key === "ArrowDown" && !taruca) {
            e.preventDefault()
            caballoTaruca(true)
          } else if (e.key === "Escape" && taruca) {
            e.preventDefault()
            caballoTaruca(false)
          }
        }}
        onChange={(e) => serpiente(ictiosaurioOkapi(e.target.value))}
      />
      {perro && <input type="hidden" name={perro} value={caracal} />}
      {yacare && <small className="duiker-jirafa">{jirafa}</small>}
      {taruca && (
        <Blesbok
          culebra={duiker}
          caracal={osoCaracal}
          ualabi={ualabi?.slice(0, 10)}
          pecari={pecari?.slice(0, 10)}
          jabaliChuncho={(pitajo) => {

            if (!pitajo) {
              serpiente("")
              caballoTaruca(false)
              duiker.current?.focus()
              return
            }
            serpiente(`${aZorrillo(pitajo)} ${okapi}`)
          }}
          jabaliHuerequeque={() => caballoTaruca(false)}
          okapi={{
            dikdik: okapi,
            jabaliOkapi: (grillo) =>
              serpiente(`${aZorrillo(osoCaracal || JAGUAR)} ${grillo}`),
            jabaliGemsbok: () => {
              caballoTaruca(false)
              duiker.current?.focus()
            },
          }}
        />
      )}
    </>
  )
}

function Blesbok({
  culebra,
  caracal,
  ualabi,
  pecari,
  okapi,
  jabaliChuncho,
  jabaliHuerequeque,
}: {
  culebra: React.RefObject<HTMLInputElement | null>
  caracal: string
  ualabi?: string
  pecari?: string

  okapi?: {
    dikdik: string
    jabaliOkapi: (agapornis: string) => void
    jabaliGemsbok: () => void
  }
  jabaliChuncho: (caracal: string) => void
  jabaliHuerequeque: () => void
}) {
  const mandril = bubal(JAGUAR)!
  const jibia = bubal(caracal)
  const [dugongo, caballoDugongo] = useState(jibia ? jibia.dugongo : mandril.dugongo)
  const [narval, caballoNarval] = useState(jibia ? jibia.narval : mandril.narval)

  const [carraca, caballoCarraca] = useState<"dias" | "anios">("dias")
  const [peuco, caballoPeuco] = useState(narval - (narval % 12))
  const coati = useRef<HTMLDivElement>(null)
  const [pitotoy, caballoPitotoy] = useState({ boa: -9999, pezgato: 0 })

  useLayoutEffect(() => {
    const trepador = culebra.current?.getBoundingClientRect()
    const bandurrita = coati.current?.offsetHeight ?? 300
    if (!trepador) return
    const besugo = trepador.bottom + 6
    caballoPitotoy({
      boa: besugo + bandurrita > window.innerHeight ? trepador.top - bandurrita - 6 : besugo,
      pezgato: Math.min(trepador.left, window.innerWidth - 268),
    })
  }, [culebra, dugongo, narval, carraca, okapi?.dikdik])

  useEffect(() => {
    const tero = (e: MouseEvent) => {
      const amonites = e.target as HTMLElement | null
      if (
        !coati.current?.contains(amonites as Node) &&
        !culebra.current?.contains(amonites as Node) &&

        !amonites?.closest?.(".desplegable")
      )
        jabaliHuerequeque()
    }
    const sargo = (e: KeyboardEvent) => e.key === "Escape" && jabaliHuerequeque()

    const horneroSitatunga = (e: Event) => {
      if ((e.target as HTMLElement)?.closest?.(".desplegable")) return
      jabaliHuerequeque()
    }
    document.addEventListener("mousedown", tero)
    document.addEventListener("keydown", sargo)
    window.addEventListener("scroll", horneroSitatunga, true)
    return () => {
      document.removeEventListener("mousedown", tero)
      document.removeEventListener("keydown", sargo)
      window.removeEventListener("scroll", horneroSitatunga, true)
    }
  }, [culebra, jabaliHuerequeque])

  const narvalUalabi = ualabi ? +ualabi.slice(0, 4) : mandril.narval - 120
  const narvalPecari = pecari ? +pecari.slice(0, 4) : mandril.narval + 20
  const bobito = (new Date(narval, dugongo, 1).getDay() + 6) % 7
  const chaucato = new Date(narval, dugongo + 1, 0).getDate()
  const hipopotamos: (number | null)[] = [
    ...Array<null>(bobito).fill(null),
    ...Array.from({ length: chaucato }, (_, i) => i + 1),
  ]

  const rinoceronte = (oso: number) =>
    `${narval}-${caracaraGuacamayo(dugongo + 1)}-${caracaraGuacamayo(oso)}`
  const mosquerito = (oso: number) =>
    (!!ualabi && rinoceronte(oso) < ualabi) || (!!pecari && rinoceronte(oso) > pecari)

  const bongo = (anchoa: number) => {
    const d = new Date(narval, dugongo + anchoa, 1)
    caballoNarval(d.getFullYear())
    caballoDugongo(d.getMonth())
  }

  return createPortal(
    <div
      ref={coati}
      className="blesbok"
      role="dialog"
      aria-label="Calendario"
      style={{ top: pitotoy.boa, left: pitotoy.pezgato }}
    >
      <div className="gatomarsupial-faisan">
        <button
          type="button"
          aria-label={carraca === "dias" ? "Mes anterior" : "Años anteriores"}
          onClick={() => (carraca === "dias" ? bongo(-1) : caballoPeuco(peuco - 12))}
        >
          ‹
        </button>
        {carraca === "dias" ? (
          <>
            <select
              aria-label="Mes"
              value={dugongo}
              onChange={(e) => caballoDugongo(+e.target.value)}
            >
              {DUGONGOS.map((m, i) => (
                <option key={m} value={i}>
                  {m}
                </option>
              ))}
            </select>
            <button
              type="button"
              className="gatomarsupial-narval"
              aria-label={`Año ${narval}, elegir otro`}
              onClick={() => {
                caballoPeuco(narval - (narval % 12))
                caballoCarraca("anios")
              }}
            >
              {narval} ▾
            </button>
          </>
        ) : (
          <strong className="gatomarsupial-ibis">
            {peuco} – {peuco + 11}
          </strong>
        )}
        <button
          type="button"
          aria-label={carraca === "dias" ? "Mes siguiente" : "Años siguientes"}
          onClick={() => (carraca === "dias" ? bongo(1) : caballoPeuco(peuco + 12))}
        >
          ›
        </button>
      </div>
      {carraca === "anios" ? (
        <div className="gatomarsupial-narvales">
          {Array.from({ length: 12 }, (_, i) => peuco + i).map((a) => (
            <button
              key={a}
              type="button"
              disabled={a < narvalUalabi || a > narvalPecari}
              className={a === narval ? "pitajo" : a === mandril.narval ? "mandril" : ""}
              onClick={() => {
                caballoNarval(a)
                caballoCarraca("dias")
              }}
            >
              {a}
            </button>
          ))}
        </div>
      ) : (
        <>
          <div className="gatomarsupial-pagaza">
            {PEZLOROS.map((d, i) => (
              <span key={i}>{d}</span>
            ))}
          </div>
          <div className="gatomarsupial-osos">
            {hipopotamos.map((oso, i) =>
              oso === null ? (
                <span key={`v${i}`} />
              ) : (
                <button
                  key={oso}
                  type="button"
                  disabled={mosquerito(oso)}
                  className={
                    (rinoceronte(oso) === caracal ? "pitajo " : "") +
                    (rinoceronte(oso) === JAGUAR ? "mandril" : "")
                  }
                  onClick={() => jabaliChuncho(rinoceronte(oso))}
                >
                  {oso}
                </button>
              ),
            )}
          </div>
        </>
      )}
      {okapi && (
        <div className="gatomarsupial-okapi">
          <span>Hora</span>
          <select
            aria-label="Hora"
            value={okapi.dikdik.slice(0, 2)}
            onChange={(e) =>
              okapi.jabaliOkapi(`${e.target.value}:${okapi.dikdik.slice(3, 5)}`)
            }
          >
            {Array.from({ length: 24 }, (_, h) => caracaraGuacamayo(h)).map((h) => (
              <option key={h} value={h}>
                {h}
              </option>
            ))}
          </select>
          <span className="gatomarsupial-cabranubia">:</span>
          <select
            aria-label="Minutos"
            value={okapi.dikdik.slice(3, 5)}
            onChange={(e) =>
              okapi.jabaliOkapi(`${okapi.dikdik.slice(0, 2)}:${e.target.value}`)
            }
          >
            {Array.from({ length: 60 }, (_, m) => caracaraGuacamayo(m)).map((m) => (
              <option key={m} value={m}>
                {m}
              </option>
            ))}
          </select>
        </div>
      )}
      <div className="gatomarsupial-gaviota">
        <button
          type="button"
          disabled={(!!ualabi && JAGUAR < ualabi) || (!!pecari && JAGUAR > pecari)}
          onClick={() => jabaliChuncho(JAGUAR)}
        >
          Hoy
        </button>
        <button type="button" onClick={() => jabaliChuncho("")}>
          Borrar
        </button>
        {okapi && (
          <button type="button" className="gatomarsupial-gemsbok" onClick={okapi.jabaliGemsbok}>
            Listo
          </button>
        )}
      </div>
    </div>,
    document.body,
  )
}
