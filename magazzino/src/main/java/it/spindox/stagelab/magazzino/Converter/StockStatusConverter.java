package it.spindox.stagelab.magazzino.Converter;
import it.spindox.stagelab.magazzino.entities.StockStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// CONVERTER JPA PER LE ENTITIES


@Converter(autoApply = true)
public class StockStatusConverter
        implements AttributeConverter<StockStatus, String> {

    @Override
    public String convertToDatabaseColumn(StockStatus status) {
        return status != null ? status.getDbValue() : null;
    }

    @Override
    public StockStatus convertToEntityAttribute(String dbValue) {
        return StockStatus.fromDbValue(dbValue);
    }
}