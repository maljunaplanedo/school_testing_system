import {combineReducers, configureStore} from "@reduxjs/toolkit";
import {whoami} from "../request/whoami"
import {login} from "../request/login";
import {loginFormSlice} from "./loginForm";
import {logout} from "../request/logout";
import {addTeacherFormSlice} from "./addTeacherForm";
import {addTeacher} from "../request/addTeacher";
import {getTeachers} from "../request/getTeachers";
import {register} from "../request/register";
import {registrationFormSlice} from "./registrationForm";

const reducer = combineReducers({
    whoami: whoami.reducer,
    login: login.reducer,
    loginForm: loginFormSlice.reducer,
    logout: logout.reducer,
    addTeacher: addTeacher.reducer,
    addTeacherForm: addTeacherFormSlice.reducer,
    getTeachers: getTeachers.reducer,
    register: register.reducer,
    registrationForm: registrationFormSlice.reducer
})

export const store = configureStore({reducer: reducer})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
