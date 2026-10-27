import {
  useEffect,
  useRef,
  type ReactNode,
  type ButtonHTMLAttributes,
} from "react"

const paths: Record<string, string> = {
  grid: "M3 3h7v7H3z M14 3h7v7h-7z M3 14h7v7H3z M14 14h7v7h-7z",
  users:
    "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2 M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M20 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75",
  user: "M20 21v-2a7 7 0 0 0-14 0v2 M12 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8",
  clipboard: "M9 4H5v17h14V4h-4 M9 2h6v5H9z M8 12h8 M8 16h5",
  tube: "M8 3h8 M9 3v13a3 3 0 0 0 6 0V3 M9 9h6 M9 13h6",
  flask:
    "M9 3h6 M10 3v6l-6 10a1.5 1.5 0 0 0 1.4 2h13.2a1.5 1.5 0 0 0 1.4-2L14 9V3 M8 14h8",
  file: "M14 2H5v20h14V7z M14 2v6h5 M8 12h8 M8 16h5",
  book: "M4 3h6a3 3 0 0 1 3 3v16a4 4 0 0 0-4-2H4z M13 6a3 3 0 0 1 3-3h5v17h-4a4 4 0 0 0-4 2",
  box: "M3 7l9-5 9 5v10l-9 5-9-5z M3 7l9 5 9-5 M12 12v10 M7 4l10 6",
  monitor: "M3 3h18v14H3z M8 21h8 M12 17v4 M6 10h3l2-4 3 7 2-3h2",
  wrench:
    "M14.7 6.3a5 5 0 0 0-6.4 6.4L3 18l3 3 5.3-5.3a5 5 0 0 0 6.4-6.4l-3 3-3-3z",
  chart: "M3 3v18h18 M7 16v-4 M12 16V8 M17 16V5",
  shield: "M12 2l8 4v6c0 5-8 10-8 10S4 17 4 12V6z M8 12l3 3 5-6",
  settings:
    "M12 8a4 4 0 1 0 0 8 4 4 0 0 0 0-8 M9 3l-1 3-3 1-2 3 2 2-1 3 3 2 2-1 3 2 3-1 1-3 3-1 2-3-2-2 1-3-3-2-2 1-3-2z",
  search: "M10.5 3a7.5 7.5 0 1 0 0 15 7.5 7.5 0 0 0 0-15 M16 16l5 5",
  bell: "M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9 M10 21h4",
  chevron: "M9 5l7 7-7 7",
  down: "M6 9l6 6 6-6",
  plus: "M12 5v14 M5 12h14",
  arrow: "M4 12h16 M15 7l5 5-5 5",
  logout: "M9 3H3v18h6 M9 12h12 M16 7l5 5-5 5",
  clock: "M12 3a9 9 0 1 0 0 18 9 9 0 0 0 0-18 M12 7v5l3 2",
  check: "M5 12l4 4L19 6",
  checkcircle: "M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20 M7 12l3 3 6-6",
  alert: "M12 3L2 21h20z M12 9v5 M12 17v.1",
  info: "M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20 M12 11v6 M12 7v.1",
  close: "M6 6l12 12 M6 18L18 6",
  calendar: "M3 5h18v16H3z M7 2v6 M17 2v6 M3 11h18",
  filter: "M3 4h18l-7 8v7l-4 2v-9z",
  download: "M12 3v12 M7 10l5 5 5-5 M3 16v5h18v-5",
  eye: "M2 12s4-7 10-7 10 7 10 7-4 7-10 7S2 12 2 12 M12 9a3 3 0 1 0 0 6 3 3 0 0 0 0-6",
  more: "M5 12h.01 M12 12h.01 M19 12h.01",
  trend: "M3 17l6-6 4 4 8-11 M15 4h6v6",
  heart: "M3 12h4l3-7 4 14 3-7h4",
  lock: "M5 10h14v12H5z M8 10V6a4 4 0 0 1 8 0v4",
  edit: "M16 3l5 5-12 12-6 1 1-6z M13 6l5 5",
  print: "M6 9V3h12v6 M6 18H3V9h18v9h-3 M6 15h12v7H6z",
  droplet: "M12 2s-7 8-7 13a7 7 0 0 0 14 0c0-5-7-13-7-13z",
  layers: "M12 2L2 7l10 5 10-5z M2 12l10 5 10-5 M2 17l10 5 10-5",
  refresh:
    "M20 7v5h-5 M4 17v-5h5 M5 7a8 8 0 0 1 13-2l2 3 M4 16l2 3a8 8 0 0 0 13-2",
  mail: "M3 5h18v14H3z M3 5l9 8 9-8",
  phone: "M7 3H3c0 10 8 18 18 18v-4l-5-2-3 3-7-7 3-3z",
}
export function Icon({
  name,
  size = 20,
  className = "",
}: {
  name: string
  size?: number
  className?: string
}) {
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="1.6"
      strokeLinecap="round"
      strokeLinejoin="round"
      className={className}
      aria-hidden="true"
    >
      <path d={paths[name] || paths.flask} />
    </svg>
  )
}
export function Button({
  children,
  icon,
  variant = "primary",
  small = false,
  ...props
}: ButtonHTMLAttributes<HTMLButtonElement> & {
  icon?: string
  variant?: string
  small?: boolean
}) {
  return (
    <button
      {...props}
      className={`button ${variant} ${
        small ? "small" : ""
      } ${props.className || ""}`}
    >
      {icon && <Icon name={icon} size={small ? 15 : 17} />} {children}
    </button>
  )
}
export function Badge({
  value,
  children,
}: {
  value: string
  children?: ReactNode
}) {
  const v = value.toLowerCase()
  const tone =
    /publicad|validado|disponible|operativo|activo|finalizado|habilitado/.test(
      v,
    ) && !v.includes("no disponible")
      ? "green"
      : /análisis|proceso|programado/.test(v)
        ? "blue"
        : /pendiente|recibida|validación|stock bajo|mantenimiento|próximo/.test(
              v,
            )
          ? "amber"
          : /agotado|vencido|no disponible|servicio|baja|inactivo|cancelado|deshabilitado/.test(
                v,
              )
            ? "red"
            : "neutral"
  return (
    <span className={`badge ${tone}`}>
      <i />
      {children || value}
    </span>
  )
}
export function Panel({
  title,
  subtitle,
  action,
  children,
  className = "",
}: {
  title?: string
  subtitle?: string
  action?: ReactNode
  children: ReactNode
  className?: string
}) {
  return (
    <section className={`panel ${className}`}>
      {title && (
        <div className="panel-heading">
          <div>
            <h2>{title}</h2>
            {subtitle && <p>{subtitle}</p>}
          </div>
          {action}
        </div>
      )}
      {children}
    </section>
  )
}
export function DataTable({
  columns,
  rows,
  empty = "No se encontraron registros",
  className = "",
}: {
  columns: string[]
  rows: ReactNode[][]
  empty?: string
  className?: string
}) {
  return (
    <div className={`table-scroll ${className}`}>
      <table>
        <thead>
          <tr>
            {columns.map((c) => (
              <th key={c}>{c}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((r, i) => (
            <tr key={i}>
              {r.map((cell, j) => (
                <td key={j}>{cell}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      {!rows.length && (
        <div className="empty">
          <Icon name="search" size={30} />
          <strong>{empty}</strong>
          <span>Prueba con otros filtros o registra un nuevo elemento.</span>
        </div>
      )}
    </div>
  )
}
export function Modal({
  title,
  subtitle,
  children,
  onClose,
  wide = false,
}: {
  title: string
  subtitle?: string
  children: ReactNode
  onClose: () => void
  wide?: boolean
}) {
  const ref = useRef<HTMLDivElement>(null)
  const closeRef = useRef(onClose)
  closeRef.current = onClose
  useEffect(() => {
    const before = document.activeElement as HTMLElement
    ref.current?.focus()
    const key = (e: KeyboardEvent) => {
      if (e.key === "Escape") closeRef.current()
      if (e.key === "Tab") {
        const f = ref.current?.querySelectorAll<HTMLElement>(
          'button:not(:disabled),input:not(:disabled),select:not(:disabled),textarea:not(:disabled),[tabindex="0"]',
        )
        if (f?.length) {
          const first = f[0],
            last = f[f.length - 1]
          if (
            e.shiftKey &&
            (document.activeElement === first ||
              document.activeElement === ref.current)
          ) {
            e.preventDefault()
            last.focus()
          } else if (!e.shiftKey && document.activeElement === last) {
            e.preventDefault()
            first.focus()
          }
        }
      }
    }
    document.addEventListener("keydown", key)
    return () => {
      document.removeEventListener("keydown", key)
      before?.focus()
    }
  }, [])
  return (
    <div
      className="modal-overlay"
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose()
      }}
    >
      <div
        ref={ref}
        role="dialog"
        aria-modal="true"
        aria-label={title}
        tabIndex={-1}
        className={`modal ${wide ? "wide" : ""}`}
      >
        <div className="modal-heading">
          <div>
            <span className="eyebrow">OPERACIÓN INTERNA</span>
            <h2>{title}</h2>
            {subtitle && <p>{subtitle}</p>}
          </div>
          <button
            className="icon-button"
            onClick={onClose}
            aria-label="Cerrar ventana"
          >
            <Icon name="close" />
          </button>
        </div>
        <div className="modal-content">{children}</div>
      </div>
    </div>
  )
}
export function Field({
  label,
  children,
  required = false,
  hint,
}: {
  label: string
  children: ReactNode
  required?: boolean
  hint?: string
}) {
  return (
    <label className="field">
      <span>
        {label}
        {required && <em> *</em>}
      </span>
      {children}
      {hint && <small>{hint}</small>}
    </label>
  )
}

export function Paginador({
  pagina,
  paginas,
  total,
  porPagina,
  ir,
}: {
  pagina: number
  paginas: number
  total: number
  porPagina: number
  ir: (n: number) => void
}) {
  if (paginas <= 1) return null
  const desde = (pagina - 1) * porPagina + 1
  const hasta = Math.min(pagina * porPagina, total)

  const inicio = Math.max(1, Math.min(pagina - 2, paginas - 4))
  const numeros = Array.from(
    { length: Math.min(5, paginas) },
    (_, i) => inicio + i,
  )
  return (
    <div className="paginador">
      <span>
        Mostrando {desde}–{hasta} de {total}
      </span>
      <div className="paginas">
        <button
          type="button"
          aria-label="Página anterior"
          disabled={pagina === 1}
          onClick={() => ir(pagina - 1)}
        >
          ‹
        </button>
        {inicio > 1 && (
          <>
            <button type="button" onClick={() => ir(1)}>
              1
            </button>
            <span className="salto">…</span>
          </>
        )}
        {numeros.map((n) => (
          <button
            key={n}
            type="button"
            className={n === pagina ? "activa" : ""}
            aria-current={n === pagina ? "page" : undefined}
            onClick={() => ir(n)}
          >
            {n}
          </button>
        ))}
        {inicio + 4 < paginas && (
          <>
            <span className="salto">…</span>
            <button type="button" onClick={() => ir(paginas)}>
              {paginas}
            </button>
          </>
        )}
        <button
          type="button"
          aria-label="Página siguiente"
          disabled={pagina === paginas}
          onClick={() => ir(pagina + 1)}
        >
          ›
        </button>
      </div>
    </div>
  )
}
export function SearchBar({
  value,
  onChange,
  placeholder = "Buscar...",
  className = "",
}: {
  value: string
  onChange: (s: string) => void
  placeholder?: string
  className?: string
}) {
  return (
    <div className={`search-box ${className}`}>
      <Icon name="search" size={18} />
      <input
        aria-label={placeholder}
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange(e.target.value)}
      />
      {value && (
        <button
          className="icon-button"
          aria-label="Limpiar búsqueda"
          onClick={() => onChange("")}
        >
          <Icon name="close" size={14} />
        </button>
      )}
    </div>
  )
}
export function StatCard({
  label,
  value,
  icon,
  detail,
  tone = "green",
  onClick,
}: {
  label: string
  value: number | string
  icon: string
  detail?: string
  tone?: string
  onClick?: () => void
}) {
  return (
    <button className={`stat-card ${tone}`} onClick={onClick}>
      <div className="stat-top">
        <span>{label}</span>
        <div className="stat-icon">
          <Icon name={icon} size={18} />
        </div>
      </div>
      <strong>
        {typeof value === "number" ? String(value).padStart(2, "0") : value}
      </strong>
      <div className="stat-bottom">
        <span>{detail || "Actualizado en tiempo real"}</span>
        <Icon name="arrow" size={14} />
      </div>
    </button>
  )
}
export function Avatar({
  name,
  size = "normal",
}: {
  name: string
  size?: string
}) {
  return (
    <span className={`avatar ${size}`}>
      {name
        .split(" ")
        .slice(0, 2)
        .map((x) => x[0])
        .join("")}
    </span>
  )
}
export function Tabs({
  items,
  active,
  onChange,
  counts,
}: {
  items: string[]
  active: string
  onChange: (s: string) => void
  counts?: Record<string, number>
}) {
  return (
    <div className="tabs">
      {items.map((x) => (
        <button
          key={x}
          className={active === x ? "active" : ""}
          onClick={() => onChange(x)}
        >
          {x}
          {counts && counts[x] !== undefined && (
            <span className="tab-count">{counts[x]}</span>
          )}
        </button>
      ))}
    </div>
  )
}
export function Notice({
  children,
  tone = "info",
}: {
  children: ReactNode
  tone?: string
}) {
  return (
    <div className={`notice ${tone}`}>
      <Icon
        name={
          tone === "success"
            ? "checkcircle"
            : tone === "warning"
              ? "alert"
              : "info"
        }
        size={19}
      />
      <div>{children}</div>
    </div>
  )
}
