import './LandingPage.css'
import {useEffect} from "react";

type LandingPageProps = {
    onChangePage: (page: string) => void,
    onGitHubLogin: () => void,
    onGoogleLogin: () => void
}

export default function LandingPage(props: Readonly<LandingPageProps>) {
    useEffect(() => {
        props.onChangePage("landing")
    }, [props]);

    return(
        <div className="page">
            <div className="landing-page">
                <h2>Dies ist eine einfache Anwendung zum Verwalten Ihrer CD-Sammlung</h2>
                <p>Eingeloggte Benutzer können eine (oder mehrere)
                    Sammlung(en) anlegen, CDs hinzufügen und in der
                    Sammlung CDs suchen.<br />
                    Die  Eingabe der CDs kann manuell erfolgen, oder
                    anhand des Barcodes auf der CD lassen sich die
                    Daten ermitteln.<br />
                    Durch klicken auf den Titel lässt sich eine
                    Sammlung oder CD öffnen.
                </p>
                <div className="logins">
                    <button id="gitbutton" type="button"
                            onClick={props.onGitHubLogin}>
                        <span className="github" >G</span>
                        <span>Mit GitHub einloggen</span>
                    </button>
                    <button id="googlebutton" type="button"
                            onClick={props.onGoogleLogin}>
                        <span className="google" > </span>
                        <span>Mit Google einloggen</span>
                    </button>
                </div>
            </div>
        </div>
    )
}