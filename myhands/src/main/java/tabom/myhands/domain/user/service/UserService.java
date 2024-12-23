package tabom.myhands.domain.user.service;

import tabom.myhands.domain.user.dto.UserRequest;
import tabom.myhands.domain.user.entity.User;

public interface UserService {
    void join(UserRequest.Join request);
    User getUserById(Long userId);
}
