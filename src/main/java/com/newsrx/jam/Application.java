package com.newsrx.jam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "jam")
// @PWA(name = "JAM", shortName = "JAM", offlineResources = {"images/logo.png"})
public class Application implements AppShellConfigurator {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
    	log.info("#main");
//    	System.setProperty("server.servlet.context-path", "/fusion-02");
    	SpringApplication.run(Application.class, args);
    }
}
