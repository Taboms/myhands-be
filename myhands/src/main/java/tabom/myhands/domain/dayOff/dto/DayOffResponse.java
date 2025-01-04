package tabom.myhands.domain.dayOff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DayOffResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getCount {
        private Float totalDayOffCnt;
        private Float usedDayOffCnt;
        private Float remainingDayOffCnt;

        public static DayOffResponse.getCount build(Float totalDayOffCnt, Float usedDayOffCnt, Float remainingDayOffCnt) {
            return getCount.builder()
                    .totalDayOffCnt(totalDayOffCnt)
                    .usedDayOffCnt(usedDayOffCnt)
                    .remainingDayOffCnt(remainingDayOffCnt)
                    .build();
        }
    }
}
