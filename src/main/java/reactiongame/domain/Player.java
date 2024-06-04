package reactiongame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import reactiongame.infrastructure.persistence.AutoPrimaryPersistable;
import reactiongame.support.Randoms;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_player_name", columnNames = {"name"}),
                @UniqueConstraint(name = "uk_player_token", columnNames = {"token"})
        }
)
@Entity
public class Player extends AutoPrimaryPersistable {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean admin;

    @Column(nullable = false)
    private String token;

    protected Player() {
    }

    public Player(final String name) {
        this(name, false);
    }

    public Player(
            final String name,
            final boolean admin
    ) {
        this(name, admin, Randoms.generateAlphanumeric(32));
    }

    Player(
            final String name,
            final boolean admin,
            final String token
    ) {
        this.name = name;
        this.admin = admin;
        this.token = token;
    }

    public String name() {
        return name;
    }

    public boolean admin() {
        return admin;
    }

    public String token() {
        return token;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + name + '\'' +
                ", admin=" + admin +
                ", token='" + token + '\'' +
                '}';
    }
}
