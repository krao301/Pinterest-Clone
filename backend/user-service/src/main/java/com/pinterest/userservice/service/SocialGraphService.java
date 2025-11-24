package com.pinterest.userservice.service;

import com.pinterest.userservice.domain.Follow;
import com.pinterest.userservice.domain.User;
import com.pinterest.userservice.dto.ConnectionResponse;
import com.pinterest.userservice.exception.DuplicateResourceException;
import com.pinterest.userservice.exception.ResourceNotFoundException;
import com.pinterest.userservice.repository.FollowRepository;
import com.pinterest.userservice.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialGraphService {

  private final UserRepository userRepository;
  private final FollowRepository followRepository;

  @Transactional
  public ConnectionResponse follow(Long followerId, Long followedId) {
    validateIds(followerId, followedId);
    if (followRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
      throw new DuplicateResourceException("Already following user");
    }
    User follower = loadUser(followerId);
    User followed = loadUser(followedId);
    Follow follow = followRepository.save(Follow.builder()
        .follower(follower)
        .followed(followed)
        .blocked(false)
        .build());
    return ConnectionResponse.builder()
        .id(followed.getId())
        .username(followed.getUsername())
        .build();
  }

  @Transactional
  public void unfollow(Long followerId, Long followedId) {
    validateIds(followerId, followedId);
    Follow follow = followRepository.findByFollowerIdAndFollowedId(followerId, followedId)
        .orElseThrow(() -> new ResourceNotFoundException("Follow relationship not found"));
    followRepository.delete(follow);
  }

  @Transactional(readOnly = true)
  public List<ConnectionResponse> listFollowers(Long userId) {
    User user = loadUser(userId);
    return followRepository.findByFollowedIdAndBlockedFalse(user.getId()).stream()
        .map(follow -> toConnection(follow.getFollower()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ConnectionResponse> listFollowing(Long userId) {
    User user = loadUser(userId);
    return followRepository.findByFollowerIdAndBlockedFalse(user.getId()).stream()
        .map(follow -> toConnection(follow.getFollowed()))
        .collect(Collectors.toList());
  }

  @Transactional
  public void blockFollower(Long userId, Long followerId) {
    validateIds(userId, followerId);
    Follow follow = followRepository.findByFollowerIdAndFollowedId(followerId, userId)
        .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
    follow.setBlocked(true);
    followRepository.save(follow);
  }

  private void validateIds(Long followerId, Long followedId) {
    if (followerId.equals(followedId)) {
      throw new IllegalArgumentException("Cannot follow yourself");
    }
  }

  private User loadUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private ConnectionResponse toConnection(User user) {
    return ConnectionResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .build();
  }
}
