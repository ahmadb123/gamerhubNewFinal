
import React, { useState, useEffect } from 'react';
import { getSteamRecentPlayedAndOwnedGames } from '../service/SteamRecentPlayedAndOwnedGames';

const RecentGames = () => {
  const [recentGames, setRecentGames] = useState([]);
  const [isFetchingRecentGames, setIsFetchingRecentGames] = useState(true);

  useEffect(() => {
    const fetchGames = async () => {
      try {
        const gamesData = await getSteamRecentPlayedAndOwnedGames();
        setRecentGames(gamesData);
      } catch (error) {
        console.error(error);
      } finally {
        setIsFetchingRecentGames(false);
      }
    };
    fetchGames();
  }, []);

  return (
    <section className="recent-games">
      <h2>Recent Games</h2>
      <div className="content-box">
        {isFetchingRecentGames ? (
          <p>Loading recent games...</p>
        ) : recentGames.length > 0 ? (
          <div className="game-list">
            {recentGames.map((game, index) => (
              <div className="game-card" key={index}>
                <img src={game.displayImage} alt={game.name} />
                <div className="game-name">{game.name}</div>
                <div className="game-info">
                  {game.titleHistory && game.titleHistory.lastTimePlayedFormatted
                    ? `Last Played: ${game.titleHistory.lastTimePlayedFormatted}`
                    : "Playtime not available"}
                </div>
                <div className="game-devices">
                  {game.devices && game.devices.length > 0
                    ? `Played on: ${game.devices.join(", ")}`
                    : "No devices found"}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p>No recent games found.</p>
        )}
      </div>
    </section>
  );
};

export default RecentGames;