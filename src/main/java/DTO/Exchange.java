package DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Currency;

import java.math.BigDecimal;
@Setter
@Getter
@NoArgsConstructor
public class Exchange {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
