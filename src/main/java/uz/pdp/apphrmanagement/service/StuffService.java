package uz.pdp.apphrmanagement.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.*;
import uz.pdp.apphrmanagement.entity.enums.RoleName;
import uz.pdp.apphrmanagement.entity.enums.Status;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.GetInfoDto;
import uz.pdp.apphrmanagement.repository.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.*;

@Service
public class StuffService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TurnstileRepository turnstileRepository;

    @Autowired
    EntriesRepository entriesRepository;

    @Autowired
    ExitsRepository exitsRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;



    public ApiResponse addManager(User user) {
        boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        if(existsByEmail)
            return new ApiResponse("This email already exists!", false);
        User newManager = new User();
        newManager.setFirstName(user.getFirstName());
        newManager.setLastName(user.getLastName());
        newManager.setEmail(user.getEmail());
        newManager.setPassword(passwordEncoder.encode(user.getPassword()));
        newManager.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_HR_MANAGER))));
        newManager.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(newManager);
        sendEmailToManager(newManager, user.getPassword());
        return new ApiResponse("Manager added!. Email has been sent to verify account!", true);
    }


    @SneakyThrows
    public void sendEmailToManager(User newManager, String password){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("sqlqueryjs@gmail.com");
        helper.setTo(newManager.getEmail());
        helper.setSubject("Verify account!");
        helper.setText("<h3>Hi Manager " + newManager.getFirstName()+ "!<br> Your username/email:<b> " + newManager.getEmail() + "</b> " +
                "Your password:<b> " + password + "</b>" +
                "<br>To verify your account click" + "<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + newManager.getEmailCode() + "&email=" + newManager.getEmail() + "'><b> this</b></a><h3>", true);
        javaMailSender.send(mimeMessage);
    }

    public ApiResponse addStuff(User user) {
        boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        if(existsByEmail)
            return new ApiResponse("This email already exists!", false);
        User newStuff = new User();
        newStuff.setFirstName(user.getFirstName());
        newStuff.setLastName(user.getLastName());
        newStuff.setEmail(user.getEmail());
        newStuff.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_STUFF))));
        newStuff.setEmailCode(UUID.randomUUID().toString());
        newStuff = userRepository.save(newStuff);
        sendEmailToStuff(newStuff);
        return new ApiResponse("Stuff added!. Email has been sent to verify account!", true);
    }

    @SneakyThrows
    public void sendEmailToStuff(User newStuff) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("sqlqueryjs@gmail.com");
        helper.setTo(newStuff.getEmail());
        helper.setSubject("Verify account and set your password!");
        helper.setText("<h3>Hi Stuff " + newStuff.getFirstName()+"!<br> Your username/email:<b> " + newStuff.getEmail() + "</b> " +
                "Please verify your account and set your password!" +
                "<br>To verify your account click" + "<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + newStuff.getEmailCode() + "&email=" + newStuff.getEmail() + "'><b> this</b></a><h3><br>" +
                "To set password send post request to this path with password after verified your email http://localhost:8080/api/auth/setPassword?userId=" + newStuff.getId() + "&email=" + newStuff.getEmail(), true);
        javaMailSender.send(mimeMessage);
    }

    public List<User> getStuffs() {
        return userRepository.findAllByRoles(new HashSet<>(Collections.singletonList(new Role(RoleName.ROLE_STUFF))));
    }

    public ApiResponse getInfoStuff(GetInfoDto getInfoDto) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(getInfoDto.getStuffId()));
        if(optionalUser.isEmpty())
            return new ApiResponse("Stuff not found!", false);
        User stuff = optionalUser.get();
        Optional<Turnstile> optionalTurnstile = turnstileRepository.findByUserId(stuff.getId());
        if(optionalTurnstile.isEmpty())
            return new ApiResponse("Stuff's turnstile not found!", false);
        Turnstile turnstile = optionalTurnstile.get();
        List<Object> infoList = new ArrayList<>();
        Timestamp startDate = new Timestamp(getInfoDto.getStartTime().getTime());
        Timestamp endDate = new Timestamp(getInfoDto.getEndTime().getTime());
        List<Entries> entryInfo = entriesRepository.findAllByTurnstileAndTimeBetween(turnstile, startDate, endDate);
        List<Exits> exitsInfo = exitsRepository.findAllByTurnstileIdAndTimeBetween(turnstile, startDate, endDate);
        infoList.add(entryInfo);
        infoList.add(exitsInfo);
        return new ApiResponse("Information sent", true, infoList);
    }

    public ApiResponse getInfoTasks(GetInfoDto getInfoDto) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(getInfoDto.getStuffId()));
        if(optionalUser.isEmpty())
            return new ApiResponse("Stuff not found!", false);
        User stuff = optionalUser.get();
        TaskStatus status = taskStatusRepository.findByStatus(Status.COMPLETED);
        List<Task> taskList = taskRepository.findAllByStaffIdAndDeadlineBetweenAndTaskStatus(stuff.getId(),
                getInfoDto.getStartTime(),
                getInfoDto.getEndTime(),
                status);
        return new ApiResponse("Task information.", true, taskList);
    }
}
