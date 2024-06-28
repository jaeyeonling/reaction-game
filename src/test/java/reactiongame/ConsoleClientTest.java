package reactiongame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactiongame.domain.ReactionBaseTime;

import java.time.LocalDateTime;

@Disabled
class ConsoleClientTest {

    private ConsoleClient consoleClient;

    @BeforeEach
    void setUp() {
        consoleClient = new ConsoleClient(
                "http://localhost:8080",
                0,
                ""
        );
    }

    @Test
    void testMain() throws InterruptedException {
        final var session = consoleClient.findSession();

        System.out.println("게임 시작 시간: " + session.startDate());
        System.out.println("게임 종료 시간: " + session.endDate());

        while (LocalDateTime.now().isBefore(session.startDate())) {
            Thread.sleep(1);
        }

        var reactionBaseTime = new ReactionBaseTime(session.startDate());
        System.out.println("게임 시작!");
        var adjustMillis = 0L;
        while (!reactionBaseTime.isAfter(session.endDate()) && LocalDateTime.now().isBefore(session.endDate())) {
            final var reaction = consoleClient.react();
            System.out.println("반응 시간: " + reaction.reactionTime());
            adjustMillis = reaction.reactionRateMillis();
            System.out.println("반응 시간(밀리초): " + adjustMillis);

            reactionBaseTime = reactionBaseTime.nextMinute();
            Thread.sleep(60_000 - adjustMillis);
        }

        while (LocalDateTime.now().isBefore(session.endDate())) {
            Thread.sleep(1);
        }

        final var result = consoleClient.leaderboard();
        System.out.println("게임 종료!");

        System.out.println("게임 결과: " + result);
    }
}
