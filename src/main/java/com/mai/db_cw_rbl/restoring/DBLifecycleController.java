package com.mai.db_cw_rbl.restoring;

import com.mai.db_cw_rbl.restoring.dto.DBProcessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db")
@RequiredArgsConstructor
public class DBLifecycleController {

    private final DBLifecycleService dbLifecycleService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/backup")
    public ResponseEntity<DBProcessResponse> backupCurrentDb() {
        return ResponseEntity.ok(dbLifecycleService.backupCurrentDB());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/restore")
    public ResponseEntity<DBProcessResponse> restoreCurrentDb() {
        return ResponseEntity.ok(dbLifecycleService.restoreCurrentDB());
    }
}
