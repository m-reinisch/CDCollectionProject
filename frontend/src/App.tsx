import './App.css'
import Header from "./components/Header.tsx";
import {Route, Routes} from "react-router-dom";
import LandingPage from "./components/LandingPage.tsx";
import Footer from "./components/Footer.tsx";
import {useEffect, useState} from "react";

function App() {
  const [page, setPage] = useState<string>("landing");
  const [appUser, setAppUser] = useState<string | null | undefined>(undefined)

  const loadUser = () => {
    setAppUser("m-reinisch")
  }

  useEffect(() => {
    loadUser()
  }, []);

  return (
    <>
      <Header pageType={page} key={"head"} />
      <Routes>
        <Route path="/" element={<LandingPage />} key={"land"} />
      </Routes>
      <Footer userName={appUser} errorMessage={""} key={"baseline"} />
    </>
  )
}

export default App
