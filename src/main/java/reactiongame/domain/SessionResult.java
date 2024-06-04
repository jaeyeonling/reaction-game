package reactiongame.domain;

import java.util.List;

public record SessionResult(
        long sessionId,
        List<SessionPlayerRanking> playerRankings
) {

}
