package reactiongame.application;

import org.springframework.stereotype.Component;
import reactiongame.domain.Player;

import java.util.List;

@Component
public final class PlayerAssembler {

    public PlayerResponse toResponse(final Player player) {
        return new PlayerResponse(
                player.id(),
                player.name(),
                player.token(),
                player.admin()
        );
    }

    public List<PlayerResponse> toResponses(final List<Player> players) {
        return players.stream()
                .map(this::toResponse)
                .toList();
    }

    public Player toEntity(final PlayerRequest request) {
        return new Player(request.name(), request.admin());
    }
}
