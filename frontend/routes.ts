import { Route, Router, Context } from '@vaadin/router';
import { Flow } from '@vaadin/flow-frontend';
import { UserInfoEndpoint } from './generated/UserInfoEndpoint';
import  UserInfo  from './generated/com/newsrx/jam/user/UserInfo';

import './views/dashboard/dashboard-view';
import './views/main-layout';
import { uiStore } from './stores/app-store';
import { autorun } from 'mobx';

// Get the server-side routes from imports generated in the server
const {serverSideRoutes} = new Flow({
  imports: () => import('../build/frontend/generated-flow-imports')
});

export type ViewRoute = Route & { title?: string; children?: ViewRoute[], isProtected?: boolean; };

const authGuard = async (context: Context, commands: Router.Commands) => {
	await uiStore.checkLoginStatus();
	
	if (!uiStore.loggedIn) {
		console.log("NOT LOGGED IN!!!");

		// Save requested path
		sessionStorage.setItem("login-redirect-path", context.pathname);
		
		console.log("location.pathname: "+location.pathname, "context.pathname: "+context.pathname);
		
		if (context.pathname.indexOf("/login") < 0) {
			console.log("redirecting to login...");
			return commands.redirect("/login");
		}
	}
	return undefined;
};

autorun(() => {
	console.log("login state has changed: "+uiStore.loggedIn);
	
  /*if (uiStore.loggedIn) {
    Router.go(sessionStorage.getItem('login-redirect-path') || '/');
  } else {
    if (location.pathname !== '/login') {
      sessionStorage.setItem('login-redirect-path', location.pathname);
      Router.go('/login');
    }
  }*/
});
	
export const views: ViewRoute[] = [
  // place routes below (more info https://vaadin.com/docs/latest/fusion/routing/overview)
  {
    path: 'main',
    component: 'dashboard-view',
    title: 'Dashboard',
	isProtected:true
  },
  {
    path: 'retractions',
    component: 'retractions-view',
    title: 'Retractions',
	isProtected:true,
    action: async (context:Context, commands: Router.Commands) => {
		/*
		const route = context.route as ViewRoute;
		
		UserInfoEndpoint.getUserInfo().then(user=> {
			console.log("user info loaded",user)
			if ((!route.isProtected) || (user.authorities.length > 0 )){
				import('./views/retractions/retractions-view');
			} else {
				console.log("user is not logged in");
				 return commands.redirect("/login");
			}
			return undefined;
		}).catch(error=>{
			console.log("USER INFO FAILED!!!", error);
		});
		*/
		return undefined;
    },
  },
  {
    path: 'docs',
    component: 'documentation-view',
    title: 'Documentation',
	isProtected:true,
    action: async () => {
      await import('./views/documentation/documentation-view');
    },
  },
  {
    path: 'signout',
    component: 'documentation-view',
    title: 'Sign Out',
    action: (context:Context, commands: Router.Commands) => {
		var logout = async () => {
			window.location.href="logout"
		}
		logout().then();
		return commands.redirect("/");
    },
  },
	...serverSideRoutes// IMPORTANT: this must be the last entry in the array
];

export const routes: ViewRoute[] = [
  {
    path: '',
    component: 'main-layout',
    children: [...views],
	isProtected : false,
	action: authGuard,
	
	
	/*async (context, commands: Router.Commands) => {
		const isLoggedIn = await chekUserStatus(context.route, commands);
		
		try {
			const viewRoute = context.route as ViewRoute;

			console.log("user is logged in: "+isLoggedIn,  (!window.location.href.endsWith("/login")));
		
//			if (!isLoggedIn) {
//				console.log("wtf?!",window.location.href, (window.location.href.endsWith("/login") === false));
//				if (window.location.href.endsWith("/login") === false) {
//					console.log("redirecting to login...");
//					window.location.assign("./login");
//				}
//			}
		} catch(err){
			console.log(err);
		}
		return undefined;
		
	}*/
  },
];

export async function chekUserStatus(route : Route, commands: Router.Commands){
		console.log("checking user status...")
		try {
		
			const viewRoute = route as ViewRoute;
			const isLoggedIn = await UserInfoEndpoint.isLoggedIn().then(response=> {
				console.log("user login loaded:", response, viewRoute);
				return response;
				/*if ((!viewRoute.isProtected) || (response)){
					console.log("user is logged in!")
					return true;
				}
				else {
					console.log("user is not logged in!");
					
					window.location.assign("/fusion-02/login");
					return false;
				} */
			});
			return false;
		}
		catch (err) { 
			console.log(err); 
			return false;
		};
	
}
