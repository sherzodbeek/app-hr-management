package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Exits;
import uz.pdp.apphrmanagement.entity.Turnstile;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.repository.ExitsRepository;
import uz.pdp.apphrmanagement.repository.TurnstileRepository;

import java.util.Optional;

@Service
public class ExitService {
    @Autowired
    ExitsRepository exitsRepository;

    @Autowired
    TurnstileRepository turnstileRepository;

    public ApiResponse exit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User stuff = (User) authentication.getPrincipal();
        Optional<Turnstile> optionalTurnstile = turnstileRepository.findByUserId(stuff.getId());
        if(optionalTurnstile.isEmpty())
            return new ApiResponse("This stuff has not got turnstile!", false);
        Turnstile turnstile = optionalTurnstile.get();
        Exits exits = new Exits(turnstile);
        exitsRepository.save(exits);
        return new ApiResponse("Exit saved!", true);
    }
}
