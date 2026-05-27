package pl.sp33dster.githubrepos.exception;

public final class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String username) {
        super("User %s not found".formatted(username));
    }
}
