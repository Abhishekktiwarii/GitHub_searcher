package com.code.github_searcher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GithubSearchRequest {

    @NotBlank(message = "Query must not be blank")
    private String query;

    private String language;

    private String sort;  // stars, forks, updated
}
