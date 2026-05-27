package pl.sp33dster.githubrepos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import pl.sp33dster.githubrepos.client.GithubClient;
import pl.sp33dster.githubrepos.dto.GithubBranchDto;
import pl.sp33dster.githubrepos.dto.GithubRepositoryDto;
import pl.sp33dster.githubrepos.exception.UserNotFoundException;
import pl.sp33dster.githubrepos.model.Branch;
import pl.sp33dster.githubrepos.model.Repository;

import java.util.List;

@Service
public final class GithubService {

    private static final Logger log = LoggerFactory.getLogger(GithubService.class);

    private final GithubClient githubClient;

    public GithubService(final GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<Repository> getNonForkRepositories(final String username) {
        log.info("Fetching repositories for user: {}", username);

        final List<GithubRepositoryDto> allRepos;
        try {
            allRepos = githubClient.getRepositories(username);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("GitHub user not found: {}", username);
            throw new UserNotFoundException(username);
        }

        log.debug("Fetched {} total repositories for user: {}", allRepos.size(), username);

        final var result = allRepos.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> toRepository(repo, username))
                .toList();

        log.info("Returning {} non-fork repositories for user: {}", result.size(), username);
        return result;
    }

    private Repository toRepository(final GithubRepositoryDto dto, final String username) {
        log.debug("Fetching branches for repo: {}", dto.name());

        final var branches = githubClient.getBranches(username, dto.name())
                .stream()
                .map(this::toBranch)
                .toList();

        log.debug("Repository {}/{} has {} branches", username, dto.name(), branches.size());

        return new Repository(dto.name(), dto.owner().login(), branches);
    }

    private Branch toBranch(final GithubBranchDto dto) {
        return new Branch(dto.name(), dto.commit().sha());
    }
}
