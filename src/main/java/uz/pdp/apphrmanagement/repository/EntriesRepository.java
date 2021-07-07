package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Entries;
import uz.pdp.apphrmanagement.entity.Turnstile;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface EntriesRepository extends JpaRepository<Entries, UUID> {

    List<Entries> findAllByTurnstileAndTimeBetween(Turnstile turnstile, Timestamp startTime, Timestamp endTime);
}
