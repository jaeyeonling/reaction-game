package reactiongame.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactiongame.domain.ReactionHistory;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static reactiongame.presentation.PlayerAcceptanceTest.givenPlayer;
import static reactiongame.presentation.PlayerAcceptanceTest.withPlayer;
import static reactiongame.presentation.PlayerAcceptanceTest.withPlayerToken;
import static reactiongame.presentation.SessionAcceptanceTest.givenSession;

public final class ReactionAcceptanceTest extends AbstractAcceptanceTest {

    @DisplayName("반응을 생성한다.")
    @Test
    void create() {
        final var session = givenSession();

        withPlayer()
                .when()
                .post("/sessions/{sessionId}/reactions", session.id())

                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("reactionTime", notNullValue())
                .body("reactionBaseTime", notNullValue())
                .body("reactionRateMillis", notNullValue())
                .log()
                .all();
    }

    @DisplayName("반응 목록을 조회한다.")
    @Test
    void findAll() {
        final var session = givenSession();
        final var reactions = Stream.generate(() -> givenReaction(session.id()))
                .limit(5)
                .toList();

        withPlayer()
                .when()
                .get("/sessions/{sessionId}/reactions", session.id())

                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(reactions.size()))
                .body("reactionBaseTime", hasItem(startsWith(reactions.getFirst().reactionBaseTime().toString())))
                .log()
                .all();
    }

    @DisplayName("내 반응 목록을 조회한다.")
    @Test
    void findMine() {
        final var player = givenPlayer();
        final var session = givenSession();
        final var myReaction = givenReaction(session.id(), player.token());
        final var otherReactions = Stream.generate(() -> givenReaction(session.id()))
                .limit(5)
                .toList();

        withPlayerToken(player.token())
                .when()
                .get("/sessions/{sessionId}/reactions/mine", session.id())

                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1))
                .body("reactionBaseTime", hasItem(startsWith(myReaction.reactionBaseTime().toString())))
                .log()
                .all();
    }

    public static ReactionHistory givenReaction(final long sessionId) {
        return givenReaction(sessionId, givenPlayer().token());
    }

    public static ReactionHistory givenReaction(
            final long sessionId,
            final String playerToken
    ) {
        return withPlayerToken(playerToken)
                .when()
                .post("/sessions/{sessionId}/reactions", sessionId)
                .as(ReactionHistory.class);
    }
}
