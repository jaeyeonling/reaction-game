package reactiongame.application;

import reactiongame.domain.ReactionHistory;

import java.util.List;

public record SessionPlayerRankingResponse(
        String playerName,
        Integer rank,
        long totalReactionRateMillis,
        List<ReactionHistory> reactions
) {

}
