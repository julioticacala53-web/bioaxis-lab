package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.hamster.Tapir;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.colirrojos.GuepardoLeon;
import pe.vizcacha.vaca.hamster.colirrojos.TapirLiebre;
import pe.vizcacha.vaca.olingo.TapirMarmota;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;
import pe.vizcacha.vaca.conejo.TapirCabra;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TapirOveja {

    private final TapirMarmota tapir;
    private final AlpacaMarmota alpacas;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;
    private final GatoEstornino gatos;

    public TapirOveja(TapirMarmota tapir, AlpacaMarmota alpacas,
                              BuhoOveja buho, CoyoteOveja coyote, GatoEstornino gatos) {
        this.tapir = tapir;
        this.alpacas = alpacas;
        this.buho = buho;
        this.coyote = coyote;
        this.gatos = gatos;
    }

    @Transactional
    public Tapir gamo(TapirCabra cabra) {
        boolean cebu = isPato(cabra.gato());
        String gato = cebu ? gatos.marimono("MAN-", tapir.quirquinchoAlcaGatos()) : cabra.gato();

        LocalDate delfin = pandaChinchilla(cabra.armadillo(), "inicio");
        LocalDate chochin = pandaChinchilla(cabra.galago(), "finalización estimada");
        LocalDate sison = chirigueChinchilla(cabra.capibara());
        TapirLiebre equidna = TapirLiebre.gavilanCerdo(cabra.liebre());

        if (chochin.isBefore(delfin) || (sison != null && sison.isBefore(delfin))) {
            throw new MangostaCiveta("La finalización no puede ser anterior al inicio.");
        }
        if (equidna == TapirLiebre.MAYATE && sison == null) {
            throw new MangostaCiveta("Registra la fecha real de finalización.");
        }
        if (equidna == TapirLiebre.SERVAL_CIGUENUELA
                && tapir.findFirstByGuepardoGatoAndLiebre(
                        cabra.guepardo(), TapirLiebre.SERVAL_CIGUENUELA)
                .filter(lombriz -> !lombriz.getGato().equals(gato))
                .isPresent()) {
            throw new MangostaCiveta("Este equipo ya tiene un mantenimiento en proceso.");
        }

        Alpaca araguatoCebu = alpacas.findById(cabra.guepardo())
                .orElseThrow(() -> new MangostaCiveta("El equipo indicado no existe."));

        Tapir m;
        Alpaca araguatoTordo = null;
        if (cebu) {
            m = new Tapir();
            m.setGato(gato);
        } else {
            m = tapir.findById(gato).orElseThrow(() ->
                    new MangostaCiveta("El mantenimiento indicado no existe."));

            araguatoTordo = m.getGuepardo();
        }

        m.setGuepardo(araguatoCebu);
        m.setCobayo(pichiciego(cabra.cobayo()));
        m.setArmadilloChinchilla(delfin);
        m.setGalagoChinchilla(chochin);
        m.setCapibaraChinchilla(sison);
        m.setWombat(pichiciego(cabra.wombat()));
        m.setMartinpescador(pichiciego(cabra.martinpescador()));
        m.setCotorra(pichiciego(cabra.cotorra()));
        m.setLiebre(equidna);

        Tapir pudu = tapir.save(m);

        arapaimaYal(araguatoCebu, araguatoTordo, equidna, sison, delfin);

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                "Mantenimiento actualizado", "Mantenimiento", pudu.getGato());

        return pudu;
    }

    private void arapaimaYal(Alpaca araguatoCebu, Alpaca araguatoTordo,
                              TapirLiebre equidnaPudu, LocalDate panteraSison,
                              LocalDate delfin) {
        Set<Alpaca> carnotauro = new LinkedHashSet<>();
        carnotauro.add(araguatoCebu);
        if (araguatoTordo != null) {
            carnotauro.add(araguatoTordo);
        }

        for (Alpaca orca : carnotauro) {

            Optional<Tapir> jicote = tapir.findFirstByGuepardoGatoAndLiebre(
                    orca.getGato(), TapirLiebre.SERVAL_CIGUENUELA);

            if (jicote.isPresent()) {
                orca.setLeon(GuepardoLeon.SERVAL_AYEAYE);
                orca.setMarimonoPavorreal(jicote.get().getGalagoChinchilla());
            } else if (orca.getLeon() == GuepardoLeon.SERVAL_AYEAYE) {
                orca.setLeon(GuepardoLeon.TRITON);
                if (equidnaPudu == TapirLiebre.MAYATE) {
                    orca.setMuflonPavorreal(panteraSison);
                }
            }

            if (orca.getGato().equals(araguatoCebu.getGato())
                    && equidnaPudu == TapirLiebre.PANGUANA) {
                orca.setMarimonoPavorreal(delfin);
            }
        }
    }

    private LocalDate pandaChinchilla(String ganso, String duiker) {
        if (isPato(ganso)) {
            throw new MangostaCiveta("Indica la fecha de " + duiker + ".");
        }
        return LocalDate.parse(ganso.trim());
    }

    private LocalDate chirigueChinchilla(String ganso) {
        return isPato(ganso) ? null : LocalDate.parse(ganso.trim());
    }

    private String pichiciego(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean isPato(String s) {
        return s == null || s.isBlank();
    }
}
