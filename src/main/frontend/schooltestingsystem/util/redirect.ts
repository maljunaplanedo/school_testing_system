import {useHistory} from "react-router-dom";
import {useEffect} from "react";

interface IRedirectStatus {
    afterRedirect: boolean
    onRedirect?: () => void
}

const redirectStatus: IRedirectStatus = {afterRedirect: false}

export default function useRedirect(path: string, onRedirect?: (() => void)) {
    useEffect(() => {
        if (redirectStatus.afterRedirect) {
            redirectStatus.afterRedirect = false
            if (redirectStatus.onRedirect !== undefined) {
                redirectStatus.onRedirect()
            }
        }
    })

    const history = useHistory()

    return (location: string) => {
        redirectStatus.afterRedirect = true
        redirectStatus.onRedirect = onRedirect
        history.push(location)
    }
}
