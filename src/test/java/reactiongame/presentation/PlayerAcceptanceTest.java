package reactiongame.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactiongame.TestAdminInitializer;
import reactiongame.application.PlayerRequest;
import reactiongame.application.PlayerResponse;
import reactiongame.support.Randoms;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static reactiongame.infrastructure.security.PlayerTokenBaseAccessToken.PLAYER_TOKEN_HEADER_NAME;

public final class PlayerAcceptanceTest extends AbstractAcceptanceTest {

    @DisplayName("플레이어를 생성한다.")
    @Test
    void save() {
        final var request = createPlayerRequest();

        withAdmin()
                .body(request)

                .when()
                .post("/players")

                .then()
                .assertThat()
                .body("id", notNullValue())
                .body("name", equalTo(request.name()))
                .body("token", notNullValue())
                .statusCode(HttpStatus.OK.value())
                .log()
                .all();
    }

    @DisplayName("플레이어 목록을 조회한다.")
    @Test
    void findAll() {
        final var players = Stream.generate(PlayerAcceptanceTest::givenPlayer)
                .limit(5)
                .toList();

        withAdmin()
                .when()
                .get("/players")

                .then()
                .assertThat()
                .body("name", hasItems(players.stream().map(PlayerResponse::name).toArray()))
                .statusCode(HttpStatus.OK.value())
                .log()
                .all();
    }

    @DisplayName("플레이어를 삭제한다.")
    @Test
    void delete() {
        final var player = givenPlayer();

        withAdmin()
                .when()
                .delete("/players/{playerId}", player.id())

                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .body(equalTo(""))
                .log()
                .all();
    }

    public static PlayerRequest createPlayerRequest() {
        return createPlayerRequest(false);
    }

    public static PlayerRequest createPlayerRequest(final boolean admin) {
        return createPlayerRequest(
                "플레이어(" + Randoms.generateAlphanumeric(5) + ")",
                admin
        );
    }

    public static PlayerRequest createPlayerRequest(
            final String name,
            final boolean admin
    ) {
        return new PlayerRequest(name, admin);
    }

    public static PlayerResponse givenPlayer() {
        return givenPlayer(createPlayerRequest());
    }

    public static PlayerResponse givenAdmin() {
        return givenPlayer(createPlayerRequest(true));
    }

    public static PlayerResponse givenPlayer(final PlayerRequest playerRequest) {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(PLAYER_TOKEN_HEADER_NAME, TestAdminInitializer.ADMIN.token())
                .body(playerRequest)
                .post("/players")
                .as(PlayerResponse.class);
    }

    public static RequestSpecification withAdmin() {
        final var admin = PlayerAcceptanceTest.givenAdmin();
        return withPlayerToken(admin.token());
    }

    public static RequestSpecification withPlayer() {
        final var player = PlayerAcceptanceTest.givenPlayer();
        return withPlayerToken(player.token());
    }

    public static RequestSpecification withPlayerToken(final String playerToken) {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(PLAYER_TOKEN_HEADER_NAME, playerToken);
    }
}
