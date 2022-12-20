import {httpRequestHandler, NoData} from "./handler";
import ITask from "../dto/Task";

export const addTask = httpRequestHandler<NoData, ITask>('addTask', '/api/teacher/task', 'POST')
