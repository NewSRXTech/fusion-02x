package com.newsrx.jam.views;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Adds an explicit link that the user has to click to login.
 */
@Route("login")
@PageTitle("Login")
public class LoginScreen extends VerticalLayout {
	
	private static final Logger log = LoggerFactory.getLogger(LoginScreen.class);

    /**
     * URL that Spring uses to connect to Google services
     */
    private static final String URL = "oauth2/authorization/google";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientkey;

    public LoginScreen() {
    	log.info("LoginScreen");
        setPadding(true);
        setAlignItems(Alignment.CENTER);
    }

    @PostConstruct
    public void initView() {
    	log.info("#initView");
        // Check that oauth keys are present
        if (clientkey == null || clientkey.isEmpty() || clientkey.length() < 16) {
            Paragraph text = new Paragraph("Could not find OAuth client key in application.properties. "
                    + "Please double-check the key and refer to the README.md file for instructions.");
            text.getStyle().set("padding-top", "100px");
            add(text);

        } else {
        	
            Anchor gplusLoginButton = new Anchor(URL, "Login with Google");
            gplusLoginButton.getStyle().set("margin-top", "100px");
            gplusLoginButton.getElement().setAttribute("router-ignore", true);
            add(gplusLoginButton);
        }

    }
}
