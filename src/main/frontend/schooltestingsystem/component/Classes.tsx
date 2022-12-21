import React, {useEffect} from "react";
import useRedirect from "../util/redirect";
import IndexButton from "./IndexButton";
import LogoutButton from "./LogoutButton";
import {getClasses} from "../request/getClasses";
import {LoadingStatus} from "../request/handler";
import Error from "./Error";
import Loading from "./Loading";

export default function Classes() {
    const getClassesState = getClasses.useState()
    const getClassesRefresh = getClasses.useRefresh()
    const getClassesSync = getClasses.useSync()

    const redirect = useRedirect("/teacher/classes", getClassesRefresh)

    useEffect(getClassesSync)

    if (getClassesState.loading === LoadingStatus.FINISHED) {
        if (getClassesState.object === undefined) {
            return <Error/>
        } else {
            return (
                <>
                    <LogoutButton redirect={redirect}/>
                    <IndexButton redirect={redirect}/>
                    <button onClick={() => redirect("/teacher/add_class")}>Добавить класс</button>
                    <ul>
                        {getClassesState.object.map((schoolClass, i) =>
                            <li key={i}><button className="ref" onClick={() => redirect('/teacher/class/' + schoolClass.id)}>
                                {schoolClass.name}
                            </button></li>
                        )}
                    </ul>
                </>
            )
        }
    }

    return <Loading/>
}
