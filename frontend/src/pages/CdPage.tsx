import "./CdPage.css"
import {type CD, Genres} from "../types/CdTypes.tsx";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import * as React from "react";

type CdPageProps = {
    cd: CD,
    onChangePage: (page: string) => void
}

export default function CdPage(props: Readonly<CdPageProps>) {
    const nav= useNavigate()
    const musicStyle= Genres[props.cd.genres] ?? {style: "Unbekannt"}
    const coverStyle: React.CSSProperties= {
        backgroundImage: `url(${props.cd.coverUrl})`,
        backgroundSize: 'contain',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
    }

    useEffect(() => {
        props.onChangePage("show-cd")
    }, [props]);

    return (
        <div className="page">
            <div className="cd-details" style={coverStyle}>
                <div className="cd-header">
                    <div className="cd-performer">{props.cd.performer}</div>
                    <div className="cd-year">© {props.cd.publicationYear}</div>
                </div>
                <ol className="track-list">
                    {props.cd.tracks.toSorted( (a, b) =>
                            a.position - b.position)
                        .map(track => (
                            <li id="track" key={track.position}>
                                <div className="cd-track-pos">
                                    {track.position}
                                </div>
                                <div className="cd-track-titel">
                                    {track.trackTitle}
                                </div>
                                <div className="cd-track-time">
                                    {track.time}
                                </div>
                            </li>
                    ))}
                </ol>
                <div className="cd-footer">
                    <div className="cd-total-time">
                        Gesamtzeit: {props.cd.totalTime}
                    </div>
                    <button className="edit-btn" type="button"
                            onClick={ ()=>
                                nav("/cd/edit/" + props.cd.id)} >
                        Bearbeiten
                    </button>
                </div>
                <div className="additional-info">
                    <div className="cd-genres">
                        Stilrichtung: {musicStyle.style}
                    </div>
                    <div className="storage-location">
                        Ablageort: {props.cd.storageLocation}
                    </div>
                </div>
            </div>
        </div>
    )
}
