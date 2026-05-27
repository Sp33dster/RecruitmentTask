package pl.sp33dster.githubrepos.dto;

public record GithubBranchDto(
        String name,
        GithubCommitDto commit
) {}
