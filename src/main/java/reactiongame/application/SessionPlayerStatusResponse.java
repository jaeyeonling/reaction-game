package reactiongame.application;

import reactiongame.domain.ReactionHistory;

import java.util.List;

public record SessionPlayerStatusResponse(
        String playerName,
        List<ReactionHistory> reactions
) {

}
