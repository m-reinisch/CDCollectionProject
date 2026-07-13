import './App.css'
import Header from "./components/Header.tsx";
import {Route, Routes} from "react-router-dom";
import LandingPage from "./components/LandingPage.tsx";
import Footer from "./components/Footer.tsx";

function App() {

  return (
    <>
      <Header key={"Kopf"} />
      <Routes>
        <Route path="/" element={<LandingPage />} />
      </Routes>
      <Footer key={"baseline"} />
    </>
  )
}

export default App
