package com.WHproject;



import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // Login olmuş kullanıcıları home.xhtml'a yönlendirme
        String loginURI = req.getContextPath() + "/pages/login.xhtml";
        String homeURI = req.getContextPath() + "/pages/home.xhtml";
        
        boolean loggedIn = (session != null && session.getAttribute("loggedInUser") != null);
        boolean loginRequest = req.getRequestURI().contains("/pages/login.xhtml");
        boolean homeRequest = req.getRequestURI().contains("/pages/home.xhtml");
        boolean resourceRequest = req.getRequestURI().contains("javax.faces.resource");

        // Kullanıcı giriş yapmamışsa login sayfasına yönlendir
        if (!loggedIn && !loginRequest && !resourceRequest) {
            res.sendRedirect(loginURI);
        } else if (loggedIn && loginRequest) {
            // Eğer kullanıcı zaten giriş yapmışsa login sayfasına gitmemeli, home sayfasına yönlendirmeli
            res.sendRedirect(homeURI);
        } else {
            chain.doFilter(request, response); // İstek devam ettirilir
        }
    }

    @Override
    public void destroy() {}
}
