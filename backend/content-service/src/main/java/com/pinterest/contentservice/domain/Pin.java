package com.pinterest.contentservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pin {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Title is required")
  @Column(nullable = false)
  private String title;

  @NotBlank(message = "Description is required")
  @Column(nullable = false, length = 4096)
  private String description;

  @NotBlank(message = "Media URL is required")
  @Column(nullable = false, length = 1024)
  private String mediaUrl;

  @Column(length = 1024)
  private String sourceUrl;

  @Column(length = 2048)
  private String keywords;

  @Column(nullable = false)
  private boolean draft;

  @Column(nullable = false)
  private boolean visible;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private Board board;

  @Builder.Default
  private Instant createdAt = Instant.now();
}
