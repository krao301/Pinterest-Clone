package com.pinterest.businessservice.repository;

import com.pinterest.businessservice.domain.Showcase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowcaseRepository extends JpaRepository<Showcase, Long> {
  List<Showcase> findByBusinessProfileId(Long businessProfileId);
  List<Showcase> findByThemeContainingIgnoreCase(String theme);
}
