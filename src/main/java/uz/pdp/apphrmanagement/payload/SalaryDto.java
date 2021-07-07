package uz.pdp.apphrmanagement.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
public class SalaryDto {
    private String salaryId;

    @NotNull
    private double amount;

    @NotNull
    private Date date;

    @NotNull
    private String stuffId;

    private boolean isGiven;
}
