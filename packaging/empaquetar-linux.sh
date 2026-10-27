#!/usr/bin/env bash
#
# Construye la aplicacion de escritorio para Linux.
#
#   ./packaging/empaquetar-linux.sh            # carpeta ejecutable
#   ./packaging/empaquetar-linux.sh rpm        # ademas, un paquete RPM
#
# Aqui si se usa jpackage, que es la herramienta de Java para esto: en Linux
# genera para Linux sin problema. Para Windows no valdria, porque jpackage no
# cruza plataformas; de ahi que ese paquete use launch4j.
#
# Resultado:
#   dist/linux/Laboratorio/        carpeta portatil, se ejecuta tal cual
#   dist/linux/*.rpm              paquete instalable (solo con "rpm")
#
# Donde guarda los datos: si se puede escribir junto al ejecutable, ahi
# mismo (modo portatil). Si no -- por ejemplo instalado en /opt --, en
# ~/.local/share/Laboratorio/. Lo decide la propia aplicacion.

set -euo pipefail

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CACHE="$RAIZ/packaging/.cache"
DESTINO="$RAIZ/dist/linux"
JDK="${JAVA_HOME:-$HOME/.jdks/jdk-21.0.12.1+1}"
VERSION="1.0.0"

MODULOS="java.base,java.desktop,java.instrument,java.logging,java.management"
MODULOS="$MODULOS,java.naming,java.net.http,java.prefs,java.scripting"
MODULOS="$MODULOS,java.security.jgss,java.sql,java.transaction.xa,java.xml"
MODULOS="$MODULOS,java.xml.crypto,jdk.crypto.ec,jdk.unsupported,jdk.zipfs"

paso() { printf '\n\033[1;36m==> %s\033[0m\n' "$1"; }

# ------------------------------------------------------------------ 1. jar

paso "Compilando (nativas de Linux)"
cd "$RAIZ"
JAVA_HOME="$JDK" mvn -q -B clean package -DskipTests

JAR="$RAIZ/target/laboratorio-clinico-$VERSION.jar"
[ -f "$JAR" ] || { echo "No se genero el jar"; exit 1; }

# -------------------------------------------------------------- 2. runtime

if [ ! -x "$CACHE/runtime-linux/bin/java" ]; then
  paso "Recortando el runtime con jlink"
  rm -rf "$CACHE/runtime-linux"
  "$JDK/bin/jlink" --module-path "$JDK/jmods" --add-modules "$MODULOS" \
    --output "$CACHE/runtime-linux" --strip-debug --no-header-files --no-man-pages
fi

# ---------------------------------------------- 3. despliegue de la aplicacion

paso "Desplegando el jar"
# Igual que en Windows: desplegado arranca ~1,5 s antes que empaquetado,
# porque Spring Boot no tiene que abrir 80 jars anidados.
rm -rf "$CACHE/app-linux"
"$JDK/bin/java" -Djarmode=tools -jar "$JAR" extract --destination "$CACHE/app-linux" >/dev/null
mv "$CACHE/app-linux/laboratorio-clinico-$VERSION.jar" "$CACHE/app-linux/laboratorio-clinico.jar"

# ------------------------------------------------------------- 4. jpackage

OPCIONES=(
  --name Laboratorio
  --app-version "$VERSION"
  --vendor "Laboratorio clinico"
  --description "Sistema de gestion de laboratorio clinico"
  --copyright "Laboratorio clinico"
  --input "$CACHE/app-linux"
  --main-jar laboratorio-clinico.jar
  --main-class pe.vizcacha.laboratorio.DesktopLauncher
  --runtime-image "$CACHE/runtime-linux"
  --icon "$RAIZ/packaging/Laboratorio-256.png"
  --java-options -XX:TieredStopAtLevel=1
  --java-options -XX:+UseSerialGC
  --java-options -Xms64m
  --java-options -Dfile.encoding=UTF-8
)

paso "Generando la carpeta ejecutable"
rm -rf "$DESTINO"
mkdir -p "$DESTINO"
"$JDK/bin/jpackage" --type app-image --dest "$DESTINO" "${OPCIONES[@]}"
cp "$RAIZ/packaging/LEEME-linux.txt" "$DESTINO/Laboratorio/" 2>/dev/null || true

if [ "${1:-}" = "rpm" ]; then
  paso "Generando el paquete RPM"
  "$JDK/bin/jpackage" --type rpm --dest "$DESTINO" "${OPCIONES[@]}" \
    --linux-shortcut \
    --linux-menu-group "Office;MedicalSoftware" \
    --linux-package-name laboratorio-clinico \
    --linux-app-category Office
fi

paso "Listo"
printf '  %s\n' "$DESTINO/Laboratorio"
du -sh "$DESTINO/Laboratorio" | awk '{print "  tamaño: "$1}'
ls "$DESTINO"/*.rpm 2>/dev/null | sed 's/^/  /' || true
printf '\n  Para ejecutarla:  %s/Laboratorio/bin/Laboratorio\n' "$DESTINO"
