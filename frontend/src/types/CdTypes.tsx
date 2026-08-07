import type {CdCollection} from "./CollectionTypes.tsx";

export type CD = {
    id: string,
    cdTitle: string,
    performer: string,
    publicationYear: number,
    tracks: Track[],
    totalTime: string,
    coverUrl:string,
    cdCollection: CdCollection
}

export type CdDTO = {
    cdTitle: string,
    performer: string,
    publicationYear: number,
    tracks: Track[],
    coverUrl: string,
    cdCollection: CdCollection
}

export type Track = {
    position: number,
    trackTitle: string,
    time: string
}

export type FoundCdDTO= {
    cdTitle: string,
    performer: string,
    publicationYear: number,
    tracks: ResponseTrack[]
}

export type ResponseTrack= {
    position: number,
    trackTitle: string,
    time: string
}
