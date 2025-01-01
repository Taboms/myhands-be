package tabom.myhands.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tabom.myhands.domain.schedule.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ScheduleDetail {
        private String title;

        private String place;

        private int category;

        private Long userId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "Asia/Seoul")
        private LocalDateTime startAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "Asia/Seoul")
        private LocalDateTime finishAt;

        private List<CandidateResponse.Detail> candidateList;

        public static ScheduleResponse.ScheduleDetail build(Schedule schedule, List<CandidateResponse.Detail> candidateList){
            return ScheduleDetail.builder()
                    .title(schedule.getTitle())
                    .place(schedule.getPlace())
                    .category(schedule.getCategory())
                    .userId(schedule.getUserId())
                    .startAt(schedule.getStartAt())
                    .finishAt(schedule.getFinishAt())
                    .candidateList(candidateList)
                    .build();
        }
    }
}
