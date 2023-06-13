package com.project.happyshop.security.authentication.handler;

import com.project.happyshop.security.model.PrincipalUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommonAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Ajax 를 통해 들어온 것인지 확인한다.
        String ajaxHeader = request.getHeader("X-Ajax-call");
        String result = "";

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");

        if (ajaxHeader == null) {
            // null 로 받은 경우는 X-Ajax-call 헤더 변수가 없다는 의미이기 때문에,
            // ajax 가 아닌 일반적인 방법으로 접근했음을 의미한다.
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();
            if (principal instanceof PrincipalUser) {
                String username = ((PrincipalUser) principal).getUsername();
                request.setAttribute("username", username);
            }
            request.setAttribute("errorMessage", accessDeniedException);
            redirectStrategy.sendRedirect(request, response, errorPage);
        } else {
            if (ajaxHeader.equals("true")) {
                // ajax 로 접근한 경우
                result = "{\"result\" : \"fail\", \"message\" : \"" + accessDeniedException.getMessage() + "\"}";
            } else {
                // 헤더 변수는 있으나 값이 틀린 경우. 헤더값이 틀렸다고 알려준다.
                result = "{\"result\":\"fail\", \"message\": \"Access Denied(Header Value Mismatch)\"}";
            }
            response.getWriter().print(result);
            response.getWriter().flush();
        }
    }

    public void setErrorPage(String errorPage) {
        if ((errorPage != null) && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with '/'");
        }

        this.errorPage = errorPage;
    }
}
