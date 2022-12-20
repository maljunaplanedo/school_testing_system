import React from "react";
import useRedirect from "../util/redirect";
import LogoutButton from "./LogoutButton";
import IndexButton from "./IndexButton";

export default function Tasks() {
    const redirect = useRedirect('/teacher/tasks')
    return (
        <>
            <LogoutButton redirect={redirect}/>
            <IndexButton redirect={redirect}/>
            <button onClick={() => redirect("/teacher/add_task")}>Добавить задание</button>
        </>
    )
}
