package tabom.myhands.domain.user.service;

import org.springframework.web.multipart.MultipartFile;
import tabom.myhands.domain.user.dto.UserRequest;
import tabom.myhands.domain.user.dto.UserResponse;
import tabom.myhands.domain.user.entity.User;

public interface UserService {
    void join(UserRequest.Join request, MultipartFile photoFile);
    UserResponse.login login (UserRequest.login request);
    User getUserById(Long userId);
}
