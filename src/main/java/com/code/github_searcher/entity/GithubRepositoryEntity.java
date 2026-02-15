package com.code.github_searcher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "repositories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  GithubRepositoryEntity {

    @Id
    private Long id;   // GitHub repo ID (unique)

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String owner;

    private String language;

    private Integer stars;

    private Integer forks;

    private Instant lastUpdated;
}
