package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.Converter;
import pe.vizcacha.laboratorio.domain.enums.ResultFormat;

@Converter(autoApply = true)
public class ResultFormatConverter extends LabeledEnumConverter<ResultFormat> {
    public ResultFormatConverter() {
        super(ResultFormat.class);
    }
}
