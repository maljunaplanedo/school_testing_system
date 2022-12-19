import {combineReducers, configureStore} from "@reduxjs/toolkit";
import {whoamiSlice} from "./whoami";

const reducer = combineReducers({whoami: whoamiSlice.reducer})

export const store = configureStore({reducer: reducer})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
