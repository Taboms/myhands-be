package tabom.myhands.domain.dayOff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tabom.myhands.domain.dayOff.dto.DayOffRequest;
import tabom.myhands.domain.dayOff.dto.DayOffResponse;
import tabom.myhands.domain.dayOff.entity.FullOff;
import tabom.myhands.domain.dayOff.entity.HalfOff;
import tabom.myhands.domain.dayOff.repository.DayOffRepository;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.domain.user.service.UserService;
import tabom.myhands.error.errorcode.DayOffErrorCode;
import tabom.myhands.error.exception.DayOffApiException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DayOffServiceImpl implements DayOffService {

    private final DayOffRepository dayOffRepository;
    private final UserService userService;
    private final DayOffRedisService dayOffRedisService;
    private final DayOffCheckValidateService dayOffCheckValidateService;

    @Override
    @Transactional
    public void createDayOff(DayOffRequest.Create request) {
        dayOffCheckValidateService.checkNull(request);

        User user = userService.getUserById(request.getUserId());

        if (request.getOffType().equals("FULL")) {
            dayOffCheckValidateService.validateFullOffInput(user, request);
            dayOffCheckValidateService.duplicateCheck(user, request);
            dayOffCheckValidateService.dayOffCntCheck(user, request);

            FullOff fullOff = FullOff.createFullOff(user, request);

            dayOffRepository.save(fullOff);
            dayOffRedisService.saveDayOffToRedis(user);
        } else if (request.getOffType().equals("HALF")) {
            dayOffCheckValidateService.validateHalfOffInput(user, request);
            dayOffCheckValidateService.duplicateCheck(user, request);
            dayOffCheckValidateService.dayOffCntCheck(user, request);

            HalfOff halfOff = HalfOff.createHalfOff(user, request);

            dayOffRepository.save(halfOff);
            dayOffRedisService.saveDayOffToRedis(user);
        } else {
            throw new DayOffApiException(DayOffErrorCode.INVALID_OFF_TYPE);
        }
    }

    @Override
    public DayOffResponse.getCount getDayOffCount(Long userId) {
        User user = userService.getUserById(userId);
        Float totalDayOffCnt = user.getDayOffCnt();
        Float usedDayOffCnt = dayOffRedisService.getUsedDayOffCnt(user);
        Float remainingDayOffCnt = totalDayOffCnt - usedDayOffCnt;
        return DayOffResponse.getCount.build(totalDayOffCnt, usedDayOffCnt, remainingDayOffCnt);
    }
}
