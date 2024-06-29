package reactiongame.documentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import reactiongame.application.SessionLeaderboardResponse;
import reactiongame.application.SessionPlayerRankingResponse;
import reactiongame.application.SessionPlayerStatusResponse;
import reactiongame.application.SessionResponse;
import reactiongame.application.SessionService;
import reactiongame.domain.Reaction;
import reactiongame.domain.ReactionHistory;
import reactiongame.presentation.SessionController;
import reactiongame.support.LocalDateTimes;
import reactiongame.support.Randoms;
import reactiongame.support.Streams;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
final class SessionDocumentTest extends AbstractDocumentTest {

    private static final LocalDateTime baseTime = LocalDateTime.of(1993, 7, 20, 12, 30, 0, 0);

    @MockBean
    private SessionService sessionService;

    @DisplayName("게임 세션 정보를 조회한다.")
    @Test
    void findById() throws Exception {
        final var response = createSessionResponse();

        when(sessionService.findById(anyLong()))
                .thenReturn(response);

        mockMvc.perform(
                get("/sessions/{sessionId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(response))
        ).andDo(
                document(
                        "sessions/find-by-id",
                        "게임 세션 정보를 조회한다. startDate와 endDate 시간 사이 매분에 맞춰 반응을 요청하면 된다.",
                        responseFields(
                                fieldWithPath("id").description("세션 ID"),
                                fieldWithPath("title").description("세션 이름"),
                                fieldWithPath("startDate").description("세션 시작 시간"),
                                fieldWithPath("endDate").description("세션 종료 시간")
                        )
                )
        );
    }

    @DisplayName("랭킹 결과를 조회한다.")
    @Test
    void leaderboard() throws Exception {
        final var response = createResultResponse();

        when(sessionService.createLeaderboard(anyLong()))
                .thenReturn(response);

        mockMvc.perform(
                get("/sessions/{sessionId}/leaderboards", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Player-Token", "{PLAYER TOKEN}")
        ).andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(response))
        ).andDo(
                document(
                        "sessions/leaderboard",
                        "랭킹 결과를 조회한다. 랭킹 결과는 게임이 끝난 endDate 이후 확인할 수 있다.",
                        authHeaders(),
                        responseFields(
                                fieldWithPath("title").description("세션 이름"),
                                fieldWithPath("startDate").description("세션 시작 시간"),
                                fieldWithPath("endDate").description("세션 종료 시간"),
                                fieldWithPath("rank").description("랭킹 목록"),
                                fieldWithPath("rank[].playerName").description("플레이어 이름"),
                                fieldWithPath("rank[].rank").description("순위"),
                                fieldWithPath("rank[].totalReactionRateMillis").description("총 반응 시간(밀리초)"),
                                fieldWithPath("rank[].reactions").description("반응 목록"),
                                fieldWithPath("rank[].reactions[].reactionTime").description("반응 시간").optional(),
                                fieldWithPath("rank[].reactions[].reactionBaseTime").description("반응 기준 시간"),
                                fieldWithPath("rank[].reactions[].reactionRateMillis").description("반응 시간(밀리초)"),
                                fieldWithPath("rank[].reactions[].miss").description("놓침 여부")
                        )
                )
        );
    }

    private SessionResponse createSessionResponse() {
        return new SessionResponse(
                1L,
                "세션(" + Randoms.generateAlphanumeric(5) + ")",
                baseTime,
                baseTime.plusMinutes(5)
        );
    }

    private SessionLeaderboardResponse createResultResponse() {
        final var players = Stream.generate(this::createPlayerStatusResponse)
                .limit(5)
                .sorted((a, b) -> {
                    final var aTotal = a.reactions().stream()
                            .map(ReactionHistory::reactionRateMillis)
                            .reduce(0L, Long::sum);
                    final var bTotal = b.reactions().stream()
                            .map(ReactionHistory::reactionRateMillis)
                            .reduce(0L, Long::sum);

                    return Long.compare(aTotal, bTotal);
                })
                .toList();
        final var scoreItemResponses = Streams.mapWithIndex(
                players.stream(),
                (index, player) -> new SessionPlayerRankingResponse(
                        player.playerName(),
                        (int) index + 1,
                        player.reactions().stream()
                                .map(ReactionHistory::reactionRateMillis)
                                .reduce(0L, Long::sum),
                        player.reactions()
                )
        ).toList();

        return new SessionLeaderboardResponse(
                "세션(" + Randoms.generateAlphanumeric(5) + ")",
                baseTime,
                baseTime.plusMinutes(5),
                scoreItemResponses
        );
    }

    private SessionPlayerStatusResponse createPlayerStatusResponse() {
        final var reactionBaseTimes = List.of(
                adjust(-2),
                adjust(-1),
                adjust(0),
                adjust(1),
                adjust(2)
        );
        return createPlayerStatusResponse(reactionBaseTimes);
    }

    private LocalDateTime adjust(final int minutes) {
        return adjust(
                minutes,
                Randoms.randomInt(-29, 29),
                Randoms.randomInt(-999, 999)
        );
    }

    private LocalDateTime adjust(
            final int minutes,
            final int seconds,
            final int millis
    ) {
        return baseTime.plusMinutes(minutes)
                .plusSeconds(seconds)
                .plusNanos(LocalDateTimes.convertMillisToNano(millis));
    }

    private SessionPlayerStatusResponse createPlayerStatusResponse(final List<LocalDateTime> reactionBaseTimes) {
        return new SessionPlayerStatusResponse(
                "플레이어(" + Randoms.generateAlphanumeric(5) + ")",
                reactionBaseTimes.stream()
                        .map(Reaction::new)
                        .map(ReactionHistory::new)
                        .toList()
        );
    }
}
