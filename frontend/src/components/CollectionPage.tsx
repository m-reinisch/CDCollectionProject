import {useEffect} from "react";

type CollectionPageProps = {
    onChangePage: (page: string) => void
}

export default function CollectionPage(props: Readonly<CollectionPageProps>) {
    useEffect(() => {
        props.onChangePage("overview")
    }, []);

    return(
        <div className="page">
            Test
        </div>
    )
}
