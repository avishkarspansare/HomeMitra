package com.homemitra.repository;

import com.homemitra.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findBySlug(String slug);
    List<Service> findByCategoryIdAndActiveTrue(Long categoryId);
    List<Service> findByFeaturedTrueAndActiveTrue();
    Page<Service> findByActiveTrueOrderByTotalBookingsDesc(Pageable pageable);

    @Query("SELECT s FROM Service s WHERE s.active = true AND " +
           "(LOWER(s.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(s.shortDesc) LIKE LOWER(CONCAT('%',:q,'%')))")
    List<Service> search(String q);
}
