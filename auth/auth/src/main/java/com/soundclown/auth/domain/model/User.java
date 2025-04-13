package com.soundclown.auth.domain.model;


import com.soundclown.auth.domain.contracts.PasswordHasher;
import com.soundclown.auth.domain.valueobject.Email;
import com.soundclown.auth.domain.valueobject.PhoneNumber;
import com.soundclown.auth.domain.valueobject.RawPassword;
import com.soundclown.auth.domain.valueobject.Username;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Table(name = "base_user", schema = "auth")
@Getter
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Embedded
    protected Username username;

    @Embedded
    protected Email email;

    @Embedded
    protected PhoneNumber phoneNumber;

    protected String passwordHash;

    public abstract List<String> getAuthorities();

    public void changeUsername(Username username) {
        this.username = username;
    }

    public void changeEmail(Email email) {
        this.email = email;
    }

    public void changePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changePassword(RawPassword oldPassword, RawPassword newPassword, PasswordHasher hasher) {
        var oldPasswordHash = hasher.hashPassword(oldPassword);
        var newPasswordHash = hasher.hashPassword(newPassword);
        if(!this.passwordHash.equals(oldPasswordHash)) {
            throw new IllegalArgumentException("Passwords dont match.");
        }

        this.passwordHash = newPasswordHash;
    }
}
