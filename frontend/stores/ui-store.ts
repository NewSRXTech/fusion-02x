import {
 login as serverLogin,
 logout as serverLogout,
} from "@vaadin/flow-frontend";

import { UserInfoEndpoint } from '../generated/UserInfoEndpoint';


export class UiStore {
	loggedIn = false;
	
	constructor() {

	}
	
	async checkLoginStatus(){
		UserInfoEndpoint.isLoggedIn().then(response=> {
			console.log("user login loaded:", response);
			if (this.loggedIn != response) {
				this.loggedIn = response;
			}
		});
	}
	
	async login(username: string, password: string) {
	 const result = await serverLogin(username, password);
	 if (!result.error) {
	   this.setLoggedIn(true);
	 } else {
	   throw new Error(result.errorMessage || 'Login failed');
	 }
	}
	
	async logout() {
	 await serverLogout();
	 this.setLoggedIn(false);
	}
	
	private setLoggedIn(loggedIn: boolean) {
	 this.loggedIn = loggedIn;
	 if (loggedIn) {
	   //crmStore.initFromServer();
	 }
	}
}