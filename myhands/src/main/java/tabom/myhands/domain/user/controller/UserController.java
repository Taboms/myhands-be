package tabom.myhands.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tabom.myhands.common.properties.ResponseProperties;
import tabom.myhands.common.response.MessageResponse;
import tabom.myhands.domain.user.dto.UserRequest;
import tabom.myhands.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ResponseProperties responseProperties;

    @PostMapping("/join")
    public ResponseEntity<MessageResponse> joinUser(@RequestBody UserRequest.Join request) {
        userService.join(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MessageResponse.of(HttpStatus.CREATED, responseProperties.getSuccess()));
    }
}
