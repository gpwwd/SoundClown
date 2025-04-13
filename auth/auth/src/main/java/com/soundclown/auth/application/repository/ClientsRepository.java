package com.soundclown.auth.application.repository;

import com.soundclown.auth.domain.model.client.Client;
import com.soundclown.auth.domain.valueobject.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUsername(Username username);
}
