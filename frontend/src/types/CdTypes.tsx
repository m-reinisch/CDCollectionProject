import type {CdCollection} from "./CollectionTypes.tsx";

export const Genres = {
    AFROBEAT: {style: "Afrobeat"},
    AMBIENT: {style: "Ambient"},
    AVANTGARDE: {style: "Avantgarde"},
    BALLAD: {style: "Ballade"},
    BALLET: {style: "Ballett"},
    BAROQUE: {style: "Barock"},
    BEAT: {style: "Beat"},
    BEBOP: {style: "Bebop"},
    BIGBAND: {style: "Bigband"},
    BLUEGRASS: {style: "Bluegrass"},
    BLUES: {style: "Blues"},
    BRAZIL: {style: "Brazil"},
    BRITPOP: {style: "Britpop"},
    CANTATA: {style: "Kantate"},
    CELTIC: {style: "Celtic"},
    CHRISTMAS_MUSIC: {style: "Weihnachtsmusik"},
    CHURCH_MUSIC: {style: "Kirchenmusik"},
    CLASSICAL: {style: "Klassik"},
    CONTEMPORARY_JAZZ: {style: "Contemporary Jazz"},
    COOL_JAZZ: {style: "Cool Jazz"},
    COUNTRY: {style: "Country"},
    DANCE: {style: "Dance"},
    DANCE_MUSIC: {style: "Tanzmusik"},
    DISCO: {style: "Disco"},
    DIXIELAND: {style: "Dixieland"},
    FLAMENCO: {style: "Flamenco"},
    FOLK: {style: "Folk"},
    FREE_JAZZ: {style: "Free Jazz"},
    FUNK: {style: "Funk"},
    FUSION: {style: "Fusion"},
    GOSPEL: {style: "Gospel"},
    GOTHIC: {style: "Gothic"},
    HARD_ROCK: {style: "Hard Rock"},
    HEAVY_METAL: {style: "Heavy Metal"},
    HIP_HOP: {style: "Hip Hop"},
    HOUSE: {style: "House"},
    IRISH_FOLK: {style: "Irish Folk"},
    JAZZ: {style: "Jazz"},
    JAZZ_ROCK: {style: "Jazz Rock"},
    K_POP: {style: "K-Pop"},
    KLEZMER: {style: "Klezmer"},
    KRAUT_ROCK: {style: "Kraut Rock"},
    LATIN: {style: "Latin"},
    LATIN_JAZZ: {style: "Latin Jazz"},
    MUSICAL: {style: "Musical"},
    NEW_AGE: {style: "New Age"},
    NEW_GERMAN_WAVE: {style: "Neue deutsche Welle"},
    OPERA: {style: "Oper"},
    POP: {style: "Pop"},
    PUNK: {style: "Punk"},
    R_AND_B: {style: "R&B"},
    REGGAE: {style: "Reggae"},
    ROCK: {style: "Rock"},
    ROCK_AND_ROLL: {style: "Rock & Roll"},
    SALSA: {style: "Salsa"},
    SCHLAGER: {style: "Schlager"},
    SKA: {style: "Ska"},
    SMOOTH_JAZZ: {style: "Smooth Jazz"},
    SOUL: {style: "Soul"},
    SOUNDTRACK: {style: "Filmmusik"},
    SWING: {style: "Swing"},
    TANGO: {style: "Tango"},
    TECHNO: {style: "Techno"},
    TRANCE: {style: "Trance"},
    WALTZ: {style: "Walzer"},
    VOLKSTUEMLICHE_MUSIK: {style: "Volkstümliche Musik"}
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
