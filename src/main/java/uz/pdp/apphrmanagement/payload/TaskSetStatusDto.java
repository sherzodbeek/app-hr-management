package uz.pdp.apphrmanagement.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TaskSetStatusDto {
    @NotNull
    private String taskId;

    @NotNull
    private Integer taskStatusId;
}
