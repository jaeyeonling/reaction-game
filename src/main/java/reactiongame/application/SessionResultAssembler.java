package reactiongame.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.domain.PlayerRepository;
import reactiongame.domain.SessionPlayer;
import reactiongame.domain.SessionPlayerRanking;
import reactiongame.domain.SessionPlayerRepository;
import reactiongame.domain.SessionRepository;
import reactiongame.domain.SessionResult;

import java.util.Comparator;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Component
public class SessionResultAssembler {

    private final SessionRepository sessionRepository;

    private final PlayerRepository playerRepository;

    private final SessionPlayerRepository sessionPlayerRepository;

    SessionResultAssembler(
            final SessionRepository sessionRepository,
            final PlayerRepository playerRepository,
            final SessionPlayerRepository sessionPlayerRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.playerRepository = playerRepository;
        this.sessionPlayerRepository = sessionPlayerRepository;
    }

    @Transactional(readOnly = true)
    public SessionResultResponse assemble(final SessionResult sessionResult) {
        // TODO: Refactor

        final var session = sessionRepository.getById(sessionResult.sessionId());
        final var playerIdToPlayer = playerRepository.findSimpleMapEachById(sessionResult.playerIds());
        final var playerIdToSessionPlayer = sessionPlayerRepository.findAllByPlayerIdIn(sessionResult.playerIds())
                .stream()
                .collect(toUnmodifiableMap(SessionPlayer::playerId, Function.identity()));

        final var rank = sessionResult.playerRankings()
                .stream()
                .sorted(Comparator.comparingInt(SessionPlayerRanking::rank))
                .map(playerRanking -> {
                    final var player = playerIdToPlayer.get(playerRanking.playerId());
                    final var sessionPlayer = playerIdToSessionPlayer.get(playerRanking.playerId());

                    return new ScoreItemResponse(
                            player.name(),
                            playerRanking.rank(),
                            session.createSessionPlayerStatus(sessionPlayer).totalReactionRateMillis(),
                            sessionPlayer.reactions()
                    );
                })
                .toList();

        return new SessionResultResponse(
                session.toString(),
                session.startDate(),
                session.endDate(),
                rank
        );
    }
}
