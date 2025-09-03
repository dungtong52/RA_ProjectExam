package com.ra.interceptor;

import com.ra.model.dto.UserResponse;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserResponse currentUser = (UserResponse) request.getSession().getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
