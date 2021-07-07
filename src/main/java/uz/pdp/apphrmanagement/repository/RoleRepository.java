package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.enums.RoleName;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);
}
