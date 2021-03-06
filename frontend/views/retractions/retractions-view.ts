import { showNotification } from '@vaadin/flow-frontend/a-notification';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import { customElement, html } from 'lit-element';
import { View } from '../../views/view';

@customElement('retractions-view')
export class RetractionsView extends View {
  name: string = '';

  render() {
    return html`
<h3>Retraction Search</h3>
<vaadin-text-field label="Your name" @value-changed="${this.nameChanged}"></vaadin-text-field>
<vaadin-button @click="${this.sayHello}">
  Say hello 
</vaadin-button>
`;
  }
  nameChanged(e: CustomEvent) {
    this.name = e.detail.value;
  }

  sayHello() {
    showNotification(`Hello ${this.name}`);
  }
}
