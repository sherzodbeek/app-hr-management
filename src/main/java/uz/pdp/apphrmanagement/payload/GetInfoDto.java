package uz.pdp.apphrmanagement.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class GetInfoDto {
    @NotNull
    private String stuffId;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;
}
