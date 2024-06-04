package reactiongame.domain;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        return findById(id).orElseThrow(() -> new NoSuchElementException("Session not found"));
    }
}
