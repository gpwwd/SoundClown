package com.soundclown.auth.domain.model;

import com.soundclown.auth.domain.contracts.PasswordHasher;
import com.soundclown.auth.domain.enums.AdminRole;
import com.soundclown.auth.domain.valueobject.Email;
import com.soundclown.auth.domain.valueobject.PhoneNumber;
import com.soundclown.auth.domain.valueobject.RawPassword;
import com.soundclown.auth.domain.valueobject.Username;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "admin", schema = "auth")
@NoArgsConstructor(access = PROTECTED)
public class Admin extends User{

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private AdminRole role = AdminRole.ADMIN_BASIC;

    @Override
    public List<String> getAuthorities() {
        return List.of(role.name());
    }

    public void changeRole(AdminRole newRole) {
        this.role = newRole;
    }

    @Builder(access = PRIVATE)
    private Admin(
            Long id, Username username, Email email, PhoneNumber phoneNumber, String passwordHash,
            AdminRole role) {
        super(id, username, email, phoneNumber, passwordHash);
        this.role = role;
    }

    public static Admin createWithEmail(
            Username username, Email email, RawPassword password,
            AdminRole role, PasswordHasher hasher) {

        var passwordHash = hasher.hashPassword(password);

        return Admin.getBaseBuilder(username, passwordHash, role)
                .email(email)
                .build();
    }

    public static Admin createWithPhoneNumber(
            Username username, PhoneNumber phoneNumber, RawPassword password,
            AdminRole role, PasswordHasher hasher) {

        var passwordHash = hasher.hashPassword(password);

        return Admin.getBaseBuilder(username, passwordHash, role)
                .phoneNumber(phoneNumber)
                .build();
    }

    private static Admin.AdminBuilder getBaseBuilder(
            Username username, String passwordHash, AdminRole role) {

        return Admin.builder()
                .username(username)
                .passwordHash(passwordHash)
                .role(role);
    }
}
