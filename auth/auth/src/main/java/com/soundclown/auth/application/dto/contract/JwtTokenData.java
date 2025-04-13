package com.soundclown.auth.application.dto.contract;

import java.util.List;

public record JwtTokenData(String username, List<String> authorities) {}
