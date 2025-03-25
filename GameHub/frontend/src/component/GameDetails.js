import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchGameDetails } from '../service/NewsService';
import { AddNewsGamesToGameList, AddToWishList, SaveToCollection } from '../NewsHelper/AddNewsGamesToGameList';
import { fetchGameTrailers } from '../service/GameTrailersFromYoutubeService';
import '../component/GameDetails.css';

function GameDetail() {
  const { id } = useParams();
  const [game, setGame] = useState(null);
  const [gameLoading, setGameLoading] = useState(true);
  const [error, setError] = useState(null);

  const [trailers, setTrailers] = useState([]);
  const [trailerLoading, setTrailerLoading] = useState(true);

  // Fetch trailers from YouTube
  const fetchTrailersForGame = async (gameName) => {
    try {
      const data = await fetchGameTrailers({ gameName });
      setTrailers(data.items);
    } catch (error) {
      console.error('Error fetching trailer:', error);
    } finally {
      setTrailerLoading(false);
    }
  };

  // Fetch game details
  useEffect(() => {
    async function fetchGame() {
      try {
        const data = await fetchGameDetails({ id });
        setGame(data);

        // Use the game name to fetch trailer
        if (data && data.name) {
          fetchTrailersForGame(data.name);
        }
      } catch (err) {
        setError(err);
      } finally {
        setGameLoading(false);
      }
    }
    fetchGame();
  }, [id]);

  // Load YouTube Iframe API and auto-play video
  useEffect(() => {
    if (!trailerLoading && trailers.length > 0) {
      const loadPlayer = () => {
        new window.YT.Player('youtube-player', {
          height: '315',
          width: '560',
          videoId: trailers[0].id.videoId,
          playerVars: {
            autoplay: 1,
            mute: 0
          },
          events: {
            onReady: (event) => event.target.playVideo()
          }
        });
      };

      if (!window.YT) {
        const tag = document.createElement('script');
        tag.src = 'https://www.youtube.com/iframe_api';
        const firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
        window.onYouTubeIframeAPIReady = loadPlayer;
      } else {
        loadPlayer();
      }
    }
  }, [trailerLoading, trailers]);

  if (gameLoading) return <p>Loading game details...</p>;
  if (error) return <p>Error: {error.message}</p>;
  if (!game) return <p>No game data found.</p>;

  // Helper to format or default fields
  const releaseDate = game.released ? game.released : 'TBA';
  const developers = game.developers?.map((dev) => dev.name).join(', ');
  const platforms = game.platforms || [];
  const genres = game.genres || [];

  return (
    <div className="game-detail-container">
      {/* Left column: Title, buttons, ratings, description, website, etc. */}
      <div className='background-image-container'
        style={{ backgroundImage: `url(${game.background_image})` }}
      />
      <div className="left-column">
        <h1>{game.name}</h1>

        {/* Action Buttons */}
        <div className="btn-container">
          <button
            className="my-games-button"
            onClick={() => AddNewsGamesToGameList({ id: game.id })}
          >
            Add to My Games
            <span className="plus-icon" />
          </button>
          <button
            className="wishlist-button"
            onClick={() => AddToWishList(game)}
          >
            Add to Wishlist
            <span className="gift-icon" />
          </button>
          <button
            className="collection-button"
            onClick={() => SaveToCollection(game)}
          >
            Add to Collection
            <span className="folder-icon" />
          </button>
        </div>

        {/* Ratings & Reviews Section */}
        {game.ratings && game.ratings.length > 0 && (
          <div className="ratings-section">
            <h3>Ratings & Reviews</h3>
            <p>
              Average Rating: {game.averageRating?.toFixed(2) || game.rating}
            </p>
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

        {/* Game Description */}
        {game.desc && (
          <div className="description-section">
            <h3>Description</h3>
            <div dangerouslySetInnerHTML={{ __html: game.desc }} />
          </div>
        )}

        {/* Official Website Link */}
        {game.website && (
          <div className="website-section">
            <h3>Official Website</h3>
            <a href={game.website} target="_blank" rel="noopener noreferrer">
              Visit Official Site
            </a>
          </div>
        )}

        {/* Additional Info: Release date, developers, etc. */}
        <div className="info-section">
          <h3>Game Info</h3>
          <div className="info-row">
            <span className="info-label">Release Date:</span>
            <span className="info-value">{releaseDate}</span>
          </div>
          <div className="info-row">
            <span className="info-label">Developers:</span>
            <span className="info-value">{developers || 'Unknown'}</span>
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
          {/* Example: ESRB rating if it exists */}
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
                    <strong>{platform?.name || 'Unknown'} </strong>
                    {released_at && <span>(Release Date: {released_at})</span>}
                    {/* If you want to display min/recommended requirements */}
                    {requirements && (
                      <div className="requirements">
                        {requirements.minimum && (
                          <details>
                            <summary>Minimum Requirements</summary>
                            <pre>{requirements.minimum}</pre>
                          </details>
                        )}
                        {requirements.recommended && (
                          <details>
                            <summary>Recommended Requirements</summary>
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

      {/* Right column: trailer on top, main images below */}
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
    </div>
  );
}

export default GameDetail;
