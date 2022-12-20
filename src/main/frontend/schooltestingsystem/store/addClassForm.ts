import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import IClass from "../dto/Class";
import {RootState} from "./store";

export const addClassFormSlice = createSlice({
    name: 'addClassForm',
    initialState: {name: ""} as IClass,
    reducers: {
        update: (state, action: PayloadAction<IClass>) => action.payload
    }
})

export const addClassFormSelector = (state: RootState) => state.addClassForm
export const addClassFormUpdate = addClassFormSlice.actions.update
