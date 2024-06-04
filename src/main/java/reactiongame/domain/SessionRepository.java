package reactiongame.domain;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.infrastructure.web.ReactionGameException;

import java.util.List;
import java.util.Optional;

import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.SESSION_NOT_FOUND;

@org.springframework.stereotype.Repository
public interface SessionRepository extends Repository<Session, Long> {

    @Transactional
    Session save(Session session);

    @Transactional(readOnly = true)
    List<Session> findAll();

    @Transactional(readOnly = true)
    Optional<Session> findById(long id);

    @Transactional
    void delete(Session session);

    @Transactional(readOnly = true)
    default Session getById(final long id) {
        return findById(id).orElseThrow(() -> new ReactionGameException(SESSION_NOT_FOUND));
    }
}
