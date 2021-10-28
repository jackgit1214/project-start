import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import rootSaga from './reducers/sagas/index';
import configureStore from './reducers/config/store.js';
import './assets/css/main.css';
import './assets/css/icon.css';
import 'element-theme-default';
import './assets/font-awesome-4.7.0/css/font-awesome.min.css'
import './assets/css/color-dark.css'
import {BrowserRouter as Router, Redirect, Route, Switch,Link} from "react-router-dom";
import ErrorPage404 from "./components/error/404";
import Login from "./views/login";
import Home from "./Framework";
import RouterComponent from "./Framework/RouterComponent";
import {createBrowserHistory} from "history";
const store = configureStore();
const history = createBrowserHistory();

store.runSaga(rootSaga);

ReactDOM.render(
    <Provider store={store}>
        <Router history={history}>
            <Route  path="/" component={Home} />
            {/*<Route path="/public" component={RouterComponent}/>*/}
            <Route path="/login" component={Login}   />
            <Route path="*" component={ErrorPage404} />
        </Router>
    </Provider>,
    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
// reportWebVitals();
