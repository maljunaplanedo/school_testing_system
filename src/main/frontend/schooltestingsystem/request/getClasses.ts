import {httpRequestHandler, NoData} from "./handler";
import IClass from "../dto/Class";

export const getClasses = httpRequestHandler<IClass[], NoData>('getClasses', '/api/teacher/class')
