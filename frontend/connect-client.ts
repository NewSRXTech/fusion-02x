import {
  ConnectClient,
  MiddlewareContext,
  MiddlewareNext,
} from '@vaadin/flow-frontend/Connect';

//import { getAccessToken } from './auth';

const client = new ConnectClient({
  prefix: 'connect',
  middlewares: [
    async (
      context: MiddlewareContext,
      next: MiddlewareNext
    ) => {
	// window.Vaadin.TypeScript.csrfToken
		var csrfToken: string = "";
		if (hasOwnProperty(window, "Vaadin")) {
			var vaadin: Object = (window.Vaadin as Object);
			if (hasOwnProperty(vaadin, "TypeScript")) {
				console.log("TypeScript", vaadin.TypeScript);
				var ts: Object = (vaadin.TypeScript as Object);
				if (hasOwnProperty(ts, "csrfToken"))	{
					csrfToken = (ts.csrfToken as string)
					console.log("csrfToken", csrfToken);
				}
			}
		}
		
        context.request.headers.set(
          'Authorization',
          "Bearer "+csrfToken
        );

      return next(context);
    },
  ],
});

function hasOwnProperty<X extends {}, Y extends PropertyKey>
  (obj: X, prop: Y): obj is X & Record<Y, unknown> {
  return obj.hasOwnProperty(prop)
}

export default client;

