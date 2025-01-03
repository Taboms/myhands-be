package tabom.myhands.domain.dayOff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tabom.myhands.common.properties.ResponseProperties;
import tabom.myhands.common.response.DtoResponse;
import tabom.myhands.common.response.MessageResponse;
import tabom.myhands.domain.dayOff.dto.DayOffRequest;
import tabom.myhands.domain.dayOff.dto.DayOffResponse;
import tabom.myhands.domain.dayOff.service.DayOffService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dayoff")
public class DayOffController {

    private final ResponseProperties responseProperties;
    private final DayOffService dayOffService;

    @PostMapping("")
    public ResponseEntity<MessageResponse> create(@RequestBody DayOffRequest.Create request) {
        dayOffService.createDayOff(request);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
    }

    @GetMapping("/count")
    public ResponseEntity<DtoResponse<DayOffResponse.getCount>> getCount(@RequestBody DayOffRequest.GetCount request) {
        DayOffResponse.getCount response = dayOffService.getDayOffCount(request.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(), response));
    }
}
