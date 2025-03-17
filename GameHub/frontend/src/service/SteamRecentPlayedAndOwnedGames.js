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
        displayImage: game.img_icon_url
          ? `https://steamcdn-a.akamaihd.net/steamcommunity/public/images/apps/${game.appid}/${game.img_icon_url}.jpg`
          : "/default-game.jpg",
        titleHistory: {
          lastTimePlayedFormatted:
            game.rtime_last_played && game.rtime_last_played > 0
              ? new Date(game.rtime_last_played * 1000).toLocaleString()
              : null,
        },
        devices: ["PC"], // or an empty array if you don't have devices
      }));
  
      return games;
    } catch (error) {
      console.error("Failed to fetch Steam recent played and owned games:", error);
      throw error;
    }
  };
  