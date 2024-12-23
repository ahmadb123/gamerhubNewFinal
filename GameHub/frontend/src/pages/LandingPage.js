import React, { useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import { toast } from "react-toastify";
import { handleLogin, exchangeCodeForTokens } from "../service/XboxAuthService";
import { fetchXboxProfile } from "../service/profileService";

const apiUrl = 'http://localhost:8080';

function LandingPage() {
    const navigate = useNavigate();

    useEffect(() => {
        const handleXboxLogin = async () => {
            await exchangeCodeForTokens();
            try {
                const profile = await fetchXboxProfile();
                console.log("Fetched Xbox profile:", profile);
                localStorage.setItem("platform", "xbox");
                navigate("/main");
            } catch (error) {
                console.error(error);
                toast.error("Failed to fetch Xbox profile.");
            }
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
                    Xbox Login
                </button>
                <button onClick={() => handlePlatformLogin("psn")} style={buttonStyle("blue")}>
                    PSN Login
                </button>
                <button onClick={() => handlePlatformLogin("steam")} style={buttonStyle("gray")}>
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
