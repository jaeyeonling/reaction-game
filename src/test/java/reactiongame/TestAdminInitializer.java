package reactiongame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactiongame.domain.Player;
import reactiongame.domain.PlayerRepository;

@Component
public final class TestAdminInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(TestAdminInitializer.class);

    public static final Player ADMIN = new Player("Neo", true);

    private final PlayerRepository playerRepository;

    TestAdminInitializer(final PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(final ApplicationArguments args) {
        if (playerRepository.count() > 0) {
            log.debug("Admin already initialized [playerCount={}]", playerRepository.count());
            return;
        }

        playerRepository.save(ADMIN);
        log.warn("Admin initialized [player={}]", ADMIN);
    }
}
