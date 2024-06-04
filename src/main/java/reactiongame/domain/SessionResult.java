package reactiongame.domain;

import java.util.List;

public record SessionResult(
        long sessionId,
        List<SessionPlayerRanking> playerRankings
) {

    public List<Long> playerIds() {
        return playerRankings.stream()
                .map(SessionPlayerRanking::playerId)
                .toList();
    }
}
