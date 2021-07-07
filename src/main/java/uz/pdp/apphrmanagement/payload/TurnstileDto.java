package uz.pdp.apphrmanagement.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TurnstileDto {
    @NotNull
    private String userId;

    private boolean active = true;

}
