package pe.vizcacha.laboratorio.service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.dto.ExportResultDto;
import pe.vizcacha.laboratorio.dto.ReportDto;
import pe.vizcacha.laboratorio.dto.ResultReportDto;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Exportaciones REALES a PDF y Excel.
 *
 * Esto es una mejora sobre el original, pedida por el autor: el prototipo
 * solo mostraba un toast que decia "simulada". Los tres textos del Anexo A
 * que lo anunciaban dejan de ser ciertos y hay que reescribirlos en el
 * frontend.
 *
 * El archivo se escribe en disco y se devuelve su ruta, en vez de servirlo
 * por HTTP: el WebView de JavaFX no tiene gestor de descargas y un enlace
 * fallaria en silencio en la aplicacion empaquetada. La carpeta es relativa
 * al directorio de trabajo, asi que en el paquete portatil queda en el USB,
 * junto al ejecutable y a datos/.
 */
@Service
public class ExportService {

    public static final String PDF = "pdf";
    public static final String XLSX = "xlsx";

    // La paleta de la aplicacion, para que el documento se reconozca como suyo.
    private static final Color FONDO_CABECERA = new Color(0x27, 0x33, 0x35);
    private static final Color TURQUESA = new Color(0x6f, 0x9e, 0x93);
    private static final Color GRIS_SUAVE = new Color(0xf2, 0xf6, 0xf5);
    /** Turquesa oscurecido: el de pantalla no se lee sobre papel blanco. */
    private static final Color TURQUESA_OSCURO = new Color(0x35, 0x68, 0x5f);
    private static final Color AMBAR = new Color(0x8a, 0x61, 0x00);

    private static final DateTimeFormatter FECHA_ES_PE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter SELLO_ARCHIVO =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final DemoClock clock;
    private final PreferenceService preferences;
    private final String exportDirPorDefecto;

    public ExportService(DemoClock clock, PreferenceService preferences,
                         @Value("${app.export-dir:exportaciones}") String exportDir) {
        this.clock = clock;
        this.preferences = preferences;
        this.exportDirPorDefecto = exportDir;
    }

    /**
     * Donde se escriben los archivos.
     *
     * Se consulta en cada exportacion, no una vez al arrancar: el usuario
     * puede cambiar la carpeta desde Configuracion y lo normal es que espere
     * que el siguiente PDF ya salga alli, sin reiniciar nada.
     */
    public Path carpeta() {
        return Paths.get(preferences.get(
                pe.vizcacha.laboratorio.domain.Preference.CARPETA_EXPORTACIONES,
                exportDirPorDefecto));
    }

    public ExportResultDto export(ReportDto report, String format, String lab, String branch) {
        String formato = formato(format);
        return exportTo(report, formato, lab, branch, carpeta().resolve(fileName(report, formato)));
    }

    public String formato(String format) {
        String formato = format == null ? PDF : format.trim().toLowerCase(Locale.ROOT);
        if (!PDF.equals(formato) && !XLSX.equals(formato)) {
            throw new BusinessException("Formato de exportación no soportado: " + format);
        }
        return formato;
    }

    public String reportFileName(ReportDto report, String format) {
        return fileName(report, formato(format));
    }

    public ExportResultDto exportTo(ReportDto report, String format, String lab, String branch,
                                    Path archivo) {
        String formato = formato(format);
        Path carpeta = archivo.toAbsolutePath().getParent();
        try {
            Files.createDirectories(carpeta);
            try (OutputStream out = Files.newOutputStream(archivo)) {
                if (PDF.equals(formato)) {
                    writePdf(report, lab, branch, out);
                } else {
                    writeXlsx(report, lab, branch, out);
                }
            }
            return new ExportResultDto(archivo.getFileName().toString(),
                    archivo.toAbsolutePath().toString(), Files.size(archivo));

        } catch (IOException e) {
            throw new BusinessException("No se pudo escribir el archivo: " + e.getMessage());
        }
    }

    /**
     * El informe imprimible de un resultado publicado, en PDF.
     *
     * Comparte formato con los reportes para que todo lo que sale de la
     * aplicacion se reconozca como suyo.
     */
    public ExportResultDto exportResult(ResultReportDto informe) {
        return exportResultTo(informe, carpeta().resolve(resultFileName(informe)));
    }

    public String resultFileName(ResultReportDto informe) {
        return "Resultado-" + informe.reportNumber() + ".pdf";
    }

    public byte[] resultPdf(ResultReportDto informe) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        writeResultPdf(informe, out);
        return out.toByteArray();
    }

    public ExportResultDto exportResultTo(ResultReportDto informe, Path archivo) {
        Path carpeta = archivo.toAbsolutePath().getParent();

        try {
            Files.createDirectories(carpeta);
            try (OutputStream out = Files.newOutputStream(archivo)) {
                writeResultPdf(informe, out);
            }
            return new ExportResultDto(archivo.getFileName().toString(),
                    archivo.toAbsolutePath().toString(), Files.size(archivo));

        } catch (IOException e) {
            throw new BusinessException("No se pudo escribir el informe: " + e.getMessage());
        }
    }

    private void writeResultPdf(ResultReportDto r, OutputStream out) {
        Document doc = new Document(PageSize.A4, 42, 42, 40, 54);
        PdfWriter writer = PdfWriter.getInstance(doc, out);
        writer.setPageEvent(new PieDeInforme(r));
        doc.addTitle("Informe de laboratorio · " + r.reportNumber());
        doc.addCreator(r.lab());
        doc.open();

        doc.add(encabezado(r));
        doc.add(tituloInforme());
        doc.add(datosEnDosPaneles(r));
        doc.add(bandaDeExamen(r));
        doc.add(tablaDeResultados(r));
        doc.add(leyendaDeMarcas(r));
        doc.add(observaciones(r));
        doc.add(firmas(r));

        doc.close();
    }

    // ------------------------------------------------------- piezas del informe

    private Font f(String familia, float tam, Color color) {
        return FontFactory.getFont(familia, tam, color);
    }

    /** Membrete: identidad del laboratorio y número de informe. */
    private PdfPTable encabezado(ResultReportDto r) {
        PdfPTable tabla = new PdfPTable(new float[] {6f, 4f});
        tabla.setWidthPercentage(100);

        PdfPCell marca = new PdfPCell();
        marca.setBorder(Rectangle.NO_BORDER);
        marca.addElement(new Paragraph(r.lab().toUpperCase(Locale.ROOT),
                f(FontFactory.HELVETICA_BOLD, 17, FONDO_CABECERA)));
        Paragraph sede = new Paragraph(r.branch(),
                f(FontFactory.HELVETICA, 8, Color.DARK_GRAY));
        sede.setSpacingBefore(2);
        marca.addElement(sede);
        tabla.addCell(marca);

        PdfPCell numero = new PdfPCell();
        numero.setBorder(Rectangle.NO_BORDER);
        numero.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph etiqueta = new Paragraph("INFORME N.º",
                f(FontFactory.HELVETICA, 7, Color.GRAY));
        etiqueta.setAlignment(Element.ALIGN_RIGHT);
        numero.addElement(etiqueta);
        Paragraph valor = new Paragraph(r.reportNumber(),
                f(FontFactory.COURIER_BOLD, 12, FONDO_CABECERA));
        valor.setAlignment(Element.ALIGN_RIGHT);
        numero.addElement(valor);
        tabla.addCell(numero);

        // Regla de la marca bajo el membrete.
        PdfPCell regla = new PdfPCell();
        regla.setColspan(2);
        regla.setFixedHeight(2.4f);
        regla.setBackgroundColor(TURQUESA);
        regla.setBorder(Rectangle.NO_BORDER);
        regla.setPaddingTop(8);
        tabla.addCell(regla);

        return tabla;
    }

    private Paragraph tituloInforme() {
        Paragraph titulo = new Paragraph("INFORME DE RESULTADOS DE LABORATORIO",
                f(FontFactory.HELVETICA_BOLD, 11, FONDO_CABECERA));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingBefore(16);
        titulo.setSpacingAfter(14);
        return titulo;
    }

    /** Paciente a la izquierda, muestra a la derecha, los dos enmarcados. */
    private PdfPTable datosEnDosPaneles(ResultReportDto r) {
        PdfPTable contenedor = new PdfPTable(2);
        contenedor.setWidthPercentage(100);
        contenedor.setSpacingAfter(16);

        contenedor.addCell(panel("DATOS DEL PACIENTE", new String[][] {
                {"Nombre", r.patientName()},
                {"Documento", r.patientDni()},
                {"Fecha de nacimiento", r.patientBirth()},
                {"Edad / Sexo", r.patientAge() + " años · " + r.patientSex()},
        }));
        contenedor.addCell(panel("DATOS DE LA MUESTRA", new String[][] {
                {"Código", r.sampleId()},
                {"Tipo de muestra", r.sampleType()},
                {"Fecha de toma", r.takenAt()},
                {"Fecha de emisión", r.issuedAt()},
        }));
        return contenedor;
    }

    private PdfPCell panel(String titulo, String[][] pares) {
        PdfPCell celda = new PdfPCell();
        celda.setBorderWidth(0.6f);
        celda.setBorderColor(new Color(0xc8, 0xd4, 0xd3));
        celda.setPadding(10);

        Paragraph cabecera = new Paragraph(titulo,
                f(FontFactory.HELVETICA_BOLD, 7, TURQUESA_OSCURO));
        cabecera.setSpacingAfter(7);
        celda.addElement(cabecera);

        PdfPTable datos = new PdfPTable(new float[] {4.2f, 6f});
        datos.setWidthPercentage(100);
        for (String[] par : pares) {
            datos.addCell(celdaDato(par[0] + ":", f(FontFactory.HELVETICA, 8, Color.GRAY)));
            datos.addCell(celdaDato(par[1], f(FontFactory.HELVETICA_BOLD, 8.5f, Color.BLACK)));
        }
        celda.addElement(datos);
        return celda;
    }

    private PdfPCell celdaDato(String texto, Font fuente) {
        PdfPCell c = new PdfPCell(new Phrase(texto, fuente));
        c.setBorder(Rectangle.NO_BORDER);
        c.setPaddingBottom(3.5f);
        c.setPaddingTop(0);
        return c;
    }

    /** Banda con el area y el examen, como separador de seccion. */
    private PdfPTable bandaDeExamen(ResultReportDto r) {
        PdfPTable banda = new PdfPTable(1);
        banda.setWidthPercentage(100);
        banda.setSpacingAfter(0);

        PdfPCell celda = new PdfPCell(new Phrase(
                r.examCategory().toUpperCase(Locale.ROOT) + "  ·  "
                        + r.examName().toUpperCase(Locale.ROOT),
                f(FontFactory.HELVETICA_BOLD, 8.5f, Color.WHITE)));
        celda.setBackgroundColor(FONDO_CABECERA);
        celda.setPadding(8);
        celda.setBorder(Rectangle.NO_BORDER);
        banda.addCell(celda);
        return banda;
    }

    private PdfPTable tablaDeResultados(ResultReportDto r) {
        PdfPTable tabla = new PdfPTable(new float[] {3f, 1.9f, 1.2f, 2.2f, 3f});
        tabla.setWidthPercentage(100);

        Font cabecera = f(FontFactory.HELVETICA_BOLD, 7, Color.GRAY);
        for (String c : new String[] {"PARÁMETRO", "RESULTADO", "UNIDAD",
                "VALORES DE REFERENCIA", "OBSERVACIÓN"}) {
            PdfPCell celda = new PdfPCell(new Phrase(c, cabecera));
            celda.setBorder(Rectangle.BOTTOM);
            celda.setBorderWidthBottom(1.1f);
            celda.setBorderColorBottom(FONDO_CABECERA);
            celda.setPadding(7);
            celda.setPaddingTop(10);
            tabla.addCell(celda);
        }
        tabla.setHeaderRows(1);

        Font normal = f(FontFactory.HELVETICA, 9, Color.BLACK);
        Font tenue = f(FontFactory.HELVETICA, 8.5f, new Color(0x4a, 0x58, 0x57));
        Font valorNormal = f(FontFactory.HELVETICA_BOLD, 9.5f, Color.BLACK);
        Font valorDesviado = f(FontFactory.HELVETICA_BOLD, 9.5f, AMBAR);
        Font marcaDesviada = f(FontFactory.HELVETICA_BOLD, 7, AMBAR);

        for (ResultReportDto.Row fila : r.rows()) {
            Phrase resultado = new Phrase();
            resultado.add(new Chunk(fila.value(),
                    fila.outOfRange() ? valorDesviado : valorNormal));
            if (!fila.flag().isEmpty()) {
                resultado.add(new Chunk("   " + fila.flag(), marcaDesviada));
            }

            tabla.addCell(celdaResultado(new Phrase(fila.name(), normal)));
            tabla.addCell(celdaResultado(resultado));
            tabla.addCell(celdaResultado(new Phrase(fila.unit(), tenue)));
            tabla.addCell(celdaResultado(new Phrase(fila.reference(), tenue)));
            tabla.addCell(celdaResultado(new Phrase(fila.observation(), tenue)));
        }
        return tabla;
    }

    private PdfPCell celdaResultado(Phrase contenido) {
        PdfPCell celda = new PdfPCell(contenido);
        celda.setBorder(Rectangle.BOTTOM);
        celda.setBorderWidthBottom(0.4f);
        celda.setBorderColorBottom(new Color(0xdd, 0xe5, 0xe4));
        celda.setPadding(7);
        return celda;
    }

    private Paragraph leyendaDeMarcas(ResultReportDto r) {
        Paragraph leyenda = new Paragraph(r.outOfRange().isEmpty()
                ? "Todos los parámetros se encuentran dentro del intervalo de referencia."
                : "ALTO / BAJO: valor fuera del intervalo de referencia configurado. "
                        + "Este indicador señala una desviación y no constituye un "
                        + "diagnóstico médico.",
                f(FontFactory.HELVETICA_OBLIQUE, 7.5f,
                        r.outOfRange().isEmpty() ? Color.GRAY : AMBAR));
        leyenda.setSpacingBefore(8);
        return leyenda;
    }

    private PdfPTable observaciones(ResultReportDto r) {
        PdfPTable tabla = new PdfPTable(1);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(16);

        PdfPCell titulo = new PdfPCell(new Phrase("OBSERVACIONES GENERALES",
                f(FontFactory.HELVETICA_BOLD, 7, TURQUESA_OSCURO)));
        titulo.setBorder(Rectangle.NO_BORDER);
        titulo.setPaddingBottom(4);
        tabla.addCell(titulo);

        PdfPCell texto = new PdfPCell(new Phrase(r.notes(),
                f(FontFactory.HELVETICA, 9, Color.BLACK)));
        texto.setBorder(Rectangle.TOP);
        texto.setBorderWidthTop(0.6f);
        texto.setBorderColorTop(new Color(0xc8, 0xd4, 0xd3));
        texto.setPadding(8);
        texto.setMinimumHeight(30);
        tabla.addCell(texto);
        return tabla;
    }

    /** Tres firmas con su linea, como en un informe en papel. */
    private PdfPTable firmas(ResultReportDto r) {
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(34);

        String[][] firmas = {
                {r.enteredBy(), "Ingreso de resultados"},
                {r.validatedBy(), "Validación técnica"},
                {r.publishedBy(), "Publicación · " + r.publishedAt()},
        };
        for (String[] firma : firmas) {
            PdfPCell celda = new PdfPCell();
            celda.setBorder(Rectangle.TOP);
            celda.setBorderWidthTop(0.6f);
            celda.setBorderColorTop(new Color(0x9a, 0xaa, 0xa9));
            celda.setPaddingTop(6);
            celda.setPaddingLeft(10);
            celda.setPaddingRight(10);

            Paragraph nombre = new Paragraph(firma[0],
                    f(FontFactory.HELVETICA_BOLD, 8.5f, Color.BLACK));
            nombre.setAlignment(Element.ALIGN_CENTER);
            celda.addElement(nombre);

            Paragraph rol = new Paragraph(firma[1],
                    f(FontFactory.HELVETICA, 7, Color.GRAY));
            rol.setAlignment(Element.ALIGN_CENTER);
            celda.addElement(rol);

            tabla.addCell(celda);
        }
        return tabla;
    }

    /**
     * Pie de pagina repetido: aviso legal y numeracion. Va como evento de
     * pagina para que aparezca en todas, no solo al final del flujo.
     */
    private final class PieDeInforme extends PdfPageEventHelper {

        private final ResultReportDto informe;

        private PieDeInforme(ResultReportDto informe) {
            this.informe = informe;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            Font legal = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Color.GRAY);
            PdfPTable pie = new PdfPTable(new float[] {8f, 2f});
            pie.setTotalWidth(document.right() - document.left());

            PdfPCell aviso = new PdfPCell(new Phrase(informe.disclaimer(), legal));
            aviso.setBorder(Rectangle.TOP);
            aviso.setBorderWidthTop(0.5f);
            aviso.setBorderColorTop(new Color(0xd7, 0xde, 0xdd));
            aviso.setPaddingTop(5);
            pie.addCell(aviso);

            PdfPCell pagina = new PdfPCell(new Phrase(
                    "Página " + writer.getPageNumber(), legal));
            pagina.setBorder(Rectangle.TOP);
            pagina.setBorderWidthTop(0.5f);
            pagina.setBorderColorTop(new Color(0xd7, 0xde, 0xdd));
            pagina.setPaddingTop(5);
            pagina.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pie.addCell(pagina);

            pie.writeSelectedRows(0, -1, document.left(),
                    document.bottom() - 6, writer.getDirectContent());
        }
    }

    private String fileName(ReportDto report, String formato) {
        return "Reporte-" + slug(report.type()) + "-"
                + clock.today().atTime(LocalTime.now().withNano(0)).format(SELLO_ARCHIVO)
                + "." + formato;
    }

    /** Sin tildes ni espacios: el nombre del archivo viaja a Windows. */
    private String slug(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    // ------------------------------------------------------------------- PDF

    private void writePdf(ReportDto report, String lab, String branch, OutputStream out) {
        // Horizontal cuando hay 4 columnas o mas: los reportes de consumo
        // llevan la accion auditada completa y no cabe en vertical.
        Document doc = new Document(
                report.columns().size() >= 4 ? PageSize.A4.rotate() : PageSize.A4,
                36, 36, 40, 40);
        PdfWriter.getInstance(doc, out);

        doc.addTitle(report.type() + " · " + lab);
        doc.addCreator(lab);
        doc.open();

        Font fuenteMarca = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, FONDO_CABECERA);
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, FONDO_CABECERA);
        Font fuenteDato = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.DARK_GRAY);
        Font fuentePie = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7, Color.GRAY);

        Paragraph marca = new Paragraph(lab.toUpperCase(Locale.ROOT), fuenteMarca);
        marca.setSpacingAfter(2);
        doc.add(marca);

        Paragraph sub = new Paragraph(branch, fuenteDato);
        sub.setSpacingAfter(12);
        doc.add(sub);

        Paragraph titulo = new Paragraph(report.type(), fuenteTitulo);
        titulo.setSpacingAfter(2);
        doc.add(titulo);

        Paragraph meta = new Paragraph("Período: " + report.range()
                + " · del " + fecha(report.from()) + " al " + fecha(report.to())
                + " · generado el " + fecha(clock.todayIso()), fuenteDato);
        meta.setSpacingAfter(10);
        doc.add(meta);

        doc.add(tabla(report));

        Paragraph pie = new Paragraph(
                "Prototipo académico · Información ficticia · "
                        + "No constituye un documento clínico ni un diagnóstico médico.",
                fuentePie);
        pie.setSpacingBefore(12);
        doc.add(pie);

        doc.close();
    }

    private PdfPTable tabla(ReportDto report) {
        PdfPTable tabla = new PdfPTable(report.columns().size());
        tabla.setWidthPercentage(100);

        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, TURQUESA);
        Font fuenteCelda = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.BLACK);

        for (String columna : report.columns()) {
            PdfPCell celda = new PdfPCell(new Phrase(columna.toUpperCase(Locale.ROOT),
                    fuenteEncabezado));
            celda.setBackgroundColor(FONDO_CABECERA);
            celda.setPadding(6);
            celda.setBorderWidth(0);
            tabla.addCell(celda);
        }
        tabla.setHeaderRows(1);

        boolean alterna = false;
        for (List<String> fila : report.rows()) {
            for (String valor : fila) {
                PdfPCell celda = new PdfPCell(new Phrase(valor, fuenteCelda));
                celda.setPadding(5);
                celda.setBorderWidth(0.3f);
                celda.setBorderColor(new Color(0xdd, 0xe5, 0xe4));
                if (alterna) {
                    celda.setBackgroundColor(GRIS_SUAVE);
                }
                tabla.addCell(celda);
            }
            alterna = !alterna;
        }

        if (report.rows().isEmpty()) {
            PdfPCell vacia = new PdfPCell(new Phrase("Sin datos en el período seleccionado.",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY)));
            vacia.setColspan(report.columns().size());
            vacia.setPadding(12);
            vacia.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(vacia);
        }

        return tabla;
    }

    private String fecha(String iso) {
        return iso == null || iso.isBlank()
                ? "—"
                : java.time.LocalDate.parse(iso).format(FECHA_ES_PE);
    }

    // ------------------------------------------------------------------ XLSX

    private void writeXlsx(ReportDto report, String lab, String branch, OutputStream out)
            throws IOException {
        try (XSSFWorkbook libro = new XSSFWorkbook()) {
            // El nombre de una hoja no admite mas de 31 caracteres.
            String nombreHoja = report.type().length() > 31
                    ? report.type().substring(0, 31)
                    : report.type();
            Sheet hoja = libro.createSheet(nombreHoja);

            CellStyle estiloEncabezado = libro.createCellStyle();
            org.apache.poi.ss.usermodel.Font negrita = libro.createFont();
            negrita.setBold(true);
            negrita.setColor(IndexedColors.WHITE.getIndex());
            estiloEncabezado.setFont(negrita);
            estiloEncabezado.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
            estiloEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            estiloEncabezado.setBorderBottom(BorderStyle.THIN);
            estiloEncabezado.setAlignment(HorizontalAlignment.LEFT);

            int fila = 0;

            Row cabecera = hoja.createRow(fila++);
            cabecera.createCell(0).setCellValue(lab + " · " + branch);

            Row metadatos = hoja.createRow(fila++);
            metadatos.createCell(0).setCellValue(report.type()
                    + " · Período: " + report.range()
                    + " · del " + fecha(report.from()) + " al " + fecha(report.to()));

            fila++;   // una fila en blanco antes de la tabla

            Row encabezados = hoja.createRow(fila++);
            for (int c = 0; c < report.columns().size(); c++) {
                var celda = encabezados.createCell(c);
                celda.setCellValue(report.columns().get(c));
                celda.setCellStyle(estiloEncabezado);
            }

            for (List<String> datos : report.rows()) {
                Row r = hoja.createRow(fila++);
                for (int c = 0; c < datos.size(); c++) {
                    r.createCell(c).setCellValue(datos.get(c));
                }
            }

            hoja.createRow(fila + 1).createCell(0)
                    .setCellValue("Prototipo académico · Información ficticia.");

            for (int c = 0; c < report.columns().size(); c++) {
                hoja.autoSizeColumn(c);
            }

            libro.write(out);
        }
    }
}
