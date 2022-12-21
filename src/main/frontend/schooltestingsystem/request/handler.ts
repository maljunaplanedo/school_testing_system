import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {useAppDispatch, useAppSelector} from "../store/hooks";

class BadRequestException {}

export class NoData {}

export enum LoadingStatus {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED
}

interface IRequestHandlerState<T> {
    object?: T
    loading: LoadingStatus
}

export interface IHttpRequestParams<B> {
    suffix?: string
    body?: B
}

export interface IWebsocketRequestParams<B> {

}

function requestHandler<R, B, P>(name: string, fetcher: ReturnType<typeof createAsyncThunk<R, P>>) {
    const useState = () => {
        return useAppSelector(state => state[name as keyof typeof state] as IRequestHandlerState<R>)
    }

    const useSync = () => {
        const dispatch = useAppDispatch()
        const state = useState()
        return (params?: P, force = false) => {
            if (force || state.loading === LoadingStatus.NOT_STARTED) {
                dispatch(fetcher(params))
            }
        }
    }

    const slice = createSlice({
        name: name,
        initialState: {loading: LoadingStatus.NOT_STARTED} as IRequestHandlerState<R>,
        reducers: {
            refresh: state => {
                state.loading = LoadingStatus.NOT_STARTED
                state.object = undefined
            }
        },
        extraReducers(builder) {
            builder
                .addCase(fetcher.pending, (state, action) => {
                    state.loading = LoadingStatus.IN_PROGRESS
                })
                .addCase(fetcher.rejected, (state, action) => {
                    return {
                        ...state,
                        loading: LoadingStatus.FINISHED,
                        object: undefined
                    }
                })
                .addCase(fetcher.fulfilled, (state, action) => {
                    return {
                        ...state,
                        object: action.payload,
                        loading: LoadingStatus.FINISHED
                    }
                })
        }
    })

    const useRefresh = () => {
        const dispatch = useAppDispatch()
        return () => {
            dispatch(slice.actions.refresh())
        }
    }

    return {
        useState: useState,
        useSync: useSync,
        useRefresh: useRefresh,
        reducer: slice.reducer
    }
}

export function httpRequestHandler<R, B>(name: string, url: string, method: string = "GET") {
    return requestHandler<R, B, IHttpRequestParams<B>>(name, createAsyncThunk(
        name + '/fetch',
        async (params?: IHttpRequestParams<B>) => {
            if (params === undefined) {
                params = {}
            }
            let fetchUrl = url

            if (params.suffix !== undefined) {
                fetchUrl += '/' + params.suffix
            }

            let requestOptions: RequestInit = {
                method: method
            }

            if (params.body !== undefined) {
                requestOptions.body = JSON.stringify(params.body)
                requestOptions.headers = {
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }

            const response = await fetch(fetchUrl, requestOptions)
            if (!response.ok) {
                throw new BadRequestException()
            }
            const responseData = await response.text()

            if (responseData.length === 0) {
                return {} as R
            }

            return JSON.parse(responseData) as R
        }
    ))
}

export type IHttpRequestHandler<R, B> = ReturnType<typeof httpRequestHandler<R, B>>
