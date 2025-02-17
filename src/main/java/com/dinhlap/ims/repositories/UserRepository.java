package com.dinhlap.ims.repositories;

import com.dinhlap.ims.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    List<User> findByRole(String role);

    User findByEmail(String email);

    User findByUsername(String username);

    boolean existsByEmail(String email);

}
