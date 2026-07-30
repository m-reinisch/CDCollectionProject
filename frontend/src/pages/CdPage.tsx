import "./CdPage.css"
import type {CD} from "../types/types.tsx";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";

type CdPageProps = {
    cd: CD,
    onChangePage: (page: string) => void
}

export default function CdPage(props: Readonly<CdPageProps>) {
    const nav= useNavigate()

    useEffect(() => {
        props.onChangePage("show-cd")
    }, []);

    return (
        <div className="page">
            <div className="cd-details">
                <div className="cd-header">
                    <div className="cd-performer">{props.cd.performer}</div>
                    <div className="cd-year">© {props.cd.publicationYear}</div>
                </div>
                <ol className="track-list">
                    {props.cd.tracks.map(track => (
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

            </div>
        </div>
    )
}