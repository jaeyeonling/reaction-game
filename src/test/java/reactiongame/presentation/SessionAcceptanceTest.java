package reactiongame.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactiongame.application.SessionRequest;
import reactiongame.application.SessionResponse;
import reactiongame.domain.ReactionBaseTime;
import reactiongame.support.Randoms;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static reactiongame.presentation.PlayerAcceptanceTest.withAdmin;

public final class SessionAcceptanceTest extends AbstractAcceptanceTest {

    @DisplayName("세션을 생성한다.")
    @Test
    void save() {
        final var request = createSessionRequest();

        withAdmin()
                .body(request)

                .when()
                .post("/sessions")

                .then()
                .assertThat()
                .body("id", notNullValue())
                .body("title", equalTo(request.title()))
                .body("startDate", startsWith(new ReactionBaseTime(request.startDate()).toString()))
                .body("endDate", startsWith(new ReactionBaseTime(request.endDate()).toString()))
                .headers("Location", startsWith("/sessions/"))
                .statusCode(HttpStatus.CREATED.value())
                .log()
                .all();
    }

    @DisplayName("세션 목록을 조회한다.")
    @Test
    void findAll() {
        final var sessions = Stream.generate(SessionAcceptanceTest::givenSession)
                .limit(5)
                .toList();

        withAdmin()
                .when()
                .get("/sessions")

                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("title", hasItems(sessions.stream().map(SessionResponse::title).toArray()))
                .log()
                .all();
    }

    @DisplayName("게임 세션 정보를 조회한다.")
    @Test
    void findById() {
        final var session = givenSession();

        withAdmin()
                .when()
                .get("/sessions/{sessionId}", session.id())

                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(session.title()))
                .log()
                .all();
    }

    @DisplayName("세션을 삭제한다.")
    @Test
    void delete() {
        final var session = givenSession();

        withAdmin()
                .when()
                .delete("/sessions/{sessionId}", session.id())

                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log()
                .all();
    }

    public static SessionRequest createSessionRequest() {
        return createSessionRequest(
                "세션(" + Randoms.generateAlphanumeric(5) + ")",
                LocalDateTime.now().withMinute(0).withNano(0),
                LocalDateTime.now().plusHours(1).withMinute(10).withNano(0)
        );
    }

    public static SessionRequest createSessionRequest(
            String title,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return new SessionRequest(
                title,
                startDate,
                endDate
        );
    }

    public static SessionResponse givenSession() {
        return givenSession(createSessionRequest());
    }

    public static SessionResponse givenSession(final SessionRequest request) {
        return withAdmin()
                .body(request)
                .post("/sessions")
                .as(SessionResponse.class);
    }
}
