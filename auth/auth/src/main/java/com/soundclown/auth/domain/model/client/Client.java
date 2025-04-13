package com.soundclown.auth.domain.model.client;

import com.soundclown.auth.domain.contracts.PasswordHasher;
import com.soundclown.auth.domain.enums.ClientAuthority;
import com.soundclown.auth.domain.model.User;
import com.soundclown.auth.domain.valueobject.Email;
import com.soundclown.auth.domain.valueobject.PhoneNumber;
import com.soundclown.auth.domain.valueobject.RawPassword;
import com.soundclown.auth.domain.valueobject.Username;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "client", schema = "auth")
@NoArgsConstructor(access = PROTECTED)
public class Client extends User {

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Subscription> subscriptions = new ArrayList<>();;

    @Override
    public List<String> getAuthorities() {
        return getActiveSubscription()
                .map(Subscription::getAuthority)
                .orElse(List.of(ClientAuthority.CLIENT_BASIC.name()));
    }

    @Builder(access = PRIVATE)
    private Client(
            Long id, Username username, Email email,
            PhoneNumber phoneNumber, String passwordHash,
            SubscriptionPlan plan) {
        super(id, username, email, phoneNumber, passwordHash);

        Subscription subscription = createSubscription(plan);
    }

    private Optional<Subscription> getActiveSubscription() {
        return subscriptions.stream()
                .filter(Subscription::isActive)
                .findFirst();
    }

    public static Client createWithEmail(
            Username username, Email email,
            RawPassword password, SubscriptionPlan plan, PasswordHasher hasher) {

        var passwordHash = hasher.hashPassword(password);

        return Client.getBaseBuilder(username, passwordHash, plan)
                .email(email)
                .build();
    }

    public static Client createWithPhoneNumber(
            Username username, PhoneNumber phoneNumber, RawPassword password,
            SubscriptionPlan plan, PasswordHasher hasher) {

        var passwordHash = hasher.hashPassword(password);

        return Client.getBaseBuilder(username, passwordHash, plan)
                .phoneNumber(phoneNumber)
                .build();
    }

    public static Client createWithPhoneNumberAndEmail(
            Username username, PhoneNumber phoneNumber, Email email, RawPassword password,
            SubscriptionPlan plan, PasswordHasher hasher) {

        var passwordHash = hasher.hashPassword(password);

        return Client.getBaseBuilder(username, passwordHash, plan)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();
    }

    private static ClientBuilder getBaseBuilder(
            Username username, String passwordHash,
            SubscriptionPlan plan) {
        return Client.builder()
                .username(username)
                .passwordHash(passwordHash)
                .plan(plan);
    }

    public Subscription createSubscription(SubscriptionPlan plan) {
        Subscription subscription = Subscription.createSubscription(plan, this);
        this.subscriptions.add(subscription);
        return subscription;
    }
}
