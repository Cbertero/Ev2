package cl.duoc.productos.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service


public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${app.security.username}")
    private String configuredUsername;

    @Value("${app.security.password}")
    private String configuredPassword;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if (!username.equals(configuredUsername)) {
            throw new UsernameNotFoundException(
                    "Usuario no encontrado: " + username);
        }
        return User.builder()
                .username(configuredUsername)
                .password(encoder.encode(configuredPassword))
                .roles("ADMIN")
                .build();
    }
}
