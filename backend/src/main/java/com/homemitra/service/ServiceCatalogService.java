package com.homemitra.service;

import com.homemitra.dto.ServiceResponse;
import com.homemitra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceCatalogService {
    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository categoryRepository;

    public List<ServiceResponse> getFeatured() {
        return serviceRepository.findByFeaturedTrueAndActiveTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ServiceResponse> getByCategory(Long categoryId) {
        return serviceRepository.findByCategoryIdAndActiveTrue(categoryId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Page<ServiceResponse> getAll(int page, int size) {
        return serviceRepository.findByActiveTrueOrderByTotalBookingsDesc(PageRequest.of(page, size))
                .map(this::toResponse);
    }

    public List<ServiceResponse> search(String q) {
        return serviceRepository.search(q)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ServiceResponse getBySlug(String slug) {
        return serviceRepository.findBySlug(slug)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    private ServiceResponse toResponse(com.homemitra.model.Service s) {
        return ServiceResponse.builder()
                .id(s.getId()).name(s.getName()).slug(s.getSlug())
                .shortDesc(s.getShortDesc()).description(s.getDescription())
                .basePrice(s.getBasePrice()).durationMins(s.getDurationMins())
                .imageUrl(s.getImageUrl()).ratingAvg(s.getRatingAvg())
                .totalBookings(s.getTotalBookings()).featured(s.isFeatured())
                .categoryName(s.getCategory().getName())
                .categoryIcon(s.getCategory().getIcon())
                .build();
    }
}
