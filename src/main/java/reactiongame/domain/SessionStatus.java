package reactiongame.domain;

import reactiongame.support.Streams;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public record SessionStatus(
        long sessionId,
        List<SessionPlayerStatus> playerStatuses
) {

    public SessionResult toResult() {
        var sortedPlayerByRank = playerStatuses.stream()
                .sorted((Comparator.comparingLong(SessionPlayerStatus::totalReactionRateMillis)));
        return Streams.mapWithIndex(
                sortedPlayerByRank,
                (index, playerStatus) -> new SessionPlayerRanking((int) index + 1, playerStatus.playerId())
        ).collect(collectingAndThen(
                toList(),
                playerRankings -> new SessionResult(sessionId, playerRankings)
        ));
    }

    public List<Long> playerIds() {
        return playerStatuses.stream()
                .map(SessionPlayerStatus::playerId)
                .toList();
    }
}
