package com.homemitra.controller;

import com.homemitra.dto.ApiResponse;
import com.homemitra.model.ServiceCategory;
import com.homemitra.repository.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ServiceCategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceCategory>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(
                categoryRepository.findByActiveTrueOrderBySortOrderAsc()));
    }
}
