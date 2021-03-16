package com.retro.collegeretro.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

@Slf4j
@Configuration
public class EventListenerConfig {

    /**
     * Callback function which fires when a user logs in with invalid credentials.
     * @param event Contains information about the incorrect login.
     */
    @EventListener
    public void handleErrorEvent(AuthenticationFailureBadCredentialsEvent event) {
        log.info("Username: " + event.getAuthentication().getPrincipal());
        log.info("Password: " + event.getAuthentication().getCredentials());
        log.info("User tried to log in with bad credentials");
    }

}
