// src/components/NavigationButton.jsx
import React from "react";
import { useNavigate } from "react-router-dom";

const NavHelper = ({ page, children }) => {
    const nav = useNavigate();
    const handleClick = () => {
        nav(page);
    };
    return (
        <button 
            onClick={handleClick}
            style={{
                background: "none",
                border: "none",
                padding: 0,
                margin: 0,
                font: "inherit",
                color: "inherit",
                cursor: "pointer"
            }}
        >
            {children}
        </button>
    );
};

export default NavHelper;