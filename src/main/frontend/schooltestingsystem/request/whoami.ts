import {NoData, httpRequestHandler} from "./handler";
import IUser from "../dto/User";
import {RootState} from "../store/store";

export const whoami = httpRequestHandler<IUser, NoData>("whoami", "/api/auth/whoami")
