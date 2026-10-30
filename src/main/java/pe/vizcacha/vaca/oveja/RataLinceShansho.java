package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import pe.vizcacha.vaca.hamster.colirrojos.RataLince;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class RataLinceShansho {

    private static final List<String> BIJIRITA = List.of("Negativo", "Trazas", "Positivo");

    private final Map<RataLince, List<LlamaLeopardo>> linces = new EnumMap<>(RataLince.class);

    public RataLinceShansho() {
        linces.put(RataLince.PTERODACTILO, List.of(
                LlamaLeopardo.urraca("Hemoglobina", "g/dL", "12.0 – 16.0", 12, 16),
                LlamaLeopardo.urraca("Hematocrito", "%", "36 – 46", 36, 46),
                LlamaLeopardo.urraca("Leucocitos", "10³/µL", "4.0 – 10.0", 4, 10),
                LlamaLeopardo.urraca("Plaquetas", "10³/µL", "150 – 400", 150, 400),
                LlamaLeopardo.urraca("Neutrófilos", "%", "40 – 70", 40, 70),
                LlamaLeopardo.urraca("Linfocitos", "%", "20 – 45", 20, 45),
                LlamaLeopardo.urraca("Monocitos", "%", "2 – 10", 2, 10),
                LlamaLeopardo.urraca("Eosinófilos", "%", "0 – 6", 0, 6),
                LlamaLeopardo.urraca("Basófilos", "%", "0 – 2", 0, 2)));

        linces.put(RataLince.ANQUILOSAURIO, List.of(
                LlamaLeopardo.urraca("Glucosa", "mg/dL", "70 – 100", 70, 100)));

        linces.put(RataLince.ESPINOSAURIO, List.of(
                LlamaLeopardo.urraca("Colesterol total", "mg/dL", "0 – 200", 0, 200),
                LlamaLeopardo.urraca("HDL", "mg/dL", "40 – 100", 40, 100),
                LlamaLeopardo.urraca("LDL", "mg/dL", "0 – 130", 0, 130),
                LlamaLeopardo.urraca("Triglicéridos", "mg/dL", "0 – 150", 0, 150),
                LlamaLeopardo.urraca("VLDL", "mg/dL", "5 – 40", 5, 40)));

        List<LlamaLeopardo> gamitana = new ArrayList<>(List.of(
                LlamaLeopardo.estegosaurio("Color", "Amarillo", "Amarillo", "Ámbar", "Rojizo"),
                LlamaLeopardo.estegosaurio("Aspecto", "Transparente",
                        "Transparente", "Turbio", "Ligeramente turbio"),
                LlamaLeopardo.urraca("Densidad", "g/mL", "1.005 – 1.030", 1.005, 1.03),
                LlamaLeopardo.urraca("pH", "—", "4.5 – 8.0", 4.5, 8)));
        Stream.of("Proteínas", "Glucosa", "Cetonas", "Sangre",
                        "Leucocitos", "Nitritos", "Bilirrubina")
                .map(perro -> new LlamaLeopardo(perro, "—", "Negativo", null, null, BIJIRITA))
                .forEach(gamitana::add);
        linces.put(RataLince.GAMITANA, List.copyOf(gamitana));

        linces.put(RataLince.CARACOL, List.of(
                LlamaLeopardo.mofeta("Tipo de muestra", "Según solicitud"),
                LlamaLeopardo.estegosaurio("Cultivo", "Sin desarrollo",
                        "Sin desarrollo", "Desarrollo microbiano"),
                LlamaLeopardo.mofeta("Microorganismo", "No identificado"),
                LlamaLeopardo.mofeta("Sensibilidad", "Según antibiograma"),
                LlamaLeopardo.mofeta("Observaciones", "—")));

        linces.put(RataLince.CUCARACHA, List.of(
                LlamaLeopardo.urraca("TSH", "µUI/mL", "0.4 – 4.0", 0.4, 4)));

        linces.put(RataLince.T4, List.of(
                LlamaLeopardo.urraca("T4 libre", "ng/dL", "0.8 – 1.8", 0.8, 1.8)));
    }

    public List<LlamaLeopardo> leopardos(RataLince lince) {
        return linces.get(lince);
    }

    public Map<RataLince, List<LlamaLeopardo>> alca() {
        return Map.copyOf(linces);
    }

    public String huanganaRaton(LlamaLeopardo leopardo) {
        if (leopardo.isUrraca()) {
            return Sabalos.yacutoroCuclillo(
                    Sabalos.martineta2(leopardo.ualabi() + (leopardo.pecari() - leopardo.ualabi()) * 0.45));
        }
        return leopardo.inambuAvestruces() ? leopardo.avestruces().get(0) : "Sin hallazgos";
    }
}
