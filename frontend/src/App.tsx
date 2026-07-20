import './App.css'
import Header from "./components/Header.tsx";
import LandingPage from "./components/LandingPage.tsx";
import Footer from "./components/Footer.tsx";
import ProtectedRoutes from "./ProtectedRoutes.tsx";
import OverviewPage from "./components/OverviewPage.tsx";
import CollectionPage from "./components/CollectionPage.tsx";
import {Route, Routes, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from 'axios';
import type {AppUser, Collection, CollectionDTO} from "./types.tsx";

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

const initialCollections: Collection[] = [
    {
        id: "0",
        name: "Meine CDs",
        cds: []
    }
]

function App() {
    const [user, setUser] = useState<AppUser | null | undefined>(undefined)
    const [userName, setUserName] = useState<string>("")
    const [userId, setUserId] = useState<string>("")
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false)
    const [title, setTitle] = useState<string>("")
    const [cdCollections, setCdCollections] = useState<Collection[]>(initialCollections)
    const [selectedCdCollection, setSelectedCdCollection] = useState<Collection>(null)
    const [errorLog, setErrorLog] = useState<string>("")
    const nav= useNavigate();

    function changePage(accessedPage: string){
        if (accessedPage === "landing"){
            setTitle("Willkommen zur CD-Sammlung App")
        } else if (accessedPage === "overview"){
            setTitle("Übersicht Sammlungen")
        }
    }
    function addCollection(collName: string){
        const newCollection: CollectionDTO = {
            name: collName,
            appUser: user!
        }

        axios.post("/api/collections", newCollection)
             .then( (response) => {
                 const respColl: Collection= response.data

                 setCdCollections( (cdCollections) =>
                     [...cdCollections, respColl])
                 nav("/collections")
             })
             .catch( (error_) => console.log(error_) )
    }
    function openCollection(id: string){
        //todo GET Collection by ID
        const testCollection: Collection = {
            id: "0",
            name: "Meine CDs",
            cds: [
                {
                    id: "6",
                    title: "Saxuality",
                    performer: "Candy Dulfer",
                    publicationYear: 1990,
                    tracks: [],
                    totalTime: "0",
                    coverUrl: ""
                }
            ]
        }

        setSelectedCdCollection(testCollection)
        nav("/collections/" + id)
    }
    function deleteCollection(id: string){
        axios.delete("/api/collections/" + id)
             .then( (response) => {
                 if(response.data){
                     loadCollections(userId)
                     nav("/collections")
                 }
             })
             .catch( (error_) => console.log(error_) )
    }
    function openCD(id: string){
        //todo
    }
    function deleteCD(id: string){
        //todo
    }

    const loadUser = () => {
        axios.get('/api/auth/user')
             .then(response => {
                 setUser(response.data)
                 setUserName(response.data.username)
                 setUserId(response.data.id)
                 setIsLoggedIn(true)
                 nav("/collections")
             })
             .catch( () => {
                setUser(null)
             })
    }
    const loadCollections = (usrId: string) => {
        axios.get('/api/collections/all/' + usrId)
             .then(response => {
                 setCdCollections(response.data);
             })
             .catch( (error_) => console.log(error_) )
    }
    const handleError = (errorMessage: string) => {
        setErrorLog(errorMessage)
    }

    useEffect(() => {
        loadUser()
        if (!userId) {
            return
        }

        loadCollections(userId)
    }, [userId]);

    return (
        <>
            <Header pageTitle={title} isLoggedIn={isLoggedIn}
                    onLogout={logout} key={"head"} />
            <Routes>
                <Route path="/"
                       element={<LandingPage onChangePage={changePage}
                                             onGitHubLogin={login} />}
                       key={"land"} />
                <Route element={<ProtectedRoutes user={user}
                                                 key={"secure"} />}
                       key={"protect"}>
                    <Route path={"/collections"}
                           element={<OverviewPage collections={cdCollections}
                                                  onChangePage={changePage}
                                                  onAddCollection={addCollection}
                                                  onOpenCollection={openCollection}
                                                  onDelete={deleteCollection}
                                                  onError={handleError}
                                                  key={"ov"} />}
                           key={"overview"} />
                    <Route path={"/collections/:id"}
                           element={<CollectionPage cdCollection={selectedCdCollection}
                                                    onChangePage={changePage}
                                                    onOpenCd={openCD}
                                                    onDelete={deleteCD}
                                                    key={"cd-coll"} />}
                           key={"details"} />
                </Route>
            </Routes>
            <Footer userName={userName}
                    errorMessage={errorLog} key={"baseline"} />
        </>
    )
}

export default App
