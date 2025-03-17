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
            window.location.href = data.redirectURL;
        } else {
            toast.error("Error during Steam login");
        }
    } catch (error) {
        toast.error(`Error during Steam login: ${error.message}`);
    }
};