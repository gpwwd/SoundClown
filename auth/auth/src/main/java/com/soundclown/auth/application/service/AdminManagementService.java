package com.soundclown.auth.application.service;

import com.soundclown.auth.application.dto.response.AdminResponse;
import com.soundclown.auth.application.repository.AdminsRepository;
import com.soundclown.auth.application.usecase.management.DeleteAdminUseCase;
import com.soundclown.auth.application.usecase.management.GetAllAdminsUseCase;
import com.soundclown.auth.domain.enums.AdminRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminManagementService implements DeleteAdminUseCase, GetAllAdminsUseCase {

    private final AdminsRepository adminsRepository;

    public AdminManagementService(AdminsRepository adminsRepository) {
        this.adminsRepository = adminsRepository;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!adminsRepository.existsById(id)) {
            throw new IllegalArgumentException("Admin not found");
        }
        adminsRepository.deleteById(id);
    }

    @Override
    public List<AdminResponse> getAll() {
        return adminsRepository.findAll().stream()
                .map(admin -> new AdminResponse(
                        admin.getId(),
                        admin.getUsername().value(),
                        admin.getEmail() != null ? admin.getEmail().value() : null,
                        admin.getPhoneNumber() != null ? admin.getPhoneNumber().value() : null,
                        AdminRole.valueOf(admin.getAuthorities().getFirst())
                ))
                .toList();
    }
}
