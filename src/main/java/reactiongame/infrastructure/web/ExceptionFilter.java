package reactiongame.infrastructure.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.INTERNAL_SERVER_ERROR;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
final class ExceptionFilter extends OncePerRequestFilter {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExceptionFilter.class);

    private static final ReactionGameExceptionStatus status = INTERNAL_SERVER_ERROR;
    private static final String ERROR_MESSAGE = """
            {
                "code": %d,
                "message": "%s",
            }
            """.formatted(status.code(), status.name());

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain
    ) throws IOException {
        try {
            filterChain.doFilter(
                    request,
                    response
            );
        } catch (final Throwable throwable) {
            log.error("Exception occurred", throwable);
            recovery(response);
        }
    }

    private void recovery(final HttpServletResponse response) throws IOException {
        response.setStatus(status.httpStatusCode().value());

        final var writer = response.getWriter();
        writer.write(ERROR_MESSAGE);
    }
}
