import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./store";
import ILoginForm from "../dto/LoginForm";

export const loginFormSlice = createSlice({
    name: 'loginForm',
    initialState: {username: "", password: ""} as ILoginForm,
    reducers: {
        update: (state, action: PayloadAction<ILoginForm>) => action.payload
    }
})

export const loginFormSelector = (state: RootState) => state.loginForm
export const loginFormUpdate = loginFormSlice.actions.update
