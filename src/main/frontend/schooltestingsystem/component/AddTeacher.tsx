import {addTeacher} from "../request/addTeacher";
import useRedirect from "../util/redirect";
import React, {FormEvent, useRef} from "react";
import {LoadingStatus} from "../request/handler";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {addTeacherFormSelector, addTeacherFormUpdate} from "../store/addTeacherForm";
import IndexButton from "./IndexButton";
import Error from "./Error";
import Loading from "./Loading";
import LogoutButton from "./LogoutButton";

export default function AddTeacher() {
    const addTeacherState = addTeacher.useState()
    const addTeacherRefresh = addTeacher.useRefresh()
    const addTeacherSync = addTeacher.useSync()

    const redirect = useRedirect('/admin/add_teacher', addTeacherRefresh)

    const addTeacherFormData = useAppSelector(addTeacherFormSelector)

    const dispatch = useAppDispatch()

    const firstNameField = useRef<HTMLInputElement>(null)
    const lastNameField = useRef<HTMLInputElement>(null)

    const onSubmit = (event: FormEvent) => {
        event.preventDefault()
        addTeacherSync({body: addTeacherFormData})
        return false
    }

    const onInput = () => {
        dispatch(addTeacherFormUpdate({
            firstName: firstNameField.current.value,
            lastName: lastNameField.current.value
        }))
    }

    if (addTeacherState.loading === LoadingStatus.FINISHED) {
        if (addTeacherState.object !== undefined) {
            return (
                <>
                    <LogoutButton redirect={redirect} />
                    <IndexButton redirect={redirect} />
                    <h1>{addTeacherState.object.inviteCode}</h1>
                    <p>А кнопка эта не сработает, потому что не https(((</p>
                    <button onClick={() => navigator.clipboard.writeText(addTeacherState.object.inviteCode)}>
                        Скопировать
                    </button>
                </>
            )
        } else {
            return <Error />
        }
    } else if (addTeacherState.loading === LoadingStatus.IN_PROGRESS) {
        return <Loading />
    } else {
        return (
            <>
                <LogoutButton redirect={redirect} />
                <IndexButton redirect={redirect} />
                <h3>Имя на русском</h3>
                <form name="add_teacher" onSubmit={onSubmit}>
                    <input type="text" onInput={onInput} placeholder="Имя" ref={firstNameField} />
                    <input type="text" onInput={onInput} placeholder="Фамилия" ref={lastNameField} />
                    <input type="submit" value="Добавить" />
                </form>
            </>
        )
    }

}
