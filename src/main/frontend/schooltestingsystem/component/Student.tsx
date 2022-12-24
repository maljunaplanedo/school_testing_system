import React, {useEffect} from "react";
import {IHttpRequestHandler, LoadingStatus, NoData} from "../request/handler";
import IUser from "../dto/User";
import useRedirect from "../util/redirect";
import Loading from "./Loading";
import Error from "./Error";
import LogoutButton from "./LogoutButton";
import IndexButton from "./IndexButton";

interface IStudentProps {
    getStudent: IHttpRequestHandler<IUser, NoData>
    isTeacher: boolean
    path: string
    id?: string
}

export default function Student({getStudent, isTeacher, path, id}: IStudentProps) {
    const getStudentState = getStudent.useState()
    const getStudentRefresh = getStudent.useRefresh()
    const getStudentSync = getStudent.useSync()

    const redirect = useRedirect(path, getStudentRefresh)

    useEffect(() => {
        if (isTeacher) {
            getStudentSync({suffix: id})
        } else {
            getStudentSync()
        }
    })

    if (getStudentState.loading !== LoadingStatus.FINISHED) {
        return <Loading />
    } else if (getStudentState.object === undefined) {
        return <Error />
    }

    const messageFromStatus = (status: string) => {
        switch (status) {
            case "NOT_STARTED":
                return "Не начато"
            case "IN_PROGRESS":
                return "Выполняется"
            case "FINISHED":
                return "Окончено"
        }
    }

    const displayMarkOrNot = (status: string) => {
        return (isTeacher && status == "FINISHED") || (!isTeacher && status != "NOT_STARTED")
    }

    const common = (
        <>
            <h1>{getStudentState.object.firstName + " " +
                getStudentState.object.lastName + " " + getStudentState.object.schoolClass.name}</h1>
            <ul>
                {getStudentState.object.studentTasks.map((task, i) =>
                    <li key={i}>
                        <>
                        {(!isTeacher && task.status !== "FINISHED")
                            ? <button className="ref" onClick={() => redirect("/student/task/" + task.id)}>
                                {task.status === "IN_PROGRESS" ? "Продолжить" : "Начать"}
                            </button>
                        : null}
                        </>
                        <>
                            {
                                task.classTask.task.name + " Статус: " + messageFromStatus(task.status) +
                                (displayMarkOrNot(task.status) ? (" Оценка: " + task.mark + "/" +
                                    task.classTask.task.maxMark
                                ) : "")
                                + " Дедлайн: " + new Date(task.classTask.deadline * 1000).toLocaleString()
                            }
                        </>
                    </li>
                )}
            </ul>
        </>
    )

    if (isTeacher) {
        return (
            <>
                <LogoutButton redirect={redirect} />
                <IndexButton redirect={redirect} />
                {common}
            </>
        )
    } else {
        return (
            <>
                <LogoutButton redirect={redirect} />
                {common}
            </>
        )
    }
}
