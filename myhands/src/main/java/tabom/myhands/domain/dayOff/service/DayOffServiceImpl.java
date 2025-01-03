package tabom.myhands.domain.dayOff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tabom.myhands.common.properties.DayOffProperties;
import tabom.myhands.domain.dayOff.dto.DayOffRequest;
import tabom.myhands.domain.dayOff.entity.FullOff;
import tabom.myhands.domain.dayOff.entity.HalfOff;
import tabom.myhands.domain.dayOff.repository.DayOffRepository;
import tabom.myhands.domain.dayOff.repository.FullOffRepository;
import tabom.myhands.domain.dayOff.repository.HalfOffRepository;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.domain.user.repository.UserRepository;
import tabom.myhands.error.errorcode.DayOffErrorCode;
import tabom.myhands.error.exception.DayOffApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DayOffServiceImpl implements DayOffService {

    private final DayOffRepository dayOffRepository;
    private final FullOffRepository fullOffRepository;
    private final HalfOffRepository halfOffRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> dayOffRedisTemplate;
    private final DayOffProperties DayOffProperties;
    private final DayOffProperties dayOffProperties;
    private final DayOffRedisService dayOffRedisService;

    @Override
    @Transactional
    public void createDayOff(DayOffRequest.Create request) {
        checkNull(request);

        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new DayOffApiException(DayOffErrorCode.USER_ID_NOT_FOUND);
        }

        User user = userOptional.get();

        if (request.getOffType().equals("FULL")) {
            validateFullOffInput(user, request);
            isDuplicated(user, request);

            FullOff fullOff = FullOff.createFullOff(user, request);

            LocalDate startAt = request.getStartAt();
            LocalDate finishAt = request.getFinishAt();

            dayOffRepository.save(fullOff);
            dayOffRedisService.saveDayOffToRedis(user);
        } else if (request.getOffType().equals("HALF")) {
            validateHalfOffInput(user, request);
            isDuplicated(user, request);

            HalfOff halfOff = HalfOff.createHalfOff(user, request);

            dayOffRepository.save(halfOff);
            dayOffRedisService.saveDayOffToRedis(user);
        } else {
            throw new DayOffApiException(DayOffErrorCode.INVALID_OFF_TYPE);
        }
    }

    private void checkNull(DayOffRequest.Create request) {
        if (request.getUserId() == null || request.getOffType() == null) {
            throw new DayOffApiException(DayOffErrorCode.REQUIRED_VALUE_IS_NULL);
        }

        if (request.getOffType().equals("FULL")) {
            if (request.getStartAt() == null || request.getFinishAt() == null) {
                throw new DayOffApiException(DayOffErrorCode.REQUIRED_VALUE_IS_NULL);
            }
        } else if (request.getOffType().equals("HALF")) {
            if (request.getMorning() == null || request.getRequestDate() == null) {
                throw new DayOffApiException(DayOffErrorCode.REQUIRED_VALUE_IS_NULL);
            }
        }
    }

    private void validateFullOffInput(User user, DayOffRequest.Create request) {
        //요청 시각이 현재보다 이전일 때
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = request.getStartAt();
        LocalDate endDate = request.getFinishAt();
        if (currentDate.isAfter(startDate) || currentDate.isAfter(endDate)) {
            throw new DayOffApiException(DayOffErrorCode.INVALID_DATE_RANGE);
        }

        //Full인 경우, 끝 < 시작일 때
        if (endDate.isBefore(startDate)) {
            throw new DayOffApiException(DayOffErrorCode.INVALID_DATE_RANGE);
        }
    }

    private void validateHalfOffInput(User user, DayOffRequest.Create request) {
        //요청 시각이 현재보다 이전인 경우
        LocalDate currentDate = LocalDate.now();
        LocalDate requestDate = request.getRequestDate();
        Boolean morning = request.getMorning();

        if (requestDate.isBefore(currentDate)) {
            throw new DayOffApiException(DayOffErrorCode.INVALID_DATE_RANGE);
        }

        if (requestDate.isEqual(currentDate)) {
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            if (hour < 12 && morning) { //현재 시각이 오전일 때, 오전 반차를 쓰는 경우
                throw new DayOffApiException(DayOffErrorCode.INVALID_DATE_RANGE);
            } else if (hour >= 12) { //현재 시각이 오후일 때, 당일 반차를 쓰는 경우
                throw new DayOffApiException(DayOffErrorCode.INVALID_DATE_RANGE);
            }
        }
    }

    private static final String REDIS_KEY_PREFIX = "dayoff:";

    private void isDuplicated(User user, DayOffRequest.Create request) {
        String key = REDIS_KEY_PREFIX + user.getUserId();
        HashOperations<String, String, String> hashOps = dayOffRedisTemplate.opsForHash();

        if (request.getOffType().equals("FULL")) {
            LocalDate startAt = request.getStartAt();
            LocalDate finishAt = request.getFinishAt();

            for (LocalDate date = startAt; !date.isAfter(finishAt); date = date.plusDays(1)) {
                String fullOff = hashOps.get(key, dayOffProperties.getFullPrefix() + date.toString());
                String morning = hashOps.get(key, dayOffProperties.getMorning() + date.toString());
                String afternoon = hashOps.get(key, dayOffProperties.getAfternoon() + date.toString());
                if (fullOff != null || morning != null || afternoon != null) {
                    throw new DayOffApiException(DayOffErrorCode.DUPLICATE_DAY_OFF_REQUEST);
                }

            }
        } else if (request.getOffType().equals("HALF")) {
            LocalDate requestDate = request.getRequestDate();
            String isMorning = request.getMorning() ? DayOffProperties.getMorning() : DayOffProperties.getAfternoon();

            String halfOff = hashOps.get(key, isMorning + requestDate.toString());
            String fullDayOff = hashOps.get(key, dayOffProperties.getFullPrefix() + requestDate.toString());
            if ("FULL".equals(fullDayOff) || isMorning.equals(halfOff)) {
                //해당 날짜에 휴가가 있거나, (오늘이 아닌) 요청 날짜 같은 시각(오전/오후)에 반차가 있는 경우
                throw new DayOffApiException(DayOffErrorCode.DUPLICATE_DAY_OFF_REQUEST);
            }
        }
    }
}
