package reactiongame.infrastructure.web;

import org.springframework.http.HttpStatusCode;

public final class ReactionGameException extends RuntimeException {

    private final ReactionGameExceptionStatus status;

    public ReactionGameException(final ReactionGameExceptionStatus status) {
        this(status, null);
    }

    public ReactionGameException(
            final ReactionGameExceptionStatus status,
            final Throwable cause
    ) {
        super(cause);

        this.status = status;
    }

    public ReactionGameExceptionStatus status() {
        return status;
    }

    public HttpStatusCode httpStatus() {
        return status.httpStatusCode();
    }

    public int code() {
        return status.code();
    }

    public String message() {
        return status.name();
    }
}
