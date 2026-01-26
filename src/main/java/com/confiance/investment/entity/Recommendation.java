package com.confiance.investment.entity;

import com.confiance.common.enums.Market;
import com.confiance.common.enums.RecommendationStatus;
import com.confiance.common.enums.RecommendationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations", indexes = {
        @Index(name = "idx_ticker_symbol", columnList = "tickerSymbol"),
        @Index(name = "idx_market", columnList = "market"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_recommendation_date", columnList = "recommendationDate")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Market market;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 20)
    private String tickerSymbol;

    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationType tradeType;

    @Column(nullable = false)
    private LocalDate recommendationDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal entryPrice;

    @Column(precision = 19, scale = 2)
    private BigDecimal targetPrice;

    @Column(precision = 19, scale = 2)
    private BigDecimal stopLoss;

    // Calculated field: (targetPrice - entryPrice) / (entryPrice - stopLoss)
    @Column(precision = 5, scale = 2)
    private BigDecimal riskRewardRatio;

    @Column(precision = 19, scale = 2)
    private BigDecimal sellPrice;

    private LocalDate exitDate;

    private Integer holdingPeriodDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RecommendationStatus status = RecommendationStatus.OPEN;

    @Column(length = 1000)
    private String remarks;

    // Admin who created this recommendation
    private Long createdByUserId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void calculateFields() {
        // Calculate risk-reward ratio
        if (targetPrice != null && entryPrice != null && stopLoss != null) {
            BigDecimal reward = targetPrice.subtract(entryPrice);
            BigDecimal risk = entryPrice.subtract(stopLoss);
            if (risk.compareTo(BigDecimal.ZERO) > 0) {
                this.riskRewardRatio = reward.divide(risk, 2, java.math.RoundingMode.HALF_UP);
            }
        }

        // Calculate holding period if exitDate is set
        if (exitDate != null && recommendationDate != null) {
            this.holdingPeriodDays = (int) java.time.temporal.ChronoUnit.DAYS.between(recommendationDate, exitDate);
        }
    }
}
