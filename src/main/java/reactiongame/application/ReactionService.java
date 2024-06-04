package reactiongame.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.domain.ReactionHistory;
import reactiongame.domain.SessionPlayerRepository;
import reactiongame.domain.SessionRepository;
import reactiongame.infrastructure.security.AccessToken;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReactionService {

    private final SessionRepository sessionRepository;

    private final SessionPlayerRepository sessionPlayerRepository;

    ReactionService(
            final SessionRepository sessionRepository,
            final SessionPlayerRepository sessionPlayerRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.sessionPlayerRepository = sessionPlayerRepository;
    }

    @Transactional
    public ReactionHistory create(
            final AccessToken accessToken,
            final long sessionId
    ) {
        final var requestAt = LocalDateTime.now();

        final var session = sessionRepository.getById(sessionId);
        final var sessionPlayer = sessionPlayerRepository.getBySessionIdAndPlayerIdOrCreate(
                sessionId,
                accessToken.playerId()
        );
        final var reaction = session.react(sessionPlayer, requestAt);

        return new ReactionHistory(reaction);
    }

    @Transactional
    public List<ReactionHistory> findBy(
            final AccessToken accessToken,
            final long sessionId
    ) {
        final var sessionPlayer = sessionPlayerRepository.getBySessionIdAndPlayerId(sessionId, accessToken.playerId());
        return sessionPlayer.reactions();
    }

    @Transactional(readOnly = true)
    public List<ReactionHistory> findBySessionId(final long sessionId) {
        var sessionPlayers = sessionPlayerRepository.findAllBySessionId(sessionId);
        return sessionPlayers.stream()
                .flatMap(sessionPlayer -> sessionPlayer.reactions().stream())
                .toList();
    }
}
