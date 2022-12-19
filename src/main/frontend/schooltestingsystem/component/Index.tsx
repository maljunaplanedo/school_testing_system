import React, {useEffect} from "react";
import {whoami} from "../request/whoami";
import useRedirect from "../util/redirect";
import Loading from "./Loading";
import Error from "./Error";
import {LoadingStatus} from "../request/handler";

export default
function Index() {
    const whoamiState = whoami.useState()
    const whoamiRefresh = whoami.useRefresh()
    const whoamiSync = whoami.useSync()

    const redirect = useRedirect("/", whoamiRefresh)

    useEffect(whoamiSync)

    useEffect(() => {
        if (whoamiState.loading === LoadingStatus.FINISHED && whoamiState.object !== undefined) {
            const whoamiUser = whoamiState.object

            if (whoamiUser.id === -1) {
                redirect('/login')
            } else {
                switch (whoamiUser.role) {
                    case "ADMIN":
                        redirect("/admin")
                        break
                    case "TEACHER":
                        redirect("/teacher")
                        break
                    case "STUDENT":
                        redirect("/student")
                }
            }
        }
    })

    if (whoamiState.loading === LoadingStatus.FINISHED && whoamiState.object === undefined) {
        return <Error />
    }
    return <Loading />
}
