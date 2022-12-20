import IUser from "./User";
import IClassTask from "./ClassTask";

export default interface IClass {
    id?: number
    name?: string
    students?: IUser[]
    assignedTasks?: IClassTask[]
}
