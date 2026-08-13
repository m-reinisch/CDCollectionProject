import type {CdCollection} from "./CollectionTypes.tsx";

export const Genres = {
    JAZZ: {style: 'Jazz'},
    LATIN_JAZZ: {style: 'Latin Jazz'}
} as const;

export type GenresKey = keyof typeof Genres;
export type GenresDetails = typeof Genres[GenresKey];

export type CD = {
    id: string,
    cdTitle: string,
    performer: string,
    publicationYear: number,
    tracks: Track[],
    totalTime: string,
    coverUrl:string,
    genres: GenresKey,
    storageLocation: string,
    cdCollection: CdCollection
}

export type CdDTO = {
    cdTitle: string,
    performer: string,
    publicationYear: number,
    genres: GenresKey,
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
