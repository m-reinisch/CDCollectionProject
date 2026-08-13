import "./CollectionPage.css"
import type {Collection} from "../types/CollectionTypes.tsx";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {type CD, Genres, type GenresDetails} from "../types/CdTypes.tsx";

type CollectionPageProps = {
    cdCollection: Collection,
    onChangePage: (page: string) => void,
    onOpenCd: (id: string) => void,
    onDelete: (id: string) => void,
    onError: (errorMessage: string) => void
}

export default function CollectionPage (props: Readonly<CollectionPageProps>) {
    const nav= useNavigate()
    const [criterion, setCriterion] = useState<string>("title")
    const [searchString, setSearchString] = useState<string>("")

    const cds: CD[] = props.cdCollection.cds.filter( cd => {
        if (criterion === "performer"){
            return cd.performer.toLowerCase().includes(searchString.toLowerCase())
        }
        return cd.cdTitle.toLowerCase().includes(searchString.toLowerCase())
    })

    useEffect(() => {
        props.onChangePage("details")
    }, [props]);

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
                                    setSearchString(event.target.value)
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
                        const musicStyle: GenresDetails= Genres[cd.genres] ?? {style: "Unbekannt"};
                        return (
                            <div className="cd" key={cd.id}>
                                <button className="cd-open"
                                        type="button"
                                        onClick={ () => {
                                            props.onOpenCd(cd.id)}}>
                                    <img id="cover" alt="no cover"
                                         src={cd.coverUrl} />
                                    <strong>{cd.cdTitle}</strong>
                                    <em className="blue">{cd.performer}</em>
                                    <small className="purple">{musicStyle.style}</small>
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
