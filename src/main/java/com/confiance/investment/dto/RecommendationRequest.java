package com.confiance.investment.dto;

import com.confiance.common.enums.Market;
import com.confiance.common.enums.RecommendationStatus;
import com.confiance.common.enums.RecommendationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {

    @NotNull(message = "Market is required")
    private Market market;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Ticker symbol is required")
    private String tickerSymbol;

    private String companyName;

    @NotNull(message = "Trade type is required")
    private RecommendationType tradeType;

    private LocalDate recommendationDate; // Defaults to today if not provided

    @NotNull(message = "Entry price is required")
    @Positive(message = "Entry price must be positive")
    private BigDecimal entryPrice;

    @Positive(message = "Target price must be positive")
    private BigDecimal targetPrice;

    @Positive(message = "Stop loss must be positive")
    private BigDecimal stopLoss;

    private BigDecimal sellPrice;

    private LocalDate exitDate;

    private RecommendationStatus status;

    private String remarks;
}
