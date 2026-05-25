package com.psi.rizerio.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sem autenticação)
                .requestMatchers("/api/v1/auth/**", "/auth/**", "/error").permitAll()
                .requestMatchers("/clientes/register-patient").permitAll()

                // Paciente pode ver seus próprios dados
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/clientes/user/**").hasAnyAuthority("ADMIN", "PSYCHOLOGIST", "USER", "CLIENTE")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/clientes/**").hasAnyAuthority("ADMIN", "PSYCHOLOGIST", "USER", "CLIENTE")

                // Dashboard de Inteligência Clínica — todos os roles autenticados
                .requestMatchers("/api/v1/dashboard/**").hasAnyAuthority("ADMIN", "PSYCHOLOGIST", "USER", "CLIENTE")

                // Relatórios IA e Feedbacks — todos os roles autenticados
                .requestMatchers("/api/v1/patients/{patientId}/reports/**", "/api/v1/feedbacks/**").hasAnyAuthority("ADMIN", "PSYCHOLOGIST", "USER", "CLIENTE")

                // Pacientes podem consultar sessões (leitura apenas)
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/sessoes", "/sessoes/**").hasAnyAuthority("ADMIN", "PSYCHOLOGIST", "USER", "CLIENTE")

                // Gestão de clientes/psicólogos/sessões — somente profissionais
                .requestMatchers("/clientes/**", "/psicologos/**", "/sessoes/**").hasAnyAuthority("ADMIN", "PSYCHOLOGIST")

                // Administração de usuários — somente ADMIN
                .requestMatchers("/api/v1/users/**").hasAuthority("ADMIN")

                // Qualquer outro endpoint requer autenticação
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite qualquer origem — necessário para celulares físicos com IPs dinâmicos.
        // Em produção, substituir por domínios específicos.
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
