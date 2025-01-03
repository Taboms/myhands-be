package tabom.myhands.domain.dayOff.service;

import tabom.myhands.domain.dayOff.dto.DayOffRequest;
import tabom.myhands.domain.user.entity.User;

public interface DayOffCheckValidateService {
    public void checkNull(DayOffRequest.Create request);
    public void validateFullOffInput(User user, DayOffRequest.Create request);
    public void validateHalfOffInput(User user, DayOffRequest.Create request);
    public void duplicateCheck(User user, DayOffRequest.Create request);
    public void dayOffCntCheck(User user, DayOffRequest.Create request);
}
