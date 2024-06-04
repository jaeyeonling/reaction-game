package reactiongame.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import reactiongame.infrastructure.persistence.CreateDateAuditable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_session_title", columnNames = "title")
        }
)
@Entity
public class Session extends CreateDateAuditable {

    @Column(nullable = false)
    private String title;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "start_time", nullable = false))
    private ReactionBaseTime startDate;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "end_date", nullable = false))
    private ReactionBaseTime endDate;

    protected Session() {
    }

    public Session(
            final String title,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        this(null, title, startDate, endDate);
    }

    Session(
            final Long id,
            final String title,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        super(id);
        this.title = title;
        this.startDate = new ReactionBaseTime(startDate);
        this.endDate = new ReactionBaseTime(endDate);

    }

    public Reaction react(
            final SessionPlayer sessionPlayer,
            final LocalDateTime requestAt
    ) {
        checkAvailableTime(requestAt);
        return sessionPlayer.react(requestAt);
    }

    private void checkAvailableTime(final LocalDateTime requestAt) {
        if (notStarted(requestAt) || finished(requestAt)) {
            throw new IllegalArgumentException("Session is not available");
        }
    }

    private boolean notStarted(final LocalDateTime requestAt) {
        return startDate.isAfter(requestAt);
    }

    private boolean finished(final LocalDateTime requestAt) {
        return endDate.isBeforeOrEquals(requestAt);
    }

    public SessionStatus createSessionStatus(final List<SessionPlayer> sessionPlayers) {
        final var sessionPlayerStatuses = sessionPlayers.stream()
                .map(this::createSessionPlayerStatus)
                .toList();

        return new SessionStatus(
                id(),
                sessionPlayerStatuses
        );
    }

    public SessionPlayerStatus createSessionPlayerStatus(final SessionPlayer sessionPlayer) {
        return createSessionPlayerStatus(
                sessionPlayer,
                ReactionBaseTime.now()
        );
    }

    public SessionPlayerStatus createSessionPlayerStatus(
            final SessionPlayer sessionPlayer,
            final ReactionBaseTime requestAt
    ) {
        final var sessionPlayerStatusReactions = createSessionPlayerStatusReactions(
                sessionPlayer,
                requestAt
        );

        return new SessionPlayerStatus(
                sessionPlayer.playerId(),
                sessionPlayerStatusReactions
        );
    }

    private List<ReactionHistory> createSessionPlayerStatusReactions(
            final SessionPlayer sessionPlayer,
            final ReactionBaseTime requestAt
    ) {
        final var sessionPlayerStatusReactions = new ArrayList<ReactionHistory>();
        for (
                ReactionBaseTime current = startDate, end = ReactionBaseTime.past(requestAt, endDate);
                end.isAfter(current);
                current = current.nextMinute()
        ) {
            final var currentBaseTime = current;

            final var reaction = sessionPlayer.findReactionByBaseTime(currentBaseTime)
                    .map(ReactionHistory::new)
                    .orElseGet(() -> ReactionHistory.missed(currentBaseTime.toLocalDateTime()));
            sessionPlayerStatusReactions.add(reaction);
        }
        return sessionPlayerStatusReactions;
    }

    public SessionResult createSessionResult(final List<SessionPlayer> sessionPlayers) {
        return createSessionResult(
                sessionPlayers,
                LocalDateTime.now()
        );
    }

    public SessionResult createSessionResult(
            final List<SessionPlayer> sessionPlayers,
            final LocalDateTime requestAt
    ) {
        if (!finished(requestAt)) {
            throw new IllegalStateException("Session is not settled");
        }

        final var sessionStatus = createSessionStatus(sessionPlayers);
        return sessionStatus.toResult();
    }

    public String title() {
        return title;
    }

    public LocalDateTime startDate() {
        return startDate.toLocalDateTime();
    }

    public LocalDateTime endDate() {
        return endDate.toLocalDateTime();
    }

    @Override
    public String toString() {
        return "Session{" +
                "title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
