package tabom.myhands.domain.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tabom.myhands.domain.schedule.dto.ScheduleRequest;
import tabom.myhands.domain.user.entity.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="schedule_id", nullable = false)
    private Long scheduleId;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String title;

    @Column(name = "start_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startAt;

    @Column(name = "finish_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishAt;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String place;

    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "DATETIME")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int category;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public static Schedule ScheduleCreate(ScheduleRequest.Create request, Long userId){
        return Schedule.builder()
                .title(request.getTitle())
                .startAt(request.getStartAt())
                .finishAt(request.getFinishAt())
                .place(request.getPlace())
                .category(request.getCategory())
                .userId(userId)
                .build();
    }
}
