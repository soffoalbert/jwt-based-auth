package com.sofo.springjwtauth.filters;

import com.sofo.springjwtauth.user.UserModel;
import com.sofo.springjwtauth.user.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.sofo.springjwtauth.auth.SecurityConstants.HEADER_STRING;
import static com.sofo.springjwtauth.auth.SecurityConstants.SECRET;

/**
 * Created by georges on 5/10/18
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    /**
     * Instantiates a new Jwt authorization filter.
     *
     * @param authenticationManager the authentication manager
     */
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
        super(authenticationManager);
        this.userRepository = ctx.getBean(UserRepository.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            UserModel userModel = userRepository.findByUsername(user);

            if (user != null && userModel.isHasValidSession()) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}

