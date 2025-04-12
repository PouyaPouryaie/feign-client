package ir.bigz.spring.server;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Component
public class CustomHeaderInterceptor implements HandlerInterceptor {

    Logger log = LoggerFactory.getLogger(CustomHeaderInterceptor.class);
    private final ServerHeaderHolder serverHeaderHolder;

    public CustomHeaderInterceptor(ServerHeaderHolder serverHeaderHolder) {
        this.serverHeaderHolder = serverHeaderHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(Objects.isNull(request.getHeader("X-Client-Requester"))) {
            throw new ServerException("Client not allowed", "10002");
        }

        serverHeaderHolder.setXClientRequester(request.getHeader("X-Client-Requester"));
        serverHeaderHolder.setXClientUUID(request.getHeader("X-UUID"));

        log.info("Request start for URI: {}, X-Client-Requester: {}, X-UUID: {}",
                request.getRequestURI(), serverHeaderHolder.getXClientRequester(), serverHeaderHolder.getXClientUUID());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.info("Request End for URI: {}, X-Client-Requester: {}, X-UUID: {}, status: {}",
                request.getRequestURI(), serverHeaderHolder.getXClientRequester(), serverHeaderHolder.getXClientUUID(), response.getStatus());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        if(response.getStatus() != 200) {
            log.info("Request Raised ERROR for URI: {}, X-Client-Requester: {}, X-UUID: {}, status: {}",
                    request.getRequestURI(), serverHeaderHolder.getXClientRequester(), serverHeaderHolder.getXClientUUID(), response.getStatus());
        }
    }
}
