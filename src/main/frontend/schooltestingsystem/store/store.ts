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
import {addClass} from "../request/addClass";
import {addClassFormSlice} from "./addClassForm";
import {getClasses} from "../request/getClasses";
import {addTask} from "../request/addTask";
import {addTaskFormSlice} from "./addTaskForm";
import {addStudent} from "../request/addStudent";
import {addStudentFormSlice} from "./addStudentForm";
import {getClass} from "../request/getClass";
import {assign} from "../request/assign";
import {assignFormSlice} from "./assignForm";
import {getTasks} from "../request/getTasks";
import {getStudentForStudent, getStudentForTeacher} from "../request/getStudent";
import {taskRunnerReducer} from "./taskRunner";
import {getStudentTask} from "../request/getStudentTask";
import {beginStudentTask} from "../request/beginStudentTask";
import {endStudentTask} from "../request/endStudentTask";

const reducer = combineReducers({
    whoami: whoami.reducer,
    login: login.reducer,
    loginForm: loginFormSlice.reducer,
    logout: logout.reducer,
    addTeacher: addTeacher.reducer,
    addTeacherForm: addTeacherFormSlice.reducer,
    getTeachers: getTeachers.reducer,
    register: register.reducer,
    registrationForm: registrationFormSlice.reducer,
    addClass: addClass.reducer,
    addClassForm: addClassFormSlice.reducer,
    getClasses: getClasses.reducer,
    addTask: addTask.reducer,
    addTaskForm: addTaskFormSlice.reducer,
    addStudent: addStudent.reducer,
    addStudentForm: addStudentFormSlice.reducer,
    getClass: getClass.reducer,
    assign: assign.reducer,
    assignForm: assignFormSlice.reducer,
    getTasks: getTasks.reducer,
    getStudentForTeacher: getStudentForTeacher.reducer,
    getStudentForStudent: getStudentForStudent.reducer,
    taskRunner: taskRunnerReducer,
    getStudentTask: getStudentTask.reducer,
    beginStudentTask: beginStudentTask.reducer,
    endStudentTask: endStudentTask.reducer
})

export const store = configureStore({reducer: reducer})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
