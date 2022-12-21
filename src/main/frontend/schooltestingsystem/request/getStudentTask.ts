import {httpRequestHandler, NoData} from "./handler";
import IStudentTask from "../dto/StudentTask";

export const getStudentTask = httpRequestHandler<IStudentTask, NoData>(
    'getStudentTask', '/api/student/task'
)
