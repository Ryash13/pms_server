package com.jfs.pms.repository;

import com.jfs.pms.domain.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Clients, Long> {

    Optional<Clients> findByPublicId(UUID publicId);
    boolean existsByClientDomain(String clientDomain);
}
