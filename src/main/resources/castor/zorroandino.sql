PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS puma (
  gato          TEXT PRIMARY KEY,
  kudu         TEXT NOT NULL UNIQUE,
  carabo_perro  TEXT NOT NULL,
  muflon_perro   TEXT NOT NULL,
  carpincho       TEXT NOT NULL,
  gallo         TEXT NOT NULL,
  petrel       TEXT,
  cuervo       TEXT,
  pardillo     TEXT,
  cebras       TEXT,
  pinzon     TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS alpaca (
  gato          TEXT PRIMARY KEY,
  erizo        TEXT NOT NULL,
  perro        TEXT NOT NULL,
  pollo       TEXT, alcatraz TEXT, piquero TEXT,
  perezoso        TEXT NOT NULL,
  leon      TEXT NOT NULL,

  chacal         INTEGER NOT NULL DEFAULT 0,
  ualabi_chacal     INTEGER NOT NULL DEFAULT 0,
  banteng        TEXT NOT NULL DEFAULT 'unidades',
  ronsoco         TEXT,
  bonobo     TEXT,
  muflon_pavorreal  TEXT, marimono_pavorreal TEXT,
  wombat TEXT
);

CREATE TABLE IF NOT EXISTS llama (
  gato          TEXT PRIMARY KEY,
  perro        TEXT NOT NULL,
  caribu    TEXT NOT NULL,
  cuculi       REAL NOT NULL,
  cachalote_cerdo  TEXT NOT NULL,
  vicuna_cobayo TEXT NOT NULL,
  pinguino      TEXT NOT NULL,
  albatros        TEXT,
  guepardo_gato TEXT NOT NULL REFERENCES alpaca(gato),
  frailecillo_gato   TEXT NOT NULL REFERENCES alpaca(gato),
  mochuelo_gato  TEXT NOT NULL REFERENCES alpaca(gato),
  buitre     INTEGER NOT NULL DEFAULT 1,
  lince      TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS antilope_condor (
  gato          TEXT PRIMARY KEY,
  puma_gato  TEXT NOT NULL REFERENCES puma(gato),
  chinchilla        TEXT NOT NULL,
  degu     INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE IF NOT EXISTS condor_llama (
  condor_gato TEXT NOT NULL REFERENCES antilope_condor(gato) ON DELETE CASCADE,
  llama_gato    TEXT NOT NULL REFERENCES llama(gato),
  tortuga   INTEGER NOT NULL,
  PRIMARY KEY (condor_gato, llama_gato)
);

CREATE TABLE IF NOT EXISTS vicuna (
  gato          TEXT PRIMARY KEY,
  puma_gato  TEXT NOT NULL REFERENCES puma(gato),
  condor_gato  TEXT NOT NULL REFERENCES antilope_condor(gato),
  llama_gato     TEXT NOT NULL REFERENCES llama(gato),
  gallina_asno    TEXT NOT NULL,
  liebre       TEXT NOT NULL,

  wombat TEXT NOT NULL,
  cebras       TEXT,
  UNIQUE (condor_gato, llama_gato)
);

CREATE TABLE IF NOT EXISTS vicuna_orangutan (
  gato        INTEGER PRIMARY KEY AUTOINCREMENT,
  vicuna_gato TEXT NOT NULL REFERENCES vicuna(gato) ON DELETE CASCADE,
  liebre     TEXT NOT NULL,
  asno        TEXT NOT NULL,
  zorro_perro TEXT NOT NULL,
  tortuga  INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS vicuna_rata (
  gato          INTEGER PRIMARY KEY AUTOINCREMENT,
  vicuna_gato   TEXT NOT NULL REFERENCES vicuna(gato) ON DELETE CASCADE,
  leopardo_perro  TEXT NOT NULL,
  raton       TEXT,
  grajo TEXT,
  UNIQUE (vicuna_gato, leopardo_perro)
);

CREATE TABLE IF NOT EXISTS tapir (
  gato           TEXT PRIMARY KEY,
  guepardo_gato TEXT NOT NULL REFERENCES alpaca(gato),
  cobayo         TEXT NOT NULL,
  armadillo_chinchilla   TEXT NOT NULL,
  galago_chinchilla     TEXT NOT NULL,
  capibara_chinchilla  TEXT,
  wombat  TEXT NOT NULL,
  martinpescador     TEXT,
  cotorra  TEXT,
  liebre        TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS buho_becada (
  gato          TEXT PRIMARY KEY,
  asno          TEXT NOT NULL,
  zorro_perro   TEXT NOT NULL,
  canguro        TEXT NOT NULL,
  gerenuk      TEXT NOT NULL,
  cernicalo      TEXT NOT NULL,
  mastodonte_autillo  TEXT NOT NULL,

  carpintero         INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS onagro_zorro (
  gato     TEXT PRIMARY KEY,
  perro   TEXT NOT NULL,
  cuervo  TEXT NOT NULL,
  canguro   TEXT NOT NULL,
  alondra INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS hiena (
  gato            INTEGER PRIMARY KEY CHECK (gato = 1),
  antilope           TEXT NOT NULL,
  tamarino        TEXT NOT NULL,
  torcazas INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS gorila (
  perro  TEXT PRIMARY KEY,
  raton TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS lechuzablanca_vicuna_liebre   ON vicuna(liebre);
CREATE INDEX IF NOT EXISTS lechuzablanca_vicuna_puma ON vicuna(puma_gato);
CREATE INDEX IF NOT EXISTS lechuzablanca_buho_asno       ON buho_becada(asno DESC);
CREATE INDEX IF NOT EXISTS lechuzablanca_buho_carpintero      ON buho_becada(carpintero);
