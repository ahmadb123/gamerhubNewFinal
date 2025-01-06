import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import HomePage from "./pages/HomePage";
import News from "./pages/News";
import LoginPage from "./pages/LoginPage";
import AuthPage from "./pages/AuthPage";
function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LandingPage />} />
                <Route path="/main" element={<HomePage />} />
                <Route path="/news" element={<News />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/auth" element={<AuthPage />} />
                <Route path="*" element={<LandingPage />} />
            </Routes>
        </Router>
    );
}

export default App;
