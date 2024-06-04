package reactiongame.application;

import org.springframework.stereotype.Component;
import reactiongame.infrastructure.web.ReactionGameException;
import reactiongame.domain.SessionResult;

import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.UNSUPPORTED_OPERATION;

@Component
public class SessionResultAssembler {

    public SessionResultResponse assemble(final SessionResult sessionResult) {
        // TODO:
        throw new ReactionGameException(UNSUPPORTED_OPERATION);
    }
}
