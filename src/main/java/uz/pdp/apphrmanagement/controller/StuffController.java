package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.GetInfoDto;
import uz.pdp.apphrmanagement.service.StuffService;

import java.util.List;

@RestController
@RequestMapping("/api/stuff")
public class StuffController {
    @Autowired
    StuffService stuffService;


    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR')")
    @PostMapping("/addManager")
    public ResponseEntity<?> addManager(@RequestBody User user) {
        ApiResponse apiResponse = stuffService.addManager(user);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_HR_MANAGER')")
    @PostMapping("/addStuff")
    public ResponseEntity<?> addStuff(@RequestBody User user) {
        ApiResponse apiResponse = stuffService.addStuff(user);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @GetMapping
    public ResponseEntity<?> getStuffs() {
        List<User> stuffs = stuffService.getStuffs();
        return ResponseEntity.ok(stuffs);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @PostMapping("/getInfo")
    public ResponseEntity<?> getInfoStuff(@RequestBody GetInfoDto getInfoDto) {
        ApiResponse apiResponse = stuffService.getInfoStuff(getInfoDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @PostMapping("/getInfoTasks")
    public ResponseEntity<?> getInfoTasks(@RequestBody GetInfoDto getInfoDto) {
        ApiResponse apiResponse = stuffService.getInfoTasks(getInfoDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
