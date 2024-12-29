package tabom.myhands.domain.schedule.service;

import tabom.myhands.domain.schedule.dto.ScheduleRequest;

public interface ScheduleService {
    void createSchedule(ScheduleRequest.Create request);
    void updateSchedule(ScheduleRequest.Create request);
    void deleteSchedule(Long scheduleId);
}
