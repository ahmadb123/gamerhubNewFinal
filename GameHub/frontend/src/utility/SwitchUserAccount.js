// SwitchUserAccount.js

{
    /*
    This function switches the user
    account by updating user current platform
    */
}
  
{
    /*
    This function switches the user
    account by updating user current platform
    */
}
import { getAllLinkedProfiles } from "../service/UserLinkedProfiles";
// SwitchUserAccount.js
{
    /* 
        *This function switches the user account by updating user current platform
        * to do so -> 
        * 1.  Find the user's record in DB by userId
        * 2.  Retrieve the tokens / credentials for the requested platform
        * 3.  Update the user’s "activePlatform" in their session
        * 4.  Now your server calls the correct provider using the correct tokens
    */
}
   // function to return the user xbox xuid and uhs-> 
   let userXuid = null;
   let userUhs = null;
   let userSteamId = null;
   
   const getXboxUserInfo = async () => {
        const response = await getAllLinkedProfiles();
        // Ensure comparison is case-insensitive
        const xboxProfile = response.find(
        profile => profile.platform.toLowerCase() === "xbox"
        );
        if (xboxProfile) {
        userXuid = xboxProfile.xuid;
        userUhs = xboxProfile.uhs;
        }
        console.log("Xbox credentials:", userXuid, userUhs);
    };

    // Function to retrieve the Steam credentials
    const getSteamUserInfo = async () => {
        const response = await getAllLinkedProfiles();
        // Ensure comparison is case-insensitive
        const steamProfile = response.find(
        profile => profile.platform.toLowerCase() === "steam");
        if (steamProfile) {
        userSteamId = steamProfile.steamId;
        }
        console.log("Steam credentials:", userSteamId);
    };

// switch accounts accoring to the platform
export const switchUserAccount = async (platform, nav) => {
    // Normalize platform value to lowercase for consistency
    const normalizedPlatform = platform.toLowerCase();
  
    if (normalizedPlatform === "xbox") {
      await getXboxUserInfo();
      if (!userXuid || !userUhs) {
        console.error("Xbox user info not found");
        return nav("/landingPage");
      }
      localStorage.setItem("platform", "xbox");
      localStorage.setItem("xuid", userXuid);
      localStorage.setItem("uhs", userUhs);
    } else if (normalizedPlatform === "steam") {
      // Call getSteamUserInfo first
      await getSteamUserInfo();
      if (!userSteamId) {
        console.error("Steam user info not found");
        return nav("/landingPage");
      }
      localStorage.setItem("platform", "steam");
      localStorage.setItem("steamId", userSteamId);
    } else {
      console.error("Unsupported platform:", platform);
      return nav("/landingPage");
    }
    console.log("User account switched to:", normalizedPlatform);
    nav("/main");
};
  