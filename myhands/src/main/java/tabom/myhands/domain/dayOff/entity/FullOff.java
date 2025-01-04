package tabom.myhands.domain.dayOff.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tabom.myhands.domain.dayOff.dto.DayOffRequest;
import tabom.myhands.domain.user.entity.User;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("FULL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FullOff extends DayOff {
    @Column(name = "start_at")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @Column(name = "finish_at")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate finishDate;

    private FullOff(User user, String reason, LocalDate startDate, LocalDate finishDate) {
        super(user, reason);
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public static FullOff createFullOff(User user, DayOffRequest.Create request) {
        return new FullOff(user, request.getReason(), request.getStartAt(), request.getFinishAt());
    }
}
