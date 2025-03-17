import React, { useEffect } from "react";
import { toast, ToastContainer } from "react-toastify";

const apiUrl = 'http://localhost:8080';
export const handleSteamLogin = async () => {
    try {
        const response = await fetch(`${apiUrl}/steam/login`, {
            method: "GET",
            credentials: "include",
        });
        if (!response.ok) {
            throw new Error(`Failed to retrieve tokens. Status: ${response.status}`);
        }
        const data = await response.json();
        console.log("Steam login response:", data);
        if (data.redirectURL) {
            localStorage.setItem("platform", "steam");
            // Redirect user to Steam for authentication
            window.location.href = data.redirectURL;
        } else {
            toast.error("Error during Steam login");
        }
    } catch (error) {
        toast.error(`Error during Steam login: ${error.message}`);
    }
};

export const handleSteamRedirect = async () => {
    try {
        const response = await fetch(`${apiUrl}/steam/return`, {
            method: "GET",
            credentials: "include",
        });
        if (!response.ok) {
            throw new Error(`Failed to retrieve Steam profile. Status: ${response.status}`);
        }
        const data = await response.json();
        localStorage.setItem("steamId", data.steamID);
        console.log("Steam return response:", data);
        return data; // Return the data so the calling function can use it
    } catch (error) {
        toast.error(`Error during Steam return: ${error.message}`);
        return null;
    }
};
