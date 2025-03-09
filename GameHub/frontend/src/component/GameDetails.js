import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchGameDetails } from '../service/NewsService';
import { AddNewsGamesToGameList, AddToWishList, SaveToCollection } from '../NewsHelper/AddNewsGamesToGameList';
import { fetchGameTrailers } from '../service/GameTrailersFromYoutubeService';

function GameDetail() {
  const { id } = useParams();
  const [game, setGame] = useState(null);
  const [gameLoading, setGameLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [trailers, setTrailers] = useState([]);
  const [trailerLoading, setTrailerLoading] = useState(true);

  // Fetch trailers from YouTube using the game name (or slug)
  const fetchTrailersForGame = async (gameName) => {
    try {
      const data = await fetchGameTrailers({ gameName });
      // Changed: use data.items instead of data
      setTrailers(data.items);
    } catch (error) {
      console.error('Error fetching trailer:', error);
    } finally {
      setTrailerLoading(false);
    }
  };

  // Fetch game details and then fetch the trailer based on the game name.
  useEffect(() => {
    async function fetchGame() {
      try {
        const data = await fetchGameDetails({ id });
        setGame(data);
        // Use the game name (or slug) to fetch the trailer.
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

  // New effect to load YouTube Iframe API and auto-play video
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
            'onReady': (event) => event.target.playVideo()
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
    <div className="game-detail">
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

      {game.ratings && game.ratings.length > 0 && (
        <div>
          <p>{game.ratings[0].title}</p>
          <p>Players added to their library: {game.ratings[0].count}</p>
        </div>
      )}

      <div>
        <h3>Platforms</h3>
        <ul>
          {game.platforms?.map((p) => (
            <li key={p.platform.id}>{p.platform.name}</li>
          ))}
        </ul>
      </div>

      <div>
        <h3>Genres</h3>
        <ul>
          {game.genres?.map((g) => (
            <li key={g.id}>{g.name}</li>
          ))}
        </ul>
      </div>

      <div className="trailer-section">
        <h3>Trailer</h3>
        {trailerLoading ? (
          <p>Loading trailer...</p>
        ) : trailers.length > 0 ? (
          // Replaced iframe with a div for the YouTube player
          <div id="youtube-player"></div>
        ) : (
          <p>No trailer available.</p>
        )}
      </div>
    </div>
  );
}

export default GameDetail;
