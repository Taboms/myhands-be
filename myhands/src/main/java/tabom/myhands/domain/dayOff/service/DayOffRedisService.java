package tabom.myhands.domain.dayOff.service;

import tabom.myhands.domain.dayOff.entity.FullOff;
import tabom.myhands.domain.dayOff.entity.HalfOff;
import tabom.myhands.domain.user.entity.User;

public interface DayOffRedisService {
    public void updateDayOff(User user);

    public void addFullOff(FullOff fullOff);

    public void addHalfOff(HalfOff halfOff);

    public Float getUsedDayOffCnt(User user);
}
