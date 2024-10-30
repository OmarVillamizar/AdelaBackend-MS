package com.example.chaea.security;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Profesor;
import com.example.chaea.entities.ProfesorEstado;
import com.example.chaea.entities.Usuario;
import com.example.chaea.entities.UsuarioEstado;
import com.example.chaea.repositories.UsuarioRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                email = jwtUtil.extractEmail(token);
            }
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Optional<Usuario> userDetails = usuarioRepository.findByEmail(email);
                if (userDetails.isPresent() && jwtUtil.validateToken(token, userDetails.get())) {
                    
                    Usuario user = userDetails.get();
                    
                    UsernamePasswordAuthenticationToken authenticationToken;
                    List<SimpleGrantedAuthority> permisos = new LinkedList<SimpleGrantedAuthority>();
                    if (user instanceof Profesor) {
                        Profesor profe = (Profesor) user;
                        if (profe.getEstado() == UsuarioEstado.ACTIVA
                                && profe.getEstadoProfesor() == ProfesorEstado.ACTIVA) {
                            if (profe.getRol() != null)
                                permisos.add(new SimpleGrantedAuthority("ROLE_" + profe.getRol().getDescripcion()));
                        }
                        if (profe.getEstado() == UsuarioEstado.INCOMPLETA) {
                            permisos.add(new SimpleGrantedAuthority("ROLE_PROFESOR_INCOMPLETO"));
                        }
                        if (profe.getEstadoProfesor() == ProfesorEstado.INACTIVA) {
                            permisos.add(new SimpleGrantedAuthority("ROLE_PROFESOR_INACTIVO"));
                        }
                        authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.get(), null,
                                permisos);
                    } else if (user instanceof Estudiante) {
                        Estudiante estud = (Estudiante) user;
                        if (estud.getEstado() == UsuarioEstado.ACTIVA) {
                            permisos.add(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"));
                        }
                        if (estud.getEstado() == UsuarioEstado.INCOMPLETA) {
                            permisos.add(new SimpleGrantedAuthority("ROLE_ESTUDIANTE_INCOMPLETO"));
                        }
                        authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.get(), null,
                                permisos);
                    } else {
                        throw new RuntimeException("El usuario no está relacionado a ninguna cuenta");
                    }
                    //System.out.println(authenticationToken);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    
                }
                
            }
            filterChain.doFilter(request, response);
        } catch (SignatureException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            response.getWriter().write("Error in JWT token filter: " + e.getMessage());
        }
        
    }
}