import React from "react";
import { Outlet } from "react-router-dom";
import GenreSidebar from "../NewsHelper/GenreSideBar";
import '../assests/News.css';

function GameLayout() {
  return (
    <div className="news-page">
      <GenreSidebar />
      <div className="news-content">
        <Outlet />
      </div>
    </div>
  );
}

export default GameLayout;