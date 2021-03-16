package com.retro.collegeretro.Config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Adds the {@link AccountSessionInterceptor} to the
 * interceptors to make sure user objects are present
 * and correct in the HttpSession for the controller.
 */
@Component
public class AccountSessionConfig implements WebMvcConfigurer {
    private final AccountSessionInterceptor accountSessionInterceptor;

    public AccountSessionConfig(AccountSessionInterceptor accountSessionInterceptor) {
        this.accountSessionInterceptor = accountSessionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(accountSessionInterceptor);
    }
}
