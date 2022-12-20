import {httpRequestHandler} from "./handler";
import IAddClassResponse from "../dto/AddClassResponse";
import IClass from "../dto/Class";

export const addClass = httpRequestHandler<IAddClassResponse, IClass>(
    'addClass', '/api/teacher/class', 'POST'
)
