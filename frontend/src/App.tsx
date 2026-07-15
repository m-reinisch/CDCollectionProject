import './App.css'
import Header from "./components/Header.tsx";
import LandingPage from "./components/LandingPage.tsx";
import Footer from "./components/Footer.tsx";
import ProtectedRoutes from "./ProtectedRoutes.tsx";
import CollectionPage from "./components/CollectionPage.tsx";
import {Route, Routes, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from 'axios';
import type {AppUser, Collection} from "./types.tsx";

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

const initalCollections: Collection[] = [
    {
        id: "0",
        name: "Meine CDs",
        cds: []
    }
]

function App() {
    const [appUser, setAppUser] = useState<AppUser | null | undefined>(undefined)
    const [userName, setUserName] = useState<string>("")
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false)
    const [title, setTitle] = useState<string>("")
    const nav= useNavigate();

    function changePage(accessedPage: string){
        if (accessedPage === "landing"){
            setTitle("Willkommen zur CD-Sammlung App")
        } else if (accessedPage === "overview"){
            setTitle("Übersicht Sammlungen")
        }
    }
    function addCollection(name: string){
        //todo POST Collection
    }
    function openCollection(id: string){
        //todo GET Collection by ID
    }
    function deleteCollection(id: string){
        //todo DELETE Collection
    }

    const loadUser = () => {
        axios.get('/api/auth/user')
             .then(response => {
                 setAppUser(response.data)
                 setUserName(response.data.username)
                 setIsLoggedIn(true)
                 nav("/collections")
             })
             .catch( () => {
                setAppUser(null)
             })
    }

    useEffect(() => {
        loadUser()
    }, []);

    return (
        <>
            <Header pageTitle={title} isLoggedIn={isLoggedIn}
                    onLogout={logout} key={"head"} />
            <Routes>
                <Route path="/"
                       element={<LandingPage onChangePage={changePage}
                                             onGitHubLogin={login} />}
                       key={"land"} />
                <Route element={<ProtectedRoutes user={appUser}
                                                 key={"secure"} />}
                       key={"protect"}>
                    <Route path={"/collections"}
                           element={<CollectionPage collections={initalCollections}
                                                    onChangePage={changePage}
                                                    onAddCollection={addCollection}
                                                    onOpenCollection={openCollection}
                                                    onDelete={deleteCollection}
                                                    key={"ov"}/>}
                           key={"overview"} />
                </Route>
            </Routes>
            <Footer userName={userName}
                    errorMessage={""} key={"baseline"} />
        </>
    )
}

export default App
