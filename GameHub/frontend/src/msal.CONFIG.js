import { PublicClientApplication } from "@azure/msal-browser";

const msalConfig = {
  auth: {
    clientId: "YOUR_MICROSOFT_APP_CLIENT_ID", // Register your app in Azure AD
    authority: "https://login.microsoftonline.com/common",
    redirectUri: "http://localhost:3000", // Your React app URL
  },
};

const msalInstance = new PublicClientApplication(msalConfig);

export default msalInstance;
