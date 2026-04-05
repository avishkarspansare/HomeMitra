package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name="addresses")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id") private User user;
    private String label;
    @Column(nullable=false) private String line1;
    private String line2;
    @Column(nullable=false) private String city;
    @Column(nullable=false) private String state;
    @Column(nullable=false) private String pincode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    @Column(name="is_default") private boolean defaultAddress = false;
}
