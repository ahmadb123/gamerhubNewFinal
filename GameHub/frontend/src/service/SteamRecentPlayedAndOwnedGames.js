const apiUrl = 'http://localhost:8080';
const jwtToken = localStorage.getItem("jwtToken");
export const getSteamRecentPlayedAndOwnedGames = async () => {
    try {
      const response = await fetch(`${apiUrl}/api/steam/userinfo/getUserGameInfo`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + jwtToken,
        },
      });
  
      if (!response.ok) {
        throw new Error(`Failed to fetch Steam games. Status: ${response.status}`);
      }
  
      const data = await response.json();
      // data looks like: { players: null, game_count: 2, owenedAndPlayedGames: [...] }
  
      // Use the correct property from your JSON
      const steamGames = data.owenedAndPlayedGames || [];
  
      // Transform to the UI format
      const games = steamGames.map((game) => ({
        ...game,
        displayImage: `https://steamcdn-a.akamaihd.net/steam/apps/${game.appid}/header.jpg`,

        titleHistory: {
          lastTimePlayedFormatted:
            game.rtime_last_played && game.rtime_last_played > 0
              ? new Date(game.rtime_last_played * 1000).toLocaleString()
              : null,
        },
        devices: ["PC"], 
      }));
  
      return games;
    } catch (error) {
      console.error("Failed to fetch Steam recent played and owned games:", error);
      throw error;
    }
  };
  
  // Helper function to format minutes into hours and minutes
const formatMinutes = (minutes) => {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h ${mins}m`;
  };
  
  export const getSteamRecentPlayedGames = async () => {
    try {
      const response = await fetch(`${apiUrl}/api/steam/userinfo/recent-played-games`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + jwtToken,
        },
      });
      
      if (!response.ok) {
        throw new Error(`Failed to fetch Steam games. Status: ${response.status}`);
      }
      
      const data = await response.json();
      // Use the correct property from your backend response
      const steamGames = data.owenedAndPlayedGames || [];
      console.log("Steam recent played games:", steamGames);
      
      // Transform UI data: convert playtime values to a readable format
      const games = steamGames.map((game) => ({
        ...game,
        displayImage: `https://steamcdn-a.akamaihd.net/steam/apps/${game.appid}/header.jpg`,
        titleHistory: {
          // Using playtime_2weeks for recent playtime
          lastTimePlayedFormatted: game.playtime_2weeks && game.playtime_2weeks > 0 
            ? formatMinutes(game.playtime_2weeks)
            : "User has not played recently",
        },
        totalPlaytime: game.playtime_forever && game.playtime_forever > 0
            ? formatMinutes(game.playtime_forever)
            : "Not available",
        devices: ["PC"],
      }));
      
      return games;
    } catch (error) {
      console.error("Failed to fetch Steam recent played games:", error);
      throw error;
    }
  };
  