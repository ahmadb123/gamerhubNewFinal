import { PublicClientApplication } from "@azure/msal-browser";

const msalConfig = {
  auth: {
    clientId: "f6916aa3-e279-4c0c-90a7-c9706fd211fe", // Your Client ID
    authority: "https://login.microsoftonline.com/common",
    redirectUri: "http://localhost:3000", // Your redirect URI
  },
};

export const msalInstance = new PublicClientApplication(msalConfig);
