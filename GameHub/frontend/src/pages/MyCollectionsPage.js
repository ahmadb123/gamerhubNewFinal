// src/pages/MyCollectionsPage.jsx
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { fetchCollections } from "../NewsHelper/AddNewsGamesToGameList";
import { fetchGameDetails } from "../service/NewsService";
import "../assests/Collections.css";

function MyCollectionsPage() {
  const [collections, setCollections] = useState([]);
  const [expandedId, setExpandedId]     = useState(null);
  const [loading, setLoading]           = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    async function loadCollections() {
      try {
        // 1️⃣ Grab collections (each should include an `items` array with gameId)
        const cols = await fetchCollections();
        console.log("raw collections from API:", cols);

        // 2️⃣ For each collection, fetch the full RAWG details for each saved gameId
        const withGameDetails = await Promise.all(
          cols.map(async (col) => {
            const games = await Promise.all(
              (col.items || []).map((item) =>
                fetchGameDetails({ id: item.gameId })
              )
            );
            return { ...col, games };
          })
        );
        setCollections(withGameDetails);
      } catch (err) {
        toast.error(`Failed to load collections: ${err.message}`);
      } finally {
        setLoading(false);
      }
    }

    loadCollections();
  }, []);

  const toggleExpand = (id) =>
    setExpandedId((prev) => (prev === id ? null : id));

  const handleImageClick = (game) =>
    navigate(`/news/game/${game.id}`);

  if (loading) {
    return <p>Loading collections…</p>;
  }

  if (!collections.length) {
    return <p>You haven’t created any collections yet.</p>;
  }

  return (
    <div className="collections-container">
      <h1>My Collections</h1>
      <div className="collection-grid">
        {collections.map((col) => (
          <div key={col.id} className="collection-card">
            <div
              className="collection-header"
              onClick={() => toggleExpand(col.id)}
            >
              <h2 className="collection-title">{col.name}</h2>
              <span className="collection-meta">
                {col.games?.length || 0} game
                {col.games?.length === 1 ? "" : "s"}
              </span>
            </div>

            {expandedId === col.id && (
              <div className="games-grid">
                {col.games && col.games.length ? (
                  col.games.map((game) => (
                    <div
                      key={game.id}
                      className="game-card"
                      onClick={() => handleImageClick(game)}
                    >
                      <img
                        src={game.background_image}
                        alt={game.name}
                        loading="lazy"
                      />
                      <span>{game.name}</span>
                    </div>
                  ))
                ) : (
                  <span style={{ color: "#888", fontSize: "0.8rem" }}>
                    No games saved.
                  </span>
                )}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}

export default MyCollectionsPage;
