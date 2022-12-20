import {httpRequestHandler, NoData} from "./handler";
import IClass from "../dto/Class";

export const getClass = httpRequestHandler<IClass, NoData>(
    'getClass', '/api/teacher/class'
)
