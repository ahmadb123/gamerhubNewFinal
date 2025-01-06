import React, { useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import { toast } from "react-toastify";
import { handleLogin, exchangeCodeForTokens } from "../service/XboxAuthService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXbox, faPlaystation, faSteam } from "@fortawesome/free-brands-svg-icons";


const apiUrl = 'http://localhost:8080';

function LandingPage() {
    const navigate = useNavigate();

    useEffect(() => {
        const handleXboxLogin = async () => {
            await exchangeCodeForTokens();
            localStorage.setItem("platform", "xbox");
            navigate("/main");
        };

        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get("code")) {
            handleXboxLogin();
        }
    }, [navigate]);

    const handlePlatformLogin = async (platform) => {
        if (platform === "xbox") {
            await handleLogin();
        }
        // Add similar logic for PSN and Steam if needed
    };

    return (
        <div className="landing-page" style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>Welcome to GamerHub</h1>
            <p>Select a platform to log in:</p>
            <div style={{ display: "flex", justifyContent: "center", gap: "20px", marginTop: "20px" }}>
                <button onClick={() => handlePlatformLogin("xbox")} style={buttonStyle("green")}>
                    <FontAwesomeIcon icon={faXbox} style={{ marginRight: "10px" }} />
                    Xbox Login
                </button>
                <button onClick={() => handlePlatformLogin("psn")} style={buttonStyle("blue")}>
                    <FontAwesomeIcon icon={faPlaystation} style={{ marginRight: "10px" }} />
                    PSN Login
                </button>
                <button onClick={() => handlePlatformLogin("steam")} style={buttonStyle("gray")}>
                    <FontAwesomeIcon icon={faSteam} style={{ marginRight: "10px" }} />
                    Steam Login
                </button>
            </div>
        </div>
    );
}

const buttonStyle = (color) => ({
    padding: "10px 20px",
    fontSize: "16px",
    cursor: "pointer",
    backgroundColor: color,
    color: "#ffffff",
    border: "none",
    borderRadius: "5px",
});

export default LandingPage;
