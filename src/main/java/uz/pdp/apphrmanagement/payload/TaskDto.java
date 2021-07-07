package uz.pdp.apphrmanagement.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Data
public class TaskDto {
    @NotNull
    private String taskName;

    @NotNull
    private String description;

    @NotNull
    private Date deadline;

    @NotNull
    private String userId;

    @NotNull
    private Integer taskStatusId;
}
