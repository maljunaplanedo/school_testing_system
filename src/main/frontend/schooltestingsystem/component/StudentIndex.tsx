import React from "react";
import Student from "./Student";
import {getStudentForStudent} from "../request/getStudent";

export default function StudentIndex() {
    return <Student getStudent={getStudentForStudent} isTeacher={false} path="/student" />
}
