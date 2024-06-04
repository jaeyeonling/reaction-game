package reactiongame.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import reactiongame.infrastructure.web.ReactionGameException;

import static java.util.Objects.requireNonNull;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.INVALID_ACCESS_TOKEN;
import static reactiongame.infrastructure.security.PlayerTokenBaseAccessToken.ACCESS_TOKEN_ATTRIBUTE_NAME;

@Component
public final class PlayerTokenBaseAccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(PlayerTokenBaseAccessTokenArgumentResolver.class);

    @Override
    public boolean supportsParameter(@NonNull final MethodParameter parameter) {
        final var isBindAccessToken = parameter.getParameterAnnotation(BindAccessToken.class) != null;
        final var isAccessTokenClass = AccessToken.class.isAssignableFrom(parameter.getParameterType());

        log.debug(
                "Access token arguments support check [parameterType={}, isBindAccessToken={}, isAccessTokenClass={}]",
                parameter.getParameterType(),
                isBindAccessToken,
                isAccessTokenClass
        );
        return isBindAccessToken && isAccessTokenClass;
    }

    @Override
    public AccessToken resolveArgument(
            @NonNull final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            @NonNull final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final var bindAccessToken = requireNonNull(parameter.getParameterAnnotation(BindAccessToken.class));
        final var accessToken = (AccessToken) webRequest.getAttribute(ACCESS_TOKEN_ATTRIBUTE_NAME, NativeWebRequest.SCOPE_REQUEST);

        if (accessToken == null) {
            log.debug("Access token not found");

            if (bindAccessToken.require()) {
                throw new ReactionGameException(INVALID_ACCESS_TOKEN);
            }
            return null;
        }

        log.debug("Access token found [accessToken={}]", accessToken);
        return accessToken;
    }
}
