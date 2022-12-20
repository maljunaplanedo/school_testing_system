import React, {useEffect, useRef} from "react";
import {login} from "../request/login";
import useRedirect from "../util/redirect";
import {LoadingStatus} from "../request/handler";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {loginFormSelector, loginFormUpdate} from "../store/loginForm";
import Loading from "./Loading";
import Error from "./Error";

export default function Login() {
    const loginState = login.useState()
    const loginRefresh = login.useRefresh()
    const loginSync = login.useSync()

    const redirect = useRedirect("/login", loginRefresh)

    const dispatch = useAppDispatch()
    const loginFormData = useAppSelector(loginFormSelector)

    useEffect(() => {
        if (loginState.loading === LoadingStatus.FINISHED && loginState.object !== undefined) {
            redirect("/")
        }
    })

    const usernameField = useRef<HTMLInputElement>(null)
    const passwordField = useRef<HTMLInputElement>(null)

    const onInput = () => {
        dispatch(loginFormUpdate({
            username: usernameField.current.value,
            password: passwordField.current.value
        }))
    }

    const onSubmit = () => {
        loginSync({body: loginFormData}, true)
        return false
    }

    if (
        loginState.loading === LoadingStatus.IN_PROGRESS ||
        (loginState.loading === LoadingStatus.FINISHED && loginState.object !== undefined)
    ) {
        return <Loading />
    } else {
        const loginForm = (
            <>
                <form name="login" onSubmit={onSubmit}>
                    <input type="text" ref={usernameField} onInput={onInput} placeholder="Логин" />
                    <input type="password" ref={passwordField} onInput={onInput} placeholder="Пароль" />
                    <input type="submit" />
                </form>
                <button onClick={() => redirect('/register')}>Зарегистрироваться</button>
            </>
        )

        if (loginState.loading === LoadingStatus.FINISHED) {
            return <> {loginForm} <Error message="Неверные данные для входа" /></>
        }
        return <>{loginForm}</>
    }
}
