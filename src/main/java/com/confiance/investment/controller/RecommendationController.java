package com.confiance.investment.controller;

import com.confiance.common.dto.ApiResponse;
import com.confiance.common.dto.PageResponse;
import com.confiance.common.enums.Market;
import com.confiance.common.enums.RecommendationStatus;
import com.confiance.investment.dto.RecommendationRequest;
import com.confiance.investment.dto.RecommendationResponse;
import com.confiance.investment.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendations", description = "Stock/Investment Recommendation APIs")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    @Operation(summary = "Create Recommendation", description = "Create a new stock recommendation (Admin only)")
    public ResponseEntity<ApiResponse<RecommendationResponse>> createRecommendation(
            @Valid @RequestBody RecommendationRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        RecommendationResponse response = recommendationService.createRecommendation(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Recommendation created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Recommendation", description = "Update an existing recommendation (Admin only)")
    public ResponseEntity<ApiResponse<RecommendationResponse>> updateRecommendation(
            @PathVariable Long id,
            @Valid @RequestBody RecommendationRequest request) {
        RecommendationResponse response = recommendationService.updateRecommendation(id, request);
        return ResponseEntity.ok(ApiResponse.success("Recommendation updated successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Recommendation", description = "Get recommendation by ID")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getRecommendation(@PathVariable Long id) {
        RecommendationResponse response = recommendationService.getRecommendationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get All Recommendations", description = "Get paginated list of all recommendations")
    public ResponseEntity<ApiResponse<PageResponse<RecommendationResponse>>> getAllRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "recommendationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PageResponse<RecommendationResponse> response = recommendationService.getAllRecommendations(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/filter")
    @Operation(summary = "Get Recommendations with Filters", description = "Get recommendations filtered by market and status")
    public ResponseEntity<ApiResponse<PageResponse<RecommendationResponse>>> getRecommendationsWithFilters(
            @RequestParam(required = false) Market market,
            @RequestParam(required = false) RecommendationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<RecommendationResponse> response = recommendationService.getRecommendationsWithFilters(market, status, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/open")
    @Operation(summary = "Get Open Recommendations", description = "Get all active/open recommendations for users")
    public ResponseEntity<ApiResponse<PageResponse<RecommendationResponse>>> getOpenRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<RecommendationResponse> response = recommendationService.getOpenRecommendations(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/market/{market}")
    @Operation(summary = "Get Recommendations by Market", description = "Get recommendations for a specific market")
    public ResponseEntity<ApiResponse<PageResponse<RecommendationResponse>>> getRecommendationsByMarket(
            @PathVariable Market market,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<RecommendationResponse> response = recommendationService.getRecommendationsByMarket(market, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Recommendation", description = "Delete a recommendation (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
        return ResponseEntity.ok(ApiResponse.success("Recommendation deleted successfully", null));
    }
}
