package com.soundclown.auth.application.service;

import com.soundclown.auth.application.dto.response.SubscriptionResponse;
import com.soundclown.auth.application.repository.ClientsRepository;
import com.soundclown.auth.application.repository.SubscriptionPlanRepository;
import com.soundclown.auth.application.usecase.subscription.GetSubscriptionUseCase;
import com.soundclown.auth.application.usecase.subscription.ManageSubscriptionUseCase;
import com.soundclown.auth.domain.enums.SubscriptionPlanEnum;
import com.soundclown.auth.domain.model.client.Client;
import com.soundclown.auth.domain.model.client.SubscriptionPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService implements ManageSubscriptionUseCase, GetSubscriptionUseCase {

    private final ClientsRepository clientsRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    public SubscriptionService(
            ClientsRepository clientsRepository,
            SubscriptionPlanRepository subscriptionPlanRepository) {
        this.clientsRepository = clientsRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    public SubscriptionResponse get(Long clientId) {
        return clientsRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"))
                .getActiveSubscriptionResponse();
    }

    @Override
    @Transactional
    public SubscriptionResponse upgrade(Long clientId, SubscriptionPlanEnum newPlan) {
        Client client = clientsRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        SubscriptionPlan plan = subscriptionPlanRepository.findByName(newPlan)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        client.addSubscription(plan);
        clientsRepository.save(client);

        return get(clientId);
    }

    @Override
    @Transactional
    public void cancel(Long clientId) {
        Client client = clientsRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        SubscriptionPlan plan = subscriptionPlanRepository.findByName(SubscriptionPlanEnum.FREE)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        client.deactivateSubscription(plan);
        clientsRepository.save(client);
    }
}
