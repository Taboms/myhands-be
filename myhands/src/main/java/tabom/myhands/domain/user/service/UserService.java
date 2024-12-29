package tabom.myhands.domain.user.service;

import org.springframework.web.multipart.MultipartFile;
import tabom.myhands.domain.user.dto.UserRequest;

public interface UserService {
    void join(UserRequest.Join request, MultipartFile photoFile);
}
