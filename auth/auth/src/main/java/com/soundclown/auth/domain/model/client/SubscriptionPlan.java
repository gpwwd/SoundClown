package com.soundclown.auth.domain.model.client;

import com.soundclown.auth.domain.enums.ClientAuthority;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "subscription_plan", schema = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_days")
    private int durationDays;

    int getDurationDays(){
        return durationDays;
    }

    private String description;

    List<String> getAuthority() {
        return List.of(
                mapPlanNameToAuthority().name()
        );
    }

    private ClientAuthority mapPlanNameToAuthority() {
        switch (name.toUpperCase()) {
            case "PRO": return ClientAuthority.CLIENT_PRO;
            case "PLUS": return ClientAuthority.CLIENT_PLUS;
            case "FREE": return ClientAuthority.CLIENT_BASIC;
            default: return ClientAuthority.CLIENT_BASIC;
        }
    }
}

