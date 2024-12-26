package tabom.myhands.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tabom.myhands.domain.user.dto.UserRequest;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.domain.user.repository.UserRepository;
import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.UserApiException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void join(UserRequest.Join request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserApiException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.addUser(request);
        userRepository.save(user);
    }
}
