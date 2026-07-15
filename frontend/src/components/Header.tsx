import {useEffect, useState} from "react";

type HeaderProps = {
    pageType: string,
    isLoggedIn: boolean,
    onLogout: () => void
}

// Wird noch für die anderen Seiten erweitert
export default function Header(props: Readonly<HeaderProps>) {
    const [title, setTitle] = useState<string>("")

    function onLoad(){
        if(props.pageType === "landing"){
            setTitle("Willkommen zur CD-Sammlung App")
        } else if (props.pageType === "overview") {
            setTitle("Übersicht Sammlungen")
        } else {
            setTitle("Test")
        }
    }

    useEffect(() => {
        onLoad()
    }, []);

    return(
        <header className="app-header">
            <div className="title">{title}</div>
            {props.isLoggedIn ?
                <button id="logut-btn"
                        onClick={props.onLogout}>
                    Logout
                </button>
                : null}
        </header>
    )
}