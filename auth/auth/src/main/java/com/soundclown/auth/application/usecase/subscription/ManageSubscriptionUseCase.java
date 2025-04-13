package com.soundclown.auth.application.usecase.subscription;

import com.soundclown.auth.application.dto.response.SubscriptionResponse;
import com.soundclown.auth.domain.enums.SubscriptionPlanEnum;

public interface ManageSubscriptionUseCase {
    SubscriptionResponse upgrade(Long clientId, SubscriptionPlanEnum newPlan);
    void cancel(Long clientId);
}
