package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Exits;
import uz.pdp.apphrmanagement.entity.Turnstile;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExitsRepository extends JpaRepository<Exits, UUID> {

    List<Exits> findAllByTurnstileIdAndTimeBetween(Turnstile turnstile_id, Timestamp startTime, Timestamp endTime);
}
