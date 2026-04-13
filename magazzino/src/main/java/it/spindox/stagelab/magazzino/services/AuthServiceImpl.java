package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.LoginRequest;
import it.spindox.stagelab.magazzino.dto.response.LoginResponse;
import it.spindox.stagelab.magazzino.exceptions.UnauthorizedException;
import it.spindox.stagelab.magazzino.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new UnauthorizedException(
                        "Credenziali non valide"
                ));

        // Genera token: Base64("<username>:<password>")
        String raw = request.getUsername() + ":" + request.getPassword();
        String encoded = Base64.getEncoder().encodeToString(raw.getBytes());

        LoginResponse response = new LoginResponse();
        response.setAccessToken("Basic " + encoded);

        return response;
    }
}