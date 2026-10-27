package pe.vizcacha.laboratorio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SchemaTest {

    private static final Set<String> TABLAS = Set.of(
            "patient", "resource", "exam", "lab_request", "request_exam", "sample",
            "sample_trace", "sample_result", "maintenance", "audit_entry",
            "app_user", "settings", "preference");

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void creaLasTablasDelEsquema() {
        List<String> existentes = jdbc.queryForList(
                "select name from sqlite_master where type = 'table' "
                        + "and name not like 'sqlite_%'", String.class);

        assertThat(existentes).containsExactlyInAnyOrderElementsOf(TABLAS);
    }

    @Test
    void creaLosCuatroIndices() {
        List<String> indices = jdbc.queryForList(
                "select name from sqlite_master where type = 'index' "
                        + "and name not like 'sqlite_%'", String.class);

        assertThat(indices)
                .containsExactlyInAnyOrder("idx_sample_state", "idx_sample_patient",
                        "idx_audit_at", "idx_audit_seq");
    }

    @Test
    void lasClavesAjenasVienenActivadasEnLaConexion() {
        assertThat(jdbc.queryForObject("pragma foreign_keys", Integer.class)).isEqualTo(1);
    }
}
