
const apiUrl = 'http://localhost:8080';

// Fetch Xbox profile
export const fetchXboxProfile = async () => {
    const jwtToken = localStorage.getItem("jwtToken");
    const uhs = localStorage.getItem("uhs");
    const XSTS_token = localStorage.getItem("XSTS_token");

    // Debugging: Log the retrieved tokens
    console.log("Fetched tokens - UHS:", uhs, "XSTS_token:", XSTS_token);

    if (!uhs || !XSTS_token) {
        console.error("Xbox authentication tokens missing. Please log in again.");
        throw new Error("Xbox authentication tokens missing.");
    }

    try {
        const response = await fetch(`${apiUrl}/api/xbox/profile`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `XBL3.0 x=${uhs};${XSTS_token}`,
                'Authorization': 'Bearer ' + jwtToken
            },
        });

        if (!response.ok) {
            throw new Error(`Failed to fetch Xbox profile. Status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        console.error("Failed to fetch Xbox profile.");
        throw error;
    }
};

// Fetch PSN profile
export const fetchPSNProfile = async () => {
    try {
        const response = await fetch(`${apiUrl}/api/psn/profile`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            throw new Error(`Failed to fetch PSN profile. Status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        console.error("Failed to fetch PSN profile.");
        throw error;
    }
};
export const fetchSteamProfile = async () => {
    const steamId = localStorage.getItem("steamId");
    console.log("Fetching Steam profile with steamId:", steamId);
    try {
      const response = await fetch(`${apiUrl}/api/steam/userinfo/getUserInfo?steamId=${steamId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      console.log("Steam profile response status:", response.status);
      if (!response.ok) {
        throw new Error(`Failed to fetch Steam profile. Status: ${response.status}`);
      }
      const data = await response.json();
      console.log("Steam profile data:", data);
      // Map Steam data to a common format
      if (data.players && data.players.length > 0) {
        const player = data.players[0];
        return {
          gamertag: player.personaname, // use personaname as gamertag
          appDisplayPicRaw: player.avatarfull, // use avatarfull as profile pic
          realname: player.realname,
          profileurl: player.profileurl,
          // include any other fields your UI expects
        };
      }
      throw new Error("No Steam player found");
    } catch (error) {
      console.error(error);
      console.error("Failed to fetch Steam profile.");
      throw error;
    }
};
  