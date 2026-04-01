package it.spindox.stagelab.magazzino.configurations.securityconfigurationssettings;
import it.spindox.stagelab.magazzino.entities.Utenti;
import it.spindox.stagelab.magazzino.repositories.UtentiRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Base64;



@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final UtentiRepository utentiRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();


        //passa tutto ad eccezzione di  fattureworkexecutionpayment
        if (!(
                "PATCH".equals(method) &&
                        path.matches("^/fattureworkexecution/\\d+/payment$")
        )) {
            filterChain.doFilter(request, response);
            return;
        }

        // poi in seguito e' protetto perche' richiede l' autorizzazione

        String authHeader = request.getHeader("Authorization");

        // se e' diverso da basic

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token mancante o non valido");
            return;
        }

        String token = authHeader.substring(6).trim();
// decoding

        String decoded;
        try {
            decoded = new String(Base64.getDecoder().decode(token));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token non valido");
            return;
        }
// e' corretto se ha formato username e password

        String[] parts = decoded.split(":");
        if (parts.length != 2) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token non valido");
            return;
        }

        String username = parts[0];// username
        String password = parts[1];// password

        Utenti user = utentiRepository.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token non valido");
            return;
        }

        // se il token e' apposta continua

        filterChain.doFilter(request, response);
    }
}
