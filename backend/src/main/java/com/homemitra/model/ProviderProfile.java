package com.homemitra.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="provider_profiles")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProviderProfile {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id") private User user;
    @Column(columnDefinition="TEXT") private String bio;
    @Column(name="experience_yrs") private int experienceYrs = 0;
    @Column(name="rating_avg") private BigDecimal ratingAvg = BigDecimal.ZERO;
    @Column(name="total_jobs") private int totalJobs = 0;
    @Column(name="is_available") private boolean available = true;
    @Column(name="kyc_verified") private boolean kycVerified = false;
    @Column(name="bank_account") private String bankAccount;
    @Column(name="ifsc_code") private String ifscCode;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
}
