package tabom.myhands.domain.dayOff.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import tabom.myhands.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "off_type")
@Table(name = "dayoff")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DayOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dayoff_id", nullable = false)
    private Long dayOffId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isCancelled;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    private String reason;


    protected DayOff(User user, String reason) {
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.isCancelled = false;
        this.reason = reason;
    }
}
