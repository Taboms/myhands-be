package tabom.myhands.domain.dayOff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tabom.myhands.domain.dayOff.dto.DayOffRequest;
import tabom.myhands.domain.dayOff.entity.FullOff;
import tabom.myhands.domain.dayOff.entity.HalfOff;
import tabom.myhands.domain.dayOff.repository.DayOffRepository;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.domain.user.repository.UserRepository;
import tabom.myhands.error.errorcode.DayOffErrorCode;
import tabom.myhands.error.exception.DayOffApiException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DayOffServiceImpl implements DayOffService {

    private final DayOffRepository dayOffRepository;
    private final UserRepository userRepository;
    private final DayOffRedisService dayOffRedisService;
    private final DayOffCheckValidateService dayOffCheckValidateService;

    @Override
    @Transactional
    public void createDayOff(DayOffRequest.Create request) {
        dayOffCheckValidateService.checkNull(request);

        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new DayOffApiException(DayOffErrorCode.USER_ID_NOT_FOUND);
        }

        User user = userOptional.get();

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
}
