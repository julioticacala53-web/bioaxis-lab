package pe.vizcacha.laboratorio.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Un parametro del formulario de resultados (§7).
 *
 * Tres formas posibles, como en el original: con rango numerico (min y max),
 * con lista de opciones (se dibuja como select), o texto libre (ninguna de
 * las dos). La unidad vacia es el guion largo "—", y los rangos de la
 * referencia usan el guion corto "–": son caracteres distintos y se copian
 * literales del prototipo.
 *
 * NON_EMPTY omite min, max y options cuando faltan, para que el JSON tenga
 * la forma EXACTA del original. No es cosmetico: el render decide el tipo de
 * campo con `param.options ?` y `param.min !== undefined`, y en JavaScript un
 * array vacio es truthy y null no es undefined. Con `options: []` todo
 * parametro numerico saldria como un desplegable vacio, y con `min: null` los
 * campos de texto libre saldrian como campos numericos.
 *
 * Cuidado al tocarlo: NON_DEFAULT omitiria tambien min=0, que es un rango
 * real en Eosinofilos, Basofilos, LDL y Colesterol total.
 */
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
