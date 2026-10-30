import { defineConfig } from "vite"
import react from "@vitejs/plugin-react"

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

    proxy: {
      "/elefante": "http://localhost:8099",
    },
  },
})
