package com.mai.db_cw_rbl.RatingPackage;

import com.mai.db_cw_rbl.RatingPackage.Dto.LawyerRatingRequest;
import com.mai.db_cw_rbl.RatingPackage.Dto.LawyerRatingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class LawyerRatingController {

    private final LawyerRatingService lawyerRatingService;

    @PostMapping("/add")
    public ResponseEntity<Void> saveLawyerRating(
            @Valid @RequestBody LawyerRatingRequest ratingRequest
    ) {
        lawyerRatingService.saveLawyerRatingByAnswerId(ratingRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/lawyer/{lawyerId}")
    public ResponseEntity<LawyerRatingResponse> findRatingByLawyerId(
            @PathVariable UUID lawyerId) {
        return ResponseEntity.ok(lawyerRatingService.calculateAverageRating(lawyerId));
    }
}
