package reactiongame.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactiongame.application.ReactionService;
import reactiongame.domain.ReactionHistory;
import reactiongame.infrastructure.security.AccessToken;
import reactiongame.infrastructure.security.BindAccessToken;
import reactiongame.infrastructure.security.Secured;

import java.util.List;

@RestController
public class ReactionController {

    private final ReactionService reactionService;

    ReactionController(final ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @Secured
    @PostMapping("/sessions/{sessionId}/reactions")
    ReactionHistory create(
            @BindAccessToken final AccessToken accessToken,
            @PathVariable final long sessionId
    ) {
        return reactionService.create(accessToken, sessionId);
    }

    @GetMapping("/sessions/{sessionId}/reactions")
    List<ReactionHistory> findAll(@PathVariable final long sessionId) {
        return reactionService.findBySessionId(sessionId);
    }

    @Secured
    @GetMapping("/sessions/{sessionId}/reactions/mine")
    List<ReactionHistory> findMine(
            @BindAccessToken final AccessToken accessToken,
            @PathVariable final long sessionId
    ) {
        return reactionService.findBy(accessToken, sessionId);
    }
}
