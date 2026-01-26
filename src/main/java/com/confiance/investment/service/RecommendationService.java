package com.confiance.investment.service;

import com.confiance.common.dto.PageResponse;
import com.confiance.common.enums.Market;
import com.confiance.common.enums.RecommendationStatus;
import com.confiance.common.exception.ResourceNotFoundException;
import com.confiance.investment.dto.RecommendationRequest;
import com.confiance.investment.dto.RecommendationResponse;
import com.confiance.investment.entity.Recommendation;
import com.confiance.investment.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Transactional
    public RecommendationResponse createRecommendation(RecommendationRequest request, Long userId) {
        log.info("Creating recommendation for ticker: {} by user: {}", request.getTickerSymbol(), userId);

        Recommendation recommendation = Recommendation.builder()
                .market(request.getMarket())
                .currency(request.getCurrency())
                .tickerSymbol(request.getTickerSymbol().toUpperCase())
                .companyName(request.getCompanyName())
                .tradeType(request.getTradeType())
                .recommendationDate(request.getRecommendationDate() != null ?
                        request.getRecommendationDate() : LocalDate.now())
                .entryPrice(request.getEntryPrice())
                .targetPrice(request.getTargetPrice())
                .stopLoss(request.getStopLoss())
                .sellPrice(request.getSellPrice())
                .exitDate(request.getExitDate())
                .status(request.getStatus() != null ? request.getStatus() : RecommendationStatus.OPEN)
                .remarks(request.getRemarks())
                .createdByUserId(userId)
                .build();

        Recommendation saved = recommendationRepository.save(recommendation);
        return toResponse(saved);
    }

    @Transactional
    public RecommendationResponse updateRecommendation(Long id, RecommendationRequest request) {
        log.info("Updating recommendation: {}", id);

        Recommendation recommendation = findById(id);

        if (request.getMarket() != null) recommendation.setMarket(request.getMarket());
        if (request.getCurrency() != null) recommendation.setCurrency(request.getCurrency());
        if (request.getTickerSymbol() != null) recommendation.setTickerSymbol(request.getTickerSymbol().toUpperCase());
        if (request.getCompanyName() != null) recommendation.setCompanyName(request.getCompanyName());
        if (request.getTradeType() != null) recommendation.setTradeType(request.getTradeType());
        if (request.getRecommendationDate() != null) recommendation.setRecommendationDate(request.getRecommendationDate());
        if (request.getEntryPrice() != null) recommendation.setEntryPrice(request.getEntryPrice());
        if (request.getTargetPrice() != null) recommendation.setTargetPrice(request.getTargetPrice());
        if (request.getStopLoss() != null) recommendation.setStopLoss(request.getStopLoss());
        if (request.getSellPrice() != null) recommendation.setSellPrice(request.getSellPrice());
        if (request.getExitDate() != null) recommendation.setExitDate(request.getExitDate());
        if (request.getStatus() != null) recommendation.setStatus(request.getStatus());
        if (request.getRemarks() != null) recommendation.setRemarks(request.getRemarks());

        Recommendation saved = recommendationRepository.save(recommendation);
        return toResponse(saved);
    }

    public RecommendationResponse getRecommendationById(Long id) {
        return toResponse(findById(id));
    }

    public PageResponse<RecommendationResponse> getAllRecommendations(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Recommendation> recommendationPage = recommendationRepository.findAll(pageable);
        return buildPageResponse(recommendationPage);
    }

    public PageResponse<RecommendationResponse> getRecommendationsWithFilters(
            Market market, RecommendationStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recommendation> recommendationPage = recommendationRepository.findWithFilters(market, status, pageable);
        return buildPageResponse(recommendationPage);
    }

    public PageResponse<RecommendationResponse> getOpenRecommendations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("recommendationDate").descending());
        Page<Recommendation> recommendationPage = recommendationRepository.findByStatus(RecommendationStatus.OPEN, pageable);
        return buildPageResponse(recommendationPage);
    }

    public PageResponse<RecommendationResponse> getRecommendationsByMarket(Market market, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("recommendationDate").descending());
        Page<Recommendation> recommendationPage = recommendationRepository.findByMarket(market, pageable);
        return buildPageResponse(recommendationPage);
    }

    @Transactional
    public void deleteRecommendation(Long id) {
        Recommendation recommendation = findById(id);
        recommendationRepository.delete(recommendation);
    }

    private Recommendation findById(Long id) {
        return recommendationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation", "id", id));
    }

    private RecommendationResponse toResponse(Recommendation r) {
        RecommendationResponse response = RecommendationResponse.builder()
                .id(r.getId())
                .market(r.getMarket())
                .currency(r.getCurrency())
                .tickerSymbol(r.getTickerSymbol())
                .companyName(r.getCompanyName())
                .tradeType(r.getTradeType())
                .recommendationDate(r.getRecommendationDate())
                .entryPrice(r.getEntryPrice())
                .targetPrice(r.getTargetPrice())
                .stopLoss(r.getStopLoss())
                .riskRewardRatio(r.getRiskRewardRatio())
                .sellPrice(r.getSellPrice())
                .exitDate(r.getExitDate())
                .holdingPeriodDays(r.getHoldingPeriodDays())
                .status(r.getStatus())
                .remarks(r.getRemarks())
                .createdByUserId(r.getCreatedByUserId())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();

        // Calculate additional fields
        if (r.getTargetPrice() != null && r.getEntryPrice() != null) {
            BigDecimal potentialReturn = r.getTargetPrice().subtract(r.getEntryPrice());
            response.setPotentialReturn(potentialReturn);
            response.setPotentialReturnPercentage(
                    potentialReturn.divide(r.getEntryPrice(), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)
            );
        }

        if (r.getEntryPrice() != null && r.getStopLoss() != null) {
            response.setPotentialRisk(r.getEntryPrice().subtract(r.getStopLoss()));
        }

        return response;
    }

    private PageResponse<RecommendationResponse> buildPageResponse(Page<Recommendation> page) {
        return PageResponse.<RecommendationResponse>builder()
                .content(page.getContent().stream().map(this::toResponse).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .empty(page.isEmpty())
                .build();
    }
}
