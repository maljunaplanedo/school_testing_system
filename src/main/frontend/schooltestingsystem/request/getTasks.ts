import {httpRequestHandler, NoData} from "./handler";
import ITask from "../dto/Task";

export const getTasks = httpRequestHandler<ITask[], NoData>(
    'getTasks', '/api/teacher/task'
)
