package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Salary {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    private User user;

    private boolean isGiven = false;

    public Salary(double amount, Date date, User user) {
        this.amount = amount;
        this.date = date;
        this.user = user;
    }
}
