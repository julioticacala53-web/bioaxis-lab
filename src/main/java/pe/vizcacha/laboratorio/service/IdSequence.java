package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Generacion de identificadores, regla literal de nextId() (§4.4):
 *
 *   nuevo = prefijo + ceros( max(0, sufijos numericos existentes) + 1 , 6 )
 *
 * El sufijo es el ultimo segmento tras partir por "-", que es lo que hace
 * funcionar a MUE-2026-000013.
 *
 * Nota sobre una inconsistencia deliberada: la semilla trae EQ-001 con 3
 * digitos, pero esta regla genera EQ-000006 con 6. Esta asi en el original y
 * se replica; corregirlo desalinearia los identificadores de la semilla
 * respecto a las capturas de la exposicion.
 */
@Service
public class IdSequence {

    public String next(String prefix, Collection<String> existingIds) {
        long max = 0;
        for (String id : existingIds) {
            max = Math.max(max, numericSuffix(id));
        }
        return prefix + String.format("%06d", max + 1);
    }

    private long numericSuffix(String id) {
        if (id == null || id.isBlank()) {
            return 0;
        }
        String last = id.substring(id.lastIndexOf('-') + 1);
        try {
            return Long.parseLong(last);
        } catch (NumberFormatException e) {
            return 0;   // Number("abc") || 0 en el original
        }
    }
}
