import React, {useEffect, useRef} from "react";
import {addClass} from "../request/addClass";
import useRedirect from "../util/redirect";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {addClassFormSelector, addClassFormUpdate} from "../store/addClassForm";
import {LoadingStatus} from "../request/handler";
import Loading from "./Loading";
import LogoutButton from "./LogoutButton";
import IndexButton from "./IndexButton";
import Error from "./Error";

export default function AddClass() {
    const addClassState = addClass.useState()
    const addClassRefresh = addClass.useRefresh()
    const addClassSync = addClass.useSync()

    const redirect = useRedirect("/teacher/add_class", addClassRefresh)

    useEffect(() => {
        if (
            addClassState.loading === LoadingStatus.FINISHED &&
            addClassState.object !== undefined &&
            addClassState.object.success
        ) {
            redirect("/teacher/classes")
        }
    })

    const dispatch = useAppDispatch()

    const addClassFormData = useAppSelector(addClassFormSelector)

    const nameField = useRef<HTMLInputElement>(null)

    const onSubmit = () => {
        addClassSync({body: addClassFormData}, true)
        return false
    }

    const onInput = () => {
        dispatch(addClassFormUpdate({
            name: nameField.current.value
        }))
    }

    if ((addClassState.loading === LoadingStatus.FINISHED &&
        addClassState.object !== undefined &&
        addClassState.object.success) || addClassState.loading === LoadingStatus.IN_PROGRESS
    ) {
        return <Loading/>
    } else {
        let errorMessage: string = null
        if (addClassState.loading === LoadingStatus.FINISHED) {
            errorMessage = ""
            if (addClassState.object !== undefined) {
                errorMessage = "Класс с таким именем уже существует"
            }
        }

        const form = (
            <>
                <LogoutButton redirect={redirect}/>
                <IndexButton redirect={redirect}/>
                <form name="addClass" onSubmit={onSubmit}>
                    <input type="text" onInput={onInput} placeholder="Название" ref={nameField}/>
                    <input type="submit" />
                </form>
            </>
        )

        if (errorMessage === null) {
            return form
        }
        return (
            <>
                {form}
                <Error message={errorMessage} />
            </>
        )
    }
}
