package com.newsrx.jam.views;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newsrx.jam.user.ep.UserInfoEndpoint;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Adds an explicit link that the user has to click to login.
 */
@Route("user-info")
@PageTitle("User Info")
public class UserInfo extends VerticalLayout {
	
	private static final Logger log = LoggerFactory.getLogger(UserInfo.class);

    public UserInfo() {
    	log.info(this.getClass().getSimpleName());
        setPadding(true);
        setAlignItems(Alignment.CENTER);
    }

    @PostConstruct
    public void initView() {
    	//
    }
    
    @Override
    protected void onAttach(AttachEvent attachEvent) {
    	super.onAttach(attachEvent);
    	UserInfoEndpoint userInfoEndpoint = new UserInfoEndpoint();
    	if (userInfoEndpoint.isLoggedIn()) {
    		this.add("USER INFO: "+userInfoEndpoint.getUserInfo().toString());
    		log.info("USER INFO: "+userInfoEndpoint.getUserInfo().toString());
    	} else {
    		log.info("Redirecting to /login");
    		getUI().ifPresent(ui->ui.navigate("/login"));
    	}
    }
}
