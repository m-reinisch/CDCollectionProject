import {useEffect} from "react";
import type {Collection} from "../types.tsx";
import {useForm} from "react-hook-form";

type FormValues = {
    name: string
}
type CollectionPageProps = {
    collections: Collection[],
    onChangePage: (page: string) => void,
    onAddCollection: (name: string) => void,
    onOpenCollection: (id: string) => void,
    onDelete: (id: string) => void
}

export default function CollectionPage(props: Readonly<CollectionPageProps>) {
    const { register, handleSubmit, reset,
            formState: { errors, isValid },
    } = useForm<FormValues>({ mode: 'onChange' });

    function submit(data: FormValues) {
        props.onAddCollection(data.name)
    }

    useEffect(() => {
        props.onChangePage("overview")
    }, []);

    return(
        <div className="page">
            {
                props.collections.map( (coll: Collection) => {
                    return(
                        <div className="collection"
                             key={coll.id}>
                            <button className="coll-open-button"
                                    type="button"
                                    onClick={ () =>
                                        props.onOpenCollection(coll.id)}>
                                <strong>{coll.name}</strong>
                                <button type="button"
                                        onClick={ () =>
                                            props.onDelete(coll.id)}>
                                    Löschen
                                </button>
                            </button>
                        </div>

                    )
                })
            }
            <form className="new-coll"
                  onSubmit={handleSubmit(submit)}>
                <label>
                    Name der neuen Sammlung:
                    <input id="" type="text"
                           {...register("name")}
                    />
                </label>
                <button type="submit" disabled={!isValid}>
                    Sammlung hinzufügen
                </button>
            </form>
        </div>
    )
}
