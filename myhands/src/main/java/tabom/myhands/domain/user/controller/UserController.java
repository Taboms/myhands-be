package tabom.myhands.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tabom.myhands.common.config.security.TokenUtils;
import tabom.myhands.common.properties.ResponseProperties;
import tabom.myhands.common.response.DtoResponse;
import tabom.myhands.common.response.MessageResponse;
import tabom.myhands.domain.user.dto.UserRequest;
import tabom.myhands.domain.user.dto.UserResponse;
import tabom.myhands.domain.user.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ResponseProperties responseProperties;

    @PostMapping(value ="/join", consumes = "multipart/form-data")
    public ResponseEntity<MessageResponse> joinUser(
            @ModelAttribute UserRequest.Join request,
            @RequestPart("photo") MultipartFile photoFile
    ) {
        userService.join(request, photoFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageResponse.of(HttpStatus.CREATED, responseProperties.getSuccess()));
    }

    @PostMapping("/login")
    public ResponseEntity<DtoResponse<UserResponse.Login>> loginUser(@RequestBody UserRequest.Login request) {
        UserResponse.Login response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(), response));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser(HttpServletRequest request, @RequestHeader("Authorization") String accessTokenHeader) {
        Long userId = (Long) request.getAttribute("userId");
        String accessToken = TokenUtils.extractToken(accessTokenHeader);
        userService.logout(userId, accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
    }

    @GetMapping("/list")
    public ResponseEntity<DtoResponse<UserResponse.UserList>> getList(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        UserResponse.UserList response =  userService.getList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(), response));
    }

}
