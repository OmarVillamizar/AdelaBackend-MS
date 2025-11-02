package com.example.chaea.security;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest.Builder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtFilter jwtFilter;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
            ClientRegistrationRepository clientRegistrationRepository,
            CorsConfigurationSource corsConfigurationSource) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource)) // ✅ ahora sí existe
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/", "/docs/**", "/api-docs/**", "/swagger-ui/**", "/health/**", 
                    "/login/**", "/oauth2/**", "/api/grupos/**", 
                    "/api/estudiantes/**", "/api/profesores/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exc -> exc.authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }))
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization -> authorization.authorizationRequestResolver(
                        customAuthorizationRequestResolver(clientRegistrationRepository)))
                .successHandler(customAuthenticationSuccessHandler())
            );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    
    // --- Resolver para OAuth2, idéntico al original ---
    private OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/oauth2/authorization");
        
        return new OAuth2AuthorizationRequestResolver() {
            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
                OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
                return customizeAuthorizationRequest(request, authorizationRequest);
            }
            
            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
                OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
                return customizeAuthorizationRequest(request, authorizationRequest);
            }
            
            private OAuth2AuthorizationRequest customizeAuthorizationRequest(HttpServletRequest request,
                    OAuth2AuthorizationRequest authorizationRequest) {
                if (authorizationRequest != null) {
                    String userType = request.getParameter("userType");
                    if (userType == null) userType = "default";
                    String redirectTo = request.getParameter("redirect_to");
                    if (redirectTo == null) redirectTo = "default";
                    String state = "userType=" + userType + "&redirect_to=" + redirectTo;
                    Builder builder = OAuth2AuthorizationRequest.from(authorizationRequest);
                    builder.state(state);
                    return builder.build();
                }
                return authorizationRequest;
            }
        };
    }
    
    // --- Custom Authentication Success Handler ---
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String state = request.getParameter("state");
            if (state == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: null state in request");
                return;
            }

            Map<String, String> parameters = new HashMap<>();
            String[] elems = state.split("&");
            for (String elem : elems) {
                String[] x = elem.split("=");
                if (x.length > 1) {
                    String val = x[1];
                    for (int i = 2; i < x.length; ++i) val += "=" + x[i];
                    parameters.put(x[0], val);
                } else if (x.length == 1) {
                    parameters.put(x[0], "");
                }
            }

            String userType = parameters.getOrDefault("userType", null);
            String redirectTo = parameters.getOrDefault("redirect_to", null);
            if ("default".equals(userType)) userType = null;
            if ("default".equals(redirectTo)) redirectTo = null;

            if (userType == null || redirectTo == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: missing required parameters");
                response.flushBuffer();
                return;
            }

            if ("profesor".equals(userType)) {
                response.sendRedirect("/auth/login/success/prof?redirect_to=" + redirectTo);
            } else if ("estudiante".equals(userType)) {
                response.sendRedirect("/auth/login/success/estud?redirect_to=" + redirectTo);
            } else {
                response.sendRedirect("/");
            }
        };
    }
    
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }
    
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        return converter;
    }
}
