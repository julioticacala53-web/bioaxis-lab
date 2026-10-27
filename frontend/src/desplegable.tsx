/**
 * La lista de un `<select>`, dibujada por nosotros.
 *
 * Por que: dentro de la ventana de escritorio, el desplegable abierto NO lo
 * pinta la web. JavaFX lo dibuja con su propio control, blanco y con el
 * estilo del sistema, y no hace caso del CSS. En medio de una aplicacion
 * oscura canta muchisimo.
 *
 * La solucion vive en un solo sitio a proposito. Este componente se monta
 * una vez en la raiz, escucha el `mousedown` de cualquier `<select>` de la
 * pagina, impide que se abra el nativo y pinta la lista en un portal. Los 29
 * desplegables de la aplicacion no cambian ni una linea: siguen siendo
 * `<select>` de verdad, con su `name`, su `value` y su validacion, asi que
 * los formularios y FormData funcionan igual.
 *
 * Al elegir, el valor se escribe con el setter nativo de HTMLSelectElement y
 * se lanza un `change`: sin el setter, React no se entera del cambio porque
 * guarda su propio valor.
 *
 * OJO: abrir y cerrar se resuelven en UN SOLO listener. Repartirlo en dos --
 * uno que abre y otro, montado en un efecto, que cierra al pulsar fuera --
 * funciona en Chrome y NO funciona dentro de la ventana: WebKit vacia la cola
 * de microtareas entre un listener y el siguiente, asi que React pinta la
 * lista y monta el efecto en mitad del mismo `mousedown` que la abrio, y el
 * listener recien nacido la cierra cuando el evento sigue subiendo. La lista
 * aparecia y desaparecia en el mismo clic: el `<select>` quedaba muerto.
 */
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
      // Dentro de la propia lista no se toca nada: elegir es cosa del boton.
      if (lista.current?.contains(destino as Node)) return
      const campo = destino?.closest?.("select") as HTMLSelectElement | null
      if (!campo || campo.disabled || campo.multiple || campo.size > 1) {
        setAbierto(null)   // pulsar en cualquier otro sitio la cierra
        return
      }
      e.preventDefault()   // esto es lo que impide el desplegable del sistema
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
      // Las flechas siguen siendo del `<select>`, que las entiende de serie;
      // aqui solo se cierra la lista para no dejarla desfasada.
      if (["Escape", "Enter", "Tab", "ArrowUp", "ArrowDown"].includes(e.key)) {
        setAbierto(null)
      }
    }
    // Desplazar la pagina mueve el campo y deja la lista descolgada, asi que
    // se cierra. Desplazar la LISTA no: con treinta opciones hay que poder
    // recorrerla, y sin esta salvedad el primer tiron la cerraba.
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
    // Con una lista larga, abrirla por arriba esconde la opcion vigente.
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
