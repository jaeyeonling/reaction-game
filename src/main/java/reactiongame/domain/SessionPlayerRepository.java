package reactiongame.domain;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.infrastructure.web.ReactionGameException;

import java.util.List;
import java.util.Optional;

import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.SESSION_PLAYER_NOT_FOUND;

@org.springframework.stereotype.Repository
public interface SessionPlayerRepository extends Repository<SessionPlayer, Long> {

    @Transactional
    SessionPlayer save(SessionPlayer sessionPlayer);

    @Transactional(readOnly = true)
    Optional<SessionPlayer> findById(long id);

    @Transactional(readOnly = true)
    Optional<SessionPlayer> findBySessionIdAndPlayerId(long sessionId, long playerId);

    @Transactional(readOnly = true)
    List<SessionPlayer> findAllBySessionId(long sessionId);

    @Transactional(readOnly = true)
    List<SessionPlayer> findAllByPlayerIdIn(List<Long> playerIds);

    @Transactional(readOnly = true)
    default SessionPlayer getById(final long id) {
        return findById(id).orElseThrow(() -> new ReactionGameException(SESSION_PLAYER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    default SessionPlayer getBySessionIdAndPlayerId(
            final long sessionId,
            final long playerId
    ) {
        return findBySessionIdAndPlayerId(sessionId, playerId)
                .orElseThrow(() -> new ReactionGameException(SESSION_PLAYER_NOT_FOUND));
    }

    @Transactional
    default SessionPlayer getBySessionIdAndPlayerIdOrCreate(
            final long sessionId,
            final long playerId
    ) {
        return findBySessionIdAndPlayerId(sessionId, playerId)
                .orElseGet(() -> save(new SessionPlayer(sessionId, playerId)));
    }
}
