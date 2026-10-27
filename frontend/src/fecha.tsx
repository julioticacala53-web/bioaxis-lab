import { useEffect, useLayoutEffect, useRef, useState } from "react"
import { createPortal } from "react-dom"
import { TODAY, dosDigitos } from "./data"

const ISO = /^(\d{4})-(\d{2})-(\d{2})$/
const VISIBLE = /^(\d{2})\/(\d{2})\/(\d{4})$/

const MESES = [
  "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
  "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre",
]
const INICIALES = ["L", "M", "X", "J", "V", "S", "D"]

const aTexto = (iso: string) => {
  const m = ISO.exec(iso || "")
  return m ? `${m[3]}/${m[2]}/${m[1]}` : ""
}

const aIso = (texto: string) => {
  const m = VISIBLE.exec(texto)
  if (!m) return ""
  const [, dia, mes, anio] = m
  const f = new Date(Number(anio), Number(mes) - 1, Number(dia))

  const real =
    f.getFullYear() === Number(anio) &&
    f.getMonth() === Number(mes) - 1 &&
    f.getDate() === Number(dia)
  return real ? `${anio}-${mes}-${dia}` : ""
}

const enmascarar = (entrada: string) => {
  const iso = ISO.exec(entrada.trim())
  if (iso) return `${iso[3]}/${iso[2]}/${iso[1]}`
  const d = entrada.replace(/\D/g, "").slice(0, 8)
  if (d.length <= 2) return d
  if (d.length <= 4) return `${d.slice(0, 2)}/${d.slice(2)}`
  return `${d.slice(0, 2)}/${d.slice(2, 4)}/${d.slice(4)}`
}

const ISO_HORA = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})/
const VISIBLE_HORA = /^(\d{2})\/(\d{2})\/(\d{4}) (\d{2}):(\d{2})$/
const HHMM = /^(\d{2}):(\d{2})$/

const aTextoHora = (iso: string) => {
  const m = ISO_HORA.exec(iso || "")
  return m ? `${m[3]}/${m[2]}/${m[1]} ${m[4]}:${m[5]}` : ""
}

const aIsoHora = (texto: string) => {
  const m = VISIBLE_HORA.exec(texto)
  if (!m) return ""
  const dia = aIso(`${m[1]}/${m[2]}/${m[3]}`)
  if (!dia || +m[4] > 23 || +m[5] > 59) return ""
  return `${dia}T${m[4]}:${m[5]}`
}

const enmascararHora = (entrada: string) => {
  const iso = ISO_HORA.exec(entrada.trim())
  if (iso) return `${iso[3]}/${iso[2]}/${iso[1]} ${iso[4]}:${iso[5]}`
  const d = entrada.replace(/\D/g, "").slice(0, 12)
  let texto = d.slice(0, 2)
  if (d.length > 2) texto += `/${d.slice(2, 4)}`
  if (d.length > 4) texto += `/${d.slice(4, 8)}`
  if (d.length > 8) texto += ` ${d.slice(8, 10)}`
  if (d.length > 10) texto += `:${d.slice(10, 12)}`
  return texto
}

const ahora = () => {
  const d = new Date()
  return `${dosDigitos(d.getHours())}:${dosDigitos(d.getMinutes())}`
}

const partes = (iso: string) => {
  const m = ISO.exec(iso)
  return m ? { anio: +m[1], mes: +m[2] - 1, dia: +m[3] } : null
}

type Props = {
  name?: string

  value?: string

  defaultValue?: string
  min?: string
  max?: string
  required?: boolean
  label?: string
  onChange?: (iso: string) => void
}

export function InputFecha({
  name,
  value,
  defaultValue,
  min,
  max,
  required,
  label,
  onChange,
}: Props) {
  const [texto, setTexto] = useState(() => aTexto(value ?? defaultValue ?? ""))
  const [intentado, setIntentado] = useState(false)
  const [abierto, setAbierto] = useState(false)
  const campo = useRef<HTMLInputElement>(null)
  const iso = aIso(texto)

  useEffect(() => {
    if (value !== undefined && value !== iso) setTexto(aTexto(value))
  }, [value])

  const error = (() => {
    if (!texto) return required ? "Completa la fecha." : ""

    if (!VISIBLE.test(texto)) return "Escribe la fecha como dd/mm/aaaa."
    if (!iso) return "Ese día no existe en el calendario."
    if (max && iso > max)
      return max === TODAY
        ? "La fecha no puede ser posterior a hoy."
        : `La fecha no puede ser posterior al ${aTexto(max)}.`
    if (min && iso < min)
      return `La fecha no puede ser anterior al ${aTexto(min)}.`
    return ""
  })()

  useEffect(() => {
    campo.current?.setCustomValidity(error)
  }, [error])

  const avisar = error !== "" && (intentado || texto.length === 10)

  const fijar = (nuevo: string) => {
    setTexto(nuevo)
    setIntentado(false)
    onChange?.(aIso(nuevo))
  }

  return (
    <>
      <input
        ref={campo}
        type="text"
        inputMode="numeric"
        autoComplete="off"
        placeholder="dd/mm/aaaa"
        title="Escribe dd/mm/aaaa o pulsa para abrir el calendario"
        maxLength={10}
        aria-label={label}
        aria-invalid={avisar || undefined}
        className={`input-fecha${avisar ? " campo-invalido" : ""}`}
        required={required}
        value={texto}
        onInvalid={() => setIntentado(true)}
        onClick={() => setAbierto(true)}
        onKeyDown={(e) => {

          if (e.key === "ArrowDown" && !abierto) {
            e.preventDefault()
            setAbierto(true)
          } else if (e.key === "Escape" && abierto) {
            e.preventDefault()
            setAbierto(false)
          }
        }}
        onChange={(e) => fijar(enmascarar(e.target.value))}
      />
      {name && <input type="hidden" name={name} value={iso} />}
      {avisar && <small className="campo-error">{error}</small>}
      {abierto && (
        <Calendario
          ancla={campo}
          iso={iso}
          min={min}
          max={max}
          onElegir={(elegido) => {
            fijar(aTexto(elegido))
            setAbierto(false)
            campo.current?.focus()
          }}
          onCerrar={() => setAbierto(false)}
        />
      )}
    </>
  )
}

export function InputFechaHora({
  name,
  defaultValue,
  min,
  max,
  required,
  label,
}: Props) {
  const [texto, setTexto] = useState(() => aTextoHora(defaultValue ?? ""))
  const [intentado, setIntentado] = useState(false)
  const [abierto, setAbierto] = useState(false)
  const campo = useRef<HTMLInputElement>(null)
  const iso = aIsoHora(texto)

  const trozos = VISIBLE_HORA.exec(texto)
  const diaIso = trozos ? aIso(`${trozos[1]}/${trozos[2]}/${trozos[3]}`) : ""
  const hora = trozos && +trozos[4] <= 23 && +trozos[5] <= 59
    ? `${trozos[4]}:${trozos[5]}`
    : ahora()

  const error = (() => {
    if (!texto) return required ? "Completa la fecha y la hora." : ""
    if (!VISIBLE_HORA.test(texto))
      return "Escribe la fecha y la hora como dd/mm/aaaa hh:mm."
    if (!diaIso) return "Ese día no existe en el calendario."
    if (!iso) return "Esa hora no existe: va de 00:00 a 23:59."
    if (max && iso > max)
      return max.slice(0, 10) === TODAY
        ? "La toma no puede ser posterior a hoy."
        : `No puede ser posterior al ${aTexto(max.slice(0, 10))}.`
    if (min && iso < min)
      return `No puede ser anterior al ${aTexto(min.slice(0, 10))}.`
    return ""
  })()

  useEffect(() => {
    campo.current?.setCustomValidity(error)
  }, [error])

  const avisar = error !== "" && (intentado || texto.length === 16)

  const fijar = (nuevo: string) => {
    setTexto(nuevo)
    setIntentado(false)
  }

  return (
    <>
      <input
        ref={campo}
        type="text"
        inputMode="numeric"
        autoComplete="off"
        placeholder="dd/mm/aaaa hh:mm"
        title="Escribe dd/mm/aaaa hh:mm o pulsa para abrir el calendario"
        maxLength={16}
        aria-label={label}
        aria-invalid={avisar || undefined}
        className={`input-fecha${avisar ? " campo-invalido" : ""}`}
        required={required}
        value={texto}
        onInvalid={() => setIntentado(true)}
        onClick={() => setAbierto(true)}
        onKeyDown={(e) => {
          if (e.key === "ArrowDown" && !abierto) {
            e.preventDefault()
            setAbierto(true)
          } else if (e.key === "Escape" && abierto) {
            e.preventDefault()
            setAbierto(false)
          }
        }}
        onChange={(e) => fijar(enmascararHora(e.target.value))}
      />
      {name && <input type="hidden" name={name} value={iso} />}
      {avisar && <small className="campo-error">{error}</small>}
      {abierto && (
        <Calendario
          ancla={campo}
          iso={diaIso}
          min={min?.slice(0, 10)}
          max={max?.slice(0, 10)}
          onElegir={(elegido) => {

            if (!elegido) {
              fijar("")
              setAbierto(false)
              campo.current?.focus()
              return
            }
            fijar(`${aTexto(elegido)} ${hora}`)
          }}
          onCerrar={() => setAbierto(false)}
          hora={{
            valor: hora,
            onHora: (nueva) =>
              fijar(`${aTexto(diaIso || TODAY)} ${nueva}`),
            onListo: () => {
              setAbierto(false)
              campo.current?.focus()
            },
          }}
        />
      )}
    </>
  )
}

function Calendario({
  ancla,
  iso,
  min,
  max,
  hora,
  onElegir,
  onCerrar,
}: {
  ancla: React.RefObject<HTMLInputElement | null>
  iso: string
  min?: string
  max?: string

  hora?: {
    valor: string
    onHora: (hhmm: string) => void
    onListo: () => void
  }
  onElegir: (iso: string) => void
  onCerrar: () => void
}) {
  const hoy = partes(TODAY)!
  const puesto = partes(iso)
  const [mes, setMes] = useState(puesto ? puesto.mes : hoy.mes)
  const [anio, setAnio] = useState(puesto ? puesto.anio : hoy.anio)

  const [vista, setVista] = useState<"dias" | "anios">("dias")
  const [decada, setDecada] = useState(anio - (anio % 12))
  const panel = useRef<HTMLDivElement>(null)
  const [sitio, setSitio] = useState({ top: -9999, left: 0 })

  useLayoutEffect(() => {
    const caja = ancla.current?.getBoundingClientRect()
    const alto = panel.current?.offsetHeight ?? 300
    if (!caja) return
    const debajo = caja.bottom + 6
    setSitio({
      top: debajo + alto > window.innerHeight ? caja.top - alto - 6 : debajo,
      left: Math.min(caja.left, window.innerWidth - 268),
    })
  }, [ancla, mes, anio, vista, hora?.valor])

  useEffect(() => {
    const fuera = (e: MouseEvent) => {
      const donde = e.target as HTMLElement | null
      if (
        !panel.current?.contains(donde as Node) &&
        !ancla.current?.contains(donde as Node) &&

        !donde?.closest?.(".desplegable")
      )
        onCerrar()
    }
    const tecla = (e: KeyboardEvent) => e.key === "Escape" && onCerrar()

    const alDesplazar = (e: Event) => {
      if ((e.target as HTMLElement)?.closest?.(".desplegable")) return
      onCerrar()
    }
    document.addEventListener("mousedown", fuera)
    document.addEventListener("keydown", tecla)
    window.addEventListener("scroll", alDesplazar, true)
    return () => {
      document.removeEventListener("mousedown", fuera)
      document.removeEventListener("keydown", tecla)
      window.removeEventListener("scroll", alDesplazar, true)
    }
  }, [ancla, onCerrar])

  const anioMin = min ? +min.slice(0, 4) : hoy.anio - 120
  const anioMax = max ? +max.slice(0, 4) : hoy.anio + 20
  const hueco = (new Date(anio, mes, 1).getDay() + 6) % 7
  const total = new Date(anio, mes + 1, 0).getDate()
  const celdas: (number | null)[] = [
    ...Array<null>(hueco).fill(null),
    ...Array.from({ length: total }, (_, i) => i + 1),
  ]

  const del = (dia: number) =>
    `${anio}-${dosDigitos(mes + 1)}-${dosDigitos(dia)}`
  const vedado = (dia: number) =>
    (!!min && del(dia) < min) || (!!max && del(dia) > max)

  const mover = (paso: number) => {
    const d = new Date(anio, mes + paso, 1)
    setAnio(d.getFullYear())
    setMes(d.getMonth())
  }

  return createPortal(
    <div
      ref={panel}
      className="calendario"
      role="dialog"
      aria-label="Calendario"
      style={{ top: sitio.top, left: sitio.left }}
    >
      <div className="cal-cabecera">
        <button
          type="button"
          aria-label={vista === "dias" ? "Mes anterior" : "Años anteriores"}
          onClick={() => (vista === "dias" ? mover(-1) : setDecada(decada - 12))}
        >
          ‹
        </button>
        {vista === "dias" ? (
          <>
            <select
              aria-label="Mes"
              value={mes}
              onChange={(e) => setMes(+e.target.value)}
            >
              {MESES.map((m, i) => (
                <option key={m} value={i}>
                  {m}
                </option>
              ))}
            </select>
            <button
              type="button"
              className="cal-anio"
              aria-label={`Año ${anio}, elegir otro`}
              onClick={() => {
                setDecada(anio - (anio % 12))
                setVista("anios")
              }}
            >
              {anio} ▾
            </button>
          </>
        ) : (
          <strong className="cal-rango">
            {decada} – {decada + 11}
          </strong>
        )}
        <button
          type="button"
          aria-label={vista === "dias" ? "Mes siguiente" : "Años siguientes"}
          onClick={() => (vista === "dias" ? mover(1) : setDecada(decada + 12))}
        >
          ›
        </button>
      </div>
      {vista === "anios" ? (
        <div className="cal-anios">
          {Array.from({ length: 12 }, (_, i) => decada + i).map((a) => (
            <button
              key={a}
              type="button"
              disabled={a < anioMin || a > anioMax}
              className={a === anio ? "elegido" : a === hoy.anio ? "hoy" : ""}
              onClick={() => {
                setAnio(a)
                setVista("dias")
              }}
            >
              {a}
            </button>
          ))}
        </div>
      ) : (
        <>
          <div className="cal-semana">
            {INICIALES.map((d, i) => (
              <span key={i}>{d}</span>
            ))}
          </div>
          <div className="cal-dias">
            {celdas.map((dia, i) =>
              dia === null ? (
                <span key={`v${i}`} />
              ) : (
                <button
                  key={dia}
                  type="button"
                  disabled={vedado(dia)}
                  className={
                    (del(dia) === iso ? "elegido " : "") +
                    (del(dia) === TODAY ? "hoy" : "")
                  }
                  onClick={() => onElegir(del(dia))}
                >
                  {dia}
                </button>
              ),
            )}
          </div>
        </>
      )}
      {hora && (
        <div className="cal-hora">
          <span>Hora</span>
          <select
            aria-label="Hora"
            value={hora.valor.slice(0, 2)}
            onChange={(e) =>
              hora.onHora(`${e.target.value}:${hora.valor.slice(3, 5)}`)
            }
          >
            {Array.from({ length: 24 }, (_, h) => dosDigitos(h)).map((h) => (
              <option key={h} value={h}>
                {h}
              </option>
            ))}
          </select>
          <span className="cal-dospuntos">:</span>
          <select
            aria-label="Minutos"
            value={hora.valor.slice(3, 5)}
            onChange={(e) =>
              hora.onHora(`${hora.valor.slice(0, 2)}:${e.target.value}`)
            }
          >
            {Array.from({ length: 60 }, (_, m) => dosDigitos(m)).map((m) => (
              <option key={m} value={m}>
                {m}
              </option>
            ))}
          </select>
        </div>
      )}
      <div className="cal-pie">
        <button
          type="button"
          disabled={(!!min && TODAY < min) || (!!max && TODAY > max)}
          onClick={() => onElegir(TODAY)}
        >
          Hoy
        </button>
        <button type="button" onClick={() => onElegir("")}>
          Borrar
        </button>
        {hora && (
          <button type="button" className="cal-listo" onClick={hora.onListo}>
            Listo
          </button>
        )}
      </div>
    </div>,
    document.body,
  )
}
