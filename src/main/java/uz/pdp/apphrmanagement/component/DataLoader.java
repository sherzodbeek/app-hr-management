package uz.pdp.apphrmanagement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.TaskStatus;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.entity.enums.RoleName;
import uz.pdp.apphrmanagement.entity.enums.Status;
import uz.pdp.apphrmanagement.repository.RoleRepository;
import uz.pdp.apphrmanagement.repository.TaskStatusRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TaskStatusRepository taskStatusRepository;


    @Override
    public void run(String... args) {
        roleRepository.save(new Role(RoleName.ROLE_DIRECTOR));
        roleRepository.save(new Role(RoleName.ROLE_HR_MANAGER));
        roleRepository.save(new Role(RoleName.ROLE_STUFF));

        taskStatusRepository.save(new TaskStatus(Status.NEW));
        taskStatusRepository.save(new TaskStatus(Status.IN_PROGRESS));
        taskStatusRepository.save(new TaskStatus(Status.COMPLETED));
        userRepository.save(new User(
            "Director",
                "admin",
                "sqlqueryjs@gmail.com",
                passwordEncoder.encode("123"),
                new HashSet<>(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR))),
                true
        ));
    }
}
