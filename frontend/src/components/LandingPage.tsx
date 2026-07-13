type LandingPageProps = {
    onGitHubLogin: () => void
}

export default function LandingPage(props: Readonly<LandingPageProps>) {
    return(
        <div className="landing-page">
            <h2>Dies ist eine einfache Anwendung zum Verwalten Ihrer CD-Sammlung</h2>
            <p>Eingeloggte Benutzer können eine (oder mehrere) Sammlung(en) anlegen,
                CDs hinzufügen und in der Sammlung suchen.
            </p>
            <button id="gitbutton" onClick={props.onGitHubLogin}>
                <span className="github" ></span>
                <span>Mit GitHub einloggen</span>
            </button>
        </div>
    )
}