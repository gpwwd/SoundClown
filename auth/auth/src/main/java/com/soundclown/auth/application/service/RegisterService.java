package com.soundclown.auth.application.service;

import com.soundclown.auth.application.contracts.security.JwtService;
import com.soundclown.auth.application.dto.contract.JwtTokenData;
import com.soundclown.auth.application.dto.request.register.admin.AdminRegisterEmailRequest;
import com.soundclown.auth.application.dto.request.register.admin.AdminRegisterPhoneNumberRequest;
import com.soundclown.auth.application.dto.request.register.client.ClientRegisterEmailRequest;
import com.soundclown.auth.application.dto.request.register.client.ClientRegisterPhoneNumberRequest;
import com.soundclown.auth.application.dto.response.AuthResponse;
import com.soundclown.auth.application.repository.AdminsRepository;
import com.soundclown.auth.application.repository.ClientsRepository;
import com.soundclown.auth.application.repository.SubscriptionPlanRepository;
import com.soundclown.auth.application.repository.UsersRepository;
import com.soundclown.auth.application.service.util.UniquenessValidator;
import com.soundclown.auth.application.usecase.auth.AdminRegisterUseCase;
import com.soundclown.auth.application.usecase.auth.ClientRegisterUseCase;
import com.soundclown.auth.domain.contracts.PasswordHasher;
import com.soundclown.auth.domain.enums.AdminRole;
import com.soundclown.auth.domain.enums.SubscriptionPlanEnum;
import com.soundclown.auth.domain.model.Admin;
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

import java.util.Arrays;

@Service
public class RegisterService implements ClientRegisterUseCase, AdminRegisterUseCase {

    private final ClientsRepository clientsRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final UniquenessValidator uniquenessValidator;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final AdminsRepository adminsRepository;

    @Autowired
    public RegisterService(
            ClientsRepository clientsRepository, JwtService jwtService,
            PasswordEncoder passwordEncoder, UsersRepository usersRepository,
            UniquenessValidator uniquenessValidator,
            SubscriptionPlanRepository subscriptionPlanRepository,
            AdminsRepository adminsRepository) {
        this.clientsRepository = clientsRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.uniquenessValidator = uniquenessValidator;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.adminsRepository = adminsRepository;
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

    @Override
    @Transactional
    public AuthResponse registerWithPhoneNumber(ClientRegisterPhoneNumberRequest request) {
        var username = new Username(request.username());
        var phoneNumber = new PhoneNumber(request.phoneNumber());
        var password = new RawPassword(request.password());

        uniquenessValidator.validateUniqueness("Username", username, usersRepository::existsByUsername);
        uniquenessValidator.validateUniqueness("Phone number", phoneNumber, usersRepository::existsByPhoneNumber);

        var client = createClient(username, null, phoneNumber, password);

        var id = clientsRepository.save(client).getId();
        var token = jwtService.generateToken(
                new JwtTokenData(client.getUsername().value(), client.getAuthorities()));

        return new AuthResponse(
                id, client.getUsername().value(),
                "", client.getPhoneNumber().value(),
                token, client.getAuthorities()
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

    @Override
    @Transactional
    public AuthResponse registerWithEmail(AdminRegisterEmailRequest request) {
        var username = new Username(request.username());
        var email = new Email(request.email());
        var password = new RawPassword(request.password());
        var role = request.role();

        uniquenessValidator.validateUniqueness("Username", username, usersRepository::existsByUsername);
        uniquenessValidator.validateUniqueness("Email", email, usersRepository::existsByEmail);

        var admin = createAdmin(username, email, null, password, role);
        var id = adminsRepository.save(admin).getId();
        var token = jwtService.generateToken(
                new JwtTokenData(admin.getUsername().value(), admin.getAuthorities()));

        return new AuthResponse(
                id,
                admin.getUsername().value(),
                admin.getEmail().value(),
                "",
                token,
                admin.getAuthorities()
        );
    }

    @Override
    @Transactional
    public AuthResponse registerWithPhoneNumber(AdminRegisterPhoneNumberRequest request) {
        var username = new Username(request.username());
        var phoneNumber = new PhoneNumber(request.phoneNumber());
        var password = new RawPassword(request.password());
        var role = request.role();

        uniquenessValidator.validateUniqueness("Username", username, usersRepository::existsByUsername);
        uniquenessValidator.validateUniqueness("Phone number", phoneNumber, usersRepository::existsByPhoneNumber);

        var admin = createAdmin(username, null, phoneNumber, password, role);
        var id = adminsRepository.save(admin).getId();
        var token = jwtService.generateToken(
                new JwtTokenData(admin.getUsername().value(), admin.getAuthorities()));

        return new AuthResponse(
                id,
                admin.getUsername().value(),
                "",
                admin.getPhoneNumber().value(),
                token,
                admin.getAuthorities()
        );
    }

    private Admin createAdmin(Username username, Email email, PhoneNumber phoneNumber,
                              RawPassword password, String adminRole) {
        boolean isValidRole = Arrays.stream(AdminRole.values())
                .anyMatch(role -> role.name().equals(adminRole));
        if (!isValidRole) {
            throw new IllegalArgumentException("Unknown admin role: " + adminRole);
        }

        var hasher = (PasswordHasher) passwordEncoder;

        if (email != null) {
            return Admin.createWithEmail(username, email, password,
                    AdminRole.valueOf(adminRole), hasher);
        }
        return Admin.createWithPhoneNumber(username, phoneNumber, password,
                AdminRole.valueOf(adminRole), hasher);
    }
}
