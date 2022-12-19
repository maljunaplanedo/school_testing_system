import {httpRequestHandler, NoData} from "./handler";
import IUser from "../dto/User";

export const getTeachers = httpRequestHandler<IUser[], NoData>( 'getTeachers', '/api/admin/teacher')
