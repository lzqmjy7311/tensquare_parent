package com.tensquare.manager;

import com.itcast.common.utils.JwtUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerZuulFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        final RequestContext currentContext = RequestContext.getCurrentContext();
        final HttpServletRequest request = currentContext.getRequest();
        final String authorizationHeader = request.getHeader("Authorization");

        final boolean options = request.getMethod().equals("OPTIONS");
        if (options) {
            return null;
        }
        final String url = request.getRequestURL().toString();
        if (url.indexOf("/admin/login") > 0) {
            return null;
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            final String token = authorizationHeader.substring(7);
            Claims claims = null;
            try {
                claims = jwtUtil.parseJWT(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final Claims roles = (Claims) claims.get("roles");
            if (claims != null && roles.equals("admin")) {
                currentContext.addZuulRequestHeader("jwtAuthrization", authorizationHeader);
                // 放行
                return null;
            }
        }
        currentContext.setSendZuulResponse(false);
        currentContext.getResponse().setContentType("application/json;charset=UTF-8");
        currentContext.setResponseBody("{\"code\": 20003,\"flag\": false,\"message\": \"您无权访问\"}");
        currentContext.setResponseStatusCode(401);
        return null;
    }
}
