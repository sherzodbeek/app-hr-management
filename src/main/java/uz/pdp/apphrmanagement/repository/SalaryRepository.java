package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Salary;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, UUID> {

    List<Salary> findAllByUserIdAndGiven(UUID user_id, boolean given);
    List<Salary> findAllByDateBetweenAndGiven(Date startDate, Date endDate, boolean given);
}
