import type {Collection} from "./CollectionTypes.tsx";

export type AppUser = {
    id: string,
    name: string,
    collection: Collection[]
}
