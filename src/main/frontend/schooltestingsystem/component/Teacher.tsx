import React from "react";
import useRedirect from "../util/redirect";
import LogoutButton from "./LogoutButton";

export default function Teacher() {
    const redirect = useRedirect("/teacher")

    return (
        <>
            <LogoutButton redirect={redirect}/>
            <button onClick={() => redirect('/teacher/classes')}>Классы</button>
            <button onClick={() => redirect('/teacher/add_student')}>Добавить ученика</button>
            <button onClick={() => redirect('/teacher/add_task')}>Добавить задание</button>
            <button onClick={() => redirect('/teacher/assign')}>Задать задание классу</button>
        </>
    )
}
