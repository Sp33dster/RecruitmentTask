package pl.sp33dster.githubrepos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sp33dster.githubrepos.model.Repository;
import pl.sp33dster.githubrepos.service.GithubService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public final class GithubController {

    private static final Logger log = LoggerFactory.getLogger(GithubController.class);

    private final GithubService githubService;

    public GithubController(final GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/users/{username}/repositories")
    public List<Repository> getRepositories(@PathVariable final String username) {
        log.info("Request for repositories of user: {}", username);

        final var result = githubService.getNonForkRepositories(username);
        log.debug("Returning {} repositories for user: {}", result.size(), username);

        return result;
    }
}
