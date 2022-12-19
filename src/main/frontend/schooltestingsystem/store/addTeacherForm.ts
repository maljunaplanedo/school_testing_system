import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./store";
import IUser from "../dto/User";

export const addTeacherFormSlice = createSlice({
    name: 'addTeacherForm',
    initialState: {firstName: "", lastName: ""} as IUser,
    reducers: {
        update: (state, action: PayloadAction<IUser>) => action.payload
    }
})

export const addTeacherFormSelector = (state: RootState) => state.addTeacherForm
export const addTeacherFormUpdate = addTeacherFormSlice.actions.update
