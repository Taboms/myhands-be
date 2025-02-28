package tabom.myhands.domain.schedule.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tabom.myhands.domain.schedule.dto.CandidateResponse;
import tabom.myhands.domain.schedule.dto.ScheduleRequest;
import tabom.myhands.domain.schedule.dto.ScheduleResponse;
import tabom.myhands.domain.schedule.entity.Candidate;
import tabom.myhands.domain.schedule.entity.Schedule;
import tabom.myhands.domain.schedule.repository.CandidateRepository;
import tabom.myhands.domain.schedule.repository.ScheduleRepository;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.domain.user.repository.UserRepository;
import tabom.myhands.error.errorcode.ScheduleErrorCode;
import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.ScheduleApiException;
import tabom.myhands.error.exception.UserApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;

    private final Long requester = 3L; // 요청 보낸 유저 아이디(임시)

    @Override
    @Transactional
    public void createSchedule(ScheduleRequest.Create request) {
        checkScheduleValue(request);

        Schedule schedule = Schedule.ScheduleCreate(request, requester);
        scheduleRepository.save(schedule);

        for(Long userId : request.getCandidateList()) {
            Optional<User> user = userRepository.findByUserId(userId);
            if (!user.isPresent()) {
                throw new UserApiException(UserErrorCode.USER_ID_NOT_FOUND);
            }

            Candidate candidate = Candidate.CandidateCreate(user.get(), schedule);
            candidateRepository.save(candidate);
        }
    }

    @Override
    @Transactional
    public void updateSchedule(ScheduleRequest.Create request) {
        // 기존 일정 삭제
        deleteSchedule(request.getScheduleId());

        // 새 일정 추가
        createSchedule(request);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findByScheduleId(scheduleId);
        if(!schedule.isPresent()) {
            throw new ScheduleApiException(ScheduleErrorCode.Schedule_ID_NOT_FOUND);
        }

        if(schedule.get().getUserId() != requester) {
            throw new ScheduleApiException(ScheduleErrorCode.DO_NOT_NATCH);
        }

        List<Candidate> candidates = candidateRepository.findBySchedule(schedule.get());
        for(Candidate candidate : candidates) {
            candidateRepository.delete(candidate);
        }

        scheduleRepository.delete(schedule.get());
    }

    @Override
    public ScheduleResponse.ScheduleDetail getScheduleDetail(Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findByScheduleId(scheduleId);
        if(!schedule.isPresent()) {
            throw new ScheduleApiException(ScheduleErrorCode.Schedule_ID_NOT_FOUND);
        }

        List<Candidate> candidates = candidateRepository.findBySchedule(schedule.get());

        PriorityQueue<User> pq = new PriorityQueue<>((o1, o2) -> {
           if(o1.getName().compareTo(o2.getName()) != 0) {
               return o1.getName().compareTo(o2.getName());
           }

           if(o1.getDepartment().getName().compareTo(o2.getDepartment().getName()) != 0) {
               return o1.getDepartment().getName().compareTo(o2.getDepartment().getName());
           }

            if(o1.getRole().getRoleId() - o2.getRole().getRoleId()!= 0) {
                return o1.getRole().getRoleId() - o2.getRole().getRoleId();
            }

           return o1.getEmail().compareTo(o2.getEmail());
        });

        for(Candidate c : candidates) {
            pq.add(c.getUser());
        }

        List<CandidateResponse.Detail> candidateList = new ArrayList<>();
        while(!pq.isEmpty()) {
            CandidateResponse.Detail d = CandidateResponse.Detail.build(pq.poll());
            candidateList.add(d);
        }

        return ScheduleResponse.ScheduleDetail.build(schedule.get(), candidateList);
    }

    public void checkScheduleValue(ScheduleRequest.Create request) {
        if(request.getStartAt().isAfter(request.getFinishAt())) {
            throw new ScheduleApiException(ScheduleErrorCode.INVALID_VALUE);
        }

        if(request.getTitle().length() > 50) {
            throw new ScheduleApiException(ScheduleErrorCode.INVALID_VALUE);
        }

        if(request.getPlace() != null && request.getPlace().length() > 50) {
            throw new ScheduleApiException(ScheduleErrorCode.INVALID_VALUE);
        }
    }

}
