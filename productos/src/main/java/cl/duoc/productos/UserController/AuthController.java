package cl.duoc.productos.UserController;

import cl.duoc.productos.DTO.JwtResponse;
import cl.duoc.productos.DTO.LoginRequest;
import cl.duoc.productos.Security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));


            String token = jwtTokenProvider.generateToken(authentication);


            return ResponseEntity.ok(new JwtResponse(token));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }
    }
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken() {
        return ResponseEntity.ok().build();
    }
}
