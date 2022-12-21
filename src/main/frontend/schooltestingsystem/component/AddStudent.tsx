import {addStudent} from "../request/addStudent";
import useRedirect from "../util/redirect";
import React, {FormEvent, useEffect, useRef} from "react";
import {LoadingStatus} from "../request/handler";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {addStudentFormSelector, addStudentFormUpdate} from "../store/addStudentForm";
import IndexButton from "./IndexButton";
import Error from "./Error";
import Loading from "./Loading";
import LogoutButton from "./LogoutButton";
import {getClasses} from "../request/getClasses";

export default function AddStudent() {
    const addStudentState = addStudent.useState()
    const addStudentRefresh = addStudent.useRefresh()
    const addStudentSync = addStudent.useSync()

    const getClassesState = getClasses.useState()
    const getClassesRefresh = getClasses.useRefresh()
    const getClassesSync = getClasses.useSync()

    useEffect(getClassesSync)

    const redirect = useRedirect('/teacher/add_student', () => {
        addStudentRefresh()
        getClassesRefresh()
    })

    const addStudentFormData = useAppSelector(addStudentFormSelector)

    const dispatch = useAppDispatch()

    const firstNameField = useRef<HTMLInputElement>(null)
    const lastNameField = useRef<HTMLInputElement>(null)
    const classSelectField = useRef<HTMLSelectElement>(null)

    const onSubmit = (event: FormEvent) => {
        event.preventDefault()
        addStudentSync({body: addStudentFormData})
        return false
    }

    const onInput = () => {
        dispatch(addStudentFormUpdate({
            firstName: firstNameField.current.value,
            lastName: lastNameField.current.value,
            schoolClass: {id: Number.parseInt(classSelectField.current.value)}
        }))
    }

    if (getClassesState.loading !== LoadingStatus.FINISHED) {
        return <Loading />
    } else if (getClassesState.object === undefined) {
        return <Error/>
    } else if (getClassesState.object.length === 0) {
        return (
            <>
                <LogoutButton redirect={redirect} />
                <IndexButton redirect={redirect} />
                <Error message="Невозможно добавить ученика! Нужен хотя бы один класс" />
            </>
        )
    }

    if (addStudentState.loading === LoadingStatus.FINISHED) {
        if (addStudentState.object !== undefined) {
            return (
                <>
                    <LogoutButton redirect={redirect} />
                    <IndexButton redirect={redirect} />
                    <h1>{addStudentState.object.inviteCode}</h1>
                    <p>А кнопка эта не сработает, потому что не https(((</p>
                    <button onClick={() => navigator.clipboard.writeText(addStudentState.object.inviteCode)}>
                        Скопировать
                    </button>
                </>
            )
        } else {
            return <Error />
        }
    } else if (addStudentState.loading === LoadingStatus.IN_PROGRESS) {
        return <Loading />
    } else {
        return (
            <>
                <LogoutButton redirect={redirect} />
                <IndexButton redirect={redirect} />
                <h3>Имя на русском</h3>
                <form name="add_student" onSubmit={onSubmit}>
                    <input type="text" onInput={onInput} placeholder="Имя" ref={firstNameField} />
                    <input type="text" onInput={onInput} placeholder="Фамилия" ref={lastNameField} />
                    <select onInput={onInput} ref={classSelectField}>
                        {
                            getClassesState.object.map((schoolClass, i) =>
                                <option value={schoolClass.id} key={i}>
                                    {schoolClass.name}
                                </option>
                            )
                        }
                    </select>
                    <input type="submit" value="Добавить" />
                </form>
            </>
        )
    }
}
