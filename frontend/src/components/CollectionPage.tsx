import type {CD, Collection} from "../types.tsx";
import {useEffect} from "react";
import {useForm} from "react-hook-form";
import {useNavigate} from "react-router-dom";

type FormValues = {
    searchString: string
}
type CollectionPageProps = {
    cdCollection: Collection,
    onChangePage: (page: string) => void,
    onOpenCd: (id: string) => void,
    onDelete: (id: string) => void,
    onError: (errorMessage: string) => void
}

export default function CollectionPage (props: Readonly<CollectionPageProps>) {
    const { register, handleSubmit, reset,
        formState: { errors, isValid },
    } = useForm<FormValues>({ mode: 'onChange' });
    const nav= useNavigate();

    function search(data: FormValues) {
        reset({searchString: ""})
    }

    useEffect(() => {
        props.onChangePage("details")
    }, []);
    useEffect(() => {
        if (errors.searchString){
            props.onError(errors.searchString.message!)
        } else {
            props.onError("")
        }
    }, [errors.searchString, props]);

    return (
        <section className="page">
            <div className="coll-header">
                <button type={"button"}
                        onClick={ () =>
                            nav("/cd/" + props.cdCollection.id) }>
                    CD hinzufügen
                </button>
                <form className="search-form"
                      onSubmit={handleSubmit(search)}>
                    <label id="search-label">
                        Suchbegriff:
                        <input id="search-text" type="text"
                               {...register("searchString", {
                                   required: "Suchbegriff ist erforderlich!"
                               })}
                        />
                    </label>
                    <button id="search-btn" type="submit"
                            disabled={!isValid}>
                        In Sammlung suchen
                    </button>
                </form>
            </div>
            <div className="cd-list">
                {
                    props.cdCollection.cds.map( (cd: CD) => {
                        return (
                            <div className="cd" key={cd.id}>
                                <button className="cd-open-button"
                                        type="button"
                                        onClick={ () => {
                                            props.onOpenCd(cd.id);}}>
                                    <strong>{cd.cdTitle}</strong>
                                    <small>{cd.performer}</small>
                                </button>
                                <button type="button"
                                        onClick={ () =>
                                            props.onDelete(cd.id)}>
                                    Löschen
                                </button>
                            </div>
                        )
                    })
                }
            </div>
        </section>
    )
}