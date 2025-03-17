import React, { useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import { toast } from "react-toastify";
import { handleLogin, exchangeCodeForTokens } from "../service/XboxAuthService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXbox, faPlaystation, faSteam } from "@fortawesome/free-brands-svg-icons";
import { handleSteamLogin } from "../service/SteamAuthService";

function LandingPage() {
    const navigate = useNavigate();

    useEffect(() => {
        const handleXboxLogin = async () => {
            await exchangeCodeForTokens();
            localStorage.setItem("platform", "xbox");
            navigate("/main");
        };

        const handleSteamReturn = () => {
            const urlParams = new URLSearchParams(window.location.search);
            const steamID = urlParams.get("steamID");
            if (steamID) {
                localStorage.setItem("steamId", steamID); // ensure key matches the fetch service
                localStorage.setItem("platform", "steam");
                navigate("/main");
            }
        };

        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get("code")) {
            handleXboxLogin();
        }
        // Check for steamID in URL and navigate to main if present.
        if (urlParams.get("steamID")) {
            handleSteamReturn();
        }
    }, [navigate]);

    const handlePlatformLogin = async (platform) => {
        if (platform === "xbox") {
            await handleLogin();
        }
        if (platform === "steam") {
            await handleSteamLogin();
        }
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
