import {httpRequestHandler} from "./handler";
import IRegistrationResponse from "../dto/RegistrationResponse";
import IRegistrationForm from "../dto/RegistrationForm";

export const register = httpRequestHandler<IRegistrationResponse, IRegistrationForm>(
    'register', '/api/auth/register', 'POST'
)
