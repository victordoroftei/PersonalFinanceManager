package com.personalfinancemanager.security;

import com.personalfinancemanager.domain.entity.UserPrincipal;
import com.personalfinancemanager.repository.UserRepository;
import com.personalfinancemanager.util.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    @Autowired
    private static JwtDecoder jwtDecoder;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JwtDecoder jwtDecoder, UserRepository userRepository) {
        super(authenticationManager);
        this.jwtDecoder = jwtDecoder;
        this.userRepository = userRepository;
    }

    public static Integer getUserIdFromJwt(String token) {
        String id = jwtDecoder.decode(token.replace(JwtProperties.TOKEN_PREFIX, "")).getSubject();

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No id found in the JWT!");
        }

        return Integer.parseInt(id);
    }

    /**
     * Main filtering method for validating the request. If the user is logged in, the request will provide an 'Authorization'
     * header with a valid JWT token
     * @param request - authorization request managed by Spring Security
     * @param response - authorization response managed by Spring Security
     * @param chain - filter chain managed by Spring Security
     * @throws IOException - if filtering fails
     * @throws ServletException - if filtering fails
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
            IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            chain.doFilter(request, response);
            return;
        }
        Authentication authentication = getUserAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    /**
     * extracts the JWT token from the 'Authorization' header, decodes it and gets the user id
     * to validate the existence of the user who made the request
     * @param request - authorization request managed by Spring Security
     * @return - Authentication object, to be managed by Spring Security
     */
    private Authentication getUserAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        String id = jwtDecoder.decode(token).getSubject();
        if (id != null) {
            UserPrincipal principal = new UserPrincipal(userRepository.findById(Integer.parseInt(id)).orElse(null));
            return new UsernamePasswordAuthenticationToken(id, null, principal.getAuthorities());
        }

        return null;
    }
}
