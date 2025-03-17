import React, { useEffect, useState, useRef } from "react";
import { fetchAllNews, searchForGame, getGenres } from "../service/NewsService";
import { AddNewsGamesToGameList, AddToWishList, SaveToCollection } from "../NewsHelper/AddNewsGamesToGameList";
import { useSearchParams, useNavigate } from "react-router-dom";
import "../assests/News.css";
import { checkAccountType } from "../utility/CheckAccountType";

function News() {
  const [searchParams] = useSearchParams();
  const selectedGenre = searchParams.get('genre') || '';
  const [newsData, setNewsData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const textRef = useRef("");
  const navigate = useNavigate();
  const username = localStorage.getItem("username");
  const [genres, setGenres] = useState([]); // Added genres state
  const [hoveredGameSlug, setHoveredGameSlug] = useState(null);

  const handleKeyPress = (event) => {
    if (event.key === "Enter") handleSearch();
  };

  const handleImageClick = (game) => {
    navigate(`/news/game/${game.id}`);
  };

  const handleSearch = async () => {
    const inputValue = textRef.current.value.trim();
    
    try {
      if (!inputValue) {
        const data = await fetchAllNews(null, selectedGenre || null);
        setNewsData(data);
      } else {
        const results = await searchForGame(inputValue);
        setNewsData(results || []);
      }
    } catch (err) {
      setError(err);
    }
    textRef.current.value = "";
  };

  useEffect(() => {
    async function getNews() {
      try {
        setLoading(true);
        const data = await fetchAllNews(null, selectedGenre || null);
        setNewsData(data);
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    }
    getNews();
  }, [selectedGenre]);

  useEffect(() => {
    async function fetchGenreOptions() {
      try {
        const data = await getGenres();
        setGenres(data);
      } catch (err) {
        console.error("Error fetching genres:", err);
      }
    }
    fetchGenreOptions();
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <>
      <h1>News and Trending Games For {username}</h1>
      <div style={{ marginBottom: "1rem" }}>
        <label htmlFor="genre-select" style={{ marginRight: "0.5rem" }}>
          Filter by Genre:
        </label>
        <select
          id="genre-select"
          value={selectedGenre}
          onChange={(e) => navigate(e.target.value ? `/news?genre=${e.target.value}` : '/news')}
        >
          <option value="">All Genres</option>
          {genres.map((genre) => (
            <option key={genre.slug} value={genre.slug}>
              {genre.name}
            </option>
          ))}
        </select>
      </div>

      <div className="search-bar">
        <input
          ref={textRef}
          type="text"
          placeholder="Search for Game"
          onKeyDown={handleKeyPress}
        />
        <button onClick={handleSearch}>Search</button>
      </div>
      
      <div className="news-grid">
        {newsData.map((item) => (
          <div className="game-item" key={item.slug}>
            <img
              src={item.background_image}
              alt={item.name}
              style={{ width: "300px", height: "auto" }}
              onClick={() => handleImageClick({ id: item.id })}
              onMouseEnter={() => setHoveredGameSlug(item.slug)}
              onMouseLeave={() => setHoveredGameSlug(null)}
            />
            <div className="platforms">
              {checkAccountType(item.platforms.map((p) => p.platform.name))}
            </div>
            <h2>{item.name}</h2>
            {hoveredGameSlug === item.slug && (
              <div className="game-mini-details">
                <p>
                  <span className="label">Release:</span>
                  <span className="value">{item.released}</span>
                </p>
                <div className="genre-for-game">
                  <p>
                    <span className="label">Genres:</span>
                    <span className="value">
                      {item.genres.map((genre) => (
                        <span key={genre.slug} className="genre">
                          {genre.name}
                        </span>
                      ))}
                    </span>
                  </p>
                </div>
                <div className="game-rating">
                  <p>
                    <span className="label">Rating:</span>
                    <span className="value">
                      {item.rating > 0 ? item.rating : item.averageRating}
                    </span>
                  </p>
                </div>
              </div>
            )}

            <div className="btn-container">
            </div>
          </div>
        ))}
      </div>
    </>
  );
}

export default News;