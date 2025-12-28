package com.confiance.investment.controller;

import com.confiance.common.dto.ApiResponse;
import com.confiance.investment.entity.InvestmentProduct;
import com.confiance.investment.repository.InvestmentProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentProductRepository repository;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<InvestmentProduct>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<InvestmentProduct> products = repository.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvestmentProduct>> getProduct(@PathVariable Long id) {
        return repository.findById(id)
                .map(product -> ResponseEntity.ok(ApiResponse.success(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InvestmentProduct>> createProduct(@RequestBody InvestmentProduct product) {
        InvestmentProduct saved = repository.save(product);
        return ResponseEntity.ok(ApiResponse.success("Investment product created", saved));
    }
}