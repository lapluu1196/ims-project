package com.dinhlap.ims.services;

import com.dinhlap.ims.dtos.user.UserDTO;
import com.dinhlap.ims.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserDTO> findAll(Pageable pageable);

    Page<UserDTO> findAll(String search, String role, Pageable pageable);

    UserDTO findById(Long id);

    String save(UserDTO userDTO);

    String update(UserDTO userDTO);

    void deleteById(Long id);

    String updateStatus(Long id);

    UserDTO findByUsername(String username);

    boolean existsByEmail(String username);

    List<UserDTO> getRecruiters();

    List<UserDTO> getInterviewers();

    UserDTO findByEmail(String email);

    void createPasswordResetTokenForUser(String email, String resetUrl, String token);

    String updatePassword(Long id, String password);

    User loadUserByUsername(String username);
}
