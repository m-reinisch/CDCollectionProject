type FooterProps = {
    userName: string | null | undefined,
    errorMessage: string
}

export default function Footer(props: Readonly<FooterProps>) {
    return (
        <footer className="app-footer">
            <div className="app-user">Benutzer: {props.userName}</div>
            <div className="error-message">{props.errorMessage}</div>
            <div className="impressum">MR 2026 © All rights reserved.</div>
        </footer>
    )
}