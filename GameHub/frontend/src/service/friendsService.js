const apiUrl = 'http://localhost:8080';


// fetch xbox friends - 

export const fetchXboxFriends = async () =>{
    // uhs and token - 
    const uhs = localStorage.getItem("uhs");
    const XSTS_token = localStorage.getItem("XSTS_token");
    console.log("XBOX FRIENFS FETCH tokens - UHS:", uhs, "XSTS_token:", XSTS_token);

    if (!uhs || !XSTS_token) {
        console.error("Xbox authentication tokens missing. Please log in again.");
        throw new Error("Xbox authentication tokens missing.");
    }

    try{
        const response = await fetch(`${apiUrl}/api/xbox/friends/top-ten`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `XBL3.0 x=${uhs};${XSTS_token}`,
            },
        });
        if(!response.ok){
            throw new Error(`Failed to fetch Xbox friends. Status: ${response.status}`);
        }
        const data = await response.json();
        return data.people || [];
    }catch(error){
        console.error(error);
        console.error("Failed to fetch Xbox friends.");
        throw error;
    }
};
export const fetchSteamFriends = async () => {
    const jwtToken = localStorage.getItem("jwtToken");
    try {
      const response = await fetch(`${apiUrl}/api/steam/userinfo/get-friends`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + jwtToken,
        },
      });
      if (!response.ok) {
        throw new Error(`Failed to fetch Steam friends. Status: ${response.status}`);
      }
      const data = await response.json();
      console.log("Steam friends data:", data);
      
      // Transform the friends array so that it matches the UI format
      const friends = data.friends.map(friend => {
        // Extract the hash from the low-quality avatar URL.
        const avatarHash = friend.friendAvatar
          .replace("https://avatars.steamstatic.com/", "")
          .replace(".jpg", "");
      
        // Construct the high-quality URL.
        const fullAvatarUrl = `https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/${avatarHash.slice(0, 2)}/${avatarHash}_full.jpg`;
      
        return {
          gamertag: friend.friendName,
          displayPicRaw: fullAvatarUrl,  // Full-quality avatar URL
          realName: friend.friendName,
          presenceState: "Offline",
          presenceText: "Offline",
          friendSince: friend.friendSince,
          relationship: friend.relationship,
          steamId: friend.steamId,
          profileImg: fullAvatarUrl,     // If needed elsewhere, use the same high-quality URL
        };
      });
      
      return friends || [];
      
    } catch (error) {
      console.error("Failed to fetch Steam friends:", error);
      throw error;
    }
  };
