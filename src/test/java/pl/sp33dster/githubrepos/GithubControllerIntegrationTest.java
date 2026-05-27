package pl.sp33dster.githubrepos;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@EnableWireMock({
        @ConfigureWireMock(name = "github-api", baseUrlProperties = "github.api.base-url")
})
class GithubControllerIntegrationTest {

    @Autowired
    private RestTestClient restTestClient;

    @InjectWireMock("github-api")
    private WireMockServer wireMockServer;

    @Test
    void shouldReturnNonForkRepositories() {
        wireMockServer.stubFor(get(urlEqualTo("/users/testuser/repos"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                    {
                                        "name": "real-repo",
                                        "fork": false,
                                        "owner": { "login": "testuser" }
                                    },
                                    {
                                        "name": "forked-repo",
                                        "fork": true,
                                        "owner": { "login": "testuser" }
                                    }
                                ]
                                """)));

        wireMockServer.stubFor(get(urlEqualTo("/repos/testuser/real-repo/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                    {
                                        "name": "main",
                                        "commit": { "sha": "abc123" }
                                    }
                                ]
                                """)));

        restTestClient.get()
                .uri("/api/v1/users/testuser/repositories")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        [
                            {
                                "repositoryName": "real-repo",
                                "ownerLogin": "testuser",
                                "branches": [
                                    {
                                        "name": "main",
                                        "lastCommitSha": "abc123"
                                    }
                                ]
                            }
                        ]
                        """);
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoRepositories() {
        wireMockServer.stubFor(get(urlEqualTo("/users/emptyuser/repos"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        restTestClient.get()
                .uri("/api/v1/users/emptyuser/repositories")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[]");
    }

    @Test
    void shouldReturn404ForNonExistingUser() {
        wireMockServer.stubFor(get(urlEqualTo("/users/nonexistent/repos"))
                .willReturn(aResponse()
                        .withStatus(404)));

        restTestClient.get()
                .uri("/api/v1/users/nonexistent/repositories")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .json("""
                        {
                            "status": 404,
                            "message": "User nonexistent not found"
                        }
                        """);
    }
}
