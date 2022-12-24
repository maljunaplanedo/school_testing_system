import React, {useEffect, useRef} from "react";
import {useParams} from "react-router-dom";
import {getStudentTask} from "../request/getStudentTask";
import {beginStudentTask} from "../request/beginStudentTask";
import {endStudentTask} from "../request/endStudentTask";
import {LoadingStatus} from "../request/handler";
import useRedirect from "../util/redirect";
import Error from "./Error";
import Loading from "./Loading";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {taskRunnerActions, taskRunnerSelector} from "../store/taskRunner";
import ILiveGapsAnswer, {ILiveGapsGap} from "../dto/LiveGapsAnswer";
import ITaskRunnerResponse from "../dto/TaskRunnerResponse";
import LogoutButton from "./LogoutButton";

interface IStudentTaskPathParams {
    id: string
}

export interface ILiveGapsRequest {
    index: number
    letter: string
}

export interface ILiveGapsResponse {
    ok: boolean
    full: boolean
}

interface ITextPartToRender {
    text: string
    isGap: boolean
    isFilled?: boolean
}

let connection: WebSocket = null
let pinger: ReturnType<typeof setInterval> = null
let timer: ReturnType<typeof setInterval> = null

export default function StudentTask() {
    const {id} = useParams<IStudentTaskPathParams>()

    const getStudentTaskState = getStudentTask.useState()
    const getStudentTaskRefresh = getStudentTask.useRefresh()
    const getStudentTaskSync = getStudentTask.useSync()

    const beginStudentTaskState = beginStudentTask.useState()
    const beginStudentTaskRefresh = beginStudentTask.useRefresh()
    const beginStudentTaskSync = beginStudentTask.useSync()

    const endStudentTaskState = endStudentTask.useState()
    const endStudentTaskRefresh = endStudentTask.useRefresh()
    const endStudentTaskSync = endStudentTask.useSync()

    const taskRunnerState = useAppSelector(taskRunnerSelector)

    const dispatch = useAppDispatch()

    const redirect = useRedirect('/student/task/' + id, () => {
        clearInterval(pinger)
        clearInterval(timer)
        getStudentTaskRefresh()
        beginStudentTaskRefresh()
        endStudentTaskRefresh()
        dispatch(taskRunnerActions.refresh())
        if (connection !== null) {
            connection.close()
            connection = null
        }
    })

    const taskRunnerStateRef = useRef<typeof taskRunnerState>()
    const dispatchRef = useRef<typeof dispatch>()
    const redirectRef = useRef<typeof redirect>()
    const gapsRef = useRef<HTMLInputElement[]>([])

    taskRunnerStateRef.current = taskRunnerState
    dispatchRef.current = dispatch
    redirectRef.current = redirect

    useEffect(() => {
        getStudentTaskSync({suffix: id})
    })

    useEffect(() => {
        if (getStudentTaskState.loading === LoadingStatus.FINISHED &&
            getStudentTaskState.object !== undefined &&
            getStudentTaskState.object.status === "NOT_STARTED"
        ) {
            beginStudentTaskSync({suffix: id})
        }
    })

    useEffect(() => {
        if (getStudentTaskState.loading === LoadingStatus.FINISHED &&
            getStudentTaskState.object !== undefined &&
            getStudentTaskState.object.status === "FINISHED"
        ) {
            redirect('/')
        }
    })

    useEffect(() => {
        if (getStudentTaskState.loading === LoadingStatus.FINISHED &&
            getStudentTaskState.object !== undefined &&
            getStudentTaskState.object.status === "NOT_STARTED" &&
            beginStudentTaskState.loading === LoadingStatus.FINISHED
        ) {
            getStudentTaskRefresh()
        }
    })

    useEffect(() => {
        if (
            getStudentTaskState.loading === LoadingStatus.FINISHED &&
            getStudentTaskState.object !== undefined &&
            getStudentTaskState.object.status === "IN_PROGRESS" &&
            taskRunnerState.answer === undefined
        ) {
            let braces = 0
            for (let i = 0; i < getStudentTaskState.object.classTask.task.statement.length; ++i) {
                braces += Number(getStudentTaskState.object.classTask.task.statement[i] == '[')
            }

            let gaps: ILiveGapsGap[]

            if (getStudentTaskState.object.answer === undefined) {
                gaps = []
                for (let i = 0; i < braces; ++i) {
                    gaps.push({
                        value: "",
                        full: false
                    })
                }
            } else {
                gaps = (JSON.parse(getStudentTaskState.object.answer) as ILiveGapsAnswer).gaps
            }

            dispatch(taskRunnerActions.update({
                answer: {gaps: gaps},
                open: false
            }))

            connection = new WebSocket('ws://' + window.location.host + '/api/live')
            connection.onopen = () => {
                dispatchRef.current(taskRunnerActions.open())
                connection.send("<PING>")
            }
            connection.onclose = () => {
                redirectRef.current('/')
            }
            connection.onerror = () => {
                redirectRef.current('/')
            }
            connection.onmessage = (event) => {
                const response = JSON.parse(event.data) as ITaskRunnerResponse

                const newState = {
                    ...taskRunnerStateRef.current,
                    mark: response.mark,
                    timeLeft: response.timeLeft
                }

                if (response.payload !== undefined) {
                    const liveGapsResponse = JSON.parse(response.payload) as ILiveGapsResponse

                    const index = taskRunnerStateRef.current.request.index
                    const letter = taskRunnerStateRef.current.request.letter

                    let newGaps = [...taskRunnerStateRef.current.answer.gaps.map(gap => {return {...gap}})]

                    if (liveGapsResponse.ok) {
                        newGaps[index].value += letter
                    }
                    if (liveGapsResponse.full) {
                        newGaps[index].full = true
                    }

                    newState.answer = {gaps: newGaps}
                    newState.open = true

                    const focus = (index: number) => {
                        gapsRef.current[index].disabled = false
                        gapsRef.current[index].focus()
                    }

                    if (!liveGapsResponse.full) {
                        focus(index)
                    } else if (index + 1 < newGaps.length) {
                        focus(index + 1)
                    } else {
                        for (let i = 0; i < newGaps.length; ++i) {
                            if (!newGaps[i].full) {
                                focus(i)
                                break
                            }
                        }
                    }
                }

                dispatchRef.current(taskRunnerActions.update(newState))
            }

            pinger = setInterval(() => {
                if (taskRunnerStateRef.current.open) {
                    connection.send("<PING>")
                }
            }, 10000)

            timer = setInterval(() => {
                dispatchRef.current(taskRunnerActions.minusSec())
            }, 1000)

        }
    })

    useEffect(() => {
        if (endStudentTaskState.loading === LoadingStatus.FINISHED) {
            redirectRef.current('/')
        }
    })

    const onInput = (index: number) => (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (!event.key.match(/^[A-Za-zА-яЁё]$/)) {
            event.preventDefault()
            return false
        }

        const request: ILiveGapsRequest = {
            index: index,
            letter: event.key
        }

        dispatch(taskRunnerActions.startRequest(request))
        connection.send(JSON.stringify(request))
    }

    if ((getStudentTaskState.loading === LoadingStatus.FINISHED && getStudentTaskState.object === undefined) ||
        (beginStudentTaskState.loading === LoadingStatus.FINISHED && beginStudentTaskState.object === undefined) ||
        (endStudentTaskState.loading === LoadingStatus.FINISHED && endStudentTaskState.object === undefined)
    ) {
        return <Error/>
    }

    if (getStudentTaskState.loading !== LoadingStatus.FINISHED ||
        (
            getStudentTaskState.loading === LoadingStatus.FINISHED &&
            getStudentTaskState.object.status === "NOT_STARTED"
        ) ||
        beginStudentTaskState.loading === LoadingStatus.IN_PROGRESS ||
        endStudentTaskState.loading === LoadingStatus.IN_PROGRESS ||
        taskRunnerState.mark === undefined
    ) {
        return <Loading/>
    }

    const convert = (statement: string) => {
        const parts = statement.split('[')
        for (let i = 0; i < parts.length; ++i) {
            if (parts[i].length > 0 && parts[i][0] == ']') {
                parts[i] = parts[i].substring(1)
            }
        }

        const textPartsToRender: ITextPartToRender[] = []

        for (let i = 0; i < parts.length; ++i) {
            textPartsToRender.push({text: parts[i], isGap: false})
            if (i < parts.length - 1) {
                textPartsToRender.push({
                    text: taskRunnerState.answer.gaps[i].value,
                    isGap: true,
                    isFilled: taskRunnerState.answer.gaps[i].full
                })
            }
        }

        return textPartsToRender
    }

    const convertTime = (time: number) => {
        const floorAndAddZeroIfNeeded = (part: number) => {
            part = Math.floor(part)
            return part < 10 ? "0" + part : part.toString()
        }

        return floorAndAddZeroIfNeeded(time / 3600)
            + ":" + floorAndAddZeroIfNeeded(time % 3600 / 60)
            + ":" + floorAndAddZeroIfNeeded(time % 60)
    }

    return (
        <>
            <LogoutButton redirect={redirect} />
            <button onClick={() => redirect('/')}>Назад</button>
            <button onClick={() => {endStudentTaskSync()}}>Завершить</button>
            <p>За каждую неверно введенную букву - штраф -3 балла. За верно введенное слово - +10 баллов. <b>Удачи!</b>
            </p>
            <h1>Осталось: {convertTime(taskRunnerState.timeLeft)}</h1>
            <h1>Оценка: {taskRunnerState.mark + "/" + getStudentTaskState.object.classTask.task.maxMark}</h1>
            <div>
                {convert(getStudentTaskState.object.classTask.task.statement).map((part, i) =>
                    part.isGap ? (
                        <input type="text" key={i}
                            className="gap"
                            ref={el => gapsRef.current[(i - 1) / 2] = el}
                            disabled={part.isFilled || !taskRunnerState.open}
                            onKeyDown={onInput((i - 1) / 2)}
                            value={part.text} />
                    ) : (<span key={i}>{part.text}</span>)
                )}
            </div>
        </>
    )
}
