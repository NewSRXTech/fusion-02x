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
@Route("")
@PageTitle("NewSRX Tech Admin")
public class MainScreen extends VerticalLayout {
	
	private static final Logger log = LoggerFactory.getLogger(MainScreen.class);

    public MainScreen() {
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
    		log.info("Redirecting to /main");
    		getUI().ifPresent(ui->ui.navigate("/user-info"));
    	} else {
    		log.info("Redirecting to /login");
    		getUI().ifPresent(ui->ui.navigate("/login"));
    	}
    }
}
