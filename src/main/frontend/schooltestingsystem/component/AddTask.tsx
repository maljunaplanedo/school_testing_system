import {addTask} from "../request/addTask";
import useRedirect from "../util/redirect";
import React, {useEffect, useRef} from "react";
import {LoadingStatus} from "../request/handler";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {addTaskFormSelector, addTaskFormUpdate, ITaskFormState} from "../store/addTaskForm";
import ITask from "../dto/Task";
import ILiveGapsAnswer from "../dto/LiveGapsAnswer";
import {addClassFormUpdate} from "../store/addClassForm";
import Error from "./Error";
import Loading from "./Loading";

export default function AddTask() {
    const addTaskState = addTask.useState()
    const addTaskSync = addTask.useSync()
    const addTaskRefresh = addTask.useRefresh()

    const redirect = useRedirect("/teacher/add_task", addTaskRefresh)

    useEffect(() => {
        if (addTaskState.loading === LoadingStatus.FINISHED && addTaskState.object !== undefined) {
            redirect("/teacher/tasks")
        }
    })

    const dispatch = useAppDispatch()

    const addTaskFormData = useAppSelector(addTaskFormSelector)

    const nameField = useRef<HTMLInputElement>(null)
    const statementField = useRef<HTMLInputElement>(null)
    const answerField = useRef<HTMLInputElement>(null)
    const durationField = useRef<HTMLInputElement>(null)

    const convert = function (formData: ITaskFormState): ITask {
        let braces = 0
        let braceBalance = 0

        for (let i = 0; i < formData.statement.length; ++i) {
            braces += Number(formData.statement[i] === '[')
            braceBalance += Number(formData.statement[i] === '[')
            braceBalance -= Number(formData.statement[i] === ']')

            if (formData.statement[i] == '[' && (
                i == formData.statement.length - 1 || formData.statement[i + 1] != ']')
            ) {
                console.log(1)
                return null
            }

            if (braceBalance > 1 || braceBalance < 0) {
                console.log(2)
                return null
            }
        }

        if (braceBalance != 0) {
            console.log(3)
            return null
        }

        let gaps = formData.answer.split(";")
        for (let i = 0; i < gaps.length; ++i) {
            gaps[i] = gaps[i].trim()
        }

        if (gaps.length != braces) {
            console.log(4)
            return null
        }

        if (!formData.duration.match(/^[0-9]+$/)) {
            console.log(5)
            return null
        }

        const result: ITask = {}
        result.answer = JSON.stringify({gaps: gaps.map(gap => {return {value: gap}})} as ILiveGapsAnswer)
        result.statement = formData.statement
        result.name = formData.name
        result.type = "LIVE_GAPS"
        result.duration = Number.parseInt(formData.duration)

        return result
    }

    const onSubmit = () => {
        const task = convert(addTaskFormData)
        if (task === null) {
            dispatch(addClassFormUpdate({...addTaskFormData, valid: false}))
            return false
        }
        addTaskSync({body: task}, true)
        return false
    }

    const onInput = () => {
        dispatch(addTaskFormUpdate({
            ...addTaskFormData,
            name: nameField.current.value,
            statement: statementField.current.value,
            answer: answerField.current.value,
            duration: durationField.current.value
        }))
    }

    if (addTaskState.loading === LoadingStatus.FINISHED && addTaskState.object === undefined) {
        return <Error/>
    } else if (addTaskState.loading === LoadingStatus.IN_PROGRESS) {
        return <Loading/>
    } else {
        const form = (
            <>
                <p>Как вводить задание? Условие - текст с пропусками. На месте пропуска - квадратные скобки.
                Например: <i>This [] gaps task [] you.</i> Ответ - ответы на пропуски через ';'. Пример:
                <i>live; for</i>
                </p>
                <form name="addTask" onSubmit={onSubmit}>
                    <input type="text" onInput={onInput} ref={nameField} name="name" placeholder="Название" />
                    <input type="text" onInput={onInput} ref={statementField} name="statement" placeholder="Условие" />
                    <input type="text" onInput={onInput} ref={answerField} name="answer" placeholder="Ответ" />
                    <input type="number" onInput={onInput} ref={durationField} name="duration" placeholder="Время на выполнение, с" />
                    <input type="submit" />
                </form>
            </>
        )

        if (!addTaskFormData.valid) {
            return <>{form} <Error message="Неправильный формат задания! Пожалуйста, прочитайте внимательно" /> </>
        }
        if (addTaskState.loading === LoadingStatus.FINISHED && addTaskState.object === undefined) {
            return <>{form} <Error /></>
        }
        return form
    }

}
