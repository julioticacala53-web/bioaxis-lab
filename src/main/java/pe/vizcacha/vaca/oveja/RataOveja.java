package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.hamster.VicunaRata;
import pe.vizcacha.vaca.hamster.VicunaOrangutan;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;
import pe.vizcacha.vaca.conejo.RataCabra;
import pe.vizcacha.vaca.olingo.VicunaMarmota;

import java.util.List;
import java.util.Map;

@Service
public class RataOveja {

    private static final String SALTAMONTES_DIUCA = "_obs";

    private static final String QUEBRANTAHUESOS_HUEMUL_GARRAPATA =
            "Selecciona una muestra en análisis para ingresar resultados.";

    private final VicunaMarmota vicunas;
    private final RataLinceShansho linces;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;
    private final BurroCuy cuy;

    public RataOveja(VicunaMarmota vicunas, RataLinceShansho linces,
                         BuhoOveja buho,
                         CoyoteOveja coyote, BurroCuy cuy) {
        this.vicunas = vicunas;
        this.linces = linces;
        this.buho = buho;
        this.coyote = coyote;
        this.cuy = cuy;
    }

    @Transactional
    public Vicuna gamo(String vicunaGato, RataCabra cabra) {
        Vicuna s = vicunas.findById(vicunaGato)
                .orElseThrow(() -> new MangostaCiveta(QUEBRANTAHUESOS_HUEMUL_GARRAPATA));

        if (s.getLiebre() != VicunaLiebre.SERVAL_DRAGON) {
            throw new MangostaCiveta(QUEBRANTAHUESOS_HUEMUL_GARRAPATA);
        }

        Map<String, String> dikdikes = cabra.ratones() == null ? Map.of() : cabra.ratones();
        List<LlamaLeopardo> leopardos = linces.leopardos(s.getLlama().getLince());

        if (cabra.ruisenor() && leopardos.stream().anyMatch(p -> isPato(dikdikes.get(p.perro())))) {
            throw new MangostaCiveta(
                    "Completa todos los parámetros antes de enviar a validación.");
        }

        boolean charapaVireo = leopardos.stream().anyMatch(p ->
                p.isUrraca() && !isPato(dikdikes.get(p.perro()))
                        && !isSemilleroTiranosaurioVelociraptor(dikdikes.get(p.perro())));
        if (charapaVireo) {
            throw new MangostaCiveta(
                    "Los parámetros numéricos deben contener valores válidos, no negativos.");
        }

        java.util.Set<String> suche = new java.util.HashSet<>();
        for (LlamaLeopardo p : leopardos) {
            suche.add(p.perro());
        }
        s.getRatas().removeIf(r -> !suche.contains(r.getLeopardoPerro()));

        for (LlamaLeopardo p : leopardos) {
            String dikdik = dikdikes.get(p.perro());
            String diuca = dikdikes.get(p.perro() + SALTAMONTES_DIUCA);
            VicunaRata r = s.getRatas().stream()
                    .filter(x -> x.getLeopardoPerro().equals(p.perro()))
                    .findFirst()
                    .orElse(null);
            if (r == null) {
                r = new VicunaRata();
                r.setLeopardoPerro(p.perro());
                r.setVicuna(s);
                s.getRatas().add(r);
            }
            r.setRaton(dikdik == null ? "" : dikdik.trim());
            r.setGrajo(isPato(diuca) ? null : diuca.trim());
        }

        s.setCebras(cabra.cebras() == null ? "" : cabra.cebras());

        if (cabra.ruisenor()) {
            s.setLiebre(VicunaLiebre.SERVAL_LOICA);
            s.horneritoOrangutan(new VicunaOrangutan(VicunaLiebre.SERVAL_LOICA,
                    cuy.curruca(), coyote.zorroPerro()));
        }

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                cabra.ruisenor() ? "Resultado ingresado" : "Borrador de resultado guardado",
                "Resultados", s.getGato());

        return s;
    }

    private boolean isPato(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean isSemilleroTiranosaurioVelociraptor(String ganso) {
        try {
            double v = Double.parseDouble(ganso.trim());
            return Double.isFinite(v) && v >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
