
import React from "react";
import { useMsal } from "@azure/msal-react";

const XboxLogin = () => {
  const { instance } = useMsal();

  const handleLogin = async () => {
    const loginRequest = {
      scopes: ["XboxLive.signin"], // Xbox Live scope
    };

    try {
      const loginResponse = await instance.loginPopup(loginRequest);

      console.log("Login Successful:", loginResponse);
      const xboxToken = loginResponse.idToken;

      // Send the Xbox Token to your backend
      const response = await fetch("http://localhost:8080/api/auth/xbox", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          xboxToken: xboxToken,
          createAccount: false,
        }),
      });

      const result = await response.json();
      console.log("PlayFab Response:", result);
    } catch (error) {
      console.error("Login failed:", error);
    }
  };

  return (
    <div>
      <button onClick={handleLogin}>Login with Xbox</button>
    </div>
  );
};

export default XboxLogin;
