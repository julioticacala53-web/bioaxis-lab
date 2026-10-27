package pe.vizcacha.laboratorio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Entregable de la Fase 1: el esquema de §4.2 se crea completo al arrancar.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SchemaTest {

    /**
     * Las 12 del contrato -- y no las 11 que menciona su prosa, porque su
     * propio DDL define ademas request_exam, la tabla puente -- mas
     * `preference`, que guarda las preferencias del equipo (hoy, la carpeta
     * de exportaciones) y se anadio despues.
     */
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

    /**
     * foreign_keys es un PRAGMA por conexion: ponerlo solo en schema.sql lo
     * activaria en la conexion que ejecuta el script y en ninguna otra. Por
     * eso viaja en la URL del datasource, y por eso hay que comprobarlo.
     */
    @Test
    void lasClavesAjenasVienenActivadasEnLaConexion() {
        assertThat(jdbc.queryForObject("pragma foreign_keys", Integer.class)).isEqualTo(1);
    }
}
