package reactiongame.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.domain.PlayerRepository;
import reactiongame.domain.SessionPlayerStatus;

@Component
public class SessionPlayerStatusAssembler {

    private final PlayerRepository playerRepository;

    SessionPlayerStatusAssembler(final PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public SessionPlayerStatusResponse assemble(final SessionPlayerStatus sessionPlayerStatus) {
        final var player = playerRepository.getSimpleById(sessionPlayerStatus.playerId());
        return new SessionPlayerStatusResponse(
                player.name(),
                sessionPlayerStatus.reactions()
        );
    }
}
