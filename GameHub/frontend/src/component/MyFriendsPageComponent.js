import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

// Helper to build the Steam game image URL.
const getSteamGameImageUrl = (game) => {
  return `http://media.steampowered.com/steamcommunity/public/images/apps/${game.appid}/${game.img_icon_url}.jpg`;
};

function MyFriendsPageComponent({ friend }) {
  const navigate = useNavigate();
  // State for toggling the recent games expansion.
  const [gamesExpanded, setGamesExpanded] = useState(false);

  // Determine which linked accounts the friend has.
  const hasXbox = friend.xboxProfiles && friend.xboxProfiles.length > 0;
  const hasSteam = friend.steamProfiles && friend.steamProfiles.length > 0;

  // Count the total number of linked accounts.
  const linkedAccountsCount =
    (hasXbox ? friend.xboxProfiles.length : 0) +
    (hasSteam ? friend.steamProfiles.length : 0);

  // Recent Games: Combine Xbox and Steam recent game data.
  const hasXboxRecentGames = friend.xboxRecentGames && friend.xboxRecentGames.length > 0;
  const hasSteamRecentGames =
    friend.steamRecentGames &&
    friend.steamRecentGames.ownedAndPlayedGames &&
    friend.steamRecentGames.ownedAndPlayedGames.length > 0;

  const combinedRecentGames = [];
  if (hasXboxRecentGames) {
    combinedRecentGames.push(
      ...friend.xboxRecentGames.map((game) => ({
        platform: "Xbox",
        ...game,
      }))
    );
  }
  if (hasSteamRecentGames) {
    combinedRecentGames.push(
      ...friend.steamRecentGames.ownedAndPlayedGames.map((game) => ({
        platform: "Steam",
        ...game,
      }))
    );
  }
  // Assuming the arrays are already ordered by recency, pick the top 5 items.
  const topFiveCombinedGames = combinedRecentGames.slice(0, 5);

  return (
    <div
      style={{
        border: "1px solid #444",
        borderRadius: "8px",
        padding: "12px",
        marginBottom: "12px",
        backgroundColor: "#222",
        color: "#fff",
      }}
    >
      {/* Header Section */}
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h3 style={{ margin: 0 }}>{friend.username}</h3>
        <button
          onClick={() => navigate(`/direct-messages?friendId=${friend.userId}`)}
          style={{
            background: "#0af",
            border: "none",
            color: "#fff",
            padding: "0.3rem 0.6rem",
            borderRadius: "4px",
            cursor: "pointer",
            fontSize: "0.9em",
          }}
        >
          Message
        </button>
      </div>

      {/* Linked Accounts Section */}
      {linkedAccountsCount > 0 && (
        <div style={{ marginTop: "8px" }}>
          <h4 style={{ marginBottom: "4px" }}>Linked Accounts:</h4>
          <div style={{ display: "flex", gap: "10px" }}>
            {hasXbox &&
              friend.xboxProfiles.map((profile, idx) => (
                <div key={idx} style={{ textAlign: "center" }}>
                  <img
                    src={profile.appDisplayPicRaw}
                    alt={profile.xboxGamertag}
                    style={{ width: "50px", height: "50px", borderRadius: "4px" }}
                  />
                  <p style={{ margin: "2px 0 0 0", fontSize: "0.8rem" }}>{profile.xboxGamertag}</p>
                  <p style={{ fontSize: "0.8rem" }}>Gamerscore: {profile.gamerscore}</p>
                  <p style={{ fontSize: "0.8rem" }}>Tenure Level: {profile.tenureLevel}</p>
                  <p style={{ fontSize: "0.7rem", fontStyle: "italic" }}>Xbox</p>
                </div>
              ))}
            {hasSteam &&
              friend.steamProfiles.map((profile, idx) => (
                <div key={idx} style={{ textAlign: "center" }}>
                  <img
                    src={profile.avatarfull}
                    alt={profile.personaname}
                    style={{ width: "50px", height: "50px", borderRadius: "4px" }}
                  />
                  <p style={{ margin: "2px 0 0 0", fontSize: "0.8rem" }}>{profile.personaname}</p>
                  <a href={profile.profileurl} style={{ fontSize: "0.8rem", color: "#0af" }}>
                    Profile
                  </a>
                  <p style={{ fontSize: "0.7rem", fontStyle: "italic" }}>Steam</p>
                </div>
              ))}
          </div>
        </div>
      )}

      {/* Recent Games Section */}
      {topFiveCombinedGames.length > 0 ? (
        <div style={{ marginTop: "10px" }}>
          <h4 style={{ marginBottom: "4px" }}>Recent Games:</h4>
          {/* Preview Mode: Show only the first recent game */}
          {!gamesExpanded ? (
            <div style={{ display: "flex", alignItems: "center" }}>
              {topFiveCombinedGames[0].platform === "Xbox" ? (
                <img
                  src={topFiveCombinedGames[0].displayImage}
                  alt={topFiveCombinedGames[0].gameName}
                  style={{
                    width: "50px",
                    height: "50px",
                    marginRight: "10px",
                    borderRadius: "4px",
                  }}
                />
              ) : (
                <img
                  src={getSteamGameImageUrl(topFiveCombinedGames[0])}
                  alt={topFiveCombinedGames[0].name}
                  style={{
                    width: "50px",
                    height: "50px",
                    marginRight: "10px",
                    borderRadius: "4px",
                  }}
                />
              )}
              <p style={{ margin: 0, fontSize: "0.9em" }}>
                {topFiveCombinedGames[0].platform === "Xbox"
                  ? topFiveCombinedGames[0].gameName
                  : topFiveCombinedGames[0].name}
              </p>
              {/* Render "Show More" only if there is more than one recent game */}
              {combinedRecentGames.length > 1 && (
                <button
                  onClick={() => setGamesExpanded(true)}
                  style={{
                    marginLeft: "auto",
                    background: "none",
                    border: "none",
                    color: "#0af",
                    cursor: "pointer",
                    fontSize: "0.9em",
                  }}
                >
                  Show More
                </button>
              )}
            </div>
          ) : (
            <>
              {/* Expanded Mode: Show top 5 recent games horizontally */}
              <div style={{ display: "flex", overflowX: "auto" }}>
                {topFiveCombinedGames.map((game, idx) => (
                  <div key={idx} style={{ marginRight: "10px", textAlign: "center" }}>
                    {game.platform === "Xbox" ? (
                      <img
                        src={game.displayImage}
                        alt={game.gameName}
                        style={{
                          width: "80px",
                          height: "80px",
                          borderRadius: "4px",
                        }}
                      />
                    ) : (
                      <img
                        src={getSteamGameImageUrl(game)}
                        alt={game.name}
                        style={{
                          width: "80px",
                          height: "80px",
                          borderRadius: "4px",
                        }}
                      />
                    )}
                    <p style={{ margin: "4px 0 0 0", fontSize: "0.8rem" }}>
                      {game.platform === "Xbox" ? game.gameName : game.name}
                    </p>
                    <p style={{ margin: 0, fontSize: "0.7rem", color: "#0af" }}>
                      {game.platform}
                    </p>
                  </div>
                ))}
              </div>
              <button
                onClick={() => setGamesExpanded(false)}
                style={{
                  marginTop: "8px",
                  background: "none",
                  border: "none",
                  color: "#0af",
                  cursor: "pointer",
                  fontSize: "0.9em",
                }}
              >
                Show Less
              </button>
            </>
          )}
        </div>
      ) : (
        <p style={{ marginTop: "8px" }}>No recent games available</p>
      )}
    </div>
  );
}

export default MyFriendsPageComponent;
