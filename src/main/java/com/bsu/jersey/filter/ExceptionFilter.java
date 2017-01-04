package com.bsu.jersey.filter;

import com.bsu.jersey.tools.U;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来拦截业务异常
 * Created by fengchong on 2016/12/22.
 */
@WebFilter(filterName="ExceptionFilter",urlPatterns = "*")
public class ExceptionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse hsr = (HttpServletResponse) servletResponse;
//        try {
            filterChain.doFilter(servletRequest, hsr);
//        } catch (Exception e) {
//            U.el1111(hsr, this.getClass(), e);
//        }
    }

    @Override
    public void destroy() {

    }
}
