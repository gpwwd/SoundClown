package com.soundclown.auth.presentation.controller;

import com.soundclown.auth.application.dto.request.subscription.UpgradeSubscriptionRequest;
import com.soundclown.auth.application.dto.response.SubscriptionResponse;
import com.soundclown.auth.application.usecase.subscription.GetSubscriptionUseCase;
import com.soundclown.auth.application.usecase.subscription.ManageSubscriptionUseCase;
import com.soundclown.common.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final ManageSubscriptionUseCase manageSubscriptionUseCase;
    private final GetSubscriptionUseCase getSubscriptionUseCase;

    public SubscriptionController(
            ManageSubscriptionUseCase manageSubscriptionUseCase,
            GetSubscriptionUseCase getSubscriptionUseCase
    ) {
        this.manageSubscriptionUseCase = manageSubscriptionUseCase;
        this.getSubscriptionUseCase = getSubscriptionUseCase;
    }

    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    @PostMapping("/upgrade")
    public ResponseEntity<SubscriptionResponse> upgradeSubscription(@CurrentUser Long clientId,
                                                    @RequestBody UpgradeSubscriptionRequest request) {
        SubscriptionResponse response = manageSubscriptionUseCase.upgrade(clientId, request.plan());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelSubscription(@CurrentUser Long clientId) {
        manageSubscriptionUseCase.cancel(clientId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    @GetMapping
    public ResponseEntity<SubscriptionResponse> getSubscription(@CurrentUser Long clientId) {
        return ResponseEntity.ok(getSubscriptionUseCase.get(clientId));
    }

}
