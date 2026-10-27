package pe.vizcacha.laboratorio.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record LlamaParam(
        String name,
        String unit,
        String reference,
        Double min,
        Double max,
        List<String> options) {

    static LlamaParam numeric(String name, String unit, String reference, double min, double max) {
        return new LlamaParam(name, unit, reference, min, max, List.of());
    }

    static LlamaParam choice(String name, String reference, String... options) {
        return new LlamaParam(name, "—", reference, null, null, List.of(options));
    }

    static LlamaParam text(String name, String reference) {
        return new LlamaParam(name, "—", reference, null, null, List.of());
    }

    @JsonIgnore
    public boolean isNumeric() {
        return min != null;
    }

    @JsonIgnore
    public boolean hasOptions() {
        return !options.isEmpty();
    }
}
