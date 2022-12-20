import IClassTask from "./ClassTask";
import IUser from "./User";

export default interface IStudentTask {
    id?: number
    classTask?: IClassTask
    student?: IUser
    status?: string
    begin?: number
    limit?: number
    end?: number
    answer?: string
    mark?: number
}
