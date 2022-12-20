import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./store";

export interface ITaskFormState {
    name: string,
    statement: string,
    answer: string,
    duration: string,
    valid: boolean
}

export const addTaskFormSlice = createSlice({
    name: 'addClassForm',
    initialState: {name: "", statement: "", answer: "", duration: "", valid: true} as ITaskFormState,
    reducers: {
        update: (state, action: PayloadAction<ITaskFormState>) => action.payload
    }
})

export const addTaskFormSelector = (state: RootState) => state.addTaskForm
export const addTaskFormUpdate = addTaskFormSlice.actions.update
