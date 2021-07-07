package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exits {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Turnstile turnstile;

    @CreationTimestamp
    private Timestamp time;

    public Exits(Turnstile turnstile) {
        this.turnstile = turnstile;
    }
}
