PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS patient (
  id          TEXT PRIMARY KEY,              -- PAC-000001
  dni         TEXT NOT NULL UNIQUE,          -- 8 dígitos
  first_name  TEXT NOT NULL,
  last_name   TEXT NOT NULL,
  birth       TEXT NOT NULL,                 -- ISO yyyy-MM-dd
  sex         TEXT NOT NULL,                 -- Femenino | Masculino | Otro
  phone       TEXT,
  email       TEXT,
  address     TEXT,
  notes       TEXT,
  created     TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS resource (
  id          TEXT PRIMARY KEY,              -- EQ-001 | REA-001 | MAT-001
  kind        TEXT NOT NULL,                 -- Equipos | Reactivos | Materiales
  name        TEXT NOT NULL,
  brand       TEXT, model TEXT, serial TEXT,
  area        TEXT NOT NULL,
  status      TEXT NOT NULL,                 -- solo Equipos: OPERATIVO | EN MANTENIMIENTO
                                             --               FUERA DE SERVICIO | BAJA
  qty         INTEGER NOT NULL DEFAULT 0,
  min_qty     INTEGER NOT NULL DEFAULT 0,
  unit        TEXT NOT NULL DEFAULT 'unidades',
  lot         TEXT,
  expires     TEXT,                          -- solo Reactivos
  last_maint  TEXT, next_maint TEXT,
  responsible TEXT
);

CREATE TABLE IF NOT EXISTS exam (
  id          TEXT PRIMARY KEY,              -- EX-001
  name        TEXT NOT NULL,
  category    TEXT NOT NULL,
  price       REAL NOT NULL,
  time_label  TEXT NOT NULL,                 -- "24 horas"
  sample_type TEXT NOT NULL,
  volume      TEXT NOT NULL,
  prep        TEXT,
  equipment_id TEXT NOT NULL REFERENCES resource(id),
  reagent_id   TEXT NOT NULL REFERENCES resource(id),
  material_id  TEXT NOT NULL REFERENCES resource(id),
  enabled     INTEGER NOT NULL DEFAULT 1,
  format      TEXT NOT NULL                  -- hemogram|glucose|lipid|urine|
);                                           -- microbiology|tsh|t4

CREATE TABLE IF NOT EXISTS lab_request (
  id          TEXT PRIMARY KEY,              -- SOL-000001
  patient_id  TEXT NOT NULL REFERENCES patient(id),
  date        TEXT NOT NULL,
  pending     INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE IF NOT EXISTS request_exam (
  request_id TEXT NOT NULL REFERENCES lab_request(id) ON DELETE CASCADE,
  exam_id    TEXT NOT NULL REFERENCES exam(id),
  position   INTEGER NOT NULL,
  PRIMARY KEY (request_id, exam_id)
);

CREATE TABLE IF NOT EXISTS sample (
  id          TEXT PRIMARY KEY,              -- MUE-2026-000001
  patient_id  TEXT NOT NULL REFERENCES patient(id),
  request_id  TEXT NOT NULL REFERENCES lab_request(id),
  exam_id     TEXT NOT NULL REFERENCES exam(id),
  taken_at    TEXT NOT NULL,                 -- yyyy-MM-ddTHH:mm:ss
  state       TEXT NOT NULL,                 -- RECIBIDA | EN ANÁLISIS | EN VALIDACIÓN
                                             -- | VALIDADO | PUBLICADA
  responsible TEXT NOT NULL,
  notes       TEXT,
  UNIQUE (request_id, exam_id)               -- regla §6.4.3, aplicada en BD
);

CREATE TABLE IF NOT EXISTS sample_trace (
  id        INTEGER PRIMARY KEY AUTOINCREMENT,
  sample_id TEXT NOT NULL REFERENCES sample(id) ON DELETE CASCADE,
  state     TEXT NOT NULL,
  at        TEXT NOT NULL,
  user_name TEXT NOT NULL,
  position  INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS sample_result (
  id          INTEGER PRIMARY KEY AUTOINCREMENT,
  sample_id   TEXT NOT NULL REFERENCES sample(id) ON DELETE CASCADE,
  param_name  TEXT NOT NULL,
  value       TEXT,
  observation TEXT,
  UNIQUE (sample_id, param_name)
);

CREATE TABLE IF NOT EXISTS maintenance (
  id           TEXT PRIMARY KEY,             -- MAN-000001
  equipment_id TEXT NOT NULL REFERENCES resource(id),
  type         TEXT NOT NULL,                -- Preventivo | Correctivo
  start_date   TEXT NOT NULL,
  end_date     TEXT NOT NULL,                -- estimada
  actual_date  TEXT,                         -- real
  responsible  TEXT NOT NULL,
  provider     TEXT,
  description  TEXT,
  state        TEXT NOT NULL                 -- Programado|En proceso|Finalizado|Cancelado
);

CREATE TABLE IF NOT EXISTS audit_entry (
  id          TEXT PRIMARY KEY,              -- AUD-000001
  at          TEXT NOT NULL,
  user_name   TEXT NOT NULL,
  role        TEXT NOT NULL,
  action      TEXT NOT NULL,
  module      TEXT NOT NULL,
  record_ref  TEXT NOT NULL,
  -- Orden de la lista, no presente en el DDL de la especificacion.
  -- El original no ordena la auditoria por fecha: la muestra en orden de
  -- array e inserta lo nuevo con unshift. Ordenar por fecha fallaria en una
  -- demostracion por la manana, porque una accion a las 09:00 quedaria por
  -- debajo de las entradas de la semilla (10:20, 11:05, 12:30) en vez de
  -- arriba. La semilla ocupa 0,1,2 y cada entrada nueva toma min-1, asi que
  -- ordenar por seq ascendente reproduce el array del original exactamente.
  seq         INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user (
  id     TEXT PRIMARY KEY,                   -- USR-001
  name   TEXT NOT NULL,
  email  TEXT NOT NULL,
  role   TEXT NOT NULL,
  active INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS settings (
  id            INTEGER PRIMARY KEY CHECK (id = 1),
  lab           TEXT NOT NULL,
  branch        TEXT NOT NULL,
  notifications INTEGER NOT NULL DEFAULT 1
);

-- Preferencias sueltas del equipo (por ahora, la carpeta de exportaciones).
-- Tabla aparte y no columnas de settings: asi aparece sola en las bases que
-- ya existian, porque este archivo corre entero en cada arranque.
CREATE TABLE IF NOT EXISTS preference (
  name  TEXT PRIMARY KEY,
  value TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_sample_state   ON sample(state);
CREATE INDEX IF NOT EXISTS idx_sample_patient ON sample(patient_id);
CREATE INDEX IF NOT EXISTS idx_audit_at       ON audit_entry(at DESC);
CREATE INDEX IF NOT EXISTS idx_audit_seq      ON audit_entry(seq);
