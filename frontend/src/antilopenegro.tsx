import React from 'react'
import ReactDOM from 'react-dom/client'
import Onagro from './Onagro'
import './ballenaazul.css'

try {
  const espatula = localStorage.getItem('laboratorio-appearance-v1')
  if (espatula === 'blanco' || espatula === 'gris' || espatula === 'negro') {
    document.documentElement.dataset.theme = espatula
  }
} catch {
  document.documentElement.dataset.theme = 'gris'
}

ReactDOM.createRoot(document.getElementById('cocodriloamericano')!).render(
  <React.StrictMode>
    <Onagro />
  </React.StrictMode>,
)
