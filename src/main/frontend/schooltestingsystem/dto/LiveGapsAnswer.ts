export interface ILiveGapsGap {
    value: string
    full?: boolean
}

export default interface ILiveGapsAnswer {
    gaps: ILiveGapsGap[]
}
