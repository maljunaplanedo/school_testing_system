import {httpRequestHandler, NoData} from "./handler";

export const endStudentTask = httpRequestHandler<NoData, NoData>(
    'endStudentTask', '/api/student/task', 'PUT'
)
