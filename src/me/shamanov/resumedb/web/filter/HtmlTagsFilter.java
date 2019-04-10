package me.shamanov.resumedb.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Author: Mike
 * Date: 09.04.2019
 *
 * Wraps the client's request when he submits information via POST method.
 * @see HtmlTagsFilterRequestWrapper
 */

public class HtmlTagsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if ("POST".equals(httpServletRequest.getMethod())) {
            servletRequest.setCharacterEncoding("UTF-8");
            HtmlTagsFilterRequestWrapper wrapper = new HtmlTagsFilterRequestWrapper(httpServletRequest);
            filterChain.doFilter(wrapper, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}
