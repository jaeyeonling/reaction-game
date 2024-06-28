package reactiongame.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.domain.SessionPlayerRepository;
import reactiongame.domain.SessionRepository;

import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    private final SessionPlayerRepository sessionPlayerRepository;

    private final SessionAssembler sessionAssembler;

    SessionService(
            final SessionRepository sessionRepository,
            final SessionPlayerRepository sessionPlayerRepository,
            final SessionAssembler sessionAssembler
    ) {
        this.sessionRepository = sessionRepository;
        this.sessionPlayerRepository = sessionPlayerRepository;
        this.sessionAssembler = sessionAssembler;
    }

    @Transactional
    public SessionResponse create(final SessionRequest request) {
        final var session = sessionRepository.save(sessionAssembler.toEntity(request));
        return sessionAssembler.toResponse(session);
    }

    @Transactional(readOnly = true)
    public List<SessionResponse> findAll() {
        return sessionAssembler.toResponses(sessionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public SessionResponse findById(final long id) {
        final var session = sessionRepository.getById(id);
        return sessionAssembler.toResponse(session);
    }


    @Transactional
    public void delete(final long sessionId) {
        final var session = sessionRepository.getById(sessionId);
        sessionRepository.delete(session);
    }

    @Transactional(readOnly = true)
    public SessionStatusResponse findSessionStatus(final long sessionId) {
        final var session = sessionRepository.getById(sessionId);
        final var sessionPlayers = sessionPlayerRepository.findAllBySessionId(sessionId);

        return sessionAssembler.toResponse(session.createSessionStatus(sessionPlayers));
    }

    @Transactional(readOnly = true)
    public SessionLeaderboardResponse createLeaderboard(final long sessionId) {
        final var session = sessionRepository.getById(sessionId);
        final var sessionPlayers = sessionPlayerRepository.findAllBySessionId(sessionId);

        return sessionAssembler.toResponse(session.createLeaderboard(sessionPlayers));
    }
}
