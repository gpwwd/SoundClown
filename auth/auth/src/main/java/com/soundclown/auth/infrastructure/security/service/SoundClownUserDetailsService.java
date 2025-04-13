package com.soundclown.auth.infrastructure.security.service;

import com.soundclown.auth.application.repository.UsersRepository;
import com.soundclown.auth.domain.valueobject.Username;
import com.soundclown.auth.infrastructure.security.user.SoundClownUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SoundClownUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Autowired
    public SoundClownUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var userOptional = usersRepository.findByUsername(new Username(username));
        if (userOptional.isPresent()) {
            return new SoundClownUserDetails(userOptional.get());
        }

        throw new UsernameNotFoundException("User with value '" + username + "' not found.");
    }
}