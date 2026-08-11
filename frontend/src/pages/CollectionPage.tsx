import "./CollectionPage.css"
import type {Collection} from "../types/CollectionTypes.tsx";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import type {CD} from "../types/CdTypes.tsx";

type CollectionPageProps = {
    cdCollection: Collection,
    onChangePage: (page: string) => void,
    onOpenCd: (id: string) => void,
    onDelete: (id: string) => void,
    onError: (errorMessage: string) => void
}

export default function CollectionPage (props: Readonly<CollectionPageProps>) {
    const nav= useNavigate()
    const [cds, setCds] = useState<CD[]>(props.cdCollection.cds)
    const [criterion, setCriterion] = useState<string>("title")

    function onSearch(searchString: string) {
        if (criterion === "title"){
            setCds(props.cdCollection.cds.filter( cd =>
                cd.cdTitle.toLowerCase().includes(searchString.toLowerCase())))
        } else if (criterion === "performer"){
            setCds(props.cdCollection.cds.filter( cd =>
                cd.performer.toLowerCase().includes(searchString.toLowerCase())))
        }
    }

    useEffect(() => {
        props.onChangePage("details")
    }, []);

    return (
        <section className="page">
            <div className="coll-header">
                <button type="button"
                        onClick={ () =>
                            nav("/cd/" + props.cdCollection.id) }>
                    CD hinzufügen
                </button>
                <form className="search-form">
                    <label id="search-label">
                        Suchbegriff:
                        <input id="search-text" type="text"
                               name="searchString"
                               onChange={
                                    event =>
                                    onSearch(event.target.value)
                        }/>
                    </label>
                    <fieldset className="search-field">
                        Suchkriterium:<br />
                        <label htmlFor="tit" className="crit">
                            Titel:
                        </label>
                        <input id="tit" type="radio"
                               name="searchCriterion"
                               value="title"
                               onChange={
                                    event =>
                                    setCriterion(event.target.value)}
                               defaultChecked={criterion === "title"} />
                        <label htmlFor="per" className="crit">
                            Interpret:
                        </label>
                        <input id="per" type="radio"
                               name="searchCriterion"
                               value="performer"
                               onChange={
                                   event =>
                                   setCriterion(event.target.value)} />
                    </fieldset>
                </form>
            </div>
            <div className="cd-list">
                {
                    cds.map( (cd: CD) => {
                        return (
                            <div className="cd" key={cd.id}>
                                <button className="cd-open"
                                        type="button"
                                        onClick={ () => {
                                            props.onOpenCd(cd.id)}}>
                                    <img id="cover" alt="no cover"
                                         src={cd.coverUrl} />
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