package it.spindox.stagelab.magazzino.services.utentiservice;
import it.spindox.stagelab.magazzino.entities.Utenti;
import it.spindox.stagelab.magazzino.repositories.UtentiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



// Per i role di utenti : ADMIN O USERS

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtentiRepository utentiRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Utenti user = utentiRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Utente non trovato");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password("{noop}" + user.getPassword()) // password in chiaro
                .roles(user.getRuolo())                  // ADMIN o USER
                .build();
    }
}
