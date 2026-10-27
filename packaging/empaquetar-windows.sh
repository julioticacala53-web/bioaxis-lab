#!/usr/bin/env bash
#
# Construye el paquete portatil de Windows: Laboratorio.exe con su runtime
# recortado y la interfaz dentro. Se ejecuta desde Linux; no hace falta
# Windows ni permisos de administrador.
#
#   ./packaging/empaquetar-windows.sh
#
# Resultado en dist/Laboratorio/. Para llevarlo al USB, se copia esa carpeta
# entera: el .exe busca runtime/ y app/ a su lado.
#
# Por que no jpackage, que es lo que propone la especificacion (§12.2):
# jpackage no cruza plataformas, solo genera instaladores para el sistema en
# el que corre. launch4j si lo hace, y produce un PE32 nativo de verdad.

set -euo pipefail

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CACHE="$RAIZ/packaging/.cache"
DESTINO="$RAIZ/dist/Laboratorio"

JDK_LOCAL="${JAVA_HOME:-$HOME/.jdks/jdk-21.0.12.1+1}"
VERSION_JDK="21.0.12.1+1"
VERSION_JDK_URL="jdk-21.0.12.1%2B1"

# Los modulos que necesita la aplicacion. La lista se valido arrancando el
# servicio completo con un runtime recortado identico: Spring, Hibernate,
# SQLite, Apache POI y OpenPDF incluidos. Si se anade una dependencia que
# falle con NoClassDefFoundError, el modulo que falta se anade aqui.
MODULOS="java.base,java.desktop,java.instrument,java.logging,java.management"
MODULOS="$MODULOS,java.naming,java.net.http,java.prefs,java.scripting"
MODULOS="$MODULOS,java.security.jgss,java.sql,java.transaction.xa,java.xml"
MODULOS="$MODULOS,java.xml.crypto,jdk.crypto.ec,jdk.unsupported,jdk.zipfs"

paso() { printf '\n\033[1;36m==> %s\033[0m\n' "$1"; }

# ---------------------------------------------------------------- 1. el jar

paso "Compilando con las nativas de Windows (-Pwin)"
cd "$RAIZ"
JAVA_HOME="$JDK_LOCAL" mvn -q -B -Pwin clean package -DskipTests

JAR="$RAIZ/target/laboratorio-clinico-1.0.0.jar"
[ -f "$JAR" ] || { echo "No se genero el jar"; exit 1; }

# ------------------------------------------------- 2. el runtime de Windows

mkdir -p "$CACHE"
JDK_WIN="$CACHE/jdk-win/jdk-$VERSION_JDK"

if [ ! -d "$JDK_WIN/jmods" ]; then
  paso "Descargando el JDK de Windows $VERSION_JDK (una sola vez, ~205 MB)"
  curl -L --progress-bar -o "$CACHE/jdk-windows.zip" \
    "https://api.adoptium.net/v3/binary/version/$VERSION_JDK_URL/windows/x64/jdk/hotspot/normal/eclipse"
  rm -rf "$CACHE/jdk-win"
  mkdir -p "$CACHE/jdk-win"
  unzip -q "$CACHE/jdk-windows.zip" -d "$CACHE/jdk-win"
fi

if [ ! -x "$CACHE/runtime-win/bin/java.exe" ]; then
  paso "Recortando el runtime de Windows con jlink"
  rm -rf "$CACHE/runtime-win"
  # jlink cruza de plataforma mientras los jmods sean de la misma version.
  "$JDK_LOCAL/bin/jlink" --module-path "$JDK_WIN/jmods" --add-modules "$MODULOS" \
    --output "$CACHE/runtime-win" --strip-debug --no-header-files --no-man-pages
fi

# ------------------------------------------------------------- 3. launch4j

if [ ! -f "$CACHE/launch4j/launch4j.jar" ]; then
  paso "Descargando launch4j (una sola vez)"
  curl -L --progress-bar -o "$CACHE/launch4j.tgz" \
    "https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/launch4j-3.50-linux-x64.tgz/download"
  tar -xzf "$CACHE/launch4j.tgz" -C "$CACHE"
fi

# ------------------------------------------------------------ 4. el paquete

paso "Armando dist/Laboratorio"
rm -rf "$DESTINO"
mkdir -p "$DESTINO"

# El jar se despliega en app/ en vez de dejarlo empaquetado: Spring Boot
# tiene que abrir cada jar anidado al arrancar, y evitarlo ahorra 1,5 s.
"$JDK_LOCAL/bin/java" -Djarmode=tools -jar "$JAR" extract --destination "$DESTINO/app" >/dev/null
mv "$DESTINO/app/laboratorio-clinico-1.0.0.jar" "$DESTINO/app/laboratorio-clinico.jar"

# Fuera las nativas de Linux: Maven las trae siempre, aunque el perfil pida
# las de Windows, y en este paquete son 48 MB que no se usan nunca.
rm -f "$DESTINO/app/lib"/javafx-*-linux.jar

cp -r "$CACHE/runtime-win" "$DESTINO/runtime"

paso "Generando el .exe"
"$JDK_LOCAL/bin/java" -jar "$CACHE/launch4j/launch4j.jar" "$RAIZ/packaging/launch4j.xml"

cp "$RAIZ/packaging/LEEME.txt" "$DESTINO/" 2>/dev/null || true

paso "Listo"
printf '  %s\n' "$DESTINO"
du -sh "$DESTINO" | awk '{print "  tamaño total: "$1}'
ls -la "$DESTINO/Laboratorio.exe" | awk '{print "  Laboratorio.exe: "$5" bytes"}'
file "$DESTINO/Laboratorio.exe" | sed 's/^/  /'
