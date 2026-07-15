type HeaderProps = {
    pageTitle: string,
    isLoggedIn: boolean,
    onLogout: () => void
}

// Wird noch für die anderen Seiten erweitert
export default function Header(props: Readonly<HeaderProps>) {

    return(
        <header className="app-header">
            <div className="title">{props.pageTitle}</div>
            {props.isLoggedIn ?
                <button id="logut-btn"
                        onClick={props.onLogout}>
                    Logout
                </button>
                : null}
        </header>
    )
}