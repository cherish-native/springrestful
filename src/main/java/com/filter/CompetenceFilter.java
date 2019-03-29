package com.filter;

import com.entity.SysUser;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/1/28
 */
public class CompetenceFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response =   (HttpServletResponse)servletResponse;
        SysUser sysUser = ((SysUser)request.getSession().getAttribute("user"));
        Cookie[] cookies = request.getCookies();
        Optional<List<Cookie>> cookieList = Optional.of(Arrays.stream(cookies).filter(t -> t.getName().equals("key")).collect(Collectors.toList()));
        if(!cookieList.isPresent() || null ==sysUser){
            response.sendRedirect("/pages/home");
        }else{
            if(cookieList.get().get(0).getValue().equals(sysUser.getId())){
                filterChain.doFilter(request,response);
            }else{
                response.sendRedirect("/pages/home");
            }
        }
    }

    @Override
    public void destroy() {}
}
