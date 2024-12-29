package tabom.myhands.domain.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tabom.myhands.common.response.DtoResponse;
import tabom.myhands.common.properties.ResponseProperties;
import tabom.myhands.common.response.MessageResponse;
import tabom.myhands.domain.schedule.dto.ScheduleRequest;
import tabom.myhands.domain.schedule.service.ScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ResponseProperties responseProperties;

    @PostMapping("")
    public ResponseEntity<MessageResponse> create(@RequestBody ScheduleRequest.Create request){
        scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
    }

    @PutMapping("")
    public ResponseEntity<MessageResponse> update(@RequestBody ScheduleRequest.Create request){
        scheduleService.updateSchedule(request);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
    }

}
