package reactiongame.infrastructure.persistence;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.Hibernate;

import java.util.Objects;

@MappedSuperclass
@SuppressWarnings("SpellCheckingInspection")
public abstract class AutoPrimaryPersistable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected AutoPrimaryPersistable() {
        this(null);
    }

    protected AutoPrimaryPersistable(final Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    // Note: https://youtrack.jetbrains.com/issue/JPAB-644
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) {
            return false;
        }
        final var that = (AutoPrimaryPersistable) other;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AutoPrimaryPersistable{" +
                "id=" + id +
                '}';
    }
}
