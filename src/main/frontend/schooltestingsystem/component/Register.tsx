import React, {FormEvent, useEffect, useRef} from "react";
import {register} from "../request/register";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {registrationFormSelector, registrationFormUpdate} from "../store/registrationForm";
import useRedirect from "../util/redirect";
import {LoadingStatus} from "../request/handler";
import Loading from "./Loading";
import Error from "./Error";

export default function Register() {
    const registerState = register.useState()
    const registerRefresh = register.useRefresh()
    const registerSync = register.useSync()

    const redirect = useRedirect("/register", registerRefresh)

    const dispatch = useAppDispatch()

    const registrationFormData = useAppSelector(registrationFormSelector)

    const usernameField = useRef<HTMLInputElement>(null)
    const passwordField = useRef<HTMLInputElement>(null)
    const inviteCodeField = useRef<HTMLInputElement>(null)

    useEffect(() => {
        if (
            registerState.loading === LoadingStatus.FINISHED &&
            registerState.object !== undefined &&
            registerState.object.success
        ) {
            redirect("/")
        }
    })

    const onInput = () => {
        dispatch(registrationFormUpdate({
            username: usernameField.current.value,
            password: passwordField.current.value,
            inviteCode: inviteCodeField.current.value
        }))
    }

    const onSubmit = (event: FormEvent) => {
        event.preventDefault()
        registerSync({body: registrationFormData}, true)
        return false
    }

    if ((registerState.loading === LoadingStatus.FINISHED
            && registerState.object !== undefined
            && registerState.object.success
    ) || registerState.loading === LoadingStatus.IN_PROGRESS) {
        return <Loading />
    }

    const registerForm = (
        <>
            <form name="register" onSubmit={onSubmit}>
                <input type="text" onInput={onInput} name="username" ref={usernameField} placeholder="Логин" />
                <input type="password" onInput={onInput} name="password" ref={passwordField} placeholder="Пароль" />
                <input type="text" onInput={onInput} name="inviteCode" ref={inviteCodeField} placeholder="Пригласительный код" />
                <input type="submit" value="Регистрация" />
                <button onClick={() => redirect("/login")}>Уже зарегистрированы?</button>
            </form>
        </>
    )

    let errorMessage: string = null;
    if (registerState.loading === LoadingStatus.FINISHED) {
        errorMessage = ""
        if (registerState.object !== undefined) {
            errorMessage = registerState.object.cause
            if (errorMessage === "USERNAME_ALREADY_USED") {
                errorMessage = "Такой логин уже используется"
            } else if (errorMessage === "WRONG_INVITE_CODE") {
                errorMessage = "Такого пригласительного кода не существует"
            }
        }
    }

    if (errorMessage !== null) {
        return <> {registerForm} <Error message={errorMessage} /> </>
    } else {
        return registerForm
    }
}
