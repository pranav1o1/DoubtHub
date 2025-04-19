package com.DoubtHub.Chat_Room.repository;

import com.DoubtHub.Chat_Room.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailId(String emailId);
    Optional<User> findByName(String name);
    Optional<User> findByNameIgnoreCaseOrEmailIdIgnoreCase(String name, String emailId);
    //Optional<User> findById(UUID id);
    boolean existsByEmailIdIgnoreCase(String emailId);

    @Override
    Page<User> findAll(Pageable pageable);
}
