package personal.finance.iam;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final RSAPublicKey key;
    private final RSAPrivateKey priv;

    public SecurityConfiguration(@Value("${jwt.public.key}") RSAPublicKey key, @Value("${jwt.private.key}") RSAPrivateKey priv) {
        this.key = key;
        this.priv = priv;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling((exceptions) -> exceptions
                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            )
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
            .headers().frameOptions().sameOrigin().and()
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                    AntPathRequestMatcher.antMatcher("/login"),
                    AntPathRequestMatcher.antMatcher("/signup"),
                    AntPathRequestMatcher.antMatcher("index.html"),
                    AntPathRequestMatcher.antMatcher("**/swagger-ui/index.html**"),
                    AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                    AntPathRequestMatcher.antMatcher("/v2/api-docs"),
                    AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                    AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                    AntPathRequestMatcher.antMatcher("/webjars/**"),
                    AntPathRequestMatcher.antMatcher("/webjars/**"),
                    AntPathRequestMatcher.antMatcher("/h2-console/**"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS)
                )
                .permitAll()
                .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    UserDetailsManager usersManager(DataSource dataSource) {
        UserDetails user = User.builder()
            .username("user")
            .password("$2a$12$AIr6rEZVrDSBk9/2OsMeS.xBVt1K7dLtVWwmy4.iCZpT/H98pasy6")
            .roles("USER")
            .build();
        UserDetails admin = User.builder()
            .username("admin")
            .password("$2a$12$AIr6rEZVrDSBk9/2OsMeS.xBVt1K7dLtVWwmy4.iCZpT/H98pasy6")
            .roles("USER", "ADMIN")
            .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        if (!users.userExists("user")) {
            users.createUser(user);
        }

        if (!users.userExists("admin")) {
            users.createUser(admin);
        }

        return users;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Remove the SCOPE_ prefix
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
