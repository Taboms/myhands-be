package tabom.myhands.domain.dayOff.service;

import tabom.myhands.domain.dayOff.dto.DayOffRequest;

public interface DayOffService {
    void createDayOff(DayOffRequest.Create request);
}
