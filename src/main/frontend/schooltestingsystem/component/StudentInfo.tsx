import React from "react";
import Student from "./Student";
import {useParams} from "react-router-dom";
import {getStudentForTeacher} from "../request/getStudent";

interface IStudentInfoPathParams {
    id: string
}

export default function StudentInfo() {
    const {id} = useParams<IStudentInfoPathParams>()
    return <Student getStudent={getStudentForTeacher} isTeacher={true} path={"/teacher/student/" + id} id={id} />
}

