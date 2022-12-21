import React from "react";
import {Provider} from "react-redux";
import {store} from "./store/store";

import './style.css'

import {BrowserRouter, Route} from "react-router-dom";
import Login from "./component/Login";
import Teacher from "./component/Teacher";
import Admin from "./component/Admin";
import {createRoot} from "react-dom/client";
import Logout from "./component/Logout";
import AddTeacher from "./component/AddTeacher";
import Register from "./component/Register";
import Classes from "./component/Classes";
import AddClass from "./component/AddClass";
import AddTask from "./component/AddTask";
import AddStudent from "./component/AddStudent";
import Class from "./component/Class";
import Assign from "./component/Assign";
import StudentInfo from "./component/StudentInfo";
import StudentIndex from "./component/StudentIndex";
import StudentTask from "./component/StudentTask";
import Index from "./component/Index";

const root = createRoot(document.querySelector('#root'))

root.render(
    <Provider store={store}>
        <BrowserRouter>
            <Route path="/" exact component={Index} />
            <Route path="/login" exact component={Login} />
            <Route path="/student" exact component={StudentIndex} />
            <Route path="/teacher" exact component={Teacher} />
            <Route path="/admin" exact component={Admin} />
            <Route path="/logout" exact component={Logout} />
            <Route path="/admin/add_teacher" exact component={AddTeacher} />
            <Route path="/register" exact component={Register} />
            <Route path="/teacher/classes" exact component={Classes} />
            <Route path="/teacher/add_class" exact component={AddClass} />
            <Route path="/teacher/add_task" exact component={AddTask} />
            <Route path="/teacher/add_student" exact component={AddStudent} />
            <Route path="/teacher/class/:id" exact component={Class} />
            <Route path="/teacher/assign" exact component={Assign} />
            <Route path="/teacher/student/:id" exact component={StudentInfo} />
            <Route path="/student/task/:id" exact component={StudentTask} />
        </BrowserRouter>
    </Provider>
)
