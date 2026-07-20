import type {CD, Collection} from "../types.tsx";
import {useEffect} from "react";

type CollectionPageProps = {
    cdCollection: Collection,
    onChangePage: (page: string) => void,
    onOpenCd: (id: string) => void,
    onDelete: (id: string) => void,
}

export default function CollectionPage (props: Readonly<CollectionPageProps>) {
    useEffect(() => {
        props.onChangePage("details")
    }, []);

    return (
        <section className="page">
            <div className="cd-list">
                {
                    props.cdCollection.cds.map( (cd: CD) => {
                        return (
                            <div className="cd" key={cd.id}>
                                <button className="cd-open-button"
                                        type="button"
                                        onClick={ () => {
                                            props.onOpenCd(cd.id);}}>
                                    <strong>{cd.title}</strong>
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