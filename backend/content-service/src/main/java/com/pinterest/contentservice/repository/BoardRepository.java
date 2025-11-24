package com.pinterest.contentservice.repository;

import com.pinterest.contentservice.domain.Board;
import com.pinterest.contentservice.domain.Visibility;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

  List<Board> findByNameContainingIgnoreCase(String name);

  List<Board> findByVisibility(Visibility visibility);
}
