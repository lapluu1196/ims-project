package com.dinhlap.ims.services.impl;

import com.dinhlap.ims.dtos.email.EmailDTO;
import com.dinhlap.ims.dtos.user.UserDTO;
import com.dinhlap.ims.entities.PasswordResetToken;
import com.dinhlap.ims.entities.User;
import com.dinhlap.ims.exceptions.AppException;
import com.dinhlap.ims.exceptions.ErrorCode;
import com.dinhlap.ims.repositories.PasswordResetTokenRepository;
import com.dinhlap.ims.repositories.UserRepository;
import com.dinhlap.ims.services.EmailService;
import com.dinhlap.ims.services.LogService;
import com.dinhlap.ims.services.UserService;
import com.dinhlap.ims.utils.PasswordGenerateUtil;
import com.dinhlap.ims.utils.UsernameGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private LogService logService;

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        var users = userRepository.findAll(pageable);

        return users.map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setGender(user.getGender());
            userDTO.setDepartment(user.getDepartment());
            userDTO.setRole(user.getRole());
            userDTO.setDateOfBirth(user.getDateOfBirth());
            userDTO.setAddress(user.getAddress());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setStatus(user.getStatus());
            userDTO.setNotes(user.getNotes());
            return userDTO;
        });
    }

    @Override
    public Page<UserDTO> findAll(String search, String role, Pageable pageable) {
        Specification<User> spec = (Specification<User>) (root, query, criteriaBuilder) -> {
            if ((search == null || search.isEmpty()) && (role == null || role.isEmpty())) {
                return null;
            }

            if (search == null || search.isEmpty()) {
                return criteriaBuilder.equal(root.get("role"), role);
            }

            if (role == null || role.isEmpty()) {
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("username"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("email"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("phoneNumber"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("role"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("status"), "%" + search + "%")
                );
            }

            return criteriaBuilder.and(
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("username"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("email"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("phoneNumber"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("role"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("status"), "%" + search + "%")
                    ),
                    criteriaBuilder.equal(root.get("role"), role)
            );
        };

        var users = userRepository.findAll(spec, pageable);

        return users.map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setGender(user.getGender());
            userDTO.setDepartment(user.getDepartment());
            userDTO.setRole(user.getRole());
            userDTO.setDateOfBirth(user.getDateOfBirth());
            userDTO.setAddress(user.getAddress());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setStatus(user.getStatus());
            userDTO.setNotes(user.getNotes());
            return userDTO;
        });
    }

    @Override
    public UserDTO findById(Long id) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setDepartment(user.getDepartment());
        userDTO.setRole(user.getRole());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setStatus(user.getStatus());
        userDTO.setNotes(user.getNotes());

        return userDTO;
    }

    @Override
    public String save(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        List<User> users = userRepository.findAll();

        long nextId = 0L;

        if (users.isEmpty()) {
            nextId = 1L;
        } else {
            nextId = users.get(users.size() - 1).getUserId() + 1;
        }

        String password = PasswordGenerateUtil.passwordGenerate();

        User user = new User();
        user.setUsername(UsernameGenerateUtil.usernameGenerate(userDTO.getFullName(), nextId));
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        user.setDepartment(userDTO.getDepartment());
        user.setRole(userDTO.getRole());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setAddress(userDTO.getAddress());
        user.setStatus(userDTO.getStatus());
        user.setNotes(userDTO.getNotes());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        if (savedUser.getUserId() != null) {
            EmailDTO emailDTO = EmailDTO.builder()
                    .subject("no-reply-email-IMS-system")
                    .from("interviewmanagementsystem.team3@gmail.com")
                    .to(user.getEmail())
                    .data(Map.of("username", user.getUsername(), "password", password))
                    .build();

            String result = emailService.sendEmail(emailDTO, "", "email-user-create-template");

            logService.logAction("Create user", "User", savedUser.getUserId(), "Create new user with userId: " + savedUser.getUserId());

            return "Successfully created user!";
        }
        return "Failed to create user!";
    }

    @Override
    public String update(UserDTO userDTO) {

        User user = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        // Kiểm tra nếu email thay đổi, và nếu có trùng lặp với người dùng khác thì ném ngoại lệ
        if (!user.getEmail().equals(userDTO.getEmail())) {
            User userWithSameEmail = userRepository.findByEmail(userDTO.getEmail());
            // Kiểm tra nếu email đã được sử dụng bởi một người dùng khác, không phải chính người dùng hiện tại
            if (userWithSameEmail != null && !userWithSameEmail.getUserId().equals(user.getUserId())) {
                throw new IllegalArgumentException("Email has been used!");
            }

            user.setEmail(userDTO.getEmail());
        }

        // Cập nhật các thông tin khác
        user.setFullName(userDTO.getFullName());
        user.setGender(userDTO.getGender());
        user.setDepartment(userDTO.getDepartment());
        user.setRole(userDTO.getRole());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setAddress(userDTO.getAddress());
        user.setStatus(userDTO.getStatus());
        user.setNotes(userDTO.getNotes());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        if (savedUser.getUserId() != null) {

            logService.logAction("Update user", "User", savedUser.getUserId(), "Old user: " + userDTO.toString() + ", New user: " + savedUser.toString());

            return "Change has been successfully updated!";
        }

        return "Fail to updated change!";
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Override
    public String updateStatus(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        if ("Active".equals(user.getStatus())) {
            user.setStatus("Inactive");
        } else {
            user.setStatus("Active");
        }

        userRepository.save(user);

        logService.logAction("Update user status", "User", user.getUserId(), "User status: " + user.getStatus());

        return user.getStatus();
    }

    @Override
    public UserDTO findByUsername(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setDepartment(user.getDepartment());
        userDTO.setRole(user.getRole());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setStatus(user.getStatus());
        userDTO.setNotes(user.getNotes());

        return userDTO;
    }

    @Override
    public UserDTO findByEmail(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setDepartment(user.getDepartment());
        userDTO.setRole(user.getRole());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setStatus(user.getStatus());
        userDTO.setNotes(user.getNotes());

        return userDTO;
    }

    @Override
    public List<UserDTO> getRecruiters() {
        List<User> users = userRepository.findByRole("Recruiter");

        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        return users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setGender(user.getGender());
            userDTO.setDepartment(user.getDepartment());
            userDTO.setRole(user.getRole());
            userDTO.setDateOfBirth(user.getDateOfBirth());
            userDTO.setAddress(user.getAddress());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setStatus(user.getStatus());
            userDTO.setNotes(user.getNotes());
            return userDTO;
        }).toList();
    }

    @Override
    public List<UserDTO> getInterviewers() {
        List<User> users = userRepository.findByRole("Interviewer");

        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        return users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setGender(user.getGender());
            userDTO.setDepartment(user.getDepartment());
            userDTO.setRole(user.getRole());
            userDTO.setDateOfBirth(user.getDateOfBirth());
            userDTO.setAddress(user.getAddress());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setStatus(user.getStatus());
            userDTO.setNotes(user.getNotes());
            return userDTO;
        }).toList();
    }

    public void createPasswordResetTokenForUser(String email, String resetUrl, String token) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        // save token
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(1440));

        passwordResetTokenRepository.save(passwordResetToken);

        // send email
        EmailDTO emailDTO = EmailDTO.builder()
                .subject("Password Reset")
                .from("interviewmanagementsystem.team3@gmail.com")
                .to(user.getEmail())
                .data(Map.of("resetEmail", email, "resetUrl", resetUrl))
                .build();
        String result = emailService.sendEmail(emailDTO, "", "email-user-password-reset-template");
    }

    @Override
    public String updatePassword(Long id, String password) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        String oldPassword = user.getPassword();

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        String newPassword = user.getPassword();

        if (!oldPassword.equals(newPassword)) {

            logService.logAction("Update user password", "User", user.getUserId(), "User updated password");

            return "Password has been updated successfully!";
        }

        return "Password has been updated failed!";
    }
}
