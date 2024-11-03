package com.mai.db_cw_rbl.LawyerPackage;

import com.mai.db_cw_rbl.LawyerPackage.Dtos.LawyerCreationRequest;
import com.mai.db_cw_rbl.LawyerPackage.Dtos.LawyerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lawyers")
@RequiredArgsConstructor
@Validated
public class LawyerController {

    private final LawyerService lawyerService;

    @PostMapping("/create")
    public ResponseEntity<LawyerResponse> createLawyer(
          @Valid @RequestBody LawyerCreationRequest creationRequest) {

        return ResponseEntity.ok(
                lawyerService.saveLawyer(creationRequest));
    }

    @GetMapping("/get/{lawyerId}")
    public ResponseEntity<LawyerResponse> getLawyer(
            @Valid @PathVariable UUID lawyerId) {
        return ResponseEntity.ok(
                lawyerService.findLawyerById(lawyerId));
    }

    @GetMapping("/get")
    public ResponseEntity<LawyerResponse> getLawyerByUserEmail(
            @RequestParam("user_id") UUID userId
    ) {
        return ResponseEntity.ok(lawyerService.findLawyerByUserId(userId));
    }

    @GetMapping("/get-all-by")
    public ResponseEntity<List<LawyerResponse>> getAllLawyers(
            @RequestParam("batch_size") int batchSize
    ) {
        return ResponseEntity.ok(lawyerService.findAllLawyers(batchSize));
    }
}
