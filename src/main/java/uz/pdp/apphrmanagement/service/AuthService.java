package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.LoginDto;
import uz.pdp.apphrmanagement.payload.PasswordDto;
import uz.pdp.apphrmanagement.repository.UserRepository;
import uz.pdp.apphrmanagement.security.JwtProvider;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }


    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(user.getEmail(), user.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Password or login incorrect!", false);
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailCodeAndEmail(emailCode, email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Account verified!", true);
        }
        return new ApiResponse("Account already verified!", false);
    }

    public ApiResponse setPassword(String userId, String email, PasswordDto passwordDto) {
        String password = passwordDto.getPassword();
        String retypePassword = passwordDto.getRetypePassword();
        if(!password.equals(retypePassword))
            return new ApiResponse("Passwords must be equal!", false);
        Optional<User> optionalUser = userRepository.findByIdAndEmail(UUID.fromString(userId), email);
        if(optionalUser.isEmpty())
            return new ApiResponse("User not found!", false);
        User user = optionalUser.get();
        if(!user.isEnabled())
            return new ApiResponse("Account not verified!", false);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return new ApiResponse("Password set!", true);
    }
}
