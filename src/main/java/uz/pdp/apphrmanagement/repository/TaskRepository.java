package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Task;
import uz.pdp.apphrmanagement.entity.TaskStatus;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByStaffId(UUID staff_id);

    List<Task> findAllByStaffIdAndDeadlineBetweenAndTaskStatus(UUID staff_id, Date deadline, Date deadline2, TaskStatus taskStatus);

    List<Task> findAllByTaskStatus(TaskStatus taskStatus);

    List<Task> findAllByDeadlineLessThanAndTaskStatus(Date deadline, TaskStatus taskStatus);
}
