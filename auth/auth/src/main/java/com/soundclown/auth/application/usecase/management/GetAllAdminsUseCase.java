package com.soundclown.auth.application.usecase.management;

import com.soundclown.auth.application.dto.response.AdminResponse;

import java.util.List;

public interface GetAllAdminsUseCase {
    public List<AdminResponse> getAll();
}
