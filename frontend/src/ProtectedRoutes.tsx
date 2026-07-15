import {Navigate, Outlet} from "react-router-dom";

type ProtectedRoutesProps = {
    user: string | null | undefined
}

export default function ProtectedRoutes(props: Readonly<ProtectedRoutesProps>) {

    if (props.user === undefined) {
        return <div>Loading...</div>
    }
    console.log(props.user)

    return (
        props.user ? <Outlet /> : <Navigate to = "/" />
    )
}
