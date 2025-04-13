package com.soundclown.auth.domain.model.client;

import com.soundclown.auth.domain.enums.ClientAuthority;
import com.soundclown.auth.domain.enums.SubscriptionPlanEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "subscription_plan", schema = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlanEnum name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_days")
    private int durationDays;

    int getDurationDays(){
        return durationDays;
    }

    private String description;

    List<String> getAuthority() {
        return List.of(name.getAuthority().name());
    }

    public boolean isFree() {
        return this.name.equals(SubscriptionPlanEnum.FREE);
    }
}

