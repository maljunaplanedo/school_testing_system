import {httpRequestHandler, NoData} from "./handler";
import IClassTask from "../dto/ClassTask";

export const assign = httpRequestHandler<NoData, IClassTask>(
    'assign', '/api/teacher/class_task', 'POST'
)
