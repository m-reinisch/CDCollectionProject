import './App.css'
import Header from "./layouts/Header.tsx";
import Footer from "./layouts/Footer.tsx";
import type {AppUser} from "./types/UserTypes.tsx";
import type {Collection, CollectionDTO} from "./types/CollectionTypes.tsx";
import type {CD, CdDTO} from "./types/CdTypes.tsx";
import ProtectedRoutes from "./routes/ProtectedRoutes.tsx";
import {gitHubLogin, googleLogin, logout} from "./features/auth/LoginLogout.tsx";
import LandingPage from "./pages/LandingPage.tsx";
import OverviewPage from "./pages/OverviewPage.tsx";
import CollectionPage from "./pages/CollectionPage.tsx";
import AddCdPage from "./pages/AddCdPage.tsx";
import CdPage from "./pages/CdPage.tsx";
import {Route, Routes, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from 'axios';
import EditCd from "./pages/EditCdPage.tsx";

const initialCollections: Collection[] = [ ]
const testCollection: Collection = {
    id: "0",
    name: "Meine CDs mit langen Namen zum Testen",
    cds: [
        {
            id: "6",
            cdTitle: "Saxuality",
            performer: "Candy Dulfer",
            publicationYear: 1990,
            tracks: [],
            totalTime: "0",
            coverUrl: "",
            cdCollection: {
                id: "0"
            }
        }
    ]
}
const initCd: CD ={
    id: "6",
    cdTitle: "Saxuality",
    performer: "Candy Dulfer",
    publicationYear: 1990,
    tracks: [],
    totalTime: "0",
    coverUrl: "",
    cdCollection: {
        id: "0"
    }
}

function App() {
    const [user, setUser] = useState<AppUser | null | undefined>(undefined)
    const [userName, setUserName] = useState<string>("")
    const [userId, setUserId] = useState<string>("")
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false)
    const [title, setTitle] = useState<string>("")
    const [pageType, setPageType] = useState<"NO" | "BACK" | "ABORT">("NO")
    const [backPage, setBackPage] = useState<string>("")
    const [cdCollections, setCdCollections] = useState<Collection[]>(initialCollections)
    const [selectedCdCollection, setSelectedCdCollection] = useState<Collection>(testCollection)
    const [selectedCd, setSelectedCd] = useState<CD>(initCd)
    const [errorLog, setErrorLog] = useState<string>("")
    const [priorityError, setPriorityError] = useState<string>("")
    const nav= useNavigate();

    function changePage(accessedPage: string){
        if (accessedPage === "landing"){
            setTitle("Willkommen zur CD-Sammlung App")
            setPageType("NO")
            setIsLoggedIn(false)
        } else if (accessedPage === "overview"){
            setTitle("Übersicht Sammlungen")
            setPageType("NO")
        } else if (accessedPage === "details"){
            setTitle(selectedCdCollection.name)
            setPageType("BACK")
            setBackPage("overview")
        } else if (accessedPage === "add-cd"){
            setTitle("Neue CD")
            setPageType("ABORT")
            setBackPage("details")
        } else if (accessedPage === "show-cd"){
            setTitle(selectedCd.cdTitle)
            setPageType("BACK")
            setBackPage("details")
        } else if (accessedPage === "edit-cd"){
            setTitle(selectedCd.cdTitle)
            setPageType("ABORT")
            setBackPage("show-cd")
        }
        handleError("")
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
             .catch( (error_) => {
                if (axios.isAxiosError(error_) && error_.response?.status === 401) {
                    setPriorityError("Unerwarteter Fehler! Versuche Sie sich aus und wieder einzuloggen.")
                } else {
                    console.log(error_)
                }
             })
    }
    function openCollection(collId: string){
        axios.get("/api/collections/" + collId)
             .then( (response) => {
                 setSelectedCdCollection(response.data)
                 nav("/collections/" + collId)
             })
             .catch( (error_) => {
                 if (axios.isAxiosError(error_) && error_.response?.status === 401) {
                     setPriorityError("Unerwarteter Fehler! Versuche Sie sich aus und wieder einzuloggen.")
                 } else {
                     console.log(error_.response?.data)
                 }
             })

    }
    function deleteCollection(collId: string){
        axios.delete("/api/collections/" + collId)
             .then( (response) => {
                 if(response.data){
                     loadCollections(userId)
                     nav("/collections")
                 }
             })
             .catch( (error_) => {
                if (axios.isAxiosError(error_) && error_.response?.status === 401) {
                    setPriorityError("Unerwarteter Fehler! Versuche Sie sich aus und wieder einzuloggen.")
                } else {
                    console.log(error_.response?.data)
                }
             })
    }
    function addCd(cd: CdDTO){
        axios.post("/api/cd", cd)
             .then( () => {
                 openCollection(cd.cdCollection.id)
             })
             .catch( (error_) => {
                 if (axios.isAxiosError(error_) && error_.response?.status === 401) {
                     setPriorityError("Unerwarteter Fehler! Versuche Sie sich aus und wieder einzuloggen.")
                 } else {
                     console.log(error_.response?.data)
                 }
             })
    }
    function openCD(cdId: string){
        axios.get("/api/cd/" + cdId)
             .then( (response) => {
                 setSelectedCd(response.data)
                 nav("/cd/show/" + cdId)
             })
             .catch( (error_) => {
                 if (axios.isAxiosError(error_) && error_.response?.status === 401) {
                     setPriorityError("Unerwarteter Fehler! Versuche Sie sich aus und wieder einzuloggen.")
                 } else {
                     console.log(error_)
                 }
             })
    }
    function editCd(cdId: string, upCd: CdDTO) {
        axios.put("/api/cd/" + cdId, upCd)
            .then( () => {
                openCD(cdId)
            })
            .catch( (error_) => {
                if (axios.isAxiosError(error_) && error_.response?.status === 401) {
                    setPriorityError("Unerwarteter Fehler! Versuche Sie sich aus und wieder einzuloggen.")
                } else {
                    console.log(error_.response?.data)
                }
            })
    }
    function deleteCD(cdId: string){
        axios.delete("/api/cd/" + cdId)
             .then( () => {
                 openCollection(selectedCdCollection.id)
             })
             .catch( (error_) => {
                 if (axios.isAxiosError(error_) && error_.response?.status === 401) {
                     setPriorityError("Unerwarteter Fehler! Versuche Sie sich aus und wieder einzuloggen.")
                 } else {
                     console.log(error_)
                 }
             })
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
                 setUserId("")
                 setUserName("")
             })
    }
    const loadCollections = (usrId: string) => {
        axios.get('/api/collections/all/' + usrId)
             .then(response => {
                 setCdCollections(response.data);
             })
             .catch( (error_) => {
                 if (axios.isAxiosError(error_) && error_.response?.status === 401) {
                     setPriorityError("Unerwarteter Fehler! Versuche Sie sich aus und wieder einzuloggen.")
                 } else if (axios.isAxiosError(error_) && error_.response?.status === 404) {
                     setCdCollections(initialCollections)
                     if (error_.response?.data.includes("Benutzer mit id:")) {
                         setPriorityError("Benutzer nicht gefunden! Versuche Sie sich aus und wieder einzuloggen.")
                     }
                 } else {
                     console.log(error_.response?.data)
                 }
             })
    }
    const handleError = (errorMessage: string) => {
            setErrorLog(errorMessage)
    }
    const handlePriorityError = (errorMessage: string) => {
        setPriorityError(errorMessage)
    }
    const handleBack = () => {
        if (backPage === "overview"){
            setTitle("Übersicht Sammlungen")
            setPageType("NO")
            nav("/collections")
        } else if (backPage === "details"){
            setTitle(selectedCdCollection.name)
            setPageType("BACK")
            openCollection(selectedCdCollection.id)
        } else if (backPage === "show-cd"){
            setTitle(selectedCd.cdTitle)
            setPageType("BACK")
            nav("/cd/show/" + selectedCd.id)
        }
    }

    useEffect(() => {
        loadUser()
    }, []);
    useEffect(() => {
        if (!userId) {
            return
        }
        loadCollections(userId)
    }, [userId]);

    return (
        <>
            <Header pageTitle={title} isLoggedIn={isLoggedIn}
                    pType={pageType} onLogout={logout}
                    onBack={handleBack} key={"head"} />
            <Routes>
                <Route path="/"
                       element={<LandingPage onChangePage={changePage}
                                             onGitHubLogin={gitHubLogin}
                                             onGoogleLogin={googleLogin} />}
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
                                                    onError={handleError}
                                                    key={"cd-coll"} />}
                           key={"details"} />
                    <Route path={"/cd/:collId"}
                           element={<AddCdPage onChangePage={changePage}
                                               onAddCd={addCd}
                                               onError={handleError}
                                               onPriorError={handlePriorityError}
                                               key="new-cd" />}
                           key={"add-cd"} />
                    <Route path={"/cd/show/:cdId"}
                           element={<CdPage cd={selectedCd}
                                            onChangePage={changePage}
                                            key={"cd"} />}
                           key={"cdId"} />
                    <Route path={"/cd/edit/:cdId"}
                           element={<EditCd cd={selectedCd}
                                            coll={selectedCdCollection}
                                            onChangePage={changePage}
                                            onEditCd={editCd}
                                            onError={handleError}
                                            key={"edit"} />}
                           key={"editCd"} />
                </Route>
            </Routes>
            <Footer userName={userName}
                    errorMessage={errorLog} key={"baseline"}
                    urgentError={priorityError} />
        </>
    )
}

export default App
