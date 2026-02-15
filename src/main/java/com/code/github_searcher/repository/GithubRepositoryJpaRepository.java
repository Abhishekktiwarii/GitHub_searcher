package com.code.github_searcher.repository;

import com.code.github_searcher.entity.GithubRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface  GithubRepositoryJpaRepository
        extends JpaRepository<GithubRepositoryEntity, Long>,
        JpaSpecificationExecutor<GithubRepositoryEntity> {
}
