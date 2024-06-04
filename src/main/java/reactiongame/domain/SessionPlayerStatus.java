package reactiongame.domain;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableSet;

public record SessionPlayerStatus(
        long playerId,
        List<ReactionHistory> reactions
) {

    public SessionPlayerStatus {
        final var reactionBaseTimes = reactions.stream()
                .map(ReactionHistory::reactionBaseTime)
                .collect(toUnmodifiableSet());
        if (reactions.size() != reactionBaseTimes.size()) {
            throw new IllegalStateException("Duplicated reactionBaseTime");
        }
    }

    public long totalReactionRateMillis() {
        return reactions.stream()
                .mapToLong(ReactionHistory::reactionRateMillis)
                .reduce(0L, Long::sum);
    }
}
