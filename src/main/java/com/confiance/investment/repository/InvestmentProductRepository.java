package com.confiance.investment.repository;

import com.confiance.common.enums.InvestmentStatus;
import com.confiance.common.enums.InvestmentType;
import com.confiance.investment.entity.InvestmentProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentProductRepository extends JpaRepository<InvestmentProduct, Long> {
    Page<InvestmentProduct> findByType(InvestmentType type, Pageable pageable);
    Page<InvestmentProduct> findByStatus(InvestmentStatus status, Pageable pageable);
}