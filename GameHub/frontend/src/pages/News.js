import React, { useEffect, useState, useRef } from "react";
import { fetchAllNews, getGenres, searchForGame } from "../service/NewsService";
import { AddNewsGamesToGameList, AddToWishList, SaveToCollection } from "../NewsHelper/AddNewsGamesToGameList";
import SavedGamesFolder from "../component/SavedGameFolder";
import GenreSidebar from "../NewsHelper/GenreSideBar";
import "../assests/News.css";

function News() {
  const [genres, setGenres] = useState([]);
  const [selectedGenre, setSelectedGenre] = useState("");
  const [newsData, setNewsData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const username = localStorage.getItem("username");
  const textRef = useRef("");


  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      handleSearch();
    }
  };

  const handleSearch = async () => {
    const inputValue = textRef.current.value.trim();
    if (!inputValue) {
      try {
        const data = await fetchAllNews(null, selectedGenre || null);
        setNewsData(data);
      } catch (err) {
        console.error(err);
        setError(err);
      } finally {
        textRef.current.value = "";
      }
      return;
    }

    try {
      const results = await searchForGame(inputValue);
      setNewsData(results || []);
    } catch (err) {
      console.error(err);
      setError(err);
    } finally {
      textRef.current.value = "";
    }
  };

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

  useEffect(() => {
    async function getNews() {
      try {
        setLoading(true);
        const data = await fetchAllNews(null, selectedGenre || null);
        setNewsData(data);
      } catch (err) {
        console.error("Error fetching news:", err);
        setError(err);
      } finally {
        setLoading(false);
      }
    }
    getNews();
  }, [selectedGenre]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (

    <div className="news-page">
      {/* Left sidebar */}
      <GenreSidebar
        genres={genres}
        selectedGenre={selectedGenre}
        onSelectGenre={(slug) => setSelectedGenre(slug)}
      />

      {/* Main content area */}
      <div className="news-content">
        <h1>News and Trending Games For {username} </h1>
        <SavedGamesFolder />
        {/* If you still want the dropdown, keep it here or remove */}
        <div style={{ marginBottom: "1rem" }}>
          <label htmlFor="genre-select" style={{ marginRight: "0.5rem" }}>
            Filter by Genre:
          </label>
          <select
            id="genre-select"
            value={selectedGenre}
            onChange={(e) => setSelectedGenre(e.target.value)}
          >
            <option value="">All Genres</option>
            {genres.map((genre) => (
              <option key={genre.slug} value={genre.slug}>
                {genre.name}
              </option>
            ))}
          </select>
        </div>

        {/* Search bar */}
        <div className="search-bar">
          <input
            ref={textRef}
            type="text"
            placeholder="Search for Game"
            onKeyDown={handleKeyPress}
          />
          <button onClick={handleSearch}>Search</button>
        </div>

        {/* Display news */}
        {newsData.map((item) => (
          <div className="game-item" key={item.slug}>
            <h2>{item.name}</h2>
            <p>Released: {item.released}</p>

            <div className="btn-container">
              <button
                className="my-games-button"
                onClick={() => AddNewsGamesToGameList({id : item.id})}
              >
                Add to My games
                <span className="plus-icon" />
              </button>
              <button
                className="wishlist-button"
                onClick={() => AddToWishList({ item })}
              >
                Add to Wishlist
                <span className="gift-icon" />
              </button>
              <button
                className="collection-button"
                onClick={() => SaveToCollection({ item })}
              >
                Save to Collection
                <span className="folder-icon" />
              </button>
            </div>

            <img
              src={item.background_image}
              alt={item.name}
              style={{ width: "300px", height: "auto" }}
            />

            <div>
              <strong>Platforms:</strong>
              <ul>
                {item.platforms.map((p) => (
                  <li key={p.platform.slug}>{p.platform.name}</li>
                ))}
              </ul>
            </div>
            <div>
              <strong>Genres:</strong>
              <ul>
                {item.genres.map((g) => (
                  <li key={g.slug}>{g.name}</li>
                ))}
              </ul>
            </div>
            <div>
              <strong>Screenshots:</strong>
              <div style={{ display: "flex", gap: "0.5rem", overflowX: "auto" }}>
                {item.short_screenshots.map((shot, index) => (
                  <img
                    key={index}
                    src={shot.image}
                    alt={`${item.name} screenshot ${index + 1}`}
                    style={{ width: "150px", height: "auto" }}
                  />
                ))}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default News;
