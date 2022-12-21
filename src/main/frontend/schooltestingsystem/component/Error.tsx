import React from "react";

interface IErrorProps {
    message?: string
}

export default function Error(props: IErrorProps) {
    return <h1 className="error">Ошибка! {props.message === undefined ? "" : props.message}</h1>
}
