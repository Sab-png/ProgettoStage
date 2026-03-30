package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.LoginRequest;
import it.spindox.stagelab.magazzino.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}