package com.abcbank.banking.common;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         29/08/17
 */
@Component
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("ABCBank");
    }
}
