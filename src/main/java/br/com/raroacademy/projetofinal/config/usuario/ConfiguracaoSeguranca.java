package br.com.raroacademy.projetofinal.config.usuario;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfiguracaoSeguranca {

    private static final String[] ROTAS_PUBLICAS = {
            "/autenticacao/login",
            "/usuarios",
            "/usuarios/confirmar",
            "/usuarios/esqueci-senha",
            "/usuarios/redefinir-senha"
    };

    @Bean
    public SecurityFilterChain configurarSeguranca(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(ROTAS_PUBLICAS).permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(this::tratarNaoAutenticado));

        return http.build();
    }

    private void tratarNaoAutenticado(
            jakarta.servlet.http.HttpServletRequest req,
            HttpServletResponse res,
            org.springframework.security.core.AuthenticationException ex) throws java.io.IOException {

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.getWriter().write("{\"erro\": \"Usuário não autenticado\"}");
    }
}
