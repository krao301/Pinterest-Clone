package com.pinterest.userservice.api;

import com.pinterest.userservice.dto.ConnectionResponse;
import com.pinterest.userservice.service.SocialGraphService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SocialController {

  private final SocialGraphService socialGraphService;

  @PostMapping("/{followerId}/follow/{followedId}")
  @ResponseStatus(HttpStatus.CREATED)
  public ConnectionResponse follow(
      @PathVariable @Positive Long followerId,
      @PathVariable @Positive Long followedId) {
    return socialGraphService.follow(followerId, followedId);
  }

  @DeleteMapping("/{followerId}/follow/{followedId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unfollow(
      @PathVariable @Positive Long followerId,
      @PathVariable @Positive Long followedId) {
    socialGraphService.unfollow(followerId, followedId);
  }

  @PostMapping("/{userId}/block/{followerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void blockFollower(
      @PathVariable @Positive Long userId,
      @PathVariable @Positive Long followerId) {
    socialGraphService.blockFollower(userId, followerId);
  }

  @GetMapping("/{userId}/followers")
  public List<ConnectionResponse> followers(@PathVariable @Positive Long userId) {
    return socialGraphService.listFollowers(userId);
  }

  @GetMapping("/{userId}/following")
  public List<ConnectionResponse> following(@PathVariable @Positive Long userId) {
    return socialGraphService.listFollowing(userId);
  }
}
