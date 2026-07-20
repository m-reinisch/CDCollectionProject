import {useEffect} from "react";
import type {Collection} from "../types.tsx";
import {useForm} from "react-hook-form";

type FormValues = {
    collName: string
}
type OverviewPageProps = {
    collections: Collection[],
    onChangePage: (page: string) => void,
    onAddCollection: (name: string) => void,
    onOpenCollection: (id: string) => void,
    onDelete: (id: string) => void,
    onError: (message: string) => void
}

export default function OverviewPage(props: Readonly<OverviewPageProps>) {
    const { register, handleSubmit, reset,
            formState: { errors, isValid },
    } = useForm<FormValues>({ mode: 'onChange' });

    function submit(data: FormValues) {
        reset({collName: ""})
        props.onAddCollection(data.collName)
    }

    useEffect(() => {
        props.onChangePage("overview")
        if (errors.collName){
            props.onError(errors.collName.message!)
        } else {
            props.onError("")
        }
    }, [errors.collName, props]);

    return(
        <section className="page">
            <div className="collections-list">
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
                                </button>
                                <button type="button"
                                        onClick={ () =>
                                            props.onDelete(coll.id)}>
                                    Löschen
                                </button>
                            </div>

                        )
                    })
                }
            </div>
            <form className="new-coll"
                  onSubmit={handleSubmit(submit)}>
                <label>
                    Name der neuen Sammlung:
                    <input id="new-coll-name" type="text"
                           {...register("collName", {
                               required: "Name ist für neue Sammlung erforderlich!",
                               minLength: {
                                   value: 3,
                                   message: "Name der Sammlung muss mind. 3 Zeichen lang sein!"
                               }
                           })}
                    />
                </label>
                <button type="submit" disabled={!isValid}>
                    Sammlung hinzufügen
                </button>
            </form>
        </section>
    )
}
