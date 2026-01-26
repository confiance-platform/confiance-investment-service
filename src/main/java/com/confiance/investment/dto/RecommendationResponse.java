package com.confiance.investment.dto;

import com.confiance.common.enums.Market;
import com.confiance.common.enums.RecommendationStatus;
import com.confiance.common.enums.RecommendationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {

    private Long id;
    private Market market;
    private String currency;
    private String tickerSymbol;
    private String companyName;
    private RecommendationType tradeType;
    private LocalDate recommendationDate;
    private BigDecimal entryPrice;
    private BigDecimal targetPrice;
    private BigDecimal stopLoss;
    private BigDecimal riskRewardRatio;
    private BigDecimal sellPrice;
    private LocalDate exitDate;
    private Integer holdingPeriodDays;
    private RecommendationStatus status;
    private String remarks;
    private Long createdByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Calculated fields for frontend display
    private BigDecimal potentialReturn;
    private BigDecimal potentialRisk;
    private BigDecimal potentialReturnPercentage;
}
