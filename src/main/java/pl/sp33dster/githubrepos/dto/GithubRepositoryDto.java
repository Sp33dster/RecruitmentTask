package pl.sp33dster.githubrepos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubRepositoryDto(
        String name,
        @JsonProperty("fork") boolean fork,
        GithubOwnerDto owner
) {}
