import React, { useCallback } from 'react';
import { msalInstance } from './msalConfig';

const XboxLogin = () => {
  const signInWithXbox = useCallback(async () => {
    try {
      // Sign in to Microsoft using MSAL - get Microsoft access token
      const authResult = await msalInstance.loginPopup({
        scopes: ["openid", "profile", "User.Read"], 
      });

      const microsoftAccessToken = authResult.accessToken;

      // Instead of calling Xbox endpoints directly, send msToken to the backend
      // The backend will exchange it for XSTS and then log into PlayFab
      const response = await fetch("http://localhost:8080/api/auth/xboxExchange", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ msToken: microsoftAccessToken })
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Failed to exchange token and log in via Xbox: ${errorText}`);
      }

      const data = await response.json();
      console.log("PlayFab login successful:", data);

    } catch (error) {
      console.error("Error during sign-in:", error);
    }
  }, []);

  return (
    <button onClick={signInWithXbox}>Sign in with Xbox</button>
  );
};

export default XboxLogin;
