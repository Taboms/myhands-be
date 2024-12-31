package tabom.myhands.domain.dayOff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tabom.myhands.common.properties.ResponseProperties;
import tabom.myhands.common.response.DtoResponse;
import tabom.myhands.common.response.MessageResponse;
import tabom.myhands.domain.dayOff.dto.DayOffRequest;
import tabom.myhands.domain.dayOff.service.DayOffService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dayoff")
public class DayOffController {

    private final DayOffService dayOffService;
    private final ResponseProperties responseProperties;

    @PostMapping("")
    public ResponseEntity<MessageResponse> create(@RequestBody DayOffRequest.Create request) {
        dayOffService.createDayOff(request);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
    }
}
