# Recruitment Task - GitHub Repository

REST API that acts as a proxy to the GitHub API, listing user repositories with branch information.


## Tech Stack

- Java 25
- Spring Boot 4
- Gradle (Kotlin DSL)

## Requirements

- Java 25
- Gradle 8+ (or use the included `./gradlew` wrapper — no installation needed)

## Checking your Java version

```bash
java -version
```

Expected output:
```
java version "25.0.x" ...
```

## Running

```bash
./gradlew bootRun
```

## Testing


```bash
./gradlew test
```

## API

### GET /api/v1/users/{username}/repositories

Returns all non-fork repositories for a given GitHub user, including branch names and last commit SHA.

**Success response (200)**

```json
[
   {
       "repositoryName": "my-repo",
       "ownerLogin": "username",
       "branches": [
           {
               "name": "main",
              "lastCommitSha": "abc123def456"
           }
       ]
   }
]
```


**User not found (404)**


```json
{
   "status": 404,
   "message": "User username not found"
}
```



## Manual Testing

You can test the API using the provided `test.http` file in the root of the project (supported natively by IntelliJ IDEA),

or with curl:



 ### Existing user
```bash
curl http://localhost:8080/api/v1/users/octocat/repositories
```


 ### Non-existing user
```bash
curl http://localhost:8080/api/v1/users/nonexistinguser123/repositories
```


## Notes

- Forked repositories are excluded from the response

- Backed by \[GitHub REST API v3](https://developer.github.com/v3)

