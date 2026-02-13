package com.code.github_searcher.repository;

import com.code.github_searcher.entity.GithubRepositoryEntity;
import org.springframework.data.jpa.domain.Specification;

public class GithubRepositorySpecification {

    public static Specification<GithubRepositoryEntity> hasLanguage(String language) {
        return (root, query, cb) ->
                language == null ? null :
                        cb.equal(cb.lower(root.get("language")), language.toLowerCase());
    }

    public static Specification<GithubRepositoryEntity> hasMinStars(Integer minStars) {
        return (root, query, cb) ->
                minStars == null ? null :
                        cb.greaterThanOrEqualTo(root.get("stars"), minStars);
    }
}
