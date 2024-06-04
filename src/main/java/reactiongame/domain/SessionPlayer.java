package reactiongame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import reactiongame.infrastructure.persistence.CreateDateAuditable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_session_player", columnNames = {"session_id", "player_id"})
        }
)
@Entity
public class SessionPlayer extends CreateDateAuditable {

    @Column(nullable = false)
    private Long sessionId;

    @Column(nullable = false)
    private Long playerId;

    @Embedded
    private Reactions reactions;

    protected SessionPlayer() {
    }

    public SessionPlayer(
            final Long sessionId,
            final Long playerId
    ) {
        this(sessionId, playerId, new Reactions());
    }

    public SessionPlayer(
            final Long sessionId,
            final Long playerId,
            final Reactions reactions
    ) {
        this.sessionId = sessionId;
        this.playerId = playerId;
        this.reactions = reactions;
    }

    public Reaction react(final LocalDateTime requestAt) {
        final var reaction = new Reaction(requestAt);
        reactions.add(reaction);

        return reaction;
    }

    public List<ReactionHistory> reactions() {
        return reactions.toList()
                .stream()
                .map(ReactionHistory::new)
                .toList();
    }

    public Optional<Reaction> findReactionByBaseTime(final ReactionBaseTime baseTime) {
        return reactions.findReactionByBaseTime(baseTime);
    }

    public Long sessionId() {
        return sessionId;
    }

    public Long playerId() {
        return playerId;
    }

    @Override
    public String toString() {
        return "SessionPlayer{" +
                "sessionId=" + sessionId +
                ", playerId=" + playerId +
                ", reactions=" + reactions +
                '}';
    }
}


