import React, { useEffect } from "react";
import { toast, ToastContainer } from "react-toastify";

const apiUrl = 'http://localhost:8080';

export const exchangeCodeForTokens = async () => {
    // get code from URL
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");

    if (code) {
        try {
            const response = await fetch(`${apiUrl}/api/auth/callback?code=${code}`, {
                method: "GET",
                credentials: "include",
            });

            if (!response.ok) {
                throw new Error(`Failed to retrieve tokens. Status: ${response.status}`);
            }

            const data = await response.json();
            console.log("Callback API JSON Response:", data);

            localStorage.setItem("uhs", data.uhs);
            localStorage.setItem("XSTS_token", data.xsts_token);

            toast.success("Xbox login successful!");
        } catch (error) {
            toast.error(`Error during Xbox login: ${error.message}`);
        }
    }
};

export const handleLogin = async () => {
    try {
        const response = await fetch(`${apiUrl}/api/auth/login`, {
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
            toast.error("Error during Xbox login");
        }
    } catch (error) {
        toast.error(`Error during Xbox login: ${error.message}`);
    }
};

function XboxLogin() {
    useEffect(() => {
        exchangeCodeForTokens();
    }, []);

    return (
       <div className="tes">
              <button onClick={handleLogin}>Login with Xbox</button>
       </div>
    );
}

export default XboxLogin;
