package reactiongame.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReactionBaseTimeTest {

    @MethodSource("roundDownValues")
    @ParameterizedTest
    @DisplayName("{0}은 내림 처리 한다.")
    void roundDown(final LocalDateTime localDateTime) {
        final var reactionDateTime = new ReactionBaseTime(localDateTime);
        final var expected = localDateTime.withSecond(0).withNano(0);

        final var actual = reactionDateTime.toLocalDateTime();

        assertThat(actual).isEqualToIgnoringNanos(expected);
    }

    private static List<LocalDateTime> roundDownValues() {
        return List.of(
                LocalDateTime.of(1993, 7, 20, 12, 30),
                LocalDateTime.of(1993, 7, 20, 12, 30, 10),
                LocalDateTime.of(1993, 7, 20, 12, 30, 29),
                LocalDateTime.of(1993, 7, 20, 12, 30, 29, 999)
        );
    }

    @MethodSource("roundUpValues")
    @ParameterizedTest
    @DisplayName("{0}은 올림 처리 한다.")
    void roundUp(final LocalDateTime localDateTime) {
        final var reactionDateTime = new ReactionBaseTime(localDateTime);
        final var expected = localDateTime.withSecond(0).withNano(0).plusMinutes(1);

        final var actual = reactionDateTime.toLocalDateTime();

        assertThat(actual).isEqualToIgnoringNanos(expected);
    }

    private static List<LocalDateTime> roundUpValues() {
        return List.of(
                LocalDateTime.of(1993, 7, 20, 12, 30, 30),
                LocalDateTime.of(1993, 7, 20, 12, 30, 50),
                LocalDateTime.of(1993, 7, 20, 12, 30, 59),
                LocalDateTime.of(1993, 7, 20, 12, 30, 59, 999)
        );
    }

    @MethodSource("reactionTimeRateMillisValues")
    @ParameterizedTest
    @DisplayName("{0}의 반응 시간은 {1}ms다.")
    void calculateReactionTimeRateMillis(final LocalDateTime requestAt, final long expected) {
        final var reactionDateTime = new ReactionBaseTime(requestAt);

        final var actual = reactionDateTime.calculateReactionTimeRateMillis(requestAt);

        assertThat(actual).isEqualTo(expected);
    }

    private static List<Arguments> reactionTimeRateMillisValues() {
        return List.of(
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30), 0),
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30, 1), 1_000L),
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30, 30), 30_000L),
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30, 50), 10_000L),
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30, 59), 1_000L),
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30, 59, 999_000_000), 1L),
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30, 59, 999_999_999), 1L),
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30, 59, 998_000_000), 2L),
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30, 59, 500_000_000), 500L),
                Arguments.of(LocalDateTime.of(1993, 7, 20, 12, 30, 59, 123_456_789), 877L)
        );
    }
}
