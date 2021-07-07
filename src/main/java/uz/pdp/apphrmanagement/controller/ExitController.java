package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.service.ExitService;


@RestController
@RequestMapping("/api/exit")
public class ExitController {
    @Autowired
    ExitService exitService;

    @PreAuthorize("hasAnyRole('ROLE_STUFF')")
    @PostMapping
    public ResponseEntity<?> exit() {
        ApiResponse apiResponse = exitService.exit();
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }
}
