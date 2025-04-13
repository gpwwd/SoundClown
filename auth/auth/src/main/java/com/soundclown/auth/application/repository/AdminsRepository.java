package com.soundclown.auth.application.repository;

import com.soundclown.auth.domain.model.Admin;
import com.soundclown.auth.domain.valueobject.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminsRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUsername(Username username);
}
