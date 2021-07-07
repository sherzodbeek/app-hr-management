package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Salary;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.SalaryDto;
import uz.pdp.apphrmanagement.payload.SalaryInfoDto;
import uz.pdp.apphrmanagement.repository.SalaryRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryService {
    @Autowired
    SalaryRepository salaryRepository;

    @Autowired
    UserRepository userRepository;

    public ApiResponse writeSalary(SalaryDto salaryDto) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(salaryDto.getStuffId()));
        if(optionalUser.isEmpty())
            return new ApiResponse("Stuff not found!", false);
        User stuff = optionalUser.get();
        Salary salary = new Salary(salaryDto.getAmount(), salaryDto.getDate(), stuff);
        salaryRepository.save(salary);
        return new ApiResponse("Salary written!", true);
    }

    public ApiResponse giveSalary(SalaryDto salaryDto) {
        Optional<Salary> optionalSalary = salaryRepository.findById(UUID.fromString(salaryDto.getSalaryId()));
        if(optionalSalary.isEmpty())
            return new ApiResponse("Salary not found!", false);
        Salary salary = optionalSalary.get();
        salary.setGiven(salary.isGiven());
        salaryRepository.save(salary);
        return new ApiResponse("Salary given!",true);
    }

    public List<Salary> getSalaryInfo(SalaryInfoDto salaryInfoDto) {
        if(salaryInfoDto.getStuffId()!=null) {
            return salaryRepository.findAllByUserIdAndGiven(UUID.fromString(salaryInfoDto.getStuffId()), true);
        } else {
            return salaryRepository.findAllByDateBetweenAndGiven(salaryInfoDto.getStartDate(), salaryInfoDto.getEndDate(), true);
        }
    }
}
