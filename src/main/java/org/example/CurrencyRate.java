package org.example;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class CurrencyRate {

    private LocalDate date;
    private String currency;
    private BigDecimal rate;

    @Override
    public String toString() {
        return "{" +
                "date:" + date +
                ", currency:" + currency +
                ", rate:" + rate +
                '}';
    }
}
