import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./store";
import IRegistrationForm from "../dto/RegistrationForm";

export const registrationFormSlice = createSlice({
    name: 'registrationForm',
    initialState: {username: "", password: "", inviteCode: ""} as IRegistrationForm,
    reducers: {
        update: (state, action: PayloadAction<IRegistrationForm>) => action.payload
    }
})

export const registrationFormSelector = (state: RootState) => state.registrationForm
export const registrationFormUpdate = registrationFormSlice.actions.update
