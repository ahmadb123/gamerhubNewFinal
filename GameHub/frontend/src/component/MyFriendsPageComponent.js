// src/components/MyFriendsPageComponent.jsx
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { fetchGameDetails } from "../service/NewsService";
import "../assests/MyFriendsPage.css";

// Helper to build the Steam game image URL
const getSteamGameImageUrl = (game) =>
  `http://media.steampowered.com/steamcommunity/public/images/apps/${game.appid}/${game.img_icon_url}.jpg`;

function MyFriendsPageComponent({
  friend,
  collections = [],
  isCollectionsLoading = false,
}) {
  const navigate = useNavigate();
  const [expandedCollectionId, setExpandedCollectionId] = useState(null);
  const [collectionGames, setCollectionGames] = useState({});
  const [loadingCollectionGames, setLoadingCollectionGames] = useState(false);
  const [gamesExpanded, setGamesExpanded] = useState(false);

  // Fetch detailed game info when a collection is expanded
  useEffect(() => {
    if (
      expandedCollectionId !== null &&
      !collectionGames[expandedCollectionId]
    ) {
      const col = collections.find((c) => c.id === expandedCollectionId);
      if (!col) return;
      setLoadingCollectionGames(true);
      (async () => {
        try {
          const games = await Promise.all(
            (col.items || []).map((item) =>
              fetchGameDetails({ id: item.gameId })
            )
          );
          setCollectionGames((prev) => ({
            ...prev,
            [expandedCollectionId]: games,
          }));
        } catch (err) {
          console.error(
            `Error fetching games for collection ${expandedCollectionId}:`,
            err
          );
          setCollectionGames((prev) => ({
            ...prev,
            [expandedCollectionId]: [],
          }));
        } finally {
          setLoadingCollectionGames(false);
        }
      })();
    }
  }, [expandedCollectionId, collections, collectionGames]);

  // Linked accounts flags
  const hasXbox = friend.xboxProfiles?.length > 0;
  const hasSteam = friend.steamProfiles?.length > 0;

  // Recent games combined
  const xboxRecent = friend.xboxRecentGames || [];
  const steamRecent = friend.steamRecentGames?.ownedAndPlayedGames || [];
  const combinedRecent = [
    ...xboxRecent.map((g) => ({ platform: "Xbox", ...g })),
    ...steamRecent.map((g) => ({ platform: "Steam", ...g })),
  ];
  const topFive = combinedRecent.slice(0, 5);

  return (
    <div className="friend-card">
      {/* Header */}
      <div className="friend-header">
        <h3 className="friend-username">{friend.username}</h3>
        <button
          className="message-btn"
          onClick={() =>
            navigate(`/direct-messages?friendId=${friend.userId}`)
          }
        >
          Message
        </button>
      </div>

      {/* Linked Accounts Section */}
      {(hasXbox || hasSteam) && (
        <div className="linked-accounts">
          <h4>Linked Accounts:</h4>
          <div className="accounts-list">
            {hasXbox &&
              friend.xboxProfiles.map((p, i) => (
                <div key={`xbox-${i}`} className="account">
                  <img
                    src={p.appDisplayPicRaw}
                    alt={p.xboxGamertag}
                  />
                  <p>{p.xboxGamertag}</p>
                  <p>Gamerscore: {p.gamerscore}</p>
                  <p className="platform-label">(Xbox)</p>
                </div>
              ))}
            {hasSteam &&
              friend.steamProfiles.map((p, i) => (
                <div key={`steam-${i}`} className="account">
                  <img src={p.avatarfull} alt={p.personaname} />
                  <p>{p.personaname}</p>
                  <a href={p.profileurl}>Profile</a>
                  <p className="platform-label">(Steam)</p>
                </div>
              ))}
          </div>
        </div>
      )}

      {/* Collections Section */}
      <div className="collections">
        <h4>Collections:</h4>
        {isCollectionsLoading ? (
          <p>Loading collections…</p>
        ) : collections.length > 0 ? (
          collections.map((col) => (
            <div key={col.id} className="collection-item">
              <div
                className="collection-header"
                onClick={() =>
                  setExpandedCollectionId((prev) =>
                    prev === col.id ? null : col.id
                  )
                }
              >
                <strong>{col.name}</strong>
                <span>{col.items?.length ?? 0} games</span>
              </div>
              {expandedCollectionId === col.id && (
                <div className="collection-games">
                  {loadingCollectionGames ? (
                    <p>Loading games…</p>
                  ) : (
                    (collectionGames[col.id] || []).map((game) => (
                      <div key={game.id} className="game-thumb">
                        <img
                          src={
                            game.background_image || getSteamGameImageUrl(game)
                          }
                          alt={game.name}
                          onClick={() =>
                            navigate(`/news/game/${game.id}`)
                          }
                        />
                      </div>
                    ))
                  )}
                </div>
              )}
            </div>
          ))
        ) : (
          <p>No collections to show.</p>
        )}
      </div>

      {/* Recent Games Section */}
      <div className="recent-games">
        <h4>Recent Games:</h4>
        {topFive.length === 0 ? (
          <p>No recent games available</p>
        ) : !gamesExpanded ? (
          <div className="recent-preview">
            <img
              src={
                topFive[0].platform === "Xbox"
                  ? topFive[0].displayImage
                  : getSteamGameImageUrl(topFive[0])
              }
              alt={
                topFive[0].platform === "Xbox"
                  ? topFive[0].gameName
                  : topFive[0].name
              }
            />
            <p>
              {topFive[0].platform === "Xbox"
                ? topFive[0].gameName
                : topFive[0].name}
            </p>
            {combinedRecent.length > 1 && (
              <button
                className="show-more-btn"
                onClick={() => setGamesExpanded(true)}
              >
                Show More
              </button>
            )}
          </div>
        ) : (
          <>
            <div className="recent-list">
              {topFive.map((game) => (
                <div key={game.id} className="recent-thumb">
                  <img
                    src={
                      game.platform === "Xbox"
                        ? game.displayImage
                        : getSteamGameImageUrl(game)
                    }
                    alt={
                      game.platform === "Xbox"
                        ? game.gameName
                        : game.name
                    }
                  />
                  <p>
                    {game.platform === "Xbox"
                      ? game.gameName
                      : game.name}
                  </p>
                  <p className="platform-label">{game.platform}</p>
                </div>
              ))}
            </div>
            <button
              className="show-more-btn"
              onClick={() => setGamesExpanded(false)}
            >
              Show Less
            </button>
          </>
        )}
      </div>
    </div>
  );
}

export default MyFriendsPageComponent;
