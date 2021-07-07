package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TurnstileDto;
import uz.pdp.apphrmanagement.service.TurnstileService;

@RestController
@RequestMapping("/api/turnstile")
public class TurnstileController {

    @Autowired
    TurnstileService turnstileService;

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @PostMapping
    public ResponseEntity<?> giveTurnstile(@RequestBody TurnstileDto turnstileDto) {
        ApiResponse apiResponse = turnstileService.giveTurnstile(turnstileDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }



}
