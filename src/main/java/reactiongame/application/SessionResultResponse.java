package reactiongame.application;

import java.time.LocalDateTime;
import java.util.List;

public record SessionResultResponse(
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<ScoreItemResponse> rank
) {

}
