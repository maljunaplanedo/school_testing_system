import {httpRequestHandler, NoData} from "./handler";
import IUser from "../dto/User";

export const getStudentForTeacher = httpRequestHandler<IUser, NoData>(
    'getStudentForTeacher', '/api/teacher/student'
)

export const getStudentForStudent = httpRequestHandler<IUser, NoData>(
    'getStudentForStudent', '/api/student'
)
