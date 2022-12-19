import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {useAppDispatch} from "./hooks";

class BadRequestException {}

export interface ISyncStoreState<T> {
    object?: T
    lastVersion: number
    storedVersion?: number
    loading: boolean
}

export function syncStoreFetcher<T>(name: string) {
    return createAsyncThunk(name, async (url: string) => {
        const response = await fetch(url)
        if (!response.ok) {
            throw new BadRequestException()
        }
        return await response.json() as T
    })
}

export function fetchIfNeeded<T>(
    fetcher: ReturnType<typeof syncStoreFetcher<T>>
) {
    return (url: string, state: ISyncStoreState<T>) => {
        const dispatch = useAppDispatch()
        if (state.lastVersion !== state.storedVersion) {
            dispatch(fetcher(url))
        }
    }
}

function initialState<T>(): ISyncStoreState<T> {
    return {lastVersion: 0, loading: false};
}

export function syncStoreSlice<T>(name: string, fetcher: ReturnType<typeof syncStoreFetcher<T>>) {
    return createSlice({
        name: name,
        initialState: initialState<T>(),
        reducers: {
            invalidate: state => {
                ++state.lastVersion
            }
        },
        extraReducers(builder) {
            builder
                .addCase(fetcher.pending, (state, action) => {
                    state.loading = true
                })
                .addCase(fetcher.rejected, (state, action) => {
                    return {
                        ...state,
                        loading: false,
                        object: null
                    }
                })
                .addCase(fetcher.fulfilled, (state, action) => {
                    return {
                        ...state,
                        object: action.payload,
                        storedVersion: state.lastVersion,
                        loading: false
                    }
                })
        }
    })
}
