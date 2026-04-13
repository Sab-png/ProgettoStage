package it.spindox.stagelab.magazzino.filters;

import it.spindox.stagelab.magazzino.repositories.UserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;

public class CheckoutAuthFilter implements Filter {

    private static final String CHECKOUT_PATH = "/cart/checkout";

    private final UserRepository userRepository;

    public CheckoutAuthFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().contains(CHECKOUT_PATH)) {
            String authHeader = httpRequest.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Basic ")) {
                sendForbidden(httpResponse, "Token mancante o non valido");
                return;
            }

            try {
                // Decodifica il token Base64
                String token = authHeader.substring(6);
                String decoded = new String(Base64.getDecoder().decode(token));

                // Formato atteso: "username:password"
                if (!decoded.contains(":")) {
                    sendForbidden(httpResponse, "Formato token non valido");
                    return;
                }

                String[] parts = decoded.split(":", 2);
                String username = parts[0];
                String password = parts[1];

                // Verifica esistenza utente a DB
                boolean exists = userRepository
                        .findByUsernameAndPassword(username, password)
                        .isPresent();

                if (!exists) {
                    sendForbidden(httpResponse, "Utente non trovato o credenziali errate");
                    return;
                }

            } catch (IllegalArgumentException e) {
                sendForbidden(httpResponse, "Token non decodificabile");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void sendForbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}