#!/usr/bin/env bash

set -euo pipefail

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CACHE="$RAIZ/packaging/.cache"
DESTINO="$RAIZ/dist/Laboratorio"

JDK_LOCAL="${JAVA_HOME:-$HOME/.jdks/jdk-21.0.12.1+1}"
VERSION_JDK="21.0.12.1+1"
VERSION_JDK_URL="jdk-21.0.12.1%2B1"

MODULOS="java.base,java.desktop,java.instrument,java.logging,java.management"
MODULOS="$MODULOS,java.naming,java.net.http,java.prefs,java.scripting"
MODULOS="$MODULOS,java.security.jgss,java.sql,java.transaction.xa,java.xml"
MODULOS="$MODULOS,java.xml.crypto,jdk.crypto.ec,jdk.unsupported,jdk.zipfs"

paso() { printf '\n\033[1;36m==> %s\033[0m\n' "$1"; }

paso "Compilando con las nativas de Windows (-Pwin)"
cd "$RAIZ"
JAVA_HOME="$JDK_LOCAL" mvn -q -B -Pwin clean package -DskipTests

JAR="$RAIZ/target/laboratorio-clinico-1.0.0.jar"
[ -f "$JAR" ] || { echo "No se genero el jar"; exit 1; }

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
  "$JDK_LOCAL/bin/jlink" --module-path "$JDK_WIN/jmods" --add-modules "$MODULOS" \
    --output "$CACHE/runtime-win" --strip-debug --no-header-files --no-man-pages
fi

if [ ! -f "$CACHE/launch4j/launch4j.jar" ]; then
  paso "Descargando launch4j (una sola vez)"
  curl -L --progress-bar -o "$CACHE/launch4j.tgz" \
    "https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/launch4j-3.50-linux-x64.tgz/download"
  tar -xzf "$CACHE/launch4j.tgz" -C "$CACHE"
fi

paso "Armando dist/Laboratorio"
rm -rf "$DESTINO"
mkdir -p "$DESTINO"

"$JDK_LOCAL/bin/java" -Djarmode=tools -jar "$JAR" extract --destination "$DESTINO/app" >/dev/null
mv "$DESTINO/app/laboratorio-clinico-1.0.0.jar" "$DESTINO/app/laboratorio-clinico.jar"

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
