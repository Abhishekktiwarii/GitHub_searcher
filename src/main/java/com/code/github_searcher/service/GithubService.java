package com.code.github_searcher.service;

import com.code.github_searcher.dto.GithubSearchRequest;
import com.code.github_searcher.dto.RepositoryResponse;

import java.util.List;

public interface GithubService {

    List<RepositoryResponse> searchAndSaveRepositories(GithubSearchRequest request);

    List<RepositoryResponse> getStoredRepositories(String language,
                                                   Integer minStars,
                                                   String sort);
}
