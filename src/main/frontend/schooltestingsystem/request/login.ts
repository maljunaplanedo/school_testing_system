import {httpRequestHandler, NoData} from "./handler";
import ILoginForm from "../dto/LoginForm";

export const login = httpRequestHandler<NoData, ILoginForm>('login', '/api/auth/login', 'POST')
