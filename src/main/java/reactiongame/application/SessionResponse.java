package reactiongame.application;

import java.time.LocalDateTime;

public record SessionResponse(
        long id,
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

}
