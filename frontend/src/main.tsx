import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'

try {
  const tema = localStorage.getItem('laboratorio-appearance-v1')
  if (tema === 'blanco' || tema === 'gris' || tema === 'negro') {
    document.documentElement.dataset.theme = tema
  }
} catch {
  document.documentElement.dataset.theme = 'gris'
}

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
