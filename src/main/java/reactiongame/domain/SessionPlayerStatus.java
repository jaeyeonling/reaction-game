package reactiongame.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactiongame.infrastructure.web.ReactionGameException;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableSet;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.INTERNAL_SERVER_ERROR;

public record SessionPlayerStatus(
        long playerId,
        List<ReactionHistory> reactions
) {

    private static final Logger log = LoggerFactory.getLogger(SessionPlayerStatus.class);

    public SessionPlayerStatus {
        final var reactionBaseTimes = reactions.stream()
                .map(ReactionHistory::reactionBaseTime)
                .collect(toUnmodifiableSet());
        if (reactions.size() != reactionBaseTimes.size()) {
            log.error("Duplicated reactionBaseTime [reactions={}]", reactions);
            throw new ReactionGameException(INTERNAL_SERVER_ERROR);
        }
    }

    public long totalReactionRateMillis() {
        return reactions.stream()
                .mapToLong(ReactionHistory::reactionRateMillis)
                .reduce(0L, Long::sum);
    }
}
