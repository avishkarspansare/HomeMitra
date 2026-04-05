package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity @Table(name="service_categories")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceCategory {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String slug;
    private String icon;
    private String description;
    @Column(name="sort_order") private int sortOrder;
    @Column(name="is_active") private boolean active = true;
    @OneToMany(mappedBy="category", fetch=FetchType.LAZY)
    private List<Service> services;
}
