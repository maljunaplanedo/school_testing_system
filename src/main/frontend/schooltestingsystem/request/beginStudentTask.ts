import {httpRequestHandler, NoData} from "./handler";

export const beginStudentTask = httpRequestHandler<NoData, NoData>(
    'beginStudentTask', '/api/student/task', 'PUT'
)
