package com.pinterest.userservice.repository;

import com.pinterest.userservice.domain.Follow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
  boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);
  Optional<Follow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);
  List<Follow> findByFollowedIdAndBlockedFalse(Long followedId);
  List<Follow> findByFollowerIdAndBlockedFalse(Long followerId);
}
