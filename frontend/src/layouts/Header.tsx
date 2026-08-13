type HeaderProps = {
    pageTitle: string,
    pType: "NOLOG" | "NO" | "BACK" | "ABORT",
    onLogout: () => void,
    onBack: () => void
}

export default function Header(props: Readonly<HeaderProps>) {

    return(
        <header className="app-header">
            {props.pType === "BACK" ?
                <button id="back-btn" type="button"
                        onClick={props.onBack}>
                    Zurück
                </button> : null}
            {props.pType === "ABORT" ?
                <button id="cancel-btn" type="button"
                        onClick={props.onBack}>
                    Abbrechen
                </button> : null}
            <div className="title">{props.pageTitle}</div>
            {props.pType === "NOLOG" ? null :
                <button id="logout-btn"
                        type="button"
                        onClick={props.onLogout}>
                    Logout
                </button>}
        </header>
    )
}