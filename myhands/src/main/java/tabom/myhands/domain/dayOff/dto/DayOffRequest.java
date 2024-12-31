package tabom.myhands.domain.dayOff.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;

public class DayOffRequest {

    @Getter
    public static class Create {
        private Long userId;

        private String offType;

        private String reason;

        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate startAt;

        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate finishAt;

        private Boolean morning;

        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate requestDate;
    }
}
