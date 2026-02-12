package it.spindox.stagelab.magazzino.Converter;
import it.spindox.stagelab.magazzino.entities.StockStatusProdotto;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// CONVERTER JPA PER LE ENTITIES


@Converter(autoApply = true)
public class StockStatusConverter
        implements AttributeConverter<StockStatusProdotto, String> {

    @Override
    public String convertToDatabaseColumn(StockStatusProdotto status) {
        return status != null ? status.getDbValue() : null;
    }

    @Override
    public StockStatusProdotto convertToEntityAttribute(String dbValue) {
        return StockStatusProdotto.fromDbValue(dbValue);
    }
}