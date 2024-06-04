package reactiongame.application;

public record PlayerResponse(
        long id,
        String name,
        String token,
        boolean admin
) {

}
