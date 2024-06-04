package reactiongame.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public final class WebContextHolder {

    private WebContextHolder() {
    }

    public static Optional<HttpServletRequest> getCurrentHttpRequest() {
        final var requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(requestAttributes)
                .map(ServletRequestAttributes::getRequest);
    }
}
