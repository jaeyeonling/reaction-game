package reactiongame.domain;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactiongame.infrastructure.web.ReactionGameException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableMap;
import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.PLAYER_NOT_FOUND;

@org.springframework.stereotype.Repository
public interface PlayerRepository extends Repository<Player, Long> {

    @Transactional
    Player save(Player player);

    @Transactional(readOnly = true)
    List<Player> findAll();

    @Transactional
    void delete(Player player);

    @Transactional(readOnly = true)
    Optional<Player> findById(long id);

    @Transactional(readOnly = true)
    Optional<Player> findByToken(String token);

    @Transactional(readOnly = true)
    long count();

    @Transactional(readOnly = true)
    List<Player> findAllByIdIn(List<Long> ids);

    @Transactional(readOnly = true)
    default Player getById(final long id) {
        return findById(id).orElseThrow(() -> new ReactionGameException(PLAYER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    default SimplePlayer getSimpleById(final long id) {
        final var player = getById(id);
        return new SimplePlayer(player.id(), player.name());
    }

    @Transactional(readOnly = true)
    default Map<Long, SimplePlayer> findSimpleMapEachById(final List<Long> ids) {
        return findAllByIdIn(ids)
                .stream()
                .map(player -> new SimplePlayer(player.id(), player.name()))
                .collect(toUnmodifiableMap(SimplePlayer::id, Function.identity()));
    }
}
