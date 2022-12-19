import {httpRequestHandler} from "./handler";
import IAddUserResponse from "../dto/AddUserResponse";
import IUser from "../dto/User";

export const addTeacher = httpRequestHandler<IAddUserResponse, IUser>(
    'addTeacher', '/api/admin/teacher', 'POST'
)
