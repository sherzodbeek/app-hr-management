package uz.pdp.apphrmanagement.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.Task;
import uz.pdp.apphrmanagement.entity.TaskStatus;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.entity.enums.RoleName;
import uz.pdp.apphrmanagement.entity.enums.Status;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TaskDto;
import uz.pdp.apphrmanagement.payload.TaskSetStatusDto;
import uz.pdp.apphrmanagement.repository.TaskRepository;
import uz.pdp.apphrmanagement.repository.TaskStatusRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import javax.mail.internet.MimeMessage;
import java.sql.Date;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    JavaMailSender mailSender;


    public ApiResponse giveTasks(TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        boolean isDirector = false;
        for (Role role : roles) {
            if (role.getRoleName().equals(RoleName.ROLE_DIRECTOR)) {
                isDirector = true;
                break;
            }
        }
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(taskDto.getUserId()));
        if (optionalUser.isEmpty())
            return new ApiResponse("Stuff not found!", false);
        User stuff = optionalUser.get();
        if (!isDirector) {
            Set<Role> stuffRoles = stuff.getRoles();
            for (Role role : stuffRoles) {
                if (role.getRoleName().equals(RoleName.ROLE_DIRECTOR)) {
                    return new ApiResponse("You can't give task to director!", false);
                }
            }
        }
        Optional<TaskStatus> statusOptional = taskStatusRepository.findById(taskDto.getTaskStatusId());
        if (statusOptional.isEmpty())
            return new ApiResponse("Task status not found!", false);
        TaskStatus taskStatus = statusOptional.get();
        Task task = new Task(taskDto.getTaskName(), taskDto.getDescription(), taskDto.getDeadline(), stuff, user, taskStatus);
        sendEmailToStuffs(stuff, task);
        taskRepository.save(task);
        return new ApiResponse("Task added!", true);
    }

    @SneakyThrows
    public void sendEmailToStuffs(User stuff, Task task) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("sqlqueryjs@gmail.com");
        helper.setSubject("New task!");
        helper.setTo(stuff.getEmail());
        helper.setText("<h3>Task name: " + task.getTaskName() +
                "<br>Task description: " + task.getDescription() +
                "<br>Deadline: " + task.getDeadline() +
                "<br> Task status: " + task.getTaskStatus().getStatus() +
                "</h3>", true);
        mailSender.send(mimeMessage);
    }

    public List<Task> getTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return taskRepository.findAllByStaffId(user.getId());
    }

    public ApiResponse setStatus(TaskSetStatusDto taskSetStatusDto) {
        Optional<Task> optionalTask = taskRepository.findById(UUID.fromString(taskSetStatusDto.getTaskId()));
        if(optionalTask.isEmpty())
            return new ApiResponse("Task not found!", false);
        Optional<TaskStatus> statusOptional = taskStatusRepository.findById(taskSetStatusDto.getTaskStatusId());
        if(statusOptional.isEmpty())
            return new ApiResponse("Task status not found!", false);
        Task task = optionalTask.get();
        TaskStatus taskStatus = statusOptional.get();
        task.setTaskStatus(taskStatus);
        taskRepository.save(task);
        sendEmailToDirectorOrManager(task.getGivenByUser().getEmail(), task.getStaff(), task);
        return new ApiResponse("Task saved!", true);
    }

    @SneakyThrows
    public void sendEmailToDirectorOrManager(String email, User stuff, Task task) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("sqlqueryjs@gmail.com");
        helper.setSubject("Task info!");
        helper.setTo(email);
        helper.setText("<h3>Responsible stuff: " + stuff.getFirstName() +
                "<br>Stuff email: " + stuff.getEmail() +
                "<br>Task name: " + task.getTaskName() +
                "<br>Task description: " + task.getDescription() +
                "<br>Deadline: " + task.getDeadline() +
                "<br> Task status: " + task.getTaskStatus().getStatus() +
                "</h3>", true);
        mailSender.send(mimeMessage);
    }

    public List<Task> getCompleteTasks() {
        TaskStatus taskStatus = taskStatusRepository.findByStatus(Status.COMPLETED);
        return taskRepository.findAllByTaskStatus(taskStatus);
    }

    public List<Task> getNotCompleteTasks() {
        List<Task> tasks  = new ArrayList<>();
        TaskStatus taskStatus = taskStatusRepository.findByStatus(Status.IN_PROGRESS);
        tasks.addAll(taskRepository.findAllByTaskStatus(taskStatus));
        TaskStatus taskStatus1 = taskStatusRepository.findByStatus(Status.NEW);
        tasks.addAll(taskRepository.findAllByTaskStatus(taskStatus1));
        return tasks;
    }

    public List<Task> getDueTasks() {
        List<Task> tasks = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        TaskStatus taskStatus = taskStatusRepository.findByStatus(Status.IN_PROGRESS);
        tasks.addAll(taskRepository.findAllByDeadlineLessThanAndTaskStatus(date, taskStatus));
        TaskStatus taskStatus1 = taskStatusRepository.findByStatus(Status.NEW);
        tasks.addAll(taskRepository.findAllByDeadlineLessThanAndTaskStatus(date, taskStatus));
        return tasks;
    }
}
