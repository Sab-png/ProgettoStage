package it.spindox.stagelab.magazzino.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class CheckoutAuthFilter implements Filter {

    private static final String CHECKOUT_PATH = "/cart/checkout";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Applica il controllo solo sulle chiamate al checkout
        if (httpRequest.getRequestURI().contains(CHECKOUT_PATH)) {
            String authHeader = httpRequest.getHeader("Authorization");

            if (authHeader == null || !isValidBasicToken(authHeader)) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"Accesso negato: token mancante o non valido\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isValidBasicToken(String authHeader) {
        if (!authHeader.startsWith("Basic ")) {
            return false;
        }
        try {
            // Verifica che il token sia decodificabile in Base64
            String token = authHeader.substring(6);
            String decoded = new String(Base64.getDecoder().decode(token));
            // Formato atteso: "username:password"
            return decoded.contains(":");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}