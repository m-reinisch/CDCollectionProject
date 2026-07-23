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
    coverUrl: string | null
}

export type Track = {
    id: string,
    position: number,
    title: string,
    time: string
}
