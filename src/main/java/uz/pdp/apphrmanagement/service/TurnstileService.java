package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Turnstile;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TurnstileDto;
import uz.pdp.apphrmanagement.repository.TurnstileRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class TurnstileService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TurnstileRepository turnstileRepository;

    public ApiResponse giveTurnstile(TurnstileDto turnstileDto) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(turnstileDto.getUserId()));
        if(optionalUser.isEmpty())
            return new ApiResponse("Stuff not found!", false);
        Optional<Turnstile> optionalTurnstile = turnstileRepository.findByUserId(UUID.fromString(turnstileDto.getUserId()));
        if(optionalTurnstile.isPresent()) {
            return new ApiResponse("Turnstile for this stuff already given!", false);
        }
        User user = optionalUser.get();
        Turnstile turnstile = new Turnstile(user, turnstileDto.isActive());
        Turnstile savedTurnstile = turnstileRepository.save(turnstile);
        return new ApiResponse("Turnstile given to stuff!", true, savedTurnstile);
    }


}
