package com.retro.collegeretro.Config;

import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The purpose of this class is to intercept all requests,
 * add the user's User object to the HttpSession if it isn't
 * already there, and then send the packet to the controller.
 * This ensures that if the user is logged in, their User
 * object will be accessible by the controller.
 */
@Component
public class AccountSessionInterceptor implements HandlerInterceptor {
    private final UserRepository userRepository;

    public AccountSessionInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Intercept request before it gets to controller.
     * If user is logged in, add the user object to the
     * HttpSession. If they aren't logged in, remove
     * the user object.
     *
     * @param request  from the client.
     * @param response will be sent back to the client.
     * @param handler  is unused.
     * @return always true.
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        if (request.getUserPrincipal() != null) {
            if (request.getSession().getAttribute("user") == null) {
                User user = userRepository.findByUsername(request.getUserPrincipal().getName());
                request.getSession().setAttribute("user", user);
            } else {
                User user = (User) request.getSession().getAttribute("user");
                user = userRepository.findById(user.getUserId()).orElse(user);
                request.getSession().setAttribute("user", user);
            }
        } else {
            request.getSession().removeAttribute("user");
        }
        return true;
    }
}

