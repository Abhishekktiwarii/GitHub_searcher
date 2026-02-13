package com.code.github_searcher.service. Impl;

import com.code.github_searcher.dto.GithubSearchRequest;
import com.code.github_searcher.dto.RepositoryResponse;
import com.code.github_searcher.entity.GithubRepositoryEntity;
import com.code.github_searcher.repository.GithubRepositoryJpaRepository;
import com.code.github_searcher.repository.GithubRepositorySpecification;
import com.code.github_searcher.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

    private final WebClient webClient;
    private final GithubRepositoryJpaRepository repository;

    // =============================
    // SEARCH AND SAVE
    // =============================
    @Override
    public List<RepositoryResponse> searchAndSaveRepositories(GithubSearchRequest request) {

        String query = buildQuery(request);

        Map<String, Object> githubResponse = fetchFromGithub(query, request.getSort());

        if (githubResponse == null || !githubResponse.containsKey("items")) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> items =
                (List<Map<String, Object>>) githubResponse.get("items");

        List<GithubRepositoryEntity> entities = items.stream()
                .map(this::mapToEntity)
                .toList();

        repository.saveAll(entities); // UPSERT

        return entities.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =============================
    // GET STORED WITH FILTERS
    // =============================
    @Override
    public List<RepositoryResponse> getStoredRepositories(String language,
                                                          Integer minStars,
                                                          String sort) {

        Specification<GithubRepositoryEntity> spec = Specification.where((Specification<GithubRepositoryEntity>) null);

        if (language != null && !language.isBlank()) {
            spec = spec.and(GithubRepositorySpecification.hasLanguage(language));
        }

        if (minStars != null) {
            spec = spec.and(GithubRepositorySpecification.hasMinStars(minStars));
        }

        List<GithubRepositoryEntity> entities =
                repository.findAll(spec, buildSort(sort));

        return entities.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =============================
    // PRIVATE METHODS
    // =============================

    private Map<String, Object> fetchFromGithub(String query, String sort) {

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/repositories")
                            .queryParam("q", query)
                            .queryParam("sort", sort != null ? sort : "stars")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

        } catch (WebClientResponseException.TooManyRequests e) {
            throw new RuntimeException("GitHub API rate limit exceeded.");
        } catch (WebClientResponseException e) {
            throw new RuntimeException("GitHub API error: " + e.getStatusCode());
        }
    }

    private String buildQuery(GithubSearchRequest request) {
        StringBuilder sb = new StringBuilder(request.getQuery());

        if (request.getLanguage() != null && !request.getLanguage().isBlank()) {
            sb.append("+language:").append(request.getLanguage());
        }

        return sb.toString();
    }

    private GithubRepositoryEntity mapToEntity(Map<String, Object> item) {

        Map<String, Object> owner =
                item.get("owner") instanceof Map ?
                        (Map<String, Object>) item.get("owner") : null;

        return GithubRepositoryEntity.builder()
                .id(((Number) item.get("id")).longValue())
                .name((String) item.get("name"))
                .description((String) item.get("description"))
                .owner(owner != null ? (String) owner.get("login") : null)
                .language((String) item.get("language"))
                .stars(getInteger(item.get("stargazers_count")))
                .forks(getInteger(item.get("forks_count")))
                .lastUpdated(item.get("updated_at") != null
                        ? Instant.parse((String) item.get("updated_at"))
                        : null)
                .build();
    }

    private Integer getInteger(Object value) {
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    private RepositoryResponse mapToResponse(GithubRepositoryEntity entity) {
        return RepositoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .owner(entity.getOwner())
                .language(entity.getLanguage())
                .stars(entity.getStars())
                .forks(entity.getForks())
                .lastUpdated(entity.getLastUpdated())
                .build();
    }

    private Sort buildSort(String sort) {
        return switch (sort != null ? sort.toLowerCase() : "stars") {
            case "forks" -> Sort.by(Sort.Direction.DESC, "forks");
            case "updated" -> Sort.by(Sort.Direction.DESC, "lastUpdated");
            default -> Sort.by(Sort.Direction.DESC, "stars");
        };
    }
}
