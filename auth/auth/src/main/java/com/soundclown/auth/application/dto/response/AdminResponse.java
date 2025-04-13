package com.soundclown.auth.application.dto.response;

import com.soundclown.auth.domain.enums.AdminRole;

public record AdminResponse(Long id, String username, String email,
                            String phoneNumber, AdminRole role) {
}
