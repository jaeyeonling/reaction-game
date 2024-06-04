package reactiongame.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SessionPlayerTest {

    @DisplayName("반응을 조회한다.")
    @Test
    void findReactionByBaseTime() {
        final var baseTime = ReactionBaseTime.now();
        final var sessionPlayer = new SessionPlayer(1L, 1L);

        final var reaction = sessionPlayer.react(baseTime.toLocalDateTime());

        Assertions.assertThat(sessionPlayer.findReactionByBaseTime(baseTime))
                .contains(reaction);
    }
}
