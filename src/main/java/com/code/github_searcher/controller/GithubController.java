package com.code.github_searcher.controller;

import com.code.github_searcher.dto.GithubSearchRequest;
import com.code.github_searcher.dto.RepositoryResponse;
import com.code.github_searcher.service.GithubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class  GithubController {

    private final GithubService githubService;

    @PostMapping("/search")
    public ResponseEntity<?> searchRepositories(
            @Valid @RequestBody GithubSearchRequest request) {

        List<RepositoryResponse> repositories =
                githubService.searchAndSaveRepositories(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Repositories fetched and saved successfully");
        response.put("repositories", repositories);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/repositories")
    public ResponseEntity<?> getStoredRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(required = false, defaultValue = "stars") String sort) {

        List<RepositoryResponse> repositories =
                githubService.getStoredRepositories(language, minStars, sort);

        Map<String, Object> response = new HashMap<>();
        response.put("repositories", repositories);

        return ResponseEntity.ok(response);
    }
}
