package org.blueline.api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.blueline.api.service.ActivityLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ActivityInterceptor implements HandlerInterceptor {

    private final ActivityLogService activityLogService;

    public ActivityInterceptor(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String action = request.getMethod();
        String endpoint = request.getRequestURI();

        activityLogService.saveActivity(authentication, action, endpoint);
        return true;
    }
}

