import { RouterLocation } from '@vaadin/router';
import { makeAutoObservable } from 'mobx';
import { UiStore } from './ui-store';

export class AppStore {
  applicationName = 'JAM';

  // The location, relative to the base path, e.g. "hello" when viewing "/hello"
  location = '';

  currentViewTitle = '';

  uiStore = new UiStore();

  constructor() {
    makeAutoObservable(this);
  }

  setLocation(location: RouterLocation) {
    if (location.route) {
      this.location = location.route.path;
    } else if (location.pathname.startsWith(location.baseUrl)) {
      this.location = location.pathname.substr(location.baseUrl.length);
    } else {
      this.location = location.pathname;
    }
    this.currentViewTitle = (location?.route as any)?.title || '';
  }
}
export const appStore = new AppStore();
export const uiStore = appStore.uiStore;