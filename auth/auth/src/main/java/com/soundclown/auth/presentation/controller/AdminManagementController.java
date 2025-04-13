package com.soundclown.auth.presentation.controller;

import com.soundclown.auth.application.dto.response.AdminResponse;
import com.soundclown.auth.application.usecase.management.DeleteAdminUseCase;
import com.soundclown.auth.application.usecase.management.GetAllAdminsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/admins")
public class AdminManagementController {

    private final GetAllAdminsUseCase getAllAdminsUseCase;
    private final DeleteAdminUseCase deleteAdminUseCase;

    @Autowired
    public AdminManagementController(GetAllAdminsUseCase getAllAdminsUseCase,
                                     DeleteAdminUseCase deleteAdminUseCase) {
        this.getAllAdminsUseCase = getAllAdminsUseCase;
        this.deleteAdminUseCase = deleteAdminUseCase;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGER', 'ADMIN_GOD')")
    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAdmins() {
        return ResponseEntity.ok(getAllAdminsUseCase.getAll());
    }

    @PreAuthorize("hasAuthority('ADMIN_GOD')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        deleteAdminUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
