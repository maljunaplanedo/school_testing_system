import React from "react";

interface IErrorProps {
    message?: string
}

export default function Error(props: IErrorProps) {
    return <h1 style={{color: 'red'}}>Error {props.message === undefined ? "" : props.message}</h1>
}
