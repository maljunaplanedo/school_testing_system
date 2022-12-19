export enum UserRole {
    ADMIN,
    TEACHER,
    STUDENT
}

export default interface IUser {
    id?: Number
    username?: string
    role?: UserRole
    inviteCode?: string
    firstName?: string
    lastName?: string
    schoolClass?: string
    studentTasks?: string[]
}
