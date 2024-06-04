package reactiongame.infrastructure.security;

public interface AccessToken {

    long playerId();

    String name();

    boolean admin();
}
