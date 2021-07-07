package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Turnstile {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(optional = false)
    private User user;

    private boolean active = true;

    public Turnstile(User user, boolean active) {
        this.user = user;
        this.active = active;
    }
}
