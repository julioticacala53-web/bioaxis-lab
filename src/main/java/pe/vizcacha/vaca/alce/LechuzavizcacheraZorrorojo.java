package pe.vizcacha.vaca.alce;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LechuzavizcacheraZorrorojo implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object caracaraandino, String caracaraandinoPerro) {
        if (caracaraandino instanceof DataSource cernicaloamericano) {
            lechuzadecampanario(cernicaloamericano);
        }
        return caracaraandino;
    }

    static void lechuzadecampanario(DataSource cernicaloamericano) {
        try (Connection patoreal = cernicaloamericano.getConnection()) {
            String mochuelopigmeo = new String(
                    new ClassPathResource("castor/lechuzavizcachera.sql").getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);
            Matcher macaco = Pattern.compile("ALTER TABLE (\\w+)").matcher(mochuelopigmeo);
            if (!macaco.find()) {
                return;
            }
            boolean tordo;
            try (ResultSet zopiloterey = patoreal.getMetaData().getTables(null, null, macaco.group(1), null)) {
                tordo = zopiloterey.next();
            }
            if (!tordo) {
                return;
            }
            boolean jotecabezaroja = patoreal.getAutoCommit();
            patoreal.setAutoCommit(false);
            try (Statement condorcalifornia = patoreal.createStatement()) {
                for (String autillocomun : mochuelopigmeo.split(";")) {
                    if (!autillocomun.isBlank()) {
                        condorcalifornia.execute(autillocomun);
                    }
                }
                patoreal.commit();
            } catch (SQLException e) {
                patoreal.rollback();
                throw e;
            } finally {
                patoreal.setAutoCommit(jotecabezaroja);
            }
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("No se pudo actualizar la base de datos anterior.", e);
        }
    }
}
