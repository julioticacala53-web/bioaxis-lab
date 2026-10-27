import { useEffect, useLayoutEffect, useRef, useState } from "react"
import { createPortal } from "react-dom"

type Abierto = { campo: HTMLSelectElement; caja: DOMRect }

export function DesplegablesDelLaboratorio() {
  const [abierto, setAbierto] = useState<Abierto | null>(null)
  const lista = useRef<HTMLDivElement>(null)
  const [sitio, setSitio] = useState({ top: -9999, left: 0, width: 160 })

  useEffect(() => {
    const alPulsar = (e: MouseEvent) => {
      const destino = e.target as HTMLElement | null

      if (lista.current?.contains(destino as Node)) return
      const campo = destino?.closest?.("select") as HTMLSelectElement | null
      if (!campo || campo.disabled || campo.multiple || campo.size > 1) {
        setAbierto(null)
        return
      }
      e.preventDefault()
      campo.focus()
      setAbierto((previo) =>
        previo?.campo === campo
          ? null
          : { campo, caja: campo.getBoundingClientRect() },
      )
    }
    document.addEventListener("mousedown", alPulsar, true)
    return () => document.removeEventListener("mousedown", alPulsar, true)
  }, [])

  useEffect(() => {
    if (!abierto) return
    const tecla = (e: KeyboardEvent) => {

      if (["Escape", "Enter", "Tab", "ArrowUp", "ArrowDown"].includes(e.key)) {
        setAbierto(null)
      }
    }

    const cerrar = (e?: Event) => {
      if (e && lista.current?.contains(e.target as Node)) return
      setAbierto(null)
    }
    document.addEventListener("keydown", tecla)
    window.addEventListener("scroll", cerrar, true)
    window.addEventListener("resize", cerrar)
    return () => {
      document.removeEventListener("keydown", tecla)
      window.removeEventListener("scroll", cerrar, true)
      window.removeEventListener("resize", cerrar)
    }
  }, [abierto])

  useLayoutEffect(() => {
    if (!abierto) return
    const alto = lista.current?.offsetHeight ?? 200
    const { caja } = abierto
    const debajo = caja.bottom + 4
    setSitio({
      top: debajo + alto > window.innerHeight ? Math.max(8, caja.top - alto - 4) : debajo,
      left: Math.min(caja.left, Math.max(8, window.innerWidth - caja.width - 8)),
      width: caja.width,
    })

    lista.current?.children[abierto.campo.selectedIndex]?.scrollIntoView({
      block: "nearest",
    })
  }, [abierto])

  if (!abierto) return null

  const opciones = Array.from(abierto.campo.options)

  const elegir = (i: number) => {
    const campo = abierto.campo
    const escribir = Object.getOwnPropertyDescriptor(
      window.HTMLSelectElement.prototype,
      "value",
    )?.set
    escribir?.call(campo, campo.options[i].value)
    campo.dispatchEvent(new Event("change", { bubbles: true }))
    setAbierto(null)
  }

  return createPortal(
    <div
      ref={lista}
      className="desplegable"
      role="listbox"
      style={{ top: sitio.top, left: sitio.left, minWidth: sitio.width }}
    >
      {opciones.map((o, i) => (
        <button
          key={i}
          type="button"
          role="option"
          aria-selected={i === abierto.campo.selectedIndex}
          disabled={o.disabled}
          className={i === abierto.campo.selectedIndex ? "elegida" : ""}
          onClick={() => elegir(i)}
        >
          {o.textContent}
        </button>
      ))}
    </div>,
    document.body,
  )
}
