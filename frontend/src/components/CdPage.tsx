import "./CdPage.css"
import type {CD} from "../types.tsx";
import {useEffect} from "react";

type CdPageProps = {
    cd: CD,
    onChangePage: (page: string) => void
}

export default function CdPage(props: Readonly<CdPageProps>) {
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
                            <div className="cd-track-titel">
                                {track.trackTitle}
                            </div>
                            <div className="cd-track-time">
                                {track.time}
                            </div>
                        </li>
                    ))}
                </ol>
                <div className="cd-total-time">Gesamtzeit: {props.cd.totalTime}</div>
            </div>
        </div>
    )
}