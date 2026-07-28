import "./CollectionPage.css"
import type {CD, Collection} from "../types.tsx";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

type CollectionPageProps = {
    cdCollection: Collection,
    onChangePage: (page: string) => void,
    onOpenCd: (id: string) => void,
    onDelete: (id: string) => void,
    onError: (errorMessage: string) => void
}

export default function CollectionPage (props: Readonly<CollectionPageProps>) {
    const nav= useNavigate();
    const [search, setSearch] = useState<string>("");
    const [cds, setCds] = useState<CD[]>(props.cdCollection.cds)

    function onSearch(searchString: string) {
        setSearch(searchString);
        setCds(props.cdCollection.cds.filter( cd =>
            cd.cdTitle.toLowerCase().includes(search.toLowerCase())))
    }

    useEffect(() => {
        props.onChangePage("details")
    }, []);

    return (
        <section className="page">
            <div className="coll-header">
                <button type={"button"}
                        onClick={ () =>
                            nav("/cd/" + props.cdCollection.id) }>
                    CD hinzufügen
                </button>
                <form className="search-form">
                    <label id="search-label">
                        Suchbegriff:
                        <input id="search-text" type="text"
                               name="searchString"
                               placeholder={search}
                               onChange={
                                    event =>
                                    onSearch(event.target.value)
                        }/>
                    </label>
                    <button id="search-btn" type="submit">
                        In Sammlung suchen
                    </button>
                </form>
            </div>
            <div className="cd-list">
                {
                    cds.map( (cd: CD) => {
                        return (
                            <div className="cd" key={cd.id}>
                                <button className="cd-open-button"
                                        type="button"
                                        onClick={ () => {
                                            props.onOpenCd(cd.id)}}>
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