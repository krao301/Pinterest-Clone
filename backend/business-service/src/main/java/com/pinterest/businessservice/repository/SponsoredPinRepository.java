package com.pinterest.businessservice.repository;

import com.pinterest.businessservice.domain.SponsoredPin;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsoredPinRepository extends JpaRepository<SponsoredPin, Long> {
  List<SponsoredPin> findByBusinessProfileId(Long businessProfileId);
  List<SponsoredPin> findByKeywordsContainingIgnoreCase(String keyword);
}
