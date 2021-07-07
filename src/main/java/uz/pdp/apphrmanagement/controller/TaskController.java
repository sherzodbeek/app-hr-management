package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.entity.Task;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TaskDto;
import uz.pdp.apphrmanagement.payload.TaskSetStatusDto;
import uz.pdp.apphrmanagement.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @PostMapping("/giveTasks")
    public ResponseEntity<?> giveTasks(@RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.giveTasks(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    @PreAuthorize("hasAnyRole('ROLE_STUFF')")
    @GetMapping("/showTask")
    public ResponseEntity<?> showTasks() {
        List<Task> tasks = taskService.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasAnyRole('ROLE_STUFF')")
    @PostMapping("/setStatus")
    public ResponseEntity<?> setStatus(@RequestBody TaskSetStatusDto taskSetStatusDto) {
        ApiResponse apiResponse = taskService.setStatus(taskSetStatusDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @GetMapping("/getCompletedTasks")
    public ResponseEntity<?> getCompletedTasks() {
        List<Task> tasks = taskService.getCompleteTasks();
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @GetMapping("/getNotCompletedTasks")
    public ResponseEntity<?> getNotCompletedTasks() {
        List<Task> tasks = taskService.getNotCompleteTasks();
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_HR_MANAGER')")
    @GetMapping("/getDueTasks")
    public ResponseEntity<?> getDueTasks() {
        List<Task> tasks = taskService.getDueTasks();
        return ResponseEntity.ok(tasks);
    }
}
