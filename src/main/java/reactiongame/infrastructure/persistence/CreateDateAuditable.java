package reactiongame.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CreateDateAuditable extends AutoPrimaryPersistable {

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    protected CreateDateAuditable() {
    }

    protected CreateDateAuditable(final Long id) {
        super(id);
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        return "CreateDateAuditable{" +
                "createdDate=" + createdDate +
                "} " + super.toString();
    }
}
