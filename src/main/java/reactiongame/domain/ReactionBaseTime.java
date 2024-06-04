package reactiongame.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static reactiongame.support.LocalDateTimes.extractMilliseconds;
import static reactiongame.support.LocalDateTimes.extractSecondInMillis;

@Embeddable
public class ReactionBaseTime {

    public static final Duration SECONDS_THRESHOLD = Duration.ofSeconds(30);
    public static final long SECONDS_THRESHOLD_IN_MILLIS = SECONDS_THRESHOLD.toMillis();
    private static final long ONE_MINUTE_IN_MILLIS = Duration.ofMinutes(1).toMillis();

    @Column(nullable = false)
    private LocalDateTime value;

    protected ReactionBaseTime() {
    }

    public ReactionBaseTime(final LocalDateTime value) {
        this.value = value.truncatedTo(ChronoUnit.MINUTES);

        if (isThirtyOrOverSeconds(value)) {
            this.value = this.value.plusMinutes(1);
        }
    }

    public static ReactionBaseTime now() {
        return new ReactionBaseTime(LocalDateTime.now());
    }

    public static boolean isThirtyOrOverSeconds(final LocalDateTime localDateTime) {
        return localDateTime.getSecond() >= SECONDS_THRESHOLD.getSeconds();
    }

    /**
     * <pre>
     *   final var x = new ReactionBaseTime(LocalDateTime.of(2012, 6, 30, 12, 00));
     *   final var y = new ReactionBaseTime(LocalDateTime.of(2012, 7, 1, 12, 00));
     *   past(x, y) == true
     *   past(x, x) == false
     *   past(y, x) == false
     * </pre>
     */
    public static ReactionBaseTime past(
            final ReactionBaseTime x,
            final ReactionBaseTime y
    ) {
        if (x.value.isBefore(y.value)) {
            return x;
        }
        return y;
    }

    public long calculateReactionTimeRateMillis(final LocalDateTime requestAt) {
        final var reactionTimeRateMillis = extractSecondInMillis(requestAt) + extractMilliseconds(requestAt);
        if (isThirtyOrOverSeconds(requestAt)) {
            return ONE_MINUTE_IN_MILLIS - reactionTimeRateMillis;
        }
        return reactionTimeRateMillis;
    }

    public boolean isAfter(final ReactionBaseTime other) {
        return isAfter(other.value);
    }

    public boolean isAfter(final LocalDateTime other) {
        return value.isAfter(other);
    }

    public boolean isBeforeOrEquals(final LocalDateTime other) {
        return value.isBefore(other) || value.isEqual(other);
    }

    public LocalDateTime toLocalDateTime() {
        return value;
    }

    public ReactionBaseTime nextMinute() {
        return new ReactionBaseTime(value.plusMinutes(1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReactionBaseTime that = (ReactionBaseTime) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
