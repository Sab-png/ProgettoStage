package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.entities.User;
import it.spindox.stagelab.magazzino.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));

        String dbRole = user.getRole() == null ? "" : user.getRole().trim().toUpperCase();
        String authorityRole = dbRole.startsWith("ROLE_") ? dbRole : "ROLE_" + dbRole;
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(authorityRole));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}

