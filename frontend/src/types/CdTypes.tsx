import type {CdCollection} from "./CollectionTypes.tsx";

const Genres = {
    Jazz: 'JAZZ',
    LatinJazz: 'LATIN_JAZZ'
} as const;

type Genres = typeof Genres[keyof typeof Genres];

export type CD = {
    id: string,
    cdTitle: string,
    performer: string,
    publicationYear: number,
    tracks: Track[],
    totalTime: string,
    coverUrl:string,
    genres: Genres,
    storageLocation: string,
    cdCollection: CdCollection
}

export type CdDTO = {
    cdTitle: string,
    performer: string,
    publicationYear: number,
    genres: Genres,
    storageLocation: string,
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
    coverUrl: string,
    tracks: ResponseTrack[]
}

export type ResponseTrack= {
    position: number,
    trackTitle: string,
    time: string
}
