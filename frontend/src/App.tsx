import './App.css'
import Header from "./components/Header.tsx";
import LandingPage from "./components/LandingPage.tsx";
import Footer from "./components/Footer.tsx";
import ProtectedRoutes from "./ProtectedRoutes.tsx";
import CollectionPage from "./components/CollectionPage.tsx";
import {Route, Routes, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from 'axios';

function login() {
    const host = globalThis.location.host === 'localhost:5173' ?
        'http://localhost:8080': globalThis.location.origin

    window.open(host + '/oauth2/authorization/github', '_self')
}
function logout() {
    const host = globalThis.location.host === 'localhost:5173' ?
        'http://localhost:8080' : globalThis.location.origin

    window.open(host + '/logout', '_self')
}

function App() {
    const [page, setPage] = useState<string>("landing");
    const [appUser, setAppUser] = useState<string | null | undefined>(undefined)
    const [userName, setUserName] = useState<string>("")
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false)
    const nav= useNavigate();

    const loadUser = () => {
        axios.get('/api/auth/user')
             .then(response => {
                setAppUser(response.data)
                setUserName(response.data.username)
                 setIsLoggedIn(true)
             })
             .catch( () => {
                setAppUser(null)
             })
            .finally( () => {
                setPage("overview")
                console.log(page)
                nav("/collections")
            })
    }

    useEffect(() => {
        loadUser()
    }, []);

    return (
        <>
            <Header pageType={page} isLoggedIn={isLoggedIn}
                    onLogout={logout} key={"head"} />
            <Routes>
                <Route path="/"
                       element={<LandingPage onGitHubLogin={login} />}
                       key={"land"} />
                <Route element={<ProtectedRoutes user={appUser}
                                                 key={"secure"} />}
                       key={"protect"}>
                    <Route path={"/collections"}
                           element={<CollectionPage />}
                           key={"overview"} />
                </Route>
            </Routes>
            <Footer userName={userName}
                    errorMessage={""} key={"baseline"} />
        </>
    )
}

export default App
