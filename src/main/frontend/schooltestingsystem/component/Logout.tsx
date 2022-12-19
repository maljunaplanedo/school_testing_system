import React, {useEffect} from "react";
import {logout} from "../request/logout";
import useRedirect from "../util/redirect";
import {LoadingStatus} from "../request/handler";
import Error from "./Error";
import Loading from "./Loading";

export default function Logout() {
    const logoutState = logout.useState()
    const logoutRefresh = logout.useRefresh()
    const logoutSync = logout.useSync()

    const redirect = useRedirect('/logout', logoutRefresh)

    useEffect(logoutSync)

    useEffect(() => {
        if (logoutState.loading === LoadingStatus.FINISHED && logoutState.object !== undefined) {
            redirect("/")
        }
    })

    if (logoutState.loading === LoadingStatus.FINISHED && logoutState.object === undefined) {
        return <Error/>
    }

    return <Loading/>
}
