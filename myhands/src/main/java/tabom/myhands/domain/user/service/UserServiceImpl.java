package tabom.myhands.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tabom.myhands.domain.user.dto.UserRequest;
import tabom.myhands.domain.user.entity.Department;
import tabom.myhands.domain.user.entity.Role;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.domain.user.repository.DepartmentRepository;
import tabom.myhands.domain.user.repository.RoleRepository;
import tabom.myhands.domain.user.repository.UserRepository;
import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.UserApiException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public void join(UserRequest.Join request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserApiException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role role = roleRepository.findById(request.getRole())
                .orElseThrow(() -> new UserApiException(UserErrorCode.INVALID_ROLE_VALUE));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new UserApiException(UserErrorCode.INVALID_DEPARTMENT_VALUE));

        User user = User.addUser(request, department, role);
        userRepository.save(user);
    }
}
