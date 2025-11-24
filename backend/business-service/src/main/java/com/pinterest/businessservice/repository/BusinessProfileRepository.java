package com.pinterest.businessservice.repository;

import com.pinterest.businessservice.domain.BusinessProfile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, Long> {
  List<BusinessProfile> findByNameContainingIgnoreCase(String name);
  List<BusinessProfile> findByCategoriesContainingIgnoreCase(String category);
}
