package com.soundclown.auth.application.dto.response;

import com.soundclown.auth.domain.enums.SubscriptionPlanEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionResponse(
        Long id, SubscriptionPlanEnum plan, LocalDateTime startDate,
        LocalDateTime endDate, BigDecimal price, String description
) { }
