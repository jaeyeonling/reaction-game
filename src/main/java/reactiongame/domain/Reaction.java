package reactiongame.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class Reaction {

    @Column(nullable = false)
    private LocalDateTime reactionTime;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "reaction_base_time", nullable = false))
    private ReactionBaseTime reactionBaseTime;

    @Column(nullable = false)
    private long reactionRateMillis;

    protected Reaction() {
    }

    public Reaction(final LocalDateTime reactionTime) {
        this.reactionTime = reactionTime;
        this.reactionBaseTime = new ReactionBaseTime(reactionTime);
        this.reactionRateMillis = reactionBaseTime.calculateReactionTimeRateMillis(reactionTime);
    }

    public boolean matchBaseTime(final Reaction reaction) {
        return matchBaseTime(reaction.reactionBaseTime);
    }

    public boolean matchBaseTime(final ReactionBaseTime baseTime) {
        return reactionBaseTime.equals(baseTime);
    }

    public LocalDateTime reactionTime() {
        return reactionTime;
    }

    public LocalDateTime reactionBaseTime() {
        return reactionBaseTime.toLocalDateTime();
    }

    public long reactionRateMillis() {
        return reactionRateMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reaction reaction = (Reaction) o;
        return reactionRateMillis == reaction.reactionRateMillis && Objects.equals(reactionTime, reaction.reactionTime) && Objects.equals(reactionBaseTime, reaction.reactionBaseTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reactionTime, reactionBaseTime, reactionRateMillis);
    }

    @Override
    public String toString() {
        return "Reaction{" +
                "reactionTime=" + reactionTime +
                ", reactionBaseTime=" + reactionBaseTime +
                ", reactionRateMillis=" + reactionRateMillis +
                '}';
    }
}
