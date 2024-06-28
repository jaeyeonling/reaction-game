package reactiongame.application;

import java.time.LocalDateTime;
import java.util.List;

public record SessionLeaderboardResponse(
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<SessionPlayerRankingResponse> rank
) {

}
