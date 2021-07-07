package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.apphrmanagement.entity.Salary;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.SalaryDto;
import uz.pdp.apphrmanagement.payload.SalaryInfoDto;
import uz.pdp.apphrmanagement.service.SalaryService;

import java.util.List;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @PostMapping("/writeSalary")
    public ResponseEntity<?> writeSalary(@RequestBody SalaryDto salaryDto) {
        ApiResponse apiResponse = salaryService.writeSalary(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @PostMapping("/giveSalary")
    public ResponseEntity<?> giveSalary(@RequestBody SalaryDto salaryDto) {
        ApiResponse apiResponse = salaryService.giveSalary(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @PostMapping("/getSalaryInfo")
    public ResponseEntity<?> getSalaryInfo(@RequestBody SalaryInfoDto salaryInfoDto) {
        List<Salary> salaries = salaryService.getSalaryInfo(salaryInfoDto);
        return ResponseEntity.ok(salaries);
    }




}
