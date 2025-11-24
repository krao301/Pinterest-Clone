package com.pinterest.contentservice.repository;

import com.pinterest.contentservice.domain.Pin;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PinRepository extends JpaRepository<Pin, Long> {

  @Query("SELECT p FROM Pin p WHERE (:query IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) "
      + "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) "
      + "OR LOWER(p.keywords) LIKE LOWER(CONCAT('%', :query, '%')))")
  List<Pin> searchPins(@Param("query") String query);

  List<Pin> findByBoardId(Long boardId);
}
