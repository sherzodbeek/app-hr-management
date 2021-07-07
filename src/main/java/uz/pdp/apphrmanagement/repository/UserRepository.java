package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmailCodeAndEmail(String emailCode, String email);

    Optional<User> findByIdAndEmail(UUID id, String email);

    List<User> findAllByRoles(Set<Role> roles);
}
