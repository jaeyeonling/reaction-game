package reactiongame.domain;

import java.util.List;

public record SessionPlayerRanking(
        int rank,
        long playerId,
        long totalReactionRateMillis,
        List<ReactionHistory> reactions
) {

}
