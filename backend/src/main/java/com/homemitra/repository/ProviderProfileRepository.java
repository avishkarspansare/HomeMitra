package com.homemitra.repository;

import com.homemitra.model.ProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {}
