import React from "react";
const apiUrl = "http://localhost:8080";
export const DiscordAuthService = async ()=>{
    try{
        const response = await fetch(`${apiUrl}/api/auth/discord/login`, {
            method: "GET",
            credentials: "include",
        });
        if (!response.ok) {
            throw new Error(`Failed to retrieve tokens. Status: ${response.status}`);
        }
        const data = await response.json();
        if (data.redirectUrl) {
            window.location.href = data.redirectUrl;
        } else {
            console.error("Error during Discord login");
        }
    }catch(error){
        console.error(`Error during Discord login: ${error.message}`);
    }
};

export const exchangeCodeForTokens = async () => {
    // get code from URL
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");

    if (code) {
        try {
            const response = await fetch(`${apiUrl}/api/auth/discord/callback?code=${code}`, {
                method: "GET",
                credentials: "include",
            });

            if (!response.ok) {
                throw new Error(`Failed to retrieve tokens. Status: ${response.status}`);
            }

            const data = await response.json();
            console.log("Callback API JSON Response:", data);

            localStorage.setItem("discord_access_token", data.discord_access_token);
            localStorage.setItem("discord_refresh_token", data.discord_refresh_token);
        } catch (error) {
            console.error(`Error during Discord login: ${error.message}`);
        }
    }
};