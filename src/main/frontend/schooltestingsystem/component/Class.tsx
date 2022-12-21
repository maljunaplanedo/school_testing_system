import React, {useEffect} from "react";
import {useParams} from "react-router-dom";
import {getClass} from "../request/getClass";
import useRedirect from "../util/redirect";
import {LoadingStatus} from "../request/handler";
import Loading from "./Loading";
import Error from "./Error";
import LogoutButton from "./LogoutButton";
import IndexButton from "./IndexButton";

interface IClassPathParams {
    id: string
}

export default function Class() {
    const {id} = useParams<IClassPathParams>()

    const getClassState = getClass.useState()
    const getClassRefresh = getClass.useRefresh()
    const getClassSync = getClass.useSync()

    const redirect = useRedirect('/teacher/class/' + id, getClassRefresh)

    useEffect(() => {
        getClassSync({suffix: id})
    })

    if (getClassState.loading !== LoadingStatus.FINISHED) {
        return <Loading />
    } else if (getClassState.object === undefined) {
        return <Error />
    }

    return (
        <>
            <LogoutButton redirect={redirect} />
            <IndexButton redirect={redirect} />
            <h1>{getClassState.object.name}</h1>
            <ul>
                {getClassState.object.students.map((student, i) =>
                    <li key={i}><button className="ref" onClick={() => redirect('/teacher/student/' + student.id)}>Перейти</button>
                        {student.firstName + " " + student.lastName + " " + (student.inviteCode || "")}
                    </li>
                )}
            </ul>
        </>
    )
}
