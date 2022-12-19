import React from "react";
import {useAppSelector} from "../store/hooks";
import {whoamiInvalidate, whoamiSelector, whoamiSync} from "../store/whoami";
import useRedirect from "../util/redirect";

var X = 0

export default
function Index() {
    const redirect = useRedirect([whoamiInvalidate])

    const whoamiState = useAppSelector(whoamiSelector)
    whoamiSync('/api/auth/whoami', whoamiState)

    if (X == 0) {
        ++X
        redirect('/login')
    }
    return (<p>{(whoamiState.object ? whoamiState.object.id : "F") + " " + whoamiState.loading}</p>)
}
