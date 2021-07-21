import { customElement, html } from 'lit-element';
import { Layout } from '../../views/view';
//import * as ClientOAuth2 from "client-oauth2";

@customElement('login-view')
export class LoginView extends Layout {
	render() {
		return html`
		<div>
		Login
		</div>
		`;
	}

	/*login(){

		const github = new ClientOAuth2({
			clientId: "GITHUB_CLIENT_ID",
			clientSecret: "GITHUB_CLIENT_SECRET",
			redirectUri: "GITHUB_CLIENT_REDIRECT",
			accessTokenUri: "https://github.com/login/oauth/access_token",
			authorizationUri: "https://github.com/login/oauth/authorize",
			scopes: ["user:email"]
		});

		const getUrl = () => {
			const response = github.code.getUri();
			return response;
		}

		const getResponse = async (url: string) => {
			const response = await github.code.getToken(url);
			return response;
		}

	}*/
}
