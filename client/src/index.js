import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './app/App';
import registerServiceWorker from './registerServiceWorker';
import { BrowserRouter as Router } from 'react-router-dom';
import { IntlProvider } from 'react-intl';

ReactDOM.render(
    <IntlProvider>
        <Router>
            <App />
        </Router>
    </IntlProvider>, 
    document.getElementById('root')
);

registerServiceWorker();
