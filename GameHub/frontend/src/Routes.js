// src/Routes.js
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import PlatformSelectPage from "./pages/PlatformSelectPage";
import LoginPage from "./pages/LoginPage";
import AuthPage from "./pages/AuthPage";
import HomePage from "./pages/HomePage"; // This could be your home content or you can create a separate HomeContent.jsx
import News from "./pages/News";
import CommunityPage from "./pages/CommunityPage";
import ClipsPage from "./pages/ClipsPage";
import MyGames from "./pages/MyGamesPage";
import DirectMessages from "./pages/DirectMessages";
import UserProfile from "./pages/UserProfile";
import GameLayout from "./component/GameLayout";
import GameDetail from "./component/GameDetails";
import MainLayout from "./component/MainLayout";
import WelcomePage from "./pages/WelcomePage";
import FriendsPage from "./pages/FriendsPage";
import MyCollectionsPage from "./pages/MyCollectionsPage";
// src/Routes.js
function App() {
  return (
    <Router>
      <Routes>
        {/* Public routes */}
        <Route path="/" element={<WelcomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/auth" element={<AuthPage />} />
        <Route path="/platform-select" element={<PlatformSelectPage />} />

        {/* Protected routes requiring platform selection */}
        <Route element={<MainLayout />}>
          <Route path="/main" element={<HomePage />} />
          <Route path="/news" element={<GameLayout />}>
            <Route index element={<News />} />
            <Route path="game/:id" element={<GameDetail />} />
          </Route>
          <Route path="/community" element={<CommunityPage />} />
          <Route path="/clips" element={<ClipsPage />} />
          <Route path="/my-games" element={<MyGames />} />
          <Route path="/my-collections" element={<MyCollectionsPage />} />
          <Route path="my-friends" element={<FriendsPage />} />
          <Route path="/direct-messages" element={<DirectMessages />} />
          <Route path="/my-profile" element={<UserProfile />} />
        </Route>

        <Route path="*" element={<WelcomePage />} />
      </Routes>
    </Router>
  );
}

export default App;
