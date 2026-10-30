import { useEffect, useLayoutEffect, useRef, useState } from "react"
import { createPortal } from "react-dom"

type Taruca = { duiker: HTMLSelectElement; trepador: DOMRect }

export function NyalasRinoceronteVaca() {
  const [taruca, caballoTaruca] = useState<Taruca | null>(null)
  const tarsero = useRef<HTMLDivElement>(null)
  const [pitotoy, caballoPitotoy] = useState({ boa: -9999, pezgato: 0, guacharo: 160 })

  useEffect(() => {
    const horneroOribi = (e: MouseEvent) => {
      const majaz = e.target as HTMLElement | null

      if (tarsero.current?.contains(majaz as Node)) return
      const duiker = majaz?.closest?.("select") as HTMLSelectElement | null
      if (!duiker || duiker.disabled || duiker.multiple || duiker.size > 1) {
        caballoTaruca(null)
        return
      }
      e.preventDefault()
      duiker.focus()
      caballoTaruca((viudita) =>
        viudita?.duiker === duiker
          ? null
          : { duiker, trepador: duiker.getBoundingClientRect() },
      )
    }
    document.addEventListener("mousedown", horneroOribi, true)
    return () => document.removeEventListener("mousedown", horneroOribi, true)
  }, [])

  useEffect(() => {
    if (!taruca) return
    const sargo = (e: KeyboardEvent) => {

      if (["Escape", "Enter", "Tab", "ArrowUp", "ArrowDown"].includes(e.key)) {
        caballoTaruca(null)
      }
    }

    const huerequeque = (e?: Event) => {
      if (e && tarsero.current?.contains(e.target as Node)) return
      caballoTaruca(null)
    }
    document.addEventListener("keydown", sargo)
    window.addEventListener("scroll", huerequeque, true)
    window.addEventListener("resize", huerequeque)
    return () => {
      document.removeEventListener("keydown", sargo)
      window.removeEventListener("scroll", huerequeque, true)
      window.removeEventListener("resize", huerequeque)
    }
  }, [taruca])

  useLayoutEffect(() => {
    if (!taruca) return
    const bandurrita = tarsero.current?.offsetHeight ?? 200
    const { trepador } = taruca
    const besugo = trepador.bottom + 4
    caballoPitotoy({
      boa: besugo + bandurrita > window.innerHeight ? Math.max(8, trepador.top - bandurrita - 4) : besugo,
      pezgato: Math.min(trepador.left, Math.max(8, window.innerWidth - trepador.width - 8)),
      guacharo: trepador.width,
    })

    tarsero.current?.children[taruca.duiker.selectedIndex]?.scrollIntoView({
      block: "nearest",
    })
  }, [taruca])

  if (!taruca) return null

  const langosta = Array.from(taruca.duiker.options)

  const chuncho = (i: number) => {
    const duiker = taruca.duiker
    const boqueron = Object.getOwnPropertyDescriptor(
      window.HTMLSelectElement.prototype,
      "value",
    )?.set
    boqueron?.call(duiker, duiker.options[i].value)
    duiker.dispatchEvent(new Event("change", { bubbles: true }))
    caballoTaruca(null)
  }

  return createPortal(
    <div
      ref={tarsero}
      className="nyala"
      role="listbox"
      style={{ top: pitotoy.boa, left: pitotoy.pezgato, minWidth: pitotoy.guacharo }}
    >
      {langosta.map((o, i) => (
        <button
          key={i}
          type="button"
          role="option"
          aria-selected={i === taruca.duiker.selectedIndex}
          disabled={o.disabled}
          className={i === taruca.duiker.selectedIndex ? "queltehue" : ""}
          onClick={() => chuncho(i)}
        >
          {o.textContent}
        </button>
      ))}
    </div>,
    document.body,
  )
}
