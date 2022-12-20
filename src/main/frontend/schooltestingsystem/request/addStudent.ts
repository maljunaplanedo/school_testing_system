import {httpRequestHandler} from "./handler";
import IAddUserResponse from "../dto/AddUserResponse";
import IUser from "../dto/User";

export const addStudent = httpRequestHandler<IAddUserResponse, IUser>(
    'addStudent', '/api/teacher/student', 'POST'
)
