package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.entities.Utenti;

public interface UserCreateService {
    Utenti createUser(String username, String password);
}