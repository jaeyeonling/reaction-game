package reactiongame.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;

public final class HealthAcceptanceTest extends AbstractAcceptanceTest {

    @DisplayName("서버 헬스 체크를 한다.")
    @Test
    void healthCheck() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)

                .when()
                .get("/health")

                .then()
                .assertThat()
                .body(equalTo("OK"))
                .statusCode(HttpStatus.OK.value())
                .log()
                .all();
    }
}
