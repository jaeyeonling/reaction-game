package reactiongame.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.domain.PlayerRepository;
import reactiongame.domain.SessionPlayerStatus;
import reactiongame.domain.SessionRepository;
import reactiongame.domain.SessionStatus;
import reactiongame.domain.SimplePlayer;

import java.util.List;
import java.util.Map;

@Component
public class SessionStatusAssembler {

    private final SessionRepository sessionRepository;

    private final PlayerRepository playerRepository;

    SessionStatusAssembler(
            final SessionRepository sessionRepository,
            final PlayerRepository playerRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public SessionStatusResponse toResponse(final SessionStatus sessionStatus) {
        final var session = sessionRepository.getById(sessionStatus.sessionId());
        final var idToPlayer = playerRepository.findSimpleMapEachById(sessionStatus.playerIds());

        return new SessionStatusResponse(
                session.id(),
                session.title(),
                session.startDate(),
                session.endDate(),
                toResponse(sessionStatus.playerStatuses(), idToPlayer)
        );
    }

    private List<SessionStatusResponse.SessionPlayerStatusResponse> toResponse(
            final List<SessionPlayerStatus> sessionPlayerStatus,
            final Map<Long, SimplePlayer> idToPlayer
    ) {
        return sessionPlayerStatus.stream()
                .map(status -> toResponse(status, idToPlayer))
                .toList();
    }

    private SessionStatusResponse.SessionPlayerStatusResponse toResponse(
            final SessionPlayerStatus status,
            final Map<Long, SimplePlayer> idToPlayer
    ) {
        return new SessionStatusResponse.SessionPlayerStatusResponse(
                idToPlayer.get(status.playerId()).name(),
                status.reactions()
        );
    }
}
