package com.newsrx.jam.config;

import java.io.IOException;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.newsrx.jam.user.User;
import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.ApplicationConstants;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;

/**
 * Configures Spring Security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form,</li>
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_URL = "/logout";
	private static final String LOGOUT_SUCCESS_URL = "/";

	private AuthenticationSuccessHandler oauthSuccess = new SavedRequestAwareAuthenticationSuccessHandler() {
		@Override
		protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) {
			log.info("#determineTargetUrl");
			
			log.info("Headers: "+response.getHeaderNames());
			
			OAuth2AuthenticationToken oauth2 = (OAuth2AuthenticationToken)authentication;
			VaadinSession session = VaadinSession.getCurrent();
			if (session!=null) {
				log.info("Session: "+session.getCsrfToken());
			} else {
				log.info("Session: null");
			}
			return super.determineTargetUrl(request, response, authentication);
		}
		
		
		
		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			try {

				log.info("#onAuthenticationSuccess");

				OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
				final User user = new User(principal.getAttribute("given_name"), principal.getAttribute("family_name"),
						principal.getAttribute("email"), principal.getAttribute("picture"));

				log.info(principal.getAttributes().toString());
				log.info("USER: " + user.toString());
				if (user.getEmail() != null && user.getEmail().toLowerCase().endsWith("@newsrx.com")) {
					super.onAuthenticationSuccess(request, response, authentication);
					return;
				}
				
			} catch (Exception e) {
				log.error(e.getLocalizedMessage(), e);
			}
			throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Not Authorized!", ""));
		}
	};

	/**
	 * Registers our UserDetailsService and the password encoder to be used on login
	 * attempts.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("#configure HttpSecurity");
		
		http //
		.formLogin().disable()
		.csrf().disable()
				// Allow all flow internal requests.
				.authorizeRequests().requestMatchers(SecurityConfiguration::isFrameworkInternalRequest).permitAll()
				// Restrict access to our application.
				.and() //
				.authorizeRequests().anyRequest().authenticated() //
				// Configure logout
				.and() //
				.logout().logoutUrl(LOGOUT_URL).logoutSuccessUrl(LOGOUT_SUCCESS_URL).permitAll()
				// Configure the login page with OAuth.
				.and().oauth2Login().loginPage(LOGIN_URL).successHandler(oauthSuccess)
				
				.permitAll();
	}

	/**
	 * Allows access to static resources, bypassing Spring Security.
	 */
	@Override
	public void configure(WebSecurity web) {
		log.info("#configure WebSecurity");
		web.ignoring().antMatchers(
				// client-side JS code
				"/VAADIN/**",

				// client-side JS code
				"/frontend/**",

				// the standard favicon URI
				"/favicon.ico",

				// web application manifest
				"/manifest.webmanifest", "/sw.js", "/offline-page.html",

				// icons and images
				"/icons/**", "/images/**",

				// for oauth2
				"/sw-runtime-resources-precache.js",

				/*
				 * stop login from turning into error 999 no error at /error | appears to be
				 * caused by a 404 for favicon.ico
				 */
				"/error",
				
				/*
				 * API endpoint which is needed in anonymous mode
				 */
				"/connect/**"

		);
	}

	/**
	 * Tests if the request is an internal framework request. The test consists of
	 * checking if the request parameter is present and if its value is consistent
	 * with any of the request types know.
	 *
	 * @param request {@link HttpServletRequest}
	 * @return true if is an internal framework request. False otherwise.
	 */
	static boolean isFrameworkInternalRequest(HttpServletRequest request) {
		final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
		return parameterValue != null && Stream.of(HandlerHelper.RequestType.values())
				.anyMatch(r -> r.getIdentifier().equals(parameterValue));
	}

	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
		log.info("#oauth2UserService");
		DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
		return request -> {
			log.info("#oauth2UserService#Bean");
			OAuth2User user = delegate.loadUser(request);
			final User newsrx = new User(user.getAttribute("given_name"), user.getAttribute("family_name"),
					user.getAttribute("email"), user.getAttribute("picture"));
			log.info(newsrx.getFirstName() + " " + newsrx.getLastName() + " [" + newsrx.getEmail() + "]");
			return user;
		};
	}
}
