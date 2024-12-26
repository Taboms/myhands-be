package tabom.myhands.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tabom.myhands.domain.user.dto.UserRequest;
import tabom.myhands.domain.user.entity.Department;
import tabom.myhands.domain.user.entity.Role;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.domain.user.repository.DepartmentRepository;
import tabom.myhands.domain.user.repository.RoleRepository;
import tabom.myhands.domain.user.repository.UserRepository;
import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.UserApiException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final S3Service s3Service;

    @Transactional
    @Override
    public void join(UserRequest.Join request, MultipartFile photoFile) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserApiException(UserErrorCode.EMAIL_ALREADY_EXISTS);
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
        user.setPhoto(photoUrl);
        userRepository.save(user);
    }
}
