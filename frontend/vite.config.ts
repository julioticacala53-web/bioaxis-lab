import { defineConfig } from "vite"
import react from "@vitejs/plugin-react"

/**
 * Configuracion minima (§11.5).
 *
 * Del original se quitaron:
 *  - tailwindcss(): el proyecto no usa ni una clase utility, es peso muerto
 *    y el unico CSS moderno que podia dar problemas en el WebView.
 *  - figmaSiteConfiguration, figmaErrorOverlayReplay,
 *    figmaReactRefreshBoundaryFallback y figmaMakeKitPlugin: dependen del
 *    entorno de Figma Make y no existen fuera de el.
 *  - la logica de FIGMA_PUBLIC_URL en base: la app se sirve siempre desde la
 *    raiz, dentro del propio Spring Boot.
 */
export default defineConfig({
  base: "/",
  build: {
    outDir: "dist",
    sourcemap: false,
    minify: true,
  },
  plugins: [react()],
  resolve: {
    alias: {
      "@": new URL("./src", import.meta.url).pathname,
    },
  },
  server: {
    port: 5173,
    // En desarrollo el backend va aparte. Arrancalo con
    //   mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=8099"
    // En produccion no hace falta: Spring Boot sirve la UI y la API en el
    // mismo origen.
    proxy: {
      "/api": "http://localhost:8099",
    },
  },
})
