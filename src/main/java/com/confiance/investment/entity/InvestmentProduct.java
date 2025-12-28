package com.confiance.investment.entity;

import com.confiance.common.enums.InvestmentStatus;
import com.confiance.common.enums.InvestmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "investment_products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class InvestmentProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentType type;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal expectedReturns;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal minInvestment;

    @Column(precision = 19, scale = 2)
    private BigDecimal maxInvestment;

    @Column(nullable = false)
    private Integer lockInPeriodMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentStatus status;

    @CreatedDate
    private LocalDateTime createdAt;
}