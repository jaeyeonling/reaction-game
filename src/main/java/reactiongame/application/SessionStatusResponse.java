package reactiongame.application;

import reactiongame.domain.ReactionHistory;

import java.time.LocalDateTime;
import java.util.List;

public record SessionStatusResponse(
        long id,
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<SessionPlayerStatusResponse> playerStatuses
) {

    public record SessionPlayerStatusResponse(
            String playerName,
            List<ReactionHistory> reactions
    ) {

    }
}
