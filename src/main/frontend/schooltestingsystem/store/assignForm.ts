import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./store";
import IClassTask from "../dto/ClassTask";

export const assignFormSlice = createSlice({
    name: 'assignForm',
    initialState: {} as IClassTask,
    reducers: {
        update: (state, action: PayloadAction<IClassTask>) => action.payload
    }
})

export const assignFormSelector = (state: RootState) => state.assignForm
export const assignFormUpdate = assignFormSlice.actions.update
