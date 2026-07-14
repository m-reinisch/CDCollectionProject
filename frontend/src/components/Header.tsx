import {useEffect, useState} from "react";

type HeaderProps = {
    pageType: string
}

// Wird noch für die anderen Seiten erweitert
export default function Header(props: Readonly<HeaderProps>) {
    const [title, setTitle] = useState<string>("")

    function onLoad(){
        if(props.pageType === "landing"){
            setTitle("Willkommen zur CD-Sammlung App")
        }  else {
            setTitle("Test")
        }
    }

    useEffect(() => {
        onLoad()
    }, []);

    return(
        <header className="app-header">
            <div className="title">{title}</div>
        </header>
    )
}