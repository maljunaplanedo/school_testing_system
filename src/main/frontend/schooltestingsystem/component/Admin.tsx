import React, {useEffect} from "react";
import useRedirect from "../util/redirect";
import LogoutButton from "./LogoutButton";
import {getTeachers} from "../request/getTeachers";
import {LoadingStatus} from "../request/handler";
import Error from "./Error";
import Loading from "./Loading";

export default function Admin() {
    const getTeachersState = getTeachers.useState()
    const getTeachersRefresh = getTeachers.useRefresh()
    const getTeachersSync = getTeachers.useSync()

    const redirect = useRedirect("/admin", getTeachersRefresh)

    useEffect(getTeachersSync)

    if (getTeachersState.loading === LoadingStatus.FINISHED) {
        if (getTeachersState.object === undefined) {
            return <Error/>
        } else {
            return (
                <>
                    <LogoutButton redirect={redirect}/>
                    <button onClick={() => redirect("/admin/add_teacher")}>Добавить учителя</button>
                    <ul>
                        {getTeachersState.object.map((teacher, i) =>
                            <li key={i}>{teacher.firstName + " " + teacher.lastName + " " + (teacher.inviteCode || "")}</li>)}
                    </ul>
                </>
            )
        }
    }

    return <Loading />
}
