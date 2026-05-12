package cl.duoc.productos.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class TokenValidatorService {

    private static final Logger log = LoggerFactory.getLogger(TokenValidatorService.class);

    private final WebClient authWebClient;

    public TokenValidatorService(WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    public boolean validate(String token) {
        try {
            log.info("Enviando validación a: http://localhost:8080/api/auth/validate");

            authWebClient.get()
                    .uri("/api/auth/validate") // Asegúrate de que la URI sea solo el endpoint
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return true;

        } catch (WebClientResponseException e) {

            log.error(" ERROR DE VALIDACIÓN ");
            log.error("Status recibido de Auth: {}", e.getStatusCode());
            log.error("Cuerpo del error de Auth: {}", e.getResponseBodyAsString());
            log.error("URL intentada: {}", e.getRequest() != null ? e.getRequest().getURI() : "N/A");


            return false;

        } catch (WebClientRequestException e) {
            log.error("Auth service no disponible: {}", e.getMessage());
            throw new ServiceUnavailableException("Servicio de autenticación no disponible");
        }
    }

    public static class ServiceUnavailableException extends RuntimeException {
        public ServiceUnavailableException(String message) {
            super(message);
        }
    }
}