package com.soundclown.auth.application.dto.request.subscription;

import com.soundclown.auth.domain.enums.SubscriptionPlanEnum;

public record UpgradeSubscriptionRequest (
        SubscriptionPlanEnum plan
) {
}
