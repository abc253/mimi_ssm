package com.li.filter;

import com.li.domain.Admin;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();

        // 不应该被拦截的资源,自动放行请求
        if("/admin/login.jsp".equals(path)){

            filterChain.doFilter(servletRequest,servletResponse);

        }else {
            HttpSession session = request.getSession();
            //从session域中获取admin对象,防止用户恶意访问
            Admin admin = (Admin) session.getAttribute("admin");

            if (admin != null) {
                //用户登录过，放行
                filterChain.doFilter(servletRequest,servletResponse);
            } else {
                //用户未登录过，跳转到登录页面
                response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
