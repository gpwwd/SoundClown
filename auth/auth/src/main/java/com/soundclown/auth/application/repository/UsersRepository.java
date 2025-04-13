package com.soundclown.auth.application.repository;

import com.soundclown.auth.domain.model.User;
import com.soundclown.auth.domain.valueobject.Email;
import com.soundclown.auth.domain.valueobject.PhoneNumber;
import com.soundclown.auth.domain.valueobject.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(Username username);
    boolean existsByEmail(Email email);
    boolean existsByPhoneNumber(PhoneNumber phoneNumber);

    Optional<User> findByUsername(Username username);
}