package reactiongame.application;

import org.springframework.stereotype.Component;
import reactiongame.domain.Reaction;
import reactiongame.domain.ReactionHistory;

import java.util.List;

@Component
public final class ReactionAssembler {

    public ReactionHistory toResponse(final Reaction reaction) {
        return new ReactionHistory(
                reaction.reactionTime(),
                reaction.reactionBaseTime(),
                reaction.reactionRateMillis(),
                false
        );
    }

    public List<ReactionHistory> toResponses(final List<Reaction> reactions) {
        return reactions.stream()
                .map(this::toResponse)
                .toList();
    }
}
