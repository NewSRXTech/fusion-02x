package com.newsrx.jam.user;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.newsrx.jam.user.ep.UserInfoEndpoint;

@Component
@SessionScope
public class UserSession implements Serializable {
	
	private static final Logger log = LoggerFactory.getLogger(UserInfoEndpoint.class);

    public User getUser() {
    	log.info("#getUser");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();

        final User user = new User(principal.getAttribute("given_name"), principal.getAttribute("family_name"), principal.getAttribute("email"),
                principal.getAttribute("picture"));
		return user;
    }

    public boolean isLoggedIn() {
    	log.info("#isLoggedIn");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null;
    }
}
