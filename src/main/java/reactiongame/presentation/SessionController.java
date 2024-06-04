package reactiongame.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactiongame.application.SessionPlayerStatusResponse;
import reactiongame.application.SessionRequest;
import reactiongame.application.SessionResponse;
import reactiongame.application.SessionResultResponse;
import reactiongame.application.SessionService;
import reactiongame.application.SessionStatusResponse;
import reactiongame.infrastructure.security.AccessToken;
import reactiongame.infrastructure.security.BindAccessToken;
import reactiongame.infrastructure.security.Secured;

import java.net.URI;
import java.util.List;

@RestController
public class SessionController {

    private final SessionService sessionService;

    SessionController(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Secured(admin = true)
    @PostMapping("/sessions")
    ResponseEntity<SessionResponse> create(@RequestBody final SessionRequest request) {
        final var session = sessionService.create(request);
        final var location = "/sessions/" + session.id();

        return ResponseEntity.created(URI.create(location))
                .body(session);
    }

    @Secured(admin = true)
    @GetMapping("/sessions")
    List<SessionResponse> findAll() {
        return sessionService.findAll();
    }

    @Secured(admin = true)
    @GetMapping("/sessions/{sessionId}")
    SessionResponse findById(@PathVariable final long sessionId) {
        return sessionService.findById(sessionId);
    }

    @Secured(admin = true)
    @DeleteMapping("/sessions/{sessionId}")
    ResponseEntity<Void> delete(@PathVariable final long sessionId) {
        sessionService.delete(sessionId);
        return ResponseEntity.noContent().build();
    }

    @Secured(admin = true)
    @GetMapping("/sessions/{sessionId}/status")
    SessionStatusResponse sessionStatus(@PathVariable final long sessionId) {
        return sessionService.findSessionStatus(sessionId);
    }

    @Secured
    @GetMapping("/sessions/{sessionId}/my-status")
    SessionPlayerStatusResponse status(
            @BindAccessToken final AccessToken accessToken,
            @PathVariable final long sessionId
    ) {
        return sessionService.findPlayerStatus(
                accessToken,
                sessionId
        );
    }

    @Secured
    @GetMapping("/sessions/{sessionId}/result")
    SessionResultResponse result(@PathVariable final long sessionId) {
        return sessionService.findResult(sessionId);
    }
}
