package com.soundclown.auth.domain.enums;

public enum SubscriptionPlanEnum {
    FREE(ClientAuthority.CLIENT_BASIC),
    PLUS(ClientAuthority.CLIENT_PLUS),
    PRO(ClientAuthority.CLIENT_PRO);

    private final ClientAuthority authority;

    SubscriptionPlanEnum(ClientAuthority authority) {
        this.authority = authority;
    }

    public ClientAuthority getAuthority() {
        return authority;
    }
}