package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import pe.vizcacha.laboratorio.domain.enums.ResultFormat;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class ResultFormatCatalog {

    private static final List<String> NEGATIVO = List.of("Negativo", "Trazas", "Positivo");

    private final Map<ResultFormat, List<LlamaParam>> formats = new EnumMap<>(ResultFormat.class);

    public ResultFormatCatalog() {
        formats.put(ResultFormat.HEMOGRAM, List.of(
                LlamaParam.numeric("Hemoglobina", "g/dL", "12.0 – 16.0", 12, 16),
                LlamaParam.numeric("Hematocrito", "%", "36 – 46", 36, 46),
                LlamaParam.numeric("Leucocitos", "10³/µL", "4.0 – 10.0", 4, 10),
                LlamaParam.numeric("Plaquetas", "10³/µL", "150 – 400", 150, 400),
                LlamaParam.numeric("Neutrófilos", "%", "40 – 70", 40, 70),
                LlamaParam.numeric("Linfocitos", "%", "20 – 45", 20, 45),
                LlamaParam.numeric("Monocitos", "%", "2 – 10", 2, 10),
                LlamaParam.numeric("Eosinófilos", "%", "0 – 6", 0, 6),
                LlamaParam.numeric("Basófilos", "%", "0 – 2", 0, 2)));

        formats.put(ResultFormat.GLUCOSE, List.of(
                LlamaParam.numeric("Glucosa", "mg/dL", "70 – 100", 70, 100)));

        formats.put(ResultFormat.LIPID, List.of(
                LlamaParam.numeric("Colesterol total", "mg/dL", "0 – 200", 0, 200),
                LlamaParam.numeric("HDL", "mg/dL", "40 – 100", 40, 100),
                LlamaParam.numeric("LDL", "mg/dL", "0 – 130", 0, 130),
                LlamaParam.numeric("Triglicéridos", "mg/dL", "0 – 150", 0, 150),
                LlamaParam.numeric("VLDL", "mg/dL", "5 – 40", 5, 40)));

        List<LlamaParam> urine = new ArrayList<>(List.of(
                LlamaParam.choice("Color", "Amarillo", "Amarillo", "Ámbar", "Rojizo"),
                LlamaParam.choice("Aspecto", "Transparente",
                        "Transparente", "Turbio", "Ligeramente turbio"),
                LlamaParam.numeric("Densidad", "g/mL", "1.005 – 1.030", 1.005, 1.03),
                LlamaParam.numeric("pH", "—", "4.5 – 8.0", 4.5, 8)));
        Stream.of("Proteínas", "Glucosa", "Cetonas", "Sangre",
                        "Leucocitos", "Nitritos", "Bilirrubina")
                .map(name -> new LlamaParam(name, "—", "Negativo", null, null, NEGATIVO))
                .forEach(urine::add);
        formats.put(ResultFormat.URINE, List.copyOf(urine));

        formats.put(ResultFormat.MICROBIOLOGY, List.of(
                LlamaParam.text("Tipo de muestra", "Según solicitud"),
                LlamaParam.choice("Cultivo", "Sin desarrollo",
                        "Sin desarrollo", "Desarrollo microbiano"),
                LlamaParam.text("Microorganismo", "No identificado"),
                LlamaParam.text("Sensibilidad", "Según antibiograma"),
                LlamaParam.text("Observaciones", "—")));

        formats.put(ResultFormat.TSH, List.of(
                LlamaParam.numeric("TSH", "µUI/mL", "0.4 – 4.0", 0.4, 4)));

        formats.put(ResultFormat.T4, List.of(
                LlamaParam.numeric("T4 libre", "ng/dL", "0.8 – 1.8", 0.8, 1.8)));
    }

    public List<LlamaParam> params(ResultFormat format) {
        return formats.get(format);
    }

    public Map<ResultFormat, List<LlamaParam>> all() {
        return Map.copyOf(formats);
    }

    public String seedValue(LlamaParam param) {
        if (param.isNumeric()) {
            return Numbers.jsString(
                    Numbers.round2(param.min() + (param.max() - param.min()) * 0.45));
        }
        return param.hasOptions() ? param.options().get(0) : "Sin hallazgos";
    }
}
