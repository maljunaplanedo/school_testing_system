import React from "react";

export interface ILogoutButtonProps {
    redirect: (location: string) => void
}

export default function LogoutButton({redirect}: ILogoutButtonProps) {
    return <button onClick={() => redirect('/')}>Назад</button>
}
