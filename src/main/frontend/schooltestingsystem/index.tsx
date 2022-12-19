import React from "react";
import Index from "./component";
import {Provider} from "react-redux";
import {store} from "./store/store";

import {BrowserRouter, Route} from "react-router-dom";
import Student from "./component/Student";
import Login from "./component/Login";
import Teacher from "./component/Teacher";
import Admin from "./component/Admin";
import {createRoot} from "react-dom/client";
import Logout from "./component/Logout";
import AddTeacher from "./component/AddTeacher";
import Register from "./component/Register";

const root = createRoot(document.querySelector('#root'))

root.render(
    <Provider store={store}>
        <BrowserRouter>
            <Route path="/" exact component={Index} />
            <Route path="/login" exact component={Login} />
            <Route path="/student" exact component={Student} />
            <Route path="/teacher" exact component={Teacher} />
            <Route path="/admin" exact component={Admin} />
            <Route path="/logout" exact component={Logout} />
            <Route path="/admin/add_teacher" exact component={AddTeacher} />
            <Route path="/register" exact component={Register} />
        </BrowserRouter>
    </Provider>
)
