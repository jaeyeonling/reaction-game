package reactiongame.infrastructure.security;

import reactiongame.infrastructure.web.WebContextHolder;

import java.util.Optional;

public final class AccessTokenHolder {

    private AccessTokenHolder() {
    }

    public static Optional<AccessToken> findAccessToken() {
        return WebContextHolder.getCurrentHttpRequest()

                // Note: if you are using a different token type, you may need to change this line
                .map(request -> (AccessToken) request.getAttribute(PlayerTokenBaseAccessToken.ACCESS_TOKEN_ATTRIBUTE_NAME));
    }
}
