import {useHistory} from "react-router-dom";
import {useAppDispatch} from "../store/hooks";
import {Action} from "@reduxjs/toolkit";

export default function useRedirect<T extends Action = Action>(invalidateActions?: T[]) {
    const history = useHistory()
    const dispatch = useAppDispatch()

    return (location: string) => {
        if (invalidateActions != null) {
            for (const action of invalidateActions) {
                dispatch(action)
            }
        }
        history.push(location)
    }
}
