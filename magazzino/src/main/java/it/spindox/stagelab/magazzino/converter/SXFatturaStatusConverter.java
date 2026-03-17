package it.spindox.stagelab.magazzino.converter;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class SXFatturaStatusConverter implements AttributeConverter<SXFatturaStatus, String> {

    @Override
    public String convertToDatabaseColumn(SXFatturaStatus attribute) {
        return attribute != null ? attribute.getDbValue() : null;
    }

    @Override
    public SXFatturaStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? SXFatturaStatus.fromDbValue(dbData) : null;
    }
}