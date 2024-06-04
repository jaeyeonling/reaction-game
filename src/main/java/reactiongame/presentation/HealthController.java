package reactiongame.presentation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class HealthController {

    @RequestMapping("/health")
    String health() {
        return "OK";
    }
}
