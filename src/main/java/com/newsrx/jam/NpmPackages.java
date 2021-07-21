package com.newsrx.jam;

import com.vaadin.flow.component.dependency.NpmPackage;

@NpmPackage(value="webpack", version="^4.46.0")
@NpmPackage(value="webpack-cli", version="^3.3.11")

//@NpmPackage(value="webpack", version="^5.44.0")
//@NpmPackage(value="webpack-cli", version="^4.7.2")

@NpmPackage(value="lumo-css-framework", version="^4.0.10")
@NpmPackage(value="@adobe/lit-mobx", version="^1.0.1")
@NpmPackage(value="mobx", version="^6.3.2")
@NpmPackage(value="jquery", version="^3.6.0")
@NpmPackage(value="jsdom", version="^16.6.0")

//devDependency only, remove for production build
@NpmPackage(value="@types/node", version="^16.3.2")

public interface NpmPackages {
	//
}
