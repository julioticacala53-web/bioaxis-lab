# Laboratorio clínico

Sistema de gestión de laboratorio clínico. Aplicación de escritorio para
Windows y Linux que funciona **entera en el equipo**: no necesita internet,
no consulta servicios externos y guarda todo en un archivo SQLite que viaja
junto al ejecutable.

Está pensado para **una sola PC y una persona** que registra lo que le
entregan los demás trabajadores: abre directamente en el Dashboard, sin
usuario, contraseña ni roles.

## Qué hace

| Módulo | Para qué sirve |
|---|---|
| Pacientes | Registro e historia de cada persona atendida |
| Solicitudes | Qué exámenes se piden y a quién |
| Muestras | Trazabilidad de extremo a extremo: recibida, en análisis, en validación, validada, publicada |
| Resultados | Ingreso de valores con rangos de referencia, validación, publicación e impresión del informe |
| Inventario | Equipos, reactivos y materiales, con stock, lotes y vencimientos |
| Mantenimiento | Intervenciones por equipo, con su efecto en la disponibilidad |
| Reportes | Actividad, exámenes solicitados y consumo, exportables a PDF y Excel |
| Auditoría | Qué se hizo, cuándo y sobre qué registro |
| Configuración | Datos del laboratorio, apariencia (Blanco clínico, Gris piedra, Oscuro carbón), áreas, parámetros y notificaciones |

Lo que ata todo es la **disponibilidad**: un examen solo se puede procesar si
su equipo está operativo y su reactivo y su material tienen stock. Comprar,
contar el inventario o poner un equipo en mantenimiento cambia al instante lo
que el laboratorio puede hacer.

## Cómo se ejecuta

**Windows** — copiar la carpeta `Laboratorio` completa y abrir `Laboratorio.exe`.
No se instala nada. Los datos quedan en `datos\`, dentro de esa misma
carpeta: si va en un USB, viaja con él.

**Linux** — entrar en `Laboratorio/bin` y abrir `Laboratorio` (doble clic, o
`./Laboratorio` en una terminal abierta en esa carpeta). Los datos quedan en
`bin/datos`, igual de portátil.

La primera vez el laboratorio arranca **vacío**. Desde la interfaz no se
puede vaciar el laboratorio ni borrar la auditoría: en *Configuración → General* está el **respaldo de
pacientes**, un CSV con cada paciente atendido, sus solicitudes, muestras,
historial de estados y resultados publicados. Ahí también se escribe el
nombre del responsable del laboratorio, que firma los informes y queda en la
auditoría. Para que las listas no se llenen con los años, *Respaldar y
eliminar atenciones antiguas* guarda primero un CSV con las atenciones ya
entregadas hace más de 1, 3, 6 o 12 meses y solo entonces las quita del
programa.

El informe de resultados de cada paciente se guarda en la carpeta que se
elija al pulsar *Guardar PDF…*: el programa la pregunta cada vez y recuerda
la última. Los reportes PDF y Excel también abren la ventana «Guardar como»,
que empieza en la carpeta de exportaciones.

## Cómo está hecho

```
React 19 + TypeScript        la interfaz
        ↓  HTTP, en localhost
Spring Boot 3.3 + JPA        toda la lógica de negocio
        ↓
SQLite                       un archivo, sin servidor
        ↓
JavaFX WebView               la ventana de escritorio
```

La interfaz se sirve desde el propio servicio, así que es la misma en el
navegador y dentro de la ventana. **Ninguna regla de negocio vive en el
frontend**: la disponibilidad, el estado derivado de un recurso y las alertas se calculan en Java y viajan ya resueltos, para que no existan
dos implementaciones de lo mismo.

### Compilar

Necesita **JDK 21** (con 25 falla Hibernate), Maven 3.9+ y Node 20+. El paso
a paso completo, con capturas, está en el documento *Para ejecutar*.

```bash
mvn package -DskipTests                       # interfaz + jar
java -jar target/laboratorio-clinico-1.0.0.jar

mvn test                                      # 134 pruebas
./packaging/empaquetar-windows.sh             # Laboratorio.exe
./packaging/empaquetar-linux.sh               # aplicación de Linux
```

## Nombres del dominio

Todos los nombres internos del código (paquetes, clases, métodos, variables,
rutas de la API, tablas, columnas, clases CSS y archivos) están formados por
nombres de animales: cada palabra corresponde siempre al mismo animal (por
ejemplo, «paciente» es `puma` y «servicio» es `oveja`, así que el servicio de
pacientes es `PumaOveja`). El diccionario completo está en el documento
*Código fuente*. No afecta a lo que se ve en pantalla. Las entidades son:

| Concepto | Nombre en el código | Código de registro |
|---|---|---|
| Paciente | `Puma` | `PAC-000001` |
| Solicitud | `Condor` | `SOL-000001` |
| Muestra | `Vicuna` | `MUE-2026-000001` |
| Examen | `Llama` | `EX-000001` |
| Equipo | `Alpaca` (familia Equipos) | `EQ-000001` |
| Reactivo | `Alpaca` (familia Reactivos) | `REA-000001` |
| Material | `Alpaca` (familia Materiales) | `MAT-000001` |
| Mantenimiento | `Tapir` | `MAN-000001` |
| Auditoría | `Buho` | `AUD-000001` |

Los códigos que ve el usuario son los de siempre: el nombre de animal vive
solo en el código fuente.

El correlativo de los tres tipos de recurso es compartido: el siguiente
recurso que se cree toma el número más alto de todos, no el de su familia.

## Límites, a propósito

- No se conecta a RENIEC, SUSALUD, analizadores ni a ningún servicio de salud.
- No emite diagnósticos: señala valores fuera del intervalo de referencia y
  nada más. Los informes impresos lo dicen en el pie.
- No muestra ni imprime valores de resultados que no estén publicados.
- Los registros de auditoría no se pueden borrar desde la interfaz.

## Documentación

En `docs/` están los tres documentos del proyecto, en Word y en PDF:

| Documento | Qué contiene |
|---|---|
| **Para ejecutar** | Requisitos del equipo, descarga, ejecución en Windows y en Linux, compilación desde el código, respaldo, actualización y errores frecuentes |
| **Manual de uso** | Cada pantalla, botón y opción del sistema, con capturas, y el recorrido completo de una atención |
| **Código fuente** | Arquitectura, piezas centrales del sistema y el índice de todos los archivos |

## Descarga

Los paquetes listos para usar están en la pestaña **Releases**:

- `Laboratorio-windows.zip`: descomprimir y abrir `Laboratorio.exe`.
- `Laboratorio-linux.zip`: descomprimir, entrar en `Laboratorio/bin` y abrir `Laboratorio`.

No necesitan instalar Java ni ningún otro programa.
