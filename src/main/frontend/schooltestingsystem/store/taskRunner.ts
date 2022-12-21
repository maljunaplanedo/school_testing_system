import ILiveGapsAnswer from "../dto/LiveGapsAnswer";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./store";
import {ILiveGapsRequest} from "../component/StudentTask";

export interface ITaskRunnerState {
    answer?: ILiveGapsAnswer
    timeLeft?: number
    mark?: number
    request?: ILiveGapsRequest
    open: boolean
}

const initialState: ITaskRunnerState = {open: false,
    answer: undefined,
    timeLeft: undefined,
    mark: undefined,
    request: undefined
}

const taskRunnerSlice = createSlice({
    name: 'taskRunner',
    initialState: initialState as ITaskRunnerState,
    reducers: {
        startRequest: (state, action: PayloadAction<ILiveGapsRequest>) => {
            state.request = action.payload
            state.open = false
        },
        update: (state, action: PayloadAction<ITaskRunnerState>) => action.payload,
        minusSec: state => {
            --state.timeLeft
        },
        open: state => {
            state.open = true
        },
        refresh: state => initialState
    }
})

export const taskRunnerActions = taskRunnerSlice.actions
export const taskRunnerReducer = taskRunnerSlice.reducer
export const taskRunnerSelector = (state: RootState) => state.taskRunner
