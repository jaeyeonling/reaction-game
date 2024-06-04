package reactiongame.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.domain.PlayerRepository;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final PlayerAssembler playerAssembler;

    PlayerService(
            final PlayerRepository playerRepository,
            final PlayerAssembler playerAssembler
    ) {
        this.playerRepository = playerRepository;
        this.playerAssembler = playerAssembler;
    }

    @Transactional
    public PlayerResponse create(final PlayerRequest request) {
        final var player = playerRepository.save(playerAssembler.toEntity(request));
        return playerAssembler.toResponse(player);
    }

    @Transactional(readOnly = true)
    public List<PlayerResponse> findAll() {
        return playerAssembler.toResponses(playerRepository.findAll());
    }

    @Transactional
    public void delete(final long playerId) {
        final var player = playerRepository.getById(playerId);
        playerRepository.delete(player);
    }
}
