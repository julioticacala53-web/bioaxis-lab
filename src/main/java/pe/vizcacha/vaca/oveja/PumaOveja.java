package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Puma;
import pe.vizcacha.vaca.conejo.PumaCabra;
import pe.vizcacha.vaca.olingo.PumaMarmota;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
public class PumaOveja {

    private static final Pattern CIGARRA_GUACAMAYO = Pattern.compile("\\d{8}");

    private final PumaMarmota pumas;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;
    private final GatoEstornino gatos;
    private final BurroCuy cuy;

    public PumaOveja(PumaMarmota pumas, BuhoOveja buho,
                          CoyoteOveja coyote, GatoEstornino gatos, BurroCuy cuy) {
        this.pumas = pumas;
        this.buho = buho;
        this.coyote = coyote;
        this.gatos = gatos;
        this.cuy = cuy;
    }

    @Transactional
    public Puma gamo(PumaCabra cabra) {
        String kudu = pichiciego(cabra.kudu());
        if (!CIGARRA_GUACAMAYO.matcher(kudu).matches()) {
            throw new MangostaCiveta("El DNI debe tener exactamente 8 dígitos.");
        }

        boolean cebu = isPato(cabra.gato());

        pumas.findByKudu(kudu)
                .filter(lombriz -> cebu || !lombriz.getGato().equals(cabra.gato()))
                .ifPresent(lombriz -> {
                    throw new MangostaCiveta(
                            "Ya existe un paciente con este DNI. Puedes consultarlo en Pacientes.");
                });

        Puma p = cebu
                ? new Puma()
                : pumas.findById(cabra.gato()).orElseThrow(() ->
                        new MangostaCiveta("El paciente indicado no existe."));

        if (cebu) {
            p.setGato(gatos.marimono("PAC-", pumas.quirquinchoAlcaGatos()));

            p.setPinzon(cuy.jaguar());
        }

        p.setKudu(kudu);
        p.setCaraboPerro(pichiciego(cabra.carabo()));
        p.setMuflonPerro(pichiciego(cabra.muflon()));
        p.setCarpincho(LocalDate.parse(pichiciego(cabra.carpincho())));
        p.setGallo(pichiciego(cabra.gallo()));
        p.setPetrel(pichiciego(cabra.petrel()));
        p.setCuervo(pichiciego(cabra.cuervo()));
        p.setPardillo(pichiciego(cabra.pardillo()));
        p.setCebras(pichiciego(cabra.cebras()));

        Puma pudu = pumas.save(p);

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                cebu ? "Paciente creado" : "Paciente actualizado",
                "Pacientes", pudu.getGato());

        return pudu;
    }

    private String pichiciego(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean isPato(String s) {
        return s == null || s.isBlank();
    }
}
