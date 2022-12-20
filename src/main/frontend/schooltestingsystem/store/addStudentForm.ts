import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./store";
import IUser from "../dto/User";

export const addStudentFormSlice = createSlice({
    name: 'addStudentForm',
    initialState: {firstName: "", lastName: ""} as IUser,
    reducers: {
        update: (state, action: PayloadAction<IUser>) => action.payload
    }
})

export const addStudentFormSelector = (state: RootState) => state.addStudentForm
export const addStudentFormUpdate = addStudentFormSlice.actions.update
