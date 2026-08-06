import type {AppUser} from "./UserTypes.tsx";
import type {CD} from "./CdTypes.tsx";

export type Collection = {
    id: string,
    name: string,
    cds: CD[]
}

export type CollectionDTO = {
    name: string,
    appUser: AppUser
}

export type CdCollection = {
    id: string
}
