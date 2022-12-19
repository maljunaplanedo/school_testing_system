import React from "react";
import ReactDOM from "react-dom";
import Index from "./component";
import {Provider} from "react-redux";
import {store} from "./store/store";

import {BrowserRouter, Route} from "react-router-dom";

ReactDOM.render(
    <Provider store={store}>
        <BrowserRouter>
            <Route path="/" exact component={Index} />
            <Route path="/login" exact component={Index} />
        </BrowserRouter>
    </Provider>,
    document.querySelector("#root")
)
