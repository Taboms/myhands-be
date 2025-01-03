package tabom.myhands.domain.dayOff.service;

import tabom.myhands.domain.user.entity.User;

public interface DayOffRedisService {
    public void saveDayOffToRedis(User user);
    public Float getUsedDayOffCnt(User user);
}
