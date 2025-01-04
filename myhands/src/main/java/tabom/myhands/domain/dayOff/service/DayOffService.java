package tabom.myhands.domain.dayOff.service;

import tabom.myhands.domain.dayOff.dto.DayOffRequest;
import tabom.myhands.domain.dayOff.dto.DayOffResponse;

public interface DayOffService {
    void createDayOff(DayOffRequest.Create request);
    DayOffResponse.getCount getDayOffCount(Long userId);
}
