import IClass from "./Class";
import IStudentTask from "./StudentTask";

export default interface IUser {
    id?: Number
    username?: string
    role?: string
    inviteCode?: string
    firstName?: string
    lastName?: string
    schoolClass?: IClass
    studentTasks?: IStudentTask[]
}
