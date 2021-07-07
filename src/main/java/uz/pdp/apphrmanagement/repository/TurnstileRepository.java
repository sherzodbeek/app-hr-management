package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Turnstile;
import uz.pdp.apphrmanagement.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TurnstileRepository extends JpaRepository<Turnstile, UUID> {

    Optional<Turnstile> findByUserId(UUID user_id);
}
