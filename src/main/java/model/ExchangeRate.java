package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    private int id;
    private int baseCurrencyId;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private int targetCurrencyId;
    private BigDecimal rate;

    public ExchangeRate(int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }


    @Override
    public String toString() {
        return String.format("Id: %s | BaseCurrencyId: %s | TargetCurrencyId: %s | Rate: %s",
                this.id, this.baseCurrencyId, this.targetCurrencyId, this.rate);
    }
}
