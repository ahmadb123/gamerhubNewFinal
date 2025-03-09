// layouts/NewsLayout.jsx
import React from "react";
import { Outlet } from "react-router-dom";
import GenreSidebar from '../NewsHelper/GenreSideBar';

function NewsLayout() {
  return (
    <div className="news-page">
      {/* Left sidebar */}
      <GenreSidebar
        // If needed, pass props or fetch genres here
      />

      {/* Main content area */}
      <div className="news-content">
        <h1>News and Trending Games</h1>
        
        {/* The nested routes (NewsList or NewsDetails) will render here */}
        <Outlet />
      </div>
    </div>
  );
}

export default NewsLayout;
