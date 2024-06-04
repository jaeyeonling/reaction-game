package reactiongame.infrastructure.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.ASYNC_REQUEST_TIMEOUT;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.BAD_REQUEST;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.CONVERSION_NOT_SUPPORTED;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.HTTP_MEDIA_TYPE_NOT_SUPPORTED;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.HTTP_MESSAGE_NOT_READABLE;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.HTTP_MESSAGE_NOT_WRITABLE;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.INTERNAL_SERVER_ERROR;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.METHOD_ARGUMENT_NOT_VALID;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.MISSING_PATH_VARIABLE;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.MISSING_SERVLET_REQUEST_PARAMETER;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.MISSING_SERVLET_REQUEST_PART;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.NOT_FOUND;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.PAYLOAD_TOO_LARGE;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.REQUEST_METHOD_NOT_SUPPORTED;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.SERVLET_REQUEST_BINDING_FAIL;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.TYPE_MISMATCH;

@ControllerAdvice
public final class ResponseExceptionHandler extends ResponseEntityExceptionHandler {


    private static final Logger log = LoggerFactory.getLogger(ResponseExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            @NonNull final HttpRequestMethodNotSupportedException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Request method not supported [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(REQUEST_METHOD_NOT_SUPPORTED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            @NonNull final HttpMediaTypeNotSupportedException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Http media type not supported [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(HTTP_MEDIA_TYPE_NOT_SUPPORTED);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            @NonNull final HttpMediaTypeNotAcceptableException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Http media type not acceptable [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(HTTP_MEDIA_TYPE_NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            @NonNull final MissingPathVariableException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Missing path variable [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(MISSING_PATH_VARIABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            @NonNull final MissingServletRequestParameterException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Missing servlet request parameter [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(MISSING_SERVLET_REQUEST_PARAMETER);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            @NonNull final ServletRequestBindingException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Servlet request binding fail [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(SERVLET_REQUEST_BINDING_FAIL);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
            @NonNull final ConversionNotSupportedException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Conversion not supported [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(CONVERSION_NOT_SUPPORTED);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            @NonNull final TypeMismatchException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Type mismatch [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(TYPE_MISMATCH);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull final HttpMessageNotReadableException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Http message not readable [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(HTTP_MESSAGE_NOT_READABLE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            @NonNull final HttpMessageNotWritableException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Http message not writable [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(HTTP_MESSAGE_NOT_WRITABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull final MethodArgumentNotValidException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Method argument not valid [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(METHOD_ARGUMENT_NOT_VALID);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            @NonNull final MissingServletRequestPartException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Missing servlet request part [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(MISSING_SERVLET_REQUEST_PART);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            @NonNull final NoHandlerFoundException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Handler not found [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            @NonNull final AsyncRequestTimeoutException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Async request timeout [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(ASYNC_REQUEST_TIMEOUT);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull final Exception e,
            @Nullable final Object body,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.error("Uncaught exception [body={}, headers={}, status={}, request={}]", body, headers, status, request, e);
        return createResponse(INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            @NonNull final HandlerMethodValidationException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        // TODO: check JSR-303 and JSR-380 annotations in the handler method
        log.debug("Handler method validation exception [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(METHOD_ARGUMENT_NOT_VALID);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            @NonNull final NoResourceFoundException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("No resource found exception [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleErrorResponseException(
            @NonNull final ErrorResponseException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        // TODO: improve error response exception handling
        log.debug("Error response exception [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(
                e.getStatusCode(),
                new FailResponse(BAD_REQUEST.code(), e.getBody().getTitle())
        );
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(
            @NonNull final MaxUploadSizeExceededException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request
    ) {
        log.debug("Max upload size exceeded [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(PAYLOAD_TOO_LARGE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodValidationException(
            @NonNull final MethodValidationException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatus status,
            @NonNull final WebRequest request
    ) {
        // TODO: check JSR-303 and JSR-380 annotations in the handler method
        log.debug("method validation exception [headers={}, status={}, request={}]", headers, status, request, e);
        return createResponse(METHOD_ARGUMENT_NOT_VALID);
    }

    private ResponseEntity<Object> createResponse(final ReactionGameExceptionStatus status) {
        return createResponse(
                status.httpStatusCode(),
                new FailResponse(status.code(), status.name())
        );
    }

    private ResponseEntity<Object> createResponse(
            final HttpStatusCode httpStatus,
            final FailResponse failResponse
    ) {
        return ResponseEntity.status(httpStatus)
                .body(failResponse);
    }
}
