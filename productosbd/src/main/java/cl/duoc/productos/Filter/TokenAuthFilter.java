package cl.duoc.productos.Filter;

import cl.duoc.productos.Service.TokenValidatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {


    private static final Logger log = LoggerFactory.getLogger(TokenAuthFilter.class);


    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenValidatorService tokenValidatorService;
    private final ObjectMapper objectMapper;


    public TokenAuthFilter(TokenValidatorService tokenValidatorService, ObjectMapper objectMapper) {
        this.tokenValidatorService = tokenValidatorService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            writeError(response, HttpStatus.UNAUTHORIZED, "Se requiere el header: Authorization: Bearer <token>");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            boolean valid = tokenValidatorService.validate(token);

            if (valid) {

                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("ADMIN")
                );

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        "admin",
                        null,
                        authorities
                );


                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info(">>> Usuario autorizado con roles: " + auth.getAuthorities());

            } else {
                writeError(response, HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
                return;
            }
        } catch (Exception ex) {
            log.error("Error validando token: {}", ex.getMessage());
            writeError(response, HttpStatus.SERVICE_UNAVAILABLE, "Servicio de autenticación no disponible");
            return;
        }

        chain.doFilter(request, response);
    }


    private void writeError(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status",    status.value(),
                "error",     message
        );
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}