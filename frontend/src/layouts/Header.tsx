type HeaderProps = {
    pageTitle: string,
    isLoggedIn: boolean,
    pType: "NO" | "BACK" | "ABORT",
    onLogout: () => void,
    onBack: () => void
}

export default function Header(props: Readonly<HeaderProps>) {

    return(
        <header className="app-header">
            {props.pType === "BACK" ?
                <button id="back-btn" onClick={props.onBack}>
                    Zurück
                </button> : null}
            {props.pType === "ABORT" ?
                <button id="cancel-btn" onClick={props.onBack}>
                    Abbrechen
                </button> : null}
            <div className="title">{props.pageTitle}</div>
            {props.isLoggedIn ?
                <button id="logout-btn"
                        onClick={props.onLogout}>
                    Logout
                </button> : null}
        </header>
    )
}