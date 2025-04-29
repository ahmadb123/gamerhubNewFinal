import React, { useEffect, useState, useRef } from "react";
import { useParams } from "react-router-dom";
import { fetchGameDetails } from "../service/NewsService";
import {
  AddNewsGamesToGameList,
  AddToWishList,
  fetchCollections,
  createCollection,
  addGameToCollection,
} from "../NewsHelper/AddNewsGamesToGameList";
import { fetchGameTrailers } from "../service/GameTrailersFromYoutubeService";
import { getGamePrices } from "../service/GamePricesService";
import "../component/GameDetails.css"; // Make sure this path is correct
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function GameDetail() {
  const { id } = useParams();

  const [game, setGame] = useState(null);
  const [gameLoading, setGameLoading] = useState(true);
  const [error, setError] = useState(null);

  const [trailers, setTrailers] = useState([]);
  const [trailerLoading, setTrailerLoading] = useState(true);

  // Price data from CheapShark
  const [prices, setPrices] = useState(null);
  const [priceLoading, setPriceLoading] = useState(true);

  // Modal & collections state
  const [collections, setCollections] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [saving, setSaving] = useState(false);
  const newNameRef = useRef(null);

  // 1) Fetch trailers from YouTube
  const fetchTrailersForGame = async (gameName) => {
    try {
      const data = await fetchGameTrailers({ gameName });
      setTrailers(data.items);
    } catch (error) {
      console.error("Error fetching trailer:", error);
    } finally {
      setTrailerLoading(false);
    }
  };

  // 2) Fetch game prices from CheapShark
  const fetchPricesForGame = async (gameName) => {
    try {
      const data = await getGamePrices({ gameName });
      setPrices(data);
    } catch (error) {
      console.error("Error fetching prices:", error);
    } finally {
      setPriceLoading(false);
    }
  };

  // 3) Fetch RAWG game details
  useEffect(() => {
    async function fetchGame() {
      try {
        const data = await fetchGameDetails({ id });
        setGame(data);

        // Once we have the game name, fetch trailers & prices
        if (data && data.name) {
          fetchTrailersForGame(data.name);
          fetchPricesForGame(data.name);
        }
      } catch (err) {
        setError(err);
      } finally {
        setGameLoading(false);
      }
    }
    fetchGame();
  }, [id]);

  // 4) Load YouTube iframe once we have trailer data
  useEffect(() => {
    if (!trailerLoading && trailers.length > 0) {
      const loadPlayer = () => {
        new window.YT.Player("youtube-player", {
          height: "315",
          width: "560",
          videoId: trailers[0].id.videoId,
          playerVars: {
            autoplay: 1,
            mute: 0,
          },
          events: {
            onReady: (event) => event.target.playVideo(),
          },
        });
      };

      if (!window.YT) {
        const tag = document.createElement("script");
        tag.src = "https://www.youtube.com/iframe_api";
        const firstScriptTag = document.getElementsByTagName("script")[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
        window.onYouTubeIframeAPIReady = loadPlayer;
      } else {
        loadPlayer();
      }
    }
  }, [trailerLoading, trailers]);

  
  const openCollectionModal = async () => {
    try {
      setCollections(await fetchCollections());
      setShowModal(true);
    } catch (err) {
      toast.error(`Failed to load collections: ${err.message}`);
    }
  };

  const chooseCollection = async (col) => {
    try {
      await addGameToCollection(col.id, game.id);
      toast.success(`✅ Added to “${col.name}”`);
    } catch (err) {
      toast.error(`❌ ${err.message}`);
    } finally {
      setShowModal(false);
    }
  };

  const handleCreateNew = async () => {
    const name = newNameRef.current.value.trim();
    if (!name) return toast.error("Name cannot be empty");

    try {
      setSaving(true);
      const col = await createCollection(name);
      toast.success(`✅ Created “${col.name}”`);
      await addGameToCollection(col.id, game.id);
      toast.success(`✅ Added to “${col.name}”`);
    } catch (err) {
      toast.error(`❌ ${err.message}`);
    } finally {
      setSaving(false);
      setShowModal(false);
    }
  };

  // Early returns for loading / error
  if (gameLoading) return <p>Loading game details...</p>;
  if (error) return <p>Error: {error.message}</p>;
  if (!game) return <p>No game data found.</p>;

  // Helper fields from RAWG
  const releaseDate = game.released || "TBA";
  const developers = game.developers?.map((dev) => dev.name).join(", ") || "Unknown";
  const platforms = game.platforms || [];
  const genres = game.genres || [];

  // Extract cheapest price or deals from the prices object
  const cheapestEver = prices?.cheapestPriceEver?.price; // e.g. "49.99"
  const deals = prices?.deals || [];

  // If you want top 3 deals:
  const topDeals = deals.slice(0, 3);

  return (
    <div className="game-detail-container">
      {/* A background container image with partial opacity */}
      <div
        className="background-image-container"
        style={{ backgroundImage: `url(${game.background_image})` }}
      />

      {/* LEFT COLUMN */}
      <div className="left-column">
        <h1>{game.name}</h1>
        <div className="btn-container">
          {/* Add to My Games */}
          <button
            className="my-games-button"
            onClick={async () => {
              try {
                await AddNewsGamesToGameList({ id: game.id });
                toast.success("✅ Added to My Games");
              } catch (err) {
                toast.error(`❌ Could not add to My Games${err.message ? `: ${err.message}` : ""}`);
              }
            }}
          >
            Add to My Games
            <span className="plus-icon" />
          </button>

          {/* Add to Wishlist */}
          <button
            className="wishlist-button"
            onClick={async () => {
              try {
                console.log("Wishlist clicked:", game.id);
                await AddToWishList({ id: game.id });
                toast.success("✅ Added to Wishlist");
              } catch (err) {
                toast.error(`❌ Could not add to Wishlist${err.message ? `: ${err.message}` : ""}`);
              }
            }}
          >
            Add to Wishlist
            <span className="gift-icon" />
          </button>

          {/* Add to Collection */}
          <button
            className="collection-button"
            onClick={openCollectionModal}
          >
            Add to Collection
            <span className="folder-icon" />
          </button>
        </div>

        {/* Ratings & Reviews */}
        {game.ratings && game.ratings.length > 0 && (
          <div className="ratings-section">
            <h3>Ratings & Reviews</h3>
            <p>Average Rating: {game.averageRating?.toFixed(2) || game.rating}</p>
            <ul>
              {game.ratings.map((rating) => (
                <li key={rating.id}>
                  <strong>{rating.title}</strong>: {rating.count} votes ({rating.percent}%)
                </li>
              ))}
            </ul>
            <p>Total Reviews: {game.reviews_count || 0}</p>
          </div>
        )}

        {/* Description */}
        {game.desc && (
          <div className="description-section">
            <h3>Description</h3>
            <div dangerouslySetInnerHTML={{ __html: game.desc }} />
          </div>
        )}

        {/* PRICE / DEALS Section */}
        <div className="price-section">
          <h3>Deals</h3>
          {priceLoading ? (
            <p>Loading prices...</p>
          ) : prices ? (
            <>
              {cheapestEver && (
                <p>Cheapest Price Ever: ${cheapestEver}</p>
              )}
              {topDeals.length > 0 ? (
                <ul>
                  {topDeals.map((deal) => (
                    <li key={deal.dealID}>
                      <a
                        href={`https://www.cheapshark.com/redirect?dealID=${deal.dealID}`}
                        target="_blank"
                        rel="noreferrer"
                      >
                        Store: {deal.storeName} {/* Now we see "Steam" etc. */}
                      </a>
                      <p>
                        Price: ${deal.price} | Retail: ${deal.retailPrice} | Savings: {deal.savings}%
                      </p>
                    </li>
                  ))}
                </ul>
              ) : (
                <p>No deals found.</p>
              )}
            </>
          ) : (
            <p>No price data available.</p>
          )}
        </div>

        {/* Official Website */}
        {game.website && (
          <div className="website-section">
            <h3>Official Website</h3>
            <a href={game.website} target="_blank" rel="noopener noreferrer">
              Visit Official Site
            </a>
          </div>
        )}

        {/* Additional Info */}
        <div className="info-section">
          <h3>Game Info</h3>
          <div className="info-row">
            <span className="info-label">Release Date:</span>
            <span className="info-value">{game.released || "TBA"}</span>
          </div>
          <div className="info-row">
            <span className="info-label">Developers:</span>
            <span className="info-value">
              {game.developers?.map((dev) => dev.name).join(", ") || "Unknown"}
            </span>
          </div>
          {game.metacritic_url && (
            <div className="info-row">
              <span className="info-label">Metacritic:</span>
              <a
                className="info-value link"
                href={game.metacritic_url}
                target="_blank"
                rel="noopener noreferrer"
              >
                {game.metacritic_url}
              </a>
            </div>
          )}
          {game.esrb_rating && (
            <div className="info-row">
              <span className="info-label">Age Rating:</span>
              <span className="info-value">{game.esrb_rating.name}</span>
            </div>
          )}
        </div>

        {/* Platforms & Genres */}
        <div className="platforms-genres-section">
          <h3>Platforms</h3>
          {platforms.length > 0 ? (
            <ul>
              {platforms.map((p, index) => {
                const { platform, released_at, requirements } = p;
                return (
                  <li key={index}>
                    <strong>{platform?.name || "Unknown"} </strong>
                    {released_at && <span>(Released: {released_at})</span>}
                    {requirements && (
                      <div className="requirements">
                        {requirements.minimum && (
                          <details>
                            <summary>Minimum</summary>
                            <pre>{requirements.minimum}</pre>
                          </details>
                        )}
                        {requirements.recommended && (
                          <details>
                            <summary>Recommended</summary>
                            <pre>{requirements.recommended}</pre>
                          </details>
                        )}
                      </div>
                    )}
                  </li>
                );
              })}
            </ul>
          ) : (
            <p>No platforms available.</p>
          )}

          <h3>Genres</h3>
          {genres.length > 0 ? (
            <ul>
              {genres.map((g) => (
                <li key={g.id}>{g.name}</li>
              ))}
            </ul>
          ) : (
            <p>No genres available.</p>
          )}
        </div>
      </div>

      {/* RIGHT COLUMN: Trailer & Additional Images */}
      <div className="right-column">
        <div className="trailer-section">
          {trailerLoading ? (
            <p>Loading trailer...</p>
          ) : trailers.length > 0 ? (
            <div id="youtube-player"></div>
          ) : (
            <p>No trailer available.</p>
          )}
        </div>

        <div className="image-section">
          {game.background_image ? (
            <div className="game-image">
              <img src={game.background_image} alt={game.name} />
              {game.background_image_additional && (
                <img
                  src={game.background_image_additional}
                  alt={`${game.name} additional`}
                />
              )}
            </div>
          ) : (
            <p>No image available.</p>
          )}
        </div>
      </div>

      {/* Collections Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Add to Collection</h3>
            <ul className="collection-list">
              {collections.map((col) => (
                <li key={col.id}>
                  <button onClick={() => chooseCollection(col)}>
                    {col.name}
                  </button>
                </li>
              ))}
            </ul>
            <div className="new-collection">
              <input
                ref={newNameRef}
                placeholder="New collection name"
              />
              <button
                onClick={handleCreateNew}
                disabled={saving}
              >
                {saving ? "Saving..." : "Create & Add"}
              </button>
            </div>
            <button
              className="close-modal"
              onClick={() => setShowModal(false)}
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default GameDetail;
