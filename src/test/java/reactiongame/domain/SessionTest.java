package reactiongame.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactiongame.infrastructure.web.ReactionGameException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.SESSION_NOT_AVAILABLE;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.SESSION_NOT_SETTLED;
import static reactiongame.support.LocalDateTimes.convertMillisToNano;

class SessionTest {

    private static final LocalDateTime baseTime = LocalDateTime.of(1993, 7, 20, 12, 30, 0, 0);
    private static final LocalDateTime endTime = baseTime.plusMinutes(10);

    private Session session;
    private SessionPlayer sessionPlayer;

    @BeforeEach
    void setUp() {
        session = new Session(1L, "테스트 세션", baseTime, endTime);
        sessionPlayer = new SessionPlayer(1L, 1L);
    }

    @DisplayName("반응을 추가한다.")
    @Test
    void react() {
        final var actual = session.react(sessionPlayer, baseTime);

        assertThat(actual).isNotNull();
    }

    @DisplayName("세션이 시작되기 전 반응은 추가할 수 없다.")
    @Test
    void react_notStarted() {
        final var reactionGameException = catchThrowableOfType(() -> session.react(
                sessionPlayer,
                baseTime.minusNanos(1)
        ), ReactionGameException.class);

        assertThat(reactionGameException).isNotNull()
                .extracting("status")
                .isEqualTo(SESSION_NOT_AVAILABLE);
    }

    @DisplayName("세션이 끝난 후 반응은 추가할 수 없다.")
    @Test
    void react_finished() {
        final var reactionGameException = catchThrowableOfType(() -> session.react(
                sessionPlayer,
                endTime.plusNanos(1)
        ), ReactionGameException.class);

        assertThat(reactionGameException).isNotNull()
                .extracting("status")
                .isEqualTo(SESSION_NOT_AVAILABLE);
    }

    @DisplayName("게임 시작 후 반응하지 않았을 경우 플레이어 상태를 생성한다.")
    @Test
    void createSessionPlayerStatus_notReact() {
        final var sessionPlayerStatus = session.createSessionPlayerStatus(
                sessionPlayer,
                new ReactionBaseTime(baseTime)
        );

        assertAll(
                () -> assertThat(sessionPlayerStatus.playerId()).isEqualTo(sessionPlayer.playerId()),
                () -> assertThat(sessionPlayerStatus.reactions()).isEmpty(),
                () -> assertThat(sessionPlayerStatus.totalReactionRateMillis()).isZero()
        );
    }

    @DisplayName("게임 시작 후 1분이 지나는 동안 반응하지 않았을 경우 플레이어 상태를 생성한다.")
    @Test
    void createSessionPlayerStatus_notReact_1minutes() {
        final var sessionPlayerStatus = session.createSessionPlayerStatus(
                sessionPlayer,
                new ReactionBaseTime(baseTime.plusMinutes(1))
        );

        assertAll(
                () -> assertThat(sessionPlayerStatus.playerId()).isEqualTo(sessionPlayer.playerId()),
                () -> assertThat(sessionPlayerStatus.reactions()).hasSize(1),
                () -> assertThat(sessionPlayerStatus.totalReactionRateMillis()).isEqualTo(30_000)
        );
    }

    @DisplayName("게임 시작 후 5분 동안 플레이한 플레이어 상태를 생성한다.")
    @Test
    void createSessionPlayerStatus_5minutes() {
        session.react(sessionPlayer, baseTime); // 0
        session.react(sessionPlayer, baseTime.plusMinutes(1).plusSeconds(3)); // 3000
        // session.react(sessionPlayer, baseTime.plusMinutes(2).plusSeconds(3)); // 누락: 30_000
        session.react(sessionPlayer, baseTime.plusMinutes(3).plusSeconds(3).plusNanos(convertMillisToNano(250))); // 3250
        session.react(sessionPlayer, baseTime.plusMinutes(4).minusSeconds(2).minusNanos(convertMillisToNano(250))); // 2250

        final var sessionPlayerStatus = session.createSessionPlayerStatus(
                sessionPlayer,
                new ReactionBaseTime(baseTime.plusMinutes(5))
        );

        assertAll(
                () -> assertThat(sessionPlayerStatus.playerId()).isEqualTo(sessionPlayer.playerId()),
                () -> assertThat(sessionPlayerStatus.reactions()).hasSize(5),
                () -> assertThat(sessionPlayerStatus.totalReactionRateMillis())
                        .isEqualTo(3000 + 30_000 + 3250 + 2250)
        );
    }

    @DisplayName("게임이 끝나지 않은 경우 결과를 생성할 수 없다.")
    @Test
    void createLeaderboard_notFinished() {
        final var reactionGameException = catchThrowableOfType(() -> session.createLeaderboard(
                List.of(),
                baseTime.minusNanos(1)
        ), ReactionGameException.class);

        assertThat(reactionGameException).isNotNull()
                .extracting("status")
                .isEqualTo(SESSION_NOT_SETTLED);
    }

    @DisplayName("게임 정산이 끝나면 결과를 생성할 수 있다.")
    @Test
    void createLeaderboard() {
        assertThatCode(() -> session.createLeaderboard(
                List.of(),
                endTime
        )).doesNotThrowAnyException();
    }

    @DisplayName("게임 참여자가 없을 때 결과를 생성할 수 있다.")
    @Test
    void createLeaderboard_empty() {
        final var sessionResult = session.createLeaderboard(
                List.of(),
                endTime
        );

        assertAll(
                () -> assertThat(sessionResult.sessionId()).isEqualTo(1L),
                () -> assertThat(sessionResult.playerRankings()).isEmpty()
        );
    }

    @DisplayName("혼자 플레이한 게임의 결과를 생성한다.")
    @Test
    void createLeaderboard_solo() {
        session.react(sessionPlayer, baseTime); // 0
        session.react(sessionPlayer, baseTime.plusMinutes(1).plusSeconds(3)); // 3000
        // session.react(sessionPlayer, baseTime.plusMinutes(2).plusSeconds(3)); // 누락: 30_000
        session.react(sessionPlayer, baseTime.plusMinutes(3).plusSeconds(3).plusNanos(convertMillisToNano(250))); // 3250
        session.react(sessionPlayer, baseTime.plusMinutes(4).minusSeconds(2).minusNanos(convertMillisToNano(250))); // 2250

        final var sessionResult = session.createLeaderboard(
                List.of(sessionPlayer),
                endTime
        );

        assertAll(
                () -> assertThat(sessionResult.sessionId()).isEqualTo(1L),
                () -> assertThat(sessionResult.playerRankings()).hasSize(1),
                () -> assertThat(sessionResult.playerRankings()).first()
                        .satisfies(playerRanking -> assertThat(playerRanking.rank()).isEqualTo(1))
        );
    }

    @DisplayName("두 플레이어의 플레이한 게임의 결과를 생성한다.")
    @Test
    void createLeaderboard_duo() {
        final var winnerPlayer = new SessionPlayer(1L, 2L);

        session.react(sessionPlayer, baseTime.plusSeconds(2));
        session.react(winnerPlayer, baseTime.plusSeconds(1));

        final var sessionResult = session.createLeaderboard(
                List.of(winnerPlayer, sessionPlayer),
                endTime
        );

        assertAll(
                () -> assertThat(sessionResult.sessionId()).isEqualTo(1L),
                () -> assertThat(sessionResult.playerRankings()).hasSize(2),
                () -> assertThat(sessionResult.playerRankings()).first()
                        .satisfies(playerRanking -> {
                            assertThat(playerRanking.rank()).isEqualTo(1);
                            assertThat(playerRanking.playerId()).isEqualTo(winnerPlayer.playerId());
                        }),
                () -> assertThat(sessionResult.playerRankings()).last()
                        .satisfies(playerRanking -> {
                            assertThat(playerRanking.rank()).isEqualTo(2);
                            assertThat(playerRanking.playerId()).isEqualTo(sessionPlayer.playerId());
                        })
        );
    }
}
