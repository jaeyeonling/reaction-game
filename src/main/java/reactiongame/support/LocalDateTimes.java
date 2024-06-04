package reactiongame.support;

import java.time.LocalDateTime;

public final class LocalDateTimes {

    private LocalDateTimes() {
    }

    public static int extractSecondInMillis(final LocalDateTime localDateTime) {
        return localDateTime.getSecond() * 1_000;
    }

    public static int extractMilliseconds(final LocalDateTime localDateTime) {
        return convertNanoToMillis(localDateTime.getNano());
    }

    public static int convertNanoToMillis(final int nano) {
        return nano / 1_000_000;
    }

    public static int convertMillisToNano(final int millis) {
        return millis * 1_000_000;
    }
}
