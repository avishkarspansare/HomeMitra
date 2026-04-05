package com.homemitra.controller;

import com.homemitra.dto.*;
import com.homemitra.service.ServiceCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceCatalogService serviceCatalogService;

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> featured() {
        return ResponseEntity.ok(ApiResponse.ok(serviceCatalogService.getFeatured()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ServiceResponse>>> all(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="12") int size) {
        return ResponseEntity.ok(ApiResponse.ok(serviceCatalogService.getAll(page, size)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> byCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.ok(serviceCatalogService.getByCategory(categoryId)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> search(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(serviceCatalogService.search(q)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ServiceResponse>> bySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.ok(serviceCatalogService.getBySlug(slug)));
    }
}
