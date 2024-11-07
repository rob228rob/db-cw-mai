package com.mai.db_cw_rbl.ConfirmedAnswersPackage;

import com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dto.ConfirmedAnswerRequest;
import com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dto.ConfirmedAnswerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/confirmed-answers")
@RequiredArgsConstructor
public class ConfirmedAnswerController {

    private final ConfirmedAnswerService confirmedAnswerService;

    @PostMapping("/confirm")
    public ResponseEntity<ConfirmedAnswerResponse> confirmAnswer(
            @RequestBody ConfirmedAnswerRequest confirmedAnswerRequest
    ) {
        return ResponseEntity.ok(confirmedAnswerService.confirm(confirmedAnswerRequest));
    }
}