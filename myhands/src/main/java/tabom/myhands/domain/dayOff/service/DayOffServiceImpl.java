package tabom.myhands.domain.dayOff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
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
            float amount = ((float) ChronoUnit.DAYS.between(startAt, finishAt)) + 1f;

            user.updateDayOffCnt(amount);
            dayOffRepository.save(fullOff);
        } else if (request.getOffType().equals("HALF")) {
            validateHalfOffInput(user, request);
            isDuplicated(user, request);
            HalfOff halfOff = HalfOff.createHalfOff(user, request);
            user.updateDayOffCnt(0.5f);
            dayOffRepository.save(halfOff);
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
        //요청 시각이 현재보다 이전
        LocalDate currentDate = LocalDate.now();
        LocalDate requestDate = request.getRequestDate();
        Boolean morning = request.getMorning();

        if (requestDate.isBefore(currentDate)) {
            throw new DayOffApiException(DayOffErrorCode.INVALID_DATE_RANGE);
        }

        // 당일에 경우 오전, 오후 판단
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        if (requestDate.isEqual(currentDate) && hour < 12 && morning) {
            throw new DayOffApiException(DayOffErrorCode.INVALID_DATE_RANGE);
        }
    }

    private static final LocalDate BASE_DATE = LocalDate.of(2024, 1, 1);

    private void isDuplicated(User user, DayOffRequest.Create request) {
        int[][] imos = new int[2][10001]; // 1행에는 존재유무, 2행에는 오전 여부(오전:1, 오후:2)
        List<FullOff> fullOffList = fullOffRepository.findFullOffsByUser(user);
        List<HalfOff> halfOffList = halfOffRepository.findHalfOffsByUser(user);

        for (FullOff fullOff : fullOffList) {
            LocalDate startDate = fullOff.getStartDate();
            LocalDate finishDate = fullOff.getFinishDate();
            int startIndex = (int) ChronoUnit.DAYS.between(BASE_DATE, startDate);
            int finishIndex = (int) ChronoUnit.DAYS.between(BASE_DATE, finishDate);
            imos[0][startIndex]++;
            imos[0][finishIndex + 1]--;
        }

        for (HalfOff halfOff : halfOffList) {
            LocalDate requestDate = halfOff.getRequestDate();
            Boolean morning = halfOff.getMorning();
            int requestIndex = (int) ChronoUnit.DAYS.between(BASE_DATE, requestDate);
            imos[0][requestIndex]++;
            if (morning) imos[1][requestIndex] = 1;
            else imos[1][requestIndex] = 2;
        }

        for (int i = 0; i < imos[0].length - 1; i++) {
            imos[0][i + 1] += imos[0][i];
        }

        if (request.getOffType().equals("FULL")) {
            LocalDate startAt = request.getStartAt();
            LocalDate finishAt = request.getFinishAt();
            int startIndex = (int) ChronoUnit.DAYS.between(BASE_DATE, startAt);
            int finishIndex = (int) ChronoUnit.DAYS.between(BASE_DATE, finishAt);

            for (int i = startIndex; i <= finishIndex; i++) {
                if (imos[0][i] > 0) {
                    throw new DayOffApiException(DayOffErrorCode.DUPLICATE_DAY_OFF_REQUEST);
                }
            }
        } else if (request.getOffType().equals("HALF")) {
            LocalDate requestDate = request.getRequestDate();
            Boolean morning = request.getMorning();
            int requestIndex = (int) ChronoUnit.DAYS.between(BASE_DATE, requestDate);
            int morningIndex = 0;
            if (morning) morningIndex = 1;
            else morningIndex = 2;
            if (imos[0][requestIndex] > 0 && (imos[1][requestIndex] == 0 || imos[1][requestIndex] == morningIndex)) {
                throw new DayOffApiException(DayOffErrorCode.DUPLICATE_DAY_OFF_REQUEST);
            }
        }
    }
}
