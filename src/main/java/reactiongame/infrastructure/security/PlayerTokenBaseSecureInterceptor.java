package reactiongame.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public final class PlayerTokenBaseSecureInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PlayerTokenBaseSecureInterceptor.class);

    @Override
    public boolean preHandle(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final Object handler
    ) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            log.debug("Handler is not a handler method [handler={}]", handler);
            return true;
        }
        final var secured = handlerMethod.getMethodAnnotation(Secured.class);
        if (secured == null) {
            log.debug("Handler is not a secured method [handler={}]", handler);
            return true;
        }

        final var accessToken = (AccessToken) request.getAttribute(PlayerTokenBaseAccessToken.ACCESS_TOKEN_ATTRIBUTE_NAME);
        if (accessToken == null) {
            throw new IllegalStateException("Access token is required");
        }
        if (secured.admin() && !accessToken.admin()) {
            throw new IllegalStateException("Admin access token is required");
        }

        log.debug("Access token is valid [accessToken={}]", accessToken);
        return true;
    }
}
