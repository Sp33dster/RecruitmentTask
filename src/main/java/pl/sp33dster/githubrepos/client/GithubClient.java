package pl.sp33dster.githubrepos.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import pl.sp33dster.githubrepos.dto.GithubBranchDto;
import pl.sp33dster.githubrepos.dto.GithubRepositoryDto;

import java.util.List;

@HttpExchange
public interface GithubClient {

    @GetExchange("/users/{username}/repos")
    List<GithubRepositoryDto> getRepositories(@PathVariable String username);

    @GetExchange("/repos/{username}/{repoName}/branches")
    List<GithubBranchDto> getBranches(
            @PathVariable String username,
            @PathVariable String repoName
    );
}
