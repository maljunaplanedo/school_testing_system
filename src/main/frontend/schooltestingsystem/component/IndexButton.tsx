import React from "react";

export interface ILogoutButtonProps {
    redirect: (location: string) => void
}

export default function IndexButton({redirect}: ILogoutButtonProps) {
    return <button onClick={() => redirect('/')}>Назад</button>
}
