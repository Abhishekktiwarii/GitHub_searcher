package com.code.github_searcher.service;

import com.code.github_searcher.repository.GithubRepositoryJpaRepository;
import com.code.github_searcher.service.Impl.GithubServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

class GithubServiceImplTest {

    private GithubRepositoryJpaRepository repository;
    private WebClient webClient;
    private GithubServiceImpl service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(GithubRepositoryJpaRepository.class);
        webClient = Mockito.mock(WebClient.class);
        service = new GithubServiceImpl(webClient, repository);
    }

    @Test
    void testGetStoredRepositories_NoFilters() {
        service.getStoredRepositories(null, null, "stars");
    }
}
