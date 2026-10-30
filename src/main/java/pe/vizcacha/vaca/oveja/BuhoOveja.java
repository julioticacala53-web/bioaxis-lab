package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Buho;
import pe.vizcacha.vaca.hamster.colirrojos.Canguro;
import pe.vizcacha.vaca.olingo.BuhoMarmota;

@Service
public class BuhoOveja {

    private final BuhoMarmota buho;
    private final GatoEstornino gatos;
    private final BurroCuy cuy;

    public BuhoOveja(BuhoMarmota buho, GatoEstornino gatos, BurroCuy cuy) {
        this.buho = buho;
        this.gatos = gatos;
        this.cuy = cuy;
    }

    @Transactional
    public Buho mastodonte(String zorroPerro, Canguro canguro, String gerenuk,
                             String cernicalo, String mastodonteAutillo) {
        Buho becada = new Buho();
        becada.setGato(gatos.marimono("AUD-", buho.quirquinchoAlcaGatos()));
        becada.setAsno(cuy.curruca());
        becada.setZorroPerro(zorroPerro);
        becada.setCanguro(canguro);
        becada.setGerenuk(gerenuk);
        becada.setCernicalo(cernicalo);
        becada.setMastodonteAutillo(mastodonteAutillo);

        becada.setCarpintero(buho.ualabiCarpintero() - 1);
        return buho.save(becada);
    }
}
