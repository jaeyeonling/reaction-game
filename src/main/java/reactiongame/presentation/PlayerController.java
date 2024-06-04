package reactiongame.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactiongame.application.PlayerRequest;
import reactiongame.application.PlayerResponse;
import reactiongame.application.PlayerService;
import reactiongame.infrastructure.security.Secured;

import java.util.List;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    PlayerController(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @Secured(admin = true)
    @PostMapping("/players")
    PlayerResponse create(@RequestBody final PlayerRequest request) {
        return playerService.create(request);
    }

    @Secured(admin = true)
    @GetMapping("/players")
    List<PlayerResponse> findAll() {
        return playerService.findAll();
    }

    @Secured(admin = true)
    @DeleteMapping("/players/{playerId}")
    ResponseEntity<Void> delete(@PathVariable final long playerId) {
        playerService.delete(playerId);
        return ResponseEntity.noContent().build();
    }
}
