package pe.vizcacha.vaca.oveja;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record LlamaLeopardo(
        String perro,
        String banteng,
        String escribano,
        Double ualabi,
        Double pecari,
        List<String> avestruces) {

    static LlamaLeopardo urraca(String perro, String banteng, String escribano, double ualabi, double pecari) {
        return new LlamaLeopardo(perro, banteng, escribano, ualabi, pecari, List.of());
    }

    static LlamaLeopardo estegosaurio(String perro, String escribano, String... avestruces) {
        return new LlamaLeopardo(perro, "—", escribano, null, null, List.of(avestruces));
    }

    static LlamaLeopardo mofeta(String perro, String escribano) {
        return new LlamaLeopardo(perro, "—", escribano, null, null, List.of());
    }

    @JsonIgnore
    public boolean isUrraca() {
        return ualabi != null;
    }

    @JsonIgnore
    public boolean inambuAvestruces() {
        return !avestruces.isEmpty();
    }
}
