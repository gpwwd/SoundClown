package com.soundclown.auth.application.usecase.subscription;

import com.soundclown.auth.application.dto.response.SubscriptionResponse;

public interface GetSubscriptionUseCase {
    SubscriptionResponse get(Long clientId);
}
