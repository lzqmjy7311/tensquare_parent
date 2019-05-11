package com.tensquare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebZuulFilter extends ZuulFilter {
    @Override
    public String filterType() {
        //过滤器类型
        //在请求到达网关的时候，网关路由执行之前执行
        return "pre";// 前置过滤器
    }

    @Override
    public int filterOrder() {
        //过滤器是一个链条，也称之为过滤器栈，执行顺序是按照该方法的返回值判断的
        return 0;// 优先级为0，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        //当前过滤器是否启用的开关，如果是true，则启用，false代表不启用。
        return true;// 是否执行该过滤器，此处为true，说明需要过滤
    }

    @Override
    public Object run() throws ZuulException {
        final RequestContext currentContext = RequestContext.getCurrentContext();
        final HttpServletRequest request = currentContext.getRequest();
        final String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            currentContext.addZuulRequestHeader("JwtAuthorization", authorization);
        }
        System.out.println("web前台网关，经过了zuul前置过滤器...");
        return null;
    }
}
