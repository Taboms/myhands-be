package tabom.myhands.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tabom.myhands.common.config.security.JwtTokenProvider;
import tabom.myhands.domain.dayOff.service.DayOffRedisService;
import tabom.myhands.domain.auth.service.RedisService;
import tabom.myhands.domain.user.dto.UserRequest;
import tabom.myhands.domain.user.dto.UserResponse;
import tabom.myhands.domain.user.entity.Department;
import tabom.myhands.domain.user.entity.Role;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.domain.user.repository.DepartmentRepository;
import tabom.myhands.domain.user.repository.RoleRepository;
import tabom.myhands.domain.user.repository.UserRepository;
import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.UserApiException;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final DayOffRedisService dayOffRedisService;
    private final RedisService redisService;

    @Transactional
    @Override
    public void join(UserRequest.Join request, MultipartFile photoFile) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserApiException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.findByEmployeeNum(request.getEmployeeNum()).isPresent()) {
            throw new UserApiException(UserErrorCode.EMPLOYEE_NUM_ALREADY_EXISTS);
        }

        Role role = roleRepository.findById(request.getRole())
                .orElseThrow(() -> new UserApiException(UserErrorCode.INVALID_ROLE_VALUE));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new UserApiException(UserErrorCode.INVALID_DEPARTMENT_VALUE));

        String photoUrl = null;
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                photoUrl = s3Service.uploadFile(photoFile);
            } catch (IOException e) {
                throw new UserApiException(UserErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        User user = User.addUser(request, department, role);
        user.updatePhoto(photoUrl);
        userRepository.save(user);
    }

    @Override
    public UserResponse.Login login(UserRequest.Login request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserApiException(UserErrorCode.LOGIN_FAILED));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new UserApiException(UserErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        return UserResponse.Login.build(accessToken, refreshToken, user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserApiException(UserErrorCode.USER_ID_NOT_FOUND));
    }

    @Override
    public void logout(Long userId, String accessToken) {
        redisService.deleteRefreshToken(userId);
        long expirationTime = jwtTokenProvider.getExpirationTime(accessToken);
        redisService.addToBlacklist(accessToken, expirationTime);
    }

    @Override
    public UserResponse.UserList getList(Long userId) {
        List<User> list = userRepository.findAllExceptCurrentUser(userId);
        return UserResponse.UserList.listBuild(list, false);
    }

    @Override
    public UserResponse.UserList getContactList() {
        List<User> list = userRepository.findAllUser();
        return UserResponse.UserList.listBuild(list, true);
    }
}
