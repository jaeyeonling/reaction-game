package reactiongame.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.OrderBy;
import jakarta.persistence.UniqueConstraint;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Embeddable
public class Reactions {

    private static final Logger log = getLogger(Reactions.class);

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "reaction",
            foreignKey = @ForeignKey(name = "fk_session_player_to_reaction"),
            uniqueConstraints = {
                    @UniqueConstraint(name = "uk_reaction", columnNames = {"session_player_id", "reaction_base_time"})
            }
    )
    @OrderBy("reaction_base_time ASC")
    private List<Reaction> reactions;

    protected Reactions() {
        this(new ArrayList<>());
    }

    public Reactions(final List<Reaction> reactions) {
        this.reactions = new ArrayList<>(reactions);
    }

    public void add(final Reaction reaction) {
        if (alreadyReacted(reaction)) {
            log.debug("Already reacted [reaction={}]", reaction);
            return;
        }
        reactions.add(reaction);
    }

    private boolean alreadyReacted(final Reaction reaction) {
        return reactions.stream()
                .anyMatch(it -> it.matchBaseTime(reaction));
    }

    public Optional<Reaction> findReactionByBaseTime(final LocalDateTime baseTime) {
        return findReactionByBaseTime(new ReactionBaseTime(baseTime));
    }

    public Optional<Reaction> findReactionByBaseTime(final ReactionBaseTime baseTime) {
        return reactions.stream()
                .filter(it -> it.matchBaseTime(baseTime))
                .findFirst();
    }

    public List<Reaction> toList() {
        return new ArrayList<>(reactions);
    }

    @Override
    public String toString() {
        return "Reactions{" +
                "reactions=" + reactions +
                '}';
    }
}
