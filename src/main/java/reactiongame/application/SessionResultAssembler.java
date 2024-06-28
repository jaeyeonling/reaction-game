package reactiongame.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.domain.PlayerRepository;
import reactiongame.domain.SessionPlayerRanking;
import reactiongame.domain.SessionRepository;
import reactiongame.domain.SessionResult;

import java.util.Comparator;

@Component
public class SessionResultAssembler {

    private final SessionRepository sessionRepository;

    private final PlayerRepository playerRepository;


    SessionResultAssembler(
            final SessionRepository sessionRepository,
            final PlayerRepository playerRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public SessionLeaderboardResponse assemble(final SessionResult sessionResult) {
        final var session = sessionRepository.getById(sessionResult.sessionId());
        final var playerIdToPlayer = playerRepository.findSimpleMapEachById(sessionResult.playerIds());

        final var rank = sessionResult.playerRankings()
                .stream()
                .sorted(Comparator.comparingInt(SessionPlayerRanking::rank))
                .map(playerRanking -> {
                    final var player = playerIdToPlayer.get(playerRanking.playerId());

                    return new SessionPlayerRankingResponse(
                            player.name(),
                            playerRanking.rank(),
                            playerRanking.totalReactionRateMillis(),
                            playerRanking.reactions()
                    );
                })
                .toList();

        return new SessionLeaderboardResponse(
                session.title(),
                session.startDate(),
                session.endDate(),
                rank
        );
    }
}
