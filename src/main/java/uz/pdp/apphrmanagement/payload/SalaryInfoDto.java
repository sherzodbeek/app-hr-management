package uz.pdp.apphrmanagement.payload;

import lombok.Data;

import java.sql.Date;

@Data
public class SalaryInfoDto {
    private String stuffId;
    private Date startDate;
    private Date endDate;
}
