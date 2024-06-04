package reactiongame.application;

import java.time.LocalDateTime;

public record SessionRequest(
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

}
