package reactiongame.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReactionsTest {

    private Reactions reactions;

    @BeforeEach
    void setUp() {
        reactions = new Reactions();
    }

    @DisplayName("같은 기준 시간을 가진 반응을 추가하면 무시된다.")
    @Test
    void add_alreadyReacted() {
        final var reaction = new Reaction(LocalDateTime.now());
        reactions.add(reaction);

        reactions.add(reaction);

        assertThat(reactions.toList()).hasSize(1);
    }

    @DisplayName("기준 시간으로 반응을 찾을 수 있다.")
    @Test
    void findReactionByBaseTime() {
        final var now = LocalDateTime.now();
        final var reaction = new Reaction(now);
        reactions.add(reaction);

        final var actual = reactions.findReactionByBaseTime(now);

        assertThat(actual).contains(reaction);
    }

    @DisplayName("기준 시간에 해당하는 반응이 없으면 찾을 수 없다.")
    @Test
    void findReactionByBaseTime_notFound() {
        final var now = LocalDateTime.now();
        final var reaction = new Reaction(now);
        reactions.add(reaction);

        final var actual = reactions.findReactionByBaseTime(now.plusMinutes(1));

        assertThat(actual).isEmpty();
    }
}
