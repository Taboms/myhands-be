package tabom.myhands.domain.dayOff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tabom.myhands.common.properties.DayOffProperties;
import tabom.myhands.domain.dayOff.entity.FullOff;
import tabom.myhands.domain.dayOff.entity.HalfOff;
import tabom.myhands.domain.dayOff.repository.FullOffRepository;
import tabom.myhands.domain.dayOff.repository.HalfOffRepository;
import tabom.myhands.domain.user.entity.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DayOffRedisServiceImpl implements DayOffRedisService {

    private final RedisTemplate<String, Object> dayOffRedisTemplate;
    private final FullOffRepository fullOffRepository;
    private final DayOffProperties dayOffProperties;
    private final HalfOffRepository halfOffRepository;

    @Override
    public void saveDayOffToRedis(User user) {
        String key = "dayoff:" + user.getUserId();
        HashOperations<String, String, String> hashOps = dayOffRedisTemplate.opsForHash();

        Map<String, String> existingData = hashOps.entries(key);
        Set<String> keysToDelete = new HashSet<>(existingData.keySet());

        float dayOffCount = 0f;

        List<FullOff> fullOffList = fullOffRepository.findFullOffsByUser(user);
        for (FullOff fullOff : fullOffList) {
            LocalDate startDate = fullOff.getStartDate();
            LocalDate finishDate = fullOff.getFinishDate();
            for (LocalDate date = startDate; !date.isAfter(finishDate); date = date.plusDays(1)) {
                String redisKey = dayOffProperties.getFullPrefix() + date.toString();
                hashOps.put(key, redisKey, dayOffProperties.getFull());
                keysToDelete.remove(redisKey);
                dayOffCount++;
            }
        }

        List<HalfOff> halfOffList = halfOffRepository.findHalfOffsByUser(user);
        for (HalfOff halfOff : halfOffList) {
            LocalDate requestDate = halfOff.getRequestDate();
            String redisKey = (halfOff.getMorning() ? dayOffProperties.getMorning() : dayOffProperties.getAfternoon())
                    + requestDate.toString();

            hashOps.put(key, redisKey, redisKey.contains(dayOffProperties.getMorning())
                    ? dayOffProperties.getMorning()
                    : dayOffProperties.getAfternoon());
            keysToDelete.remove(redisKey);

            dayOffCount += 0.5f;
        }

        for (String staleKey : keysToDelete) {
            hashOps.delete(key, staleKey);
        }

        String countKey = "dayOffCnt";
        hashOps.put(key, countKey, String.valueOf(dayOffCount));
    }
}
