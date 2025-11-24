package com.pinterest.userservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pinterest.userservice.domain.Follow;
import com.pinterest.userservice.domain.User;
import com.pinterest.userservice.dto.ConnectionResponse;
import com.pinterest.userservice.exception.DuplicateResourceException;
import com.pinterest.userservice.exception.ResourceNotFoundException;
import com.pinterest.userservice.repository.FollowRepository;
import com.pinterest.userservice.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SocialGraphServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private FollowRepository followRepository;

  @InjectMocks
  private SocialGraphService socialGraphService;

  private User alice;
  private User bob;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    alice = User.builder().id(1L).username("alice").build();
    bob = User.builder().id(2L).username("bob").build();
  }

  @Test
  void followCreatesRelationship() {
    when(followRepository.existsByFollowerIdAndFollowedId(1L, 2L)).thenReturn(false);
    when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
    when(userRepository.findById(2L)).thenReturn(Optional.of(bob));
    when(followRepository.save(any(Follow.class))).thenAnswer(invocation -> {
      Follow follow = invocation.getArgument(0);
      follow.setId(5L);
      return follow;
    });

    ConnectionResponse response = socialGraphService.follow(1L, 2L);

    assertEquals(2L, response.getId());
    assertEquals("bob", response.getUsername());
    verify(followRepository).save(any(Follow.class));
  }

  @Test
  void followDuplicateThrows() {
    when(followRepository.existsByFollowerIdAndFollowedId(1L, 2L)).thenReturn(true);
    assertThrows(DuplicateResourceException.class, () -> socialGraphService.follow(1L, 2L));
  }

  @Test
  void unfollowRemovesRelationship() {
    Follow follow = Follow.builder().id(3L).follower(alice).followed(bob).build();
    when(followRepository.findByFollowerIdAndFollowedId(1L, 2L)).thenReturn(Optional.of(follow));
    doNothing().when(followRepository).delete(follow);

    socialGraphService.unfollow(1L, 2L);

    verify(followRepository).delete(follow);
  }

  @Test
  void unfollowMissingThrows() {
    when(followRepository.findByFollowerIdAndFollowedId(1L, 2L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> socialGraphService.unfollow(1L, 2L));
  }

  @Test
  void listFollowersReturnsConnections() {
    Follow follow = Follow.builder().id(4L).follower(alice).followed(bob).blocked(false).build();
    when(userRepository.findById(2L)).thenReturn(Optional.of(bob));
    when(followRepository.findByFollowedIdAndBlockedFalse(2L)).thenReturn(List.of(follow));

    List<ConnectionResponse> results = socialGraphService.listFollowers(2L);

    assertEquals(1, results.size());
    assertEquals("alice", results.get(0).getUsername());
  }

  @Test
  void listFollowingReturnsConnections() {
    Follow follow = Follow.builder().id(4L).follower(alice).followed(bob).blocked(false).build();
    when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
    when(followRepository.findByFollowerIdAndBlockedFalse(1L)).thenReturn(List.of(follow));

    List<ConnectionResponse> results = socialGraphService.listFollowing(1L);

    assertEquals(1, results.size());
    assertEquals("bob", results.get(0).getUsername());
  }

  @Test
  void blockFollowerMarksRecord() {
    Follow follow = Follow.builder().id(4L).follower(alice).followed(bob).blocked(false).build();
    when(followRepository.findByFollowerIdAndFollowedId(1L, 2L)).thenReturn(Optional.of(follow));

    socialGraphService.blockFollower(2L, 1L);

    verify(followRepository).save(follow);
    assertEquals(true, follow.isBlocked());
  }
}
