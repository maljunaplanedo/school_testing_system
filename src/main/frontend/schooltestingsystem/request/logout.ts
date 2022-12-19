import {httpRequestHandler, NoData} from "./handler";

export const logout = httpRequestHandler<NoData, NoData>('logout', 'api/auth/logout', 'POST')
