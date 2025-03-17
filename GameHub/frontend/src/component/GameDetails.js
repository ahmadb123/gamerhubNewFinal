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

  return (
    <div className="game-detail-container">
      {/* Left column: Title, buttons, ratings, platforms, genres, etc. */}
      <div className="left-column">
        <h1>{game.name}</h1>

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

        {/* Example rating info */}
        {game.ratings && game.ratings.length > 0 && (
          <div className="ratings-section">
            <p>{game.ratings[0].title}</p>
            <p>Players added to their library: {game.ratings[0].count}</p>
          </div>
        )}

        <div className="platforms-genres-section">
          <h3>Platforms</h3>
          <ul>
            {game.platforms?.map((p) => (
              <li key={p.platform.id}>{p.platform.name}</li>
            ))}
          </ul>

          <h3>Genres</h3>
          <ul>
            {game.genres?.map((g) => (
              <li key={g.id}>{g.name}</li>
            ))}
          </ul>
        </div>
      </div>

      {/* Right column: trailer on top, main image below */}
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
            <div className='game-image'>
              <img src={game.background_image} alt={game.name} />
              <img src={game.background_image_additional} alt={game.name} />
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
