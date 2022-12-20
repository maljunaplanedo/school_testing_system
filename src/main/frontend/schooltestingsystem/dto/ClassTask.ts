import IClass from "./Class";
import ITask from "./Task";
import IStudentTask from "./StudentTask";

export default interface IClassTask {
    id?: number
    task?: ITask
    schoolClass?: IClass
    deadline?: number
    studentTasks?: IStudentTask[]
}
