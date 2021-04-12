package ru.faimon.instazoo.security;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.faimon.instazoo.payload.response.InvalidLoginResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
        InvalidLoginResponse loginResponse = new InvalidLoginResponse();
        String jsonLoginResponse = new Gson().toJson(loginResponse);
        resp.setContentType(SecurityConstants.CONTENT_TYPE);
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        resp.getWriter().println(jsonLoginResponse);
    }
}
