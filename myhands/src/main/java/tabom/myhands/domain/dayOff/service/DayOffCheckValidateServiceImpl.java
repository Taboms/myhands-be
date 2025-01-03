package tabom.myhands.domain.dayOff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tabom.myhands.common.properties.DayOffProperties;
import tabom.myhands.domain.dayOff.dto.DayOffRequest;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.error.errorcode.DayOffErrorCode;
import tabom.myhands.error.exception.DayOffApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DayOffCheckValidateServiceImpl implements DayOffCheckValidateService {

    private final DayOffProperties dayOffProperties;
    private final RedisTemplate<String, Object> dayOffRedisTemplate;
    private final DayOffRedisService dayOffRedisService;

    @Override
    public void checkNull(DayOffRequest.Create request) {
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

    @Override
    public void validateFullOffInput(User user, DayOffRequest.Create request) {
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

    @Override
    public void validateHalfOffInput(User user, DayOffRequest.Create request) {
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

    @Override
    public void duplicateCheck(User user, DayOffRequest.Create request) {
        String key = dayOffProperties.getRedisKeyPrefix() + user.getUserId();
        HashOperations<String, String, String> hashOps = dayOffRedisTemplate.opsForHash();

        if (request.getOffType().equals("FULL")) {
            LocalDate startAt = request.getStartAt();
            LocalDate finishAt = request.getFinishAt();

            for (LocalDate date = startAt; !date.isAfter(finishAt); date = date.plusDays(1)) {
                String fullOff = hashOps.get(key, dayOffProperties.getFullPrefix() + date.toString());
                String morning = hashOps.get(key, dayOffProperties.getMorningPrefix() + date.toString());
                String afternoon = hashOps.get(key, dayOffProperties.getAfternoonPrefix() + date.toString());
                if (fullOff != null || morning != null || afternoon != null) {
                    throw new DayOffApiException(DayOffErrorCode.DUPLICATE_DAY_OFF_REQUEST);
                }

            }
        } else if (request.getOffType().equals("HALF")) {
            LocalDate requestDate = request.getRequestDate();
            String value = hashOps.get(key, dayOffProperties.getFullPrefix() + requestDate.toString());
            if (value != null) { // 당일 휴가가 있는 경우
                throw new DayOffApiException(DayOffErrorCode.DUPLICATE_DAY_OFF_REQUEST);
            }

            String hashKey;
            if (request.getMorning()) {
                hashKey = dayOffProperties.getMorningPrefix() + requestDate.toString();
            } else {
                hashKey = dayOffProperties.getAfternoonPrefix() + requestDate.toString();
            }

            value = hashOps.get(key, hashKey);
            if (value != null) {
                throw new DayOffApiException(DayOffErrorCode.DUPLICATE_DAY_OFF_REQUEST);
            }
        }
    }

    @Override
    public void dayOffCntCheck(User user, DayOffRequest.Create request) {
        Float usedDayOffCnt = dayOffRedisService.getDayOffCnt(user);
        Float allDayOffCnt = user.getDayOffCnt();

        if (request.getOffType().equals("FULL")) {
            LocalDate startAt = request.getStartAt();
            LocalDate finishAt = request.getFinishAt();
            long requestDays = ChronoUnit.DAYS.between(startAt, finishAt) + 1;

            if (usedDayOffCnt + requestDays > allDayOffCnt) {
                throw new DayOffApiException(DayOffErrorCode.PERIOD_LIMIT_EXCEEDED);
            }
        } else if (request.getOffType().equals("HALF")) {
            if (usedDayOffCnt + 0.5f > allDayOffCnt) {
                throw new DayOffApiException(DayOffErrorCode.PERIOD_LIMIT_EXCEEDED);
            }
        }
    }
}
