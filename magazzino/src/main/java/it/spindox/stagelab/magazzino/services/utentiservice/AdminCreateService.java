package it.spindox.stagelab.magazzino.services.utentiservice;
import it.spindox.stagelab.magazzino.entities.Utenti;



// MODIFICHE PER GLI ADMINISTRATORS

public interface AdminCreateService {
    Utenti createAdmin(String username, String password);
}
