package pl.sp33dster.githubrepos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pl.sp33dster.githubrepos.client.GithubClient;

@Configuration
public final class GithubClientConfig {

    @Value("${github.api.base-url}")
    private String baseUrl;

    @Bean
    public GithubClient githubClient(final RestClient.Builder builder) {
        final var restClient = builder
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2026-03-10")
                .build();

        final var adapter = RestClientAdapter.create(restClient);
        final var factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(GithubClient.class);
    }
}
