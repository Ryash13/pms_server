package com.jfs.pms.repository;

import com.jfs.pms.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query(value = """
            SELECT COUNT(*) FROM JFS_PMS_USERS WHERE EMAIL = :email
            """, nativeQuery = true)
    int checkIfMailAlreadyExists(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByPublicId(UUID publicId);

    Optional<User> findByUsername(String username);
}
