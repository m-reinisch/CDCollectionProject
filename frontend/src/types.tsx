export type AppUser = {
    id: string,
    name: string,
    collection: Collection[]
}

export type Collection = {
    id: string,
    name: string,
    cds: CD[]
}

export type CollectionDTO = {
    name: string,
    appUser: AppUser
}

export type CD = {
    id: string,
    cdTitle: string,
    performer: string,
    publicationYear: number,
    tracks: Track[],
    totalTime: string,
    coverUrl:string
}

export type CdDTO = {
    cdTitle: string,
    performer: string,
    publicationYear: number,
    tracks: Track[],
    coverUrl: string | null,
    cdCollection: Coll
}

export type Track = {
    position: number,
    trackTitle: string,
    time: string
}

export type Coll = {
    id: string
}