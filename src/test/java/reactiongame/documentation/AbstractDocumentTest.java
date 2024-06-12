package reactiongame.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import reactiongame.infrastructure.security.PlayerTokenBaseAccessTokenArgumentResolver;
import reactiongame.infrastructure.security.PlayerTokenBaseAccessTokenBindInterceptor;
import reactiongame.infrastructure.security.PlayerTokenBaseSecureInterceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
public abstract class AbstractDocumentTest {

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @MockBean
    private PlayerTokenBaseSecureInterceptor playerTokenBaseSecureInterceptor;

    @MockBean
    private PlayerTokenBaseAccessTokenBindInterceptor playerTokenBaseAccessTokenBindInterceptor;

    @MockBean
    private PlayerTokenBaseAccessTokenArgumentResolver playerTokenBaseAccessTokenArgumentResolver;

    @BeforeEach
    void setUp(
            final WebApplicationContext webApplicationContext,
            final RestDocumentationContextProvider restDocumentationContextProvider,
            @Autowired final ObjectMapper objectMapper
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .apply(MockMvcRestDocumentation.documentationConfiguration(
                                restDocumentationContextProvider
                        ).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
        this.objectMapper = objectMapper;

        when(playerTokenBaseSecureInterceptor.preHandle(any(), any(), any()))
                .thenReturn(true);
        when(playerTokenBaseAccessTokenBindInterceptor.preHandle(any(), any(), any()))
                .thenReturn(true);
    }

    public RequestHeadersSnippet authHeaders() {
        return requestHeaders(headerWithName("X-Player-Token").description("플레이어 토큰"));
    }
}
