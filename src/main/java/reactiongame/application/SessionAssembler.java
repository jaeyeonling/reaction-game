package reactiongame.application;

import org.springframework.stereotype.Component;
import reactiongame.domain.Session;
import reactiongame.domain.SessionResult;
import reactiongame.domain.SessionStatus;

import java.util.List;

@Component
public final class SessionAssembler {

    private final SessionStatusAssembler sessionStatusAssembler;

    private final SessionResultAssembler sessionResultAssembler;

    SessionAssembler(
            final SessionStatusAssembler sessionStatusAssembler,
            final SessionResultAssembler sessionResultAssembler
    ) {
        this.sessionStatusAssembler = sessionStatusAssembler;
        this.sessionResultAssembler = sessionResultAssembler;
    }

    public SessionResponse toResponse(final Session session) {
        return new SessionResponse(
                session.id(),
                session.title(),
                session.startDate(),
                session.endDate()
        );
    }

    public List<SessionResponse> toResponses(final List<Session> sessions) {
        return sessions.stream()
                .map(this::toResponse)
                .toList();
    }

    public Session toEntity(final SessionRequest request) {
        return new Session(
                request.title(),
                request.startDate(),
                request.endDate()
        );
    }

    public SessionStatusResponse toResponse(final SessionStatus sessionStatus) {
        return sessionStatusAssembler.toResponse(sessionStatus);
    }

    public SessionLeaderboardResponse toResponse(final SessionResult sessionResult) {
        return sessionResultAssembler.assemble(sessionResult);
    }
}
