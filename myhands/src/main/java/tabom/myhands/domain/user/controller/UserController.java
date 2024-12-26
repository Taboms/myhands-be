package tabom.myhands.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tabom.myhands.common.properties.ResponseProperties;
import tabom.myhands.common.response.MessageResponse;
import tabom.myhands.domain.user.dto.UserRequest;
import tabom.myhands.domain.user.service.UserService;
import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.UserApiException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ResponseProperties responseProperties;

    @PostMapping("/join")
    public ResponseEntity<MessageResponse> joinUser(
            @RequestPart("user") String userJson,
            @RequestPart("photo") MultipartFile photoFile
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        UserRequest.Join request;

        try {
            request = objectMapper.readValue(userJson, UserRequest.Join.class);
        } catch (JsonProcessingException e) {
            throw new UserApiException(UserErrorCode.INVALID_JSON_FORMAT);
        }

        userService.join(request, photoFile);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MessageResponse.of(HttpStatus.CREATED, responseProperties.getSuccess()));
    }

}
