package com.soundclown.auth.domain.model.client;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "subscription", schema = "auth")
@Setter(PACKAGE)
@Getter(PACKAGE)
@NoArgsConstructor(access = PROTECTED)
class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    @Getter
    private SubscriptionPlan plan;

    @Column(name = "start_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    @Getter
    private boolean isActive;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    public List<String> getAuthority() {
        return plan.getAuthority();
    }

    public static Subscription createSubscription(SubscriptionPlan plan, Client client) {
        if (plan == null || client == null) {
            throw new IllegalArgumentException("Plan or Client cannot be null.");
        }

        LocalDateTime endDate;
        if (plan.getDurationDays() > 0) {
            endDate = LocalDateTime.now().plusDays(plan.getDurationDays());
        } else {
            endDate = null;
        }

        Subscription subscription = new Subscription();
        subscription.setClient(client);
        subscription.setPlan(plan);
        subscription.setActive(true);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(endDate);
        subscription.setCanceledAt(null);
        return subscription;
    }

    public void deactivate() {
        this.setActive(false);
        this.setCanceledAt(LocalDateTime.now());
    }
}
