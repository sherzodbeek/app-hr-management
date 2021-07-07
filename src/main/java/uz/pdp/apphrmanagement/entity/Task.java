package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String taskName;

    @Type(type = "text")
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date deadline;

    @ManyToOne
    private User staff;

    @ManyToOne
    private User givenByUser;

    @ManyToOne
    private TaskStatus taskStatus;

    public Task(String taskName, String description, Date deadline, User staff, User givenByUser, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.description = description;
        this.deadline = deadline;
        this.staff = staff;
        this.givenByUser = givenByUser;
        this.taskStatus = taskStatus;
    }
}
