import {assign} from "../request/assign";
import useRedirect from "../util/redirect";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {assignFormSelector, assignFormUpdate} from "../store/assignForm";
import React, {FormEvent, useEffect, useRef} from "react";
import {LoadingStatus} from "../request/handler";
import Loading from "./Loading";
import LogoutButton from "./LogoutButton";
import IndexButton from "./IndexButton";
import {getClasses} from "../request/getClasses";
import {getTasks} from "../request/getTasks";
import Error from "./Error";

export default function Assign() {
    const assignState = assign.useState()
    const assignRefresh = assign.useRefresh()
    const assignSync = assign.useSync()

    const getClassesState = getClasses.useState()
    const getClassesRefresh = getClasses.useRefresh()
    const getClassesSync = getClasses.useSync()

    const getTasksState = getTasks.useState()
    const getTasksRefresh = getTasks.useRefresh()
    const getTasksSync = getTasks.useSync()

    const redirect = useRedirect('/teacher/assign', () => {
        assignRefresh()
        getClassesRefresh()
        getTasksRefresh()
    })

    useEffect(() => {
        getClassesSync()
        getTasksSync()
    })

    useEffect(() => {
        if (assignState.loading === LoadingStatus.FINISHED && assignState.object !== undefined) {
            redirect('/')
        }
    })

    const dispatch = useAppDispatch()

    const assignFormData = useAppSelector(assignFormSelector)

    const classField = useRef<HTMLSelectElement>(null)
    const taskField = useRef<HTMLSelectElement>(null)
    const deadlineField = useRef<HTMLInputElement>(null)

    if (getClassesState.loading !== LoadingStatus.FINISHED || getTasksState.loading !== LoadingStatus.FINISHED) {
        return <Loading />
    } else if (getClassesState.object === undefined || getTasksState.object === undefined) {
        return <Error/>
    } else if (getClassesState.object.length === 0) {
        return (
            <>
                <LogoutButton redirect={redirect} />
                <IndexButton redirect={redirect} />
                <Error message="Невозможно задать задание классу - нет ни одного класса!" />
            </>
        )
    } else if (getTasksState.object.length === 0) {
        return (
            <>
                <LogoutButton redirect={redirect} />
                <IndexButton redirect={redirect} />
                <Error message="Невозможно задать задание классу - не добавлено ни одного задания!" />
            </>
        )
    }

    const onSubmit = (event: FormEvent) => {
        event.preventDefault()
        assignSync({body: assignFormData}, true)
        return false
    }

    const onInput = () => {
        dispatch(assignFormUpdate({
            schoolClass: {id: Number.parseInt(classField.current.value)},
            task: {id: Number.parseInt(taskField.current.value)},
            deadline: Date.parse(deadlineField.current.value) / 1000
        }))
    }

    if (assignState.loading === LoadingStatus.FINISHED && assignState.object === undefined) {
        return <Error />
    } else if (assignState.loading === LoadingStatus.IN_PROGRESS || (
        assignState.loading === LoadingStatus.FINISHED && assignState.object !== undefined
    )) {
        return <Loading />
    } else {
        return (
            <>
                <LogoutButton redirect={redirect} />
                <IndexButton redirect={redirect} />
                <form name="assign" onSubmit={onSubmit}>
                    <select onInput={onInput} ref={classField}>
                        {
                            getClassesState.object.map((schoolClass, i) =>
                                <option value={schoolClass.id} key={i}>
                                    {schoolClass.name}
                                </option>
                            )
                        }
                    </select>
                    <select onInput={onInput} ref={taskField}>
                        {
                            getTasksState.object.map((task, i) =>
                                <option value={task.id} key={i}>
                                    {task.name}
                                </option>
                            )
                        }
                    </select>
                    <input type="datetime-local" onInput={onInput} ref={deadlineField} />
                    <input type="submit" value="Задать" />
                </form>
            </>
        )
    }
}
