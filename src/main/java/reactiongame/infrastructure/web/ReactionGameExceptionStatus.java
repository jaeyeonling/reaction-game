package reactiongame.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ReactionGameExceptionStatus {

    BAD_REQUEST(-101, HttpStatus.BAD_REQUEST),
    NOT_FOUND(-102, HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(-103, HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(-104, HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN(-105, HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(-106, HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_OPERATION(-107, HttpStatus.NOT_IMPLEMENTED),
    FORBIDDEN(-108, HttpStatus.FORBIDDEN),
    REQUEST_METHOD_NOT_SUPPORTED(-109, HttpStatus.METHOD_NOT_ALLOWED),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED(-110, HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE(-111, HttpStatus.NOT_ACCEPTABLE),
    MISSING_PATH_VARIABLE(-112, HttpStatus.NOT_FOUND),
    MISSING_SERVLET_REQUEST_PARAMETER(-113, HttpStatus.UNPROCESSABLE_ENTITY),
    SERVLET_REQUEST_BINDING_FAIL(-114, HttpStatus.BAD_REQUEST),
    CONVERSION_NOT_SUPPORTED(-115, HttpStatus.INTERNAL_SERVER_ERROR),
    TYPE_MISMATCH(-116, HttpStatus.BAD_REQUEST),
    HTTP_MESSAGE_NOT_READABLE(-117, HttpStatus.BAD_REQUEST),
    HTTP_MESSAGE_NOT_WRITABLE(-118, HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_ARGUMENT_NOT_VALID(-119, HttpStatus.BAD_REQUEST),
    MISSING_SERVLET_REQUEST_PART(-120, HttpStatus.BAD_REQUEST),
    ASYNC_REQUEST_TIMEOUT(-121, HttpStatus.REQUEST_TIMEOUT),
    SERVER_BUSY(-122, HttpStatus.SERVICE_UNAVAILABLE),
    PAYLOAD_TOO_LARGE(-123, HttpStatus.PAYLOAD_TOO_LARGE),

    SESSION_NOT_FOUND(-201, HttpStatus.NOT_FOUND),
    SESSION_NOT_AVAILABLE(-202, HttpStatus.BAD_REQUEST),
    SESSION_NOT_SETTLED(-203, HttpStatus.BAD_REQUEST),
    SESSION_PLAYER_NOT_FOUND(-204, HttpStatus.NOT_FOUND),
    PLAYER_NOT_FOUND(-205, HttpStatus.NOT_FOUND);

    private final int code;

    private final HttpStatusCode httpStatusCode;

    ReactionGameExceptionStatus(
            final int code,
            final HttpStatusCode httpStatusCode
    ) {
        this.code = code;
        this.httpStatusCode = httpStatusCode;
    }

    public int code() {
        return code;
    }

    public HttpStatusCode httpStatusCode() {
        return httpStatusCode;
    }
}
