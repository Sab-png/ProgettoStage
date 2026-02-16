package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class InvalidStockStatusValueException extends ProdottoException{
    public InvalidStockStatusValueException(String dbValue) {
        super("StockStatusProdotto non riconosciuto: " + dbValue);
    }
}