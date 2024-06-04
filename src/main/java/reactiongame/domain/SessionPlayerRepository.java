package reactiongame.domain;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    default SessionPlayer getById(final long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("SessionPlayer not found"));
    }

    @Transactional(readOnly = true)
    default SessionPlayer getBySessionIdAndPlayerId(
            final long sessionId,
            final long playerId
    ) {
        return findBySessionIdAndPlayerId(sessionId, playerId)
                .orElseThrow(() -> new NoSuchElementException("SessionPlayer not found"));
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
