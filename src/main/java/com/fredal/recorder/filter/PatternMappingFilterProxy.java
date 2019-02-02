package com.fredal.recorder.filter;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fredal
 */
class PatternMappingFilterProxy implements Filter {
    private final Filter delegate;
    private final List<String> pathUrlPatterns = new ArrayList();

    private PathMatcher pathMatcher;

    public PatternMappingFilterProxy(Filter delegate, String... urlPatterns) {
        Assert.notNull(delegate, "A delegate Filter is required");
        this.delegate = delegate;
        int length = urlPatterns.length;
        pathMatcher = new AntPathMatcher();

        for (int index = 0; index < length; ++index) {
            String urlPattern = urlPatterns[index];
            this.pathUrlPatterns.add(urlPattern);
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        if (this.matches(path)) {
            this.delegate.doFilter(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }

    }

    private boolean matches(String requestPath) {
        for (String pattern : pathUrlPatterns) {
            if (pathMatcher.match(pattern, requestPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.delegate.init(filterConfig);
    }

    @Override
    public void destroy() {
        this.delegate.destroy();
    }

    public List<String> getPathUrlPatterns() {
        return pathUrlPatterns;
    }

    public void setPathUrlPatterns(List<String> urlPatterns) {
        pathUrlPatterns.clear();
        pathUrlPatterns.addAll(urlPatterns);
    }
}
