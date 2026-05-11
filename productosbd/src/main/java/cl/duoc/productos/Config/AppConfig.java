package cl.duoc.productos.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;


import java.time.Duration;

@Configuration
public class AppConfig {

   @Value("${auth.base-url:http://localhost:8080}")
    private String authValidateUrl;

   @Bean
    public WebClient authWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)        // 3s conexión
                .responseTimeout(Duration.ofSeconds(5));                     // 5s lectura

        return WebClient.builder()
                .baseUrl(authValidateUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
