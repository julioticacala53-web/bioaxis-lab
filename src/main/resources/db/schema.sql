PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS patient (
  id          TEXT PRIMARY KEY,
  dni         TEXT NOT NULL UNIQUE,
  first_name  TEXT NOT NULL,
  last_name   TEXT NOT NULL,
  birth       TEXT NOT NULL,
  sex         TEXT NOT NULL,
  phone       TEXT,
  email       TEXT,
  address     TEXT,
  notes       TEXT,
  created     TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS resource (
  id          TEXT PRIMARY KEY,
  kind        TEXT NOT NULL,
  name        TEXT NOT NULL,
  brand       TEXT, model TEXT, serial TEXT,
  area        TEXT NOT NULL,
  status      TEXT NOT NULL,

  qty         INTEGER NOT NULL DEFAULT 0,
  min_qty     INTEGER NOT NULL DEFAULT 0,
  unit        TEXT NOT NULL DEFAULT 'unidades',
  lot         TEXT,
  expires     TEXT,
  last_maint  TEXT, next_maint TEXT,
  responsible TEXT
);

CREATE TABLE IF NOT EXISTS exam (
  id          TEXT PRIMARY KEY,
  name        TEXT NOT NULL,
  category    TEXT NOT NULL,
  price       REAL NOT NULL,
  time_label  TEXT NOT NULL,
  sample_type TEXT NOT NULL,
  volume      TEXT NOT NULL,
  prep        TEXT,
  equipment_id TEXT NOT NULL REFERENCES resource(id),
  reagent_id   TEXT NOT NULL REFERENCES resource(id),
  material_id  TEXT NOT NULL REFERENCES resource(id),
  enabled     INTEGER NOT NULL DEFAULT 1,
  format      TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS lab_request (
  id          TEXT PRIMARY KEY,
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
  id          TEXT PRIMARY KEY,
  patient_id  TEXT NOT NULL REFERENCES patient(id),
  request_id  TEXT NOT NULL REFERENCES lab_request(id),
  exam_id     TEXT NOT NULL REFERENCES exam(id),
  taken_at    TEXT NOT NULL,
  state       TEXT NOT NULL,

  responsible TEXT NOT NULL,
  notes       TEXT,
  UNIQUE (request_id, exam_id)
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
  id           TEXT PRIMARY KEY,
  equipment_id TEXT NOT NULL REFERENCES resource(id),
  type         TEXT NOT NULL,
  start_date   TEXT NOT NULL,
  end_date     TEXT NOT NULL,
  actual_date  TEXT,
  responsible  TEXT NOT NULL,
  provider     TEXT,
  description  TEXT,
  state        TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS audit_entry (
  id          TEXT PRIMARY KEY,
  at          TEXT NOT NULL,
  user_name   TEXT NOT NULL,
  role        TEXT NOT NULL,
  action      TEXT NOT NULL,
  module      TEXT NOT NULL,
  record_ref  TEXT NOT NULL,

  seq         INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user (
  id     TEXT PRIMARY KEY,
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

CREATE TABLE IF NOT EXISTS preference (
  name  TEXT PRIMARY KEY,
  value TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_sample_state   ON sample(state);
CREATE INDEX IF NOT EXISTS idx_sample_patient ON sample(patient_id);
CREATE INDEX IF NOT EXISTS idx_audit_at       ON audit_entry(at DESC);
CREATE INDEX IF NOT EXISTS idx_audit_seq      ON audit_entry(seq);
