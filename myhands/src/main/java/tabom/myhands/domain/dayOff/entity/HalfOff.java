package tabom.myhands.domain.dayOff.entity;

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
@DiscriminatorValue("HALF")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HalfOff extends DayOff {

    @Column
    private Boolean morning;

    @Column(name = "request_at")
    private LocalDate requestDate;

    private HalfOff(User user, String reason, DayOffRequest.Create request) {
        super(user, reason);
        this.requestDate = request.getRequestDate();
        this.morning = request.getMorning();
    }

    public static HalfOff createHalfOff(User user, DayOffRequest.Create request) {
        return new HalfOff(user, request.getReason(), request);
    }
}
