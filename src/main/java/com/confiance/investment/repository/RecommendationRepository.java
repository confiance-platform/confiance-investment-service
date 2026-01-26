package com.confiance.investment.repository;

import com.confiance.common.enums.Market;
import com.confiance.common.enums.RecommendationStatus;
import com.confiance.investment.entity.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    Page<Recommendation> findByStatus(RecommendationStatus status, Pageable pageable);

    Page<Recommendation> findByMarket(Market market, Pageable pageable);

    Page<Recommendation> findByMarketAndStatus(Market market, RecommendationStatus status, Pageable pageable);

    List<Recommendation> findByTickerSymbol(String tickerSymbol);

    @Query("SELECT r FROM Recommendation r WHERE r.status = :status ORDER BY r.recommendationDate DESC")
    List<Recommendation> findActiveRecommendations(@Param("status") RecommendationStatus status);

    @Query("SELECT r FROM Recommendation r WHERE r.recommendationDate BETWEEN :startDate AND :endDate ORDER BY r.recommendationDate DESC")
    Page<Recommendation> findByDateRange(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          Pageable pageable);

    @Query("SELECT r FROM Recommendation r WHERE " +
           "(:market IS NULL OR r.market = :market) AND " +
           "(:status IS NULL OR r.status = :status) " +
           "ORDER BY r.recommendationDate DESC")
    Page<Recommendation> findWithFilters(@Param("market") Market market,
                                          @Param("status") RecommendationStatus status,
                                          Pageable pageable);
}
