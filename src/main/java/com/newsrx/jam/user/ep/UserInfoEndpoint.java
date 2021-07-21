package com.newsrx.jam.user.ep;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import com.newsrx.jam.user.User;
import com.newsrx.jam.user.UserInfo;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.connect.Endpoint;

@PermitAll
@Endpoint
public class UserInfoEndpoint {
	
	private static final Logger log = LoggerFactory.getLogger(UserInfoEndpoint.class);

    @Nonnull
    public UserInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();

        final List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final UserInfo userInfo = new UserInfo(auth.getName(), authorities);
        log.info("#getUserInfo: "+userInfo.toString());
		return userInfo;
    }
    
    @Nonnull
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
        final User user = new User(principal.getAttribute("given_name"), principal.getAttribute("family_name"), principal.getAttribute("email"),
                principal.getAttribute("picture"));
        log.info("#getUser: "+user.toString());
		return user;
    }

    @AnonymousAllowed
    public boolean isLoggedIn() {
//    	log.info("#isLogginIn"+VaadinSession.getCurrent().getCsrfToken());
    	final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			log.info("#isLoggedIn=false");
    		return false;
    	}
//    	if (!(authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal)) {
//    		log.info("#isLoggedIn=false");
//    		return false;
//    	}
    	log.info("#isLoggedIn=true");
        return true;
    }

}