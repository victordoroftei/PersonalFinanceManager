package com.personalfinancemanager.security;

import com.personalfinancemanager.domain.dto.LoginRequest;
import com.personalfinancemanager.domain.entity.UserPrincipal;
import com.personalfinancemanager.util.JwtProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtEncoder jwtEncoder;

    /**
     * Attempts to authenticate a user with the credentials received from an authentication http request
     * @param request - should have 'Authorization' header, with basic auth
     * @param response - the response sent after the authentication process ends
     * @return Authentication - object that will be managed by Spring Security
     * @throws AuthenticationException - if the authentication wasn't successfully
     */
    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String basicAuth = request.getHeader("Authorization");

        if(basicAuth != null && basicAuth.toLowerCase().startsWith("basic")) {
            //Authorization: Basic base64credentials
            String base64Credentials = basicAuth.substring("Basic".length()).trim();
            byte[] credentialsDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credentialsDecoded, StandardCharsets.UTF_8);

            //credentials = username:password
            final String[] credentialsArray = credentials.split(":", 2);

            LoginRequest loginCredentials = new LoginRequest(credentialsArray[0], credentialsArray[1]);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginCredentials.getEmail(),
                    loginCredentials.getPassword(),
                    new ArrayList<>());
            return authenticationManager.authenticate(authenticationToken);
        } else {
            throw new InternalAuthenticationServiceException("Could not parse authentication payload");
        }
    }

    /**
     * method that generates a JWT token if the user authenticates successfully
     * @param request - authentication request managed by Spring security
     * @param response - authentication response managed by Spring security, which will contain JWT token in its header
     * @param chain - filter chain managed by Spring Security
     * @param authResult - the authentication result
     */
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
        String scope = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claimsSet= JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME).toInstant())
                .subject(principal.getId().toString())
                .claim("scope", scope)
                .build();
        String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
    }

    /**
     * If the user was not authenticated successfully, we check the specific errors that could be thrown,
     * we set the response status as unauthorized(401), and we customize the error response
     * @param request - authentication request managed by Spring Security
     * @param response - authentication response managed by Spring Security
     * @param failed - AuthenticationException object
     * @throws IOException - if it cannot write the error message to response
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        if (failed.getMessage().equals("Bad credentials")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid credentials!");
        }
        if(failed.getMessage().equals("User is disabled")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Your account is awaiting approval!");
        }
    }
}