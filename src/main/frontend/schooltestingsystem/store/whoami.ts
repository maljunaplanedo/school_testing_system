import {fetchIfNeeded, syncStoreFetcher, syncStoreSlice} from "./sync";
import IUser from "../dto/User";
import {RootState} from "./store";

const whoamiFetcher = syncStoreFetcher<IUser>('/whoami/fetch')
export const whoamiSync = fetchIfNeeded(whoamiFetcher)

export const whoamiSlice = syncStoreSlice('whoami', whoamiFetcher)
export const whoamiInvalidate = whoamiSlice.actions.invalidate

export const whoamiSelector = (state: RootState) => state.whoami
