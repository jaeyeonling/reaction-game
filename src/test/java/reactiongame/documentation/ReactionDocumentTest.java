package reactiongame.documentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import reactiongame.application.ReactionService;
import reactiongame.domain.Reaction;
import reactiongame.domain.ReactionHistory;
import reactiongame.presentation.ReactionController;
import reactiongame.support.LocalDateTimes;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReactionController.class)
final class ReactionDocumentTest extends AbstractDocumentTest {

    private static final LocalDateTime baseTime = LocalDateTime.of(1993, 7, 20, 12, 30, 0, 0);

    @MockBean
    private ReactionService reactionService;

    @DisplayName("반응을 생성한다.")
    @Test
    void save() throws Exception {
        final var response = createResponse(baseTime.plusSeconds(5).minusNanos(LocalDateTimes.convertMillisToNano(123)));

        when(reactionService.create(any(), anyLong()))
                .thenReturn(response);

        mockMvc.perform(
                post("/sessions/{sessionId}/reactions", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Player-Token", "{PLAYER TOKEN}")
        ).andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(response))
        ).andDo(
                document(
                        "reactions/create",
                        "요청 시간 기준으로 반응을 기록한다.",
                        responseFields(
                                fieldWithPath("reactionTime").description("반응 시간"),
                                fieldWithPath("reactionBaseTime").description("반응 기준 시간"),
                                fieldWithPath("reactionRateMillis").description("반응 시간(밀리초)"),
                                fieldWithPath("miss").description("놓침 여부")
                        )
                )
        );
    }

    @DisplayName("반응 목록을 조회한다.")
    @Test
    void findAll() throws Exception {
        final var responses = List.of(
                createResponse(baseTime.minusMinutes(1).minusSeconds(15).minusNanos(LocalDateTimes.convertMillisToNano(234))),
                createResponse(baseTime.plusNanos(LocalDateTimes.convertMillisToNano(567))),
                createResponse(baseTime.plusMinutes(2).plusSeconds(10).plusNanos(LocalDateTimes.convertMillisToNano(234)))
        );

        when(reactionService.findBySessionId(anyLong()))
                .thenReturn(responses);

        mockMvc.perform(
                get("/sessions/{sessionId}/reactions", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Player-Token", "{PLAYER TOKEN}")
        ).andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(responses))
        ).andDo(
                document(
                        "reactions/list",
                        "반응 목록을 조회한다.",
                        responseFields(
                                fieldWithPath("[].reactionTime").description("반응 시간"),
                                fieldWithPath("[].reactionBaseTime").description("반응 기준 시간"),
                                fieldWithPath("[].reactionRateMillis").description("반응 시간(밀리초)"),
                                fieldWithPath("[].miss").description("놓침 여부")
                        )
                )
        );
    }

    @DisplayName("내 반응 목록을 조회한다.")
    @Test
    void findMine() throws Exception {
        final var responses = List.of(
                createResponse(baseTime.minusMinutes(1).minusSeconds(23).minusNanos(LocalDateTimes.convertMillisToNano(456))),
                createResponse(baseTime.plusNanos(LocalDateTimes.convertMillisToNano(789))),
                createResponse(baseTime.plusMinutes(2).plusSeconds(3).plusNanos(LocalDateTimes.convertMillisToNano(45)))
        );

        when(reactionService.findBy(any(), anyLong()))
                .thenReturn(responses);

        mockMvc.perform(
                get("/sessions/{sessionId}/reactions/mine", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Player-Token", "{PLAYER TOKEN}")
        ).andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(responses))
        ).andDo(
                document(
                        "reactions/mine",
                        "내 반응 목록을 조회한다.",
                        responseFields(
                                fieldWithPath("[].reactionTime").description("반응 시간"),
                                fieldWithPath("[].reactionBaseTime").description("반응 기준 시간"),
                                fieldWithPath("[].reactionRateMillis").description("반응 시간(밀리초)"),
                                fieldWithPath("[].miss").description("놓침 여부")
                        )
                )
        );
    }

    private ReactionHistory createResponse(final LocalDateTime reactionTime) {
        return new ReactionHistory(new Reaction(reactionTime));
    }
}
