package com.soundclown.auth.application.service;

import com.soundclown.auth.application.contracts.security.JwtService;
import com.soundclown.auth.application.dto.contract.JwtTokenData;
import com.soundclown.auth.application.dto.request.ClientRegisterEmailRequest;
import com.soundclown.auth.application.dto.response.AuthResponse;
import com.soundclown.auth.application.repository.ClientsRepository;
import com.soundclown.auth.application.repository.SubscriptionPlanRepository;
import com.soundclown.auth.application.repository.UsersRepository;
import com.soundclown.auth.application.usecase.ClientRegisterUseCase;
import com.soundclown.auth.domain.contracts.PasswordHasher;
import com.soundclown.auth.domain.enums.SubscriptionPlanEnum;
import com.soundclown.auth.domain.model.client.Client;
import com.soundclown.auth.domain.model.client.SubscriptionPlan;
import com.soundclown.auth.domain.valueobject.Email;
import com.soundclown.auth.domain.valueobject.PhoneNumber;
import com.soundclown.auth.domain.valueobject.RawPassword;
import com.soundclown.auth.domain.valueobject.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService implements ClientRegisterUseCase {

    private final ClientsRepository clientsRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final UniquenessValidator uniquenessValidator;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    public RegisterService(
            ClientsRepository clientsRepository, JwtService jwtService,
            PasswordEncoder passwordEncoder, UsersRepository usersRepository,
            UniquenessValidator uniquenessValidator,
            SubscriptionPlanRepository subscriptionPlanRepository) {
        this.clientsRepository = clientsRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.uniquenessValidator = uniquenessValidator;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    @Transactional
    public AuthResponse registerWithEmail(ClientRegisterEmailRequest request) {
        var username = new Username(request.username());
        var email = new Email(request.email());
        var password = new RawPassword(request.password());

        uniquenessValidator.validateUniqueness("Username", username, usersRepository::existsByUsername);
        uniquenessValidator.validateUniqueness("Email", email, usersRepository::existsByEmail);

        var client = createClient(username, email, null, password);

        var id = clientsRepository.save(client).getId();
        var token = jwtService.generateToken(
                new JwtTokenData(client.getUsername().value(), client.getAuthorities()));

        return new AuthResponse(
                id, client.getUsername().value(),
                client.getEmail().value(),
                "", token,
                client.getAuthorities()
        );
    }

    private Client createClient(Username username, Email email,
                                PhoneNumber phoneNumber, RawPassword password) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository
                .findByName(SubscriptionPlanEnum.FREE)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Subscription plan not found: " + SubscriptionPlanEnum.FREE.name()));

        var hasher = (PasswordHasher) passwordEncoder;

        if (email != null && phoneNumber != null) {
            return Client.createWithPhoneNumberAndEmail(username, phoneNumber, email, password,
                    subscriptionPlan, hasher);
        }

        if (email != null) {
            return Client.createWithEmail(username, email, password, subscriptionPlan, hasher);
        }

        return Client.createWithPhoneNumber(username, phoneNumber, password, subscriptionPlan, hasher);
    }
}
