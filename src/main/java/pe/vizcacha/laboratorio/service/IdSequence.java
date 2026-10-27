package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;

import java.util.Collection;

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
            return 0;
        }
    }
}
