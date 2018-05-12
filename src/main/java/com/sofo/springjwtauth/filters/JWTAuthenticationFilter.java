package com.sofo.springjwtauth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sofo.springjwtauth.auth.AuthVO;
import com.sofo.springjwtauth.user.UserModel;
import com.sofo.springjwtauth.user.UserRepository;
import com.sofo.springjwtauth.user.UserVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.sofo.springjwtauth.auth.SecurityConstants.EXPIRATION_TIME;
import static com.sofo.springjwtauth.auth.SecurityConstants.SECRET;

/**
 * Created by georges on 5/10/18.
 */
@Configurable
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    /**
     * Instantiates a new Jwt authentication filter.
     *
     * @param authenticationManager the authentication manager
     * @param ctx                   the ctx
     */
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
        this.authenticationManager = authenticationManager;
        this.userRepository = ctx.getBean(UserRepository.class);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserVO creds = new ObjectMapper()
                    .readValue(request.getInputStream(), UserVO.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();

        UserModel userModel = userRepository.findByUsername(((User) auth.getPrincipal()).getUsername());
        userModel.setHasValidSession(true);
        userRepository.save(userModel);
        Gson gson = new Gson();
        AuthVO authVO = new AuthVO(userModel.getId(),token);
        String result = gson.toJson(authVO);
        response.getWriter().write(result);
    }
}