package reactiongame.infrastructure.security;

public record PlayerTokenBaseAccessToken(
        long playerId,
        String name,
        boolean admin
) implements AccessToken {

    public static final String PLAYER_TOKEN_HEADER_NAME = "X-PLAYER-TOKEN";
    public static final String ACCESS_TOKEN_ATTRIBUTE_NAME = "PLAYER";
}
