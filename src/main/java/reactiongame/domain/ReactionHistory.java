package reactiongame.domain;

import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

import static reactiongame.domain.ReactionBaseTime.SECONDS_THRESHOLD_IN_MILLIS;

public record ReactionHistory(
        @Nullable LocalDateTime reactionTime,
        LocalDateTime reactionBaseTime,
        long reactionRateMillis,
        boolean miss
) {

    public ReactionHistory(final Reaction reaction) {
        this(
                reaction.reactionTime(),
                reaction.reactionBaseTime(),
                reaction.reactionRateMillis()
        );
    }

    public ReactionHistory(
            @Nullable final LocalDateTime reactionTime,
            final LocalDateTime reactionBaseTime,
            final long reactionRateMillis
    ) {
        this(
                reactionTime,
                reactionBaseTime,
                reactionRateMillis,
                false
        );
    }

    public static ReactionHistory missed(final LocalDateTime reactionBaseTime) {
        return new ReactionHistory(
                null,
                reactionBaseTime,
                SECONDS_THRESHOLD_IN_MILLIS,
                true
        );
    }
}
