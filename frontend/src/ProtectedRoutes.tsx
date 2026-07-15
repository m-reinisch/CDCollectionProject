import {Navigate, Outlet} from "react-router-dom";
import type {AppUser} from "./types.tsx";

type ProtectedRoutesProps = {
    user: AppUser | null | undefined
}

export default function ProtectedRoutes(props: Readonly<ProtectedRoutesProps>) {

    if (props.user === undefined) {
        return <div>Loading...</div>
    }

    return (
        props.user ? <Outlet /> : <Navigate to = "/" />
    )
}
