package reactiongame.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import reactiongame.domain.PlayerRepository;

import static reactiongame.infrastructure.security.PlayerTokenBaseAccessToken.ACCESS_TOKEN_ATTRIBUTE_NAME;
import static reactiongame.infrastructure.security.PlayerTokenBaseAccessToken.PLAYER_TOKEN_HEADER_NAME;

@Component
public final class PlayerTokenBaseAccessTokenBindInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PlayerTokenBaseAccessTokenBindInterceptor.class);

    private final PlayerRepository playerRepository;

    PlayerTokenBaseAccessTokenBindInterceptor(final PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public boolean preHandle(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final Object handler
    ) {
        final var playerTokenValue = request.getHeader(PLAYER_TOKEN_HEADER_NAME);
        if (playerTokenValue == null) {
            return true;
        }

        savePlayerToAttributeIfExists(request, playerTokenValue);
        return true;
    }

    private void savePlayerToAttributeIfExists(
            final HttpServletRequest request,
            final String playerTokenValue
    ) {
        playerRepository.findByToken(playerTokenValue)
                .map(it -> new PlayerTokenBaseAccessToken(it.id(), it.name(), it.admin()))
                .ifPresentOrElse(
                        accessToken -> saveAccessTokenToRequestAttribute(request, accessToken),
                        () -> log.debug("Player not found [token={}]", playerTokenValue)
                );
    }

    private static void saveAccessTokenToRequestAttribute(
            final HttpServletRequest request,
            final AccessToken accessToken
    ) {
        log.debug("Player token found [accessToken={}]", accessToken);
        request.setAttribute(ACCESS_TOKEN_ATTRIBUTE_NAME, accessToken);
    }

    @Override
    public void postHandle(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final Object handler,
            final ModelAndView modelAndView
    ) {
        request.removeAttribute(ACCESS_TOKEN_ATTRIBUTE_NAME);
    }

    @Override
    public void afterCompletion(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final Object handler,
            final Exception e
    ) {
        request.removeAttribute(ACCESS_TOKEN_ATTRIBUTE_NAME);
    }
}
