import React, { useEffect, useState, useRef } from "react";
import { fetchAllNews, getGenres, searchForGame } from "../service/NewsService";

function News() {
  const [genres, setGenres] = useState([]);
  const [selectedGenre, setSelectedGenre] = useState("");
  // newsData now holds an array of game objects
  const [newsData, setNewsData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const searchTimeoutRef = useRef(null);

  // Updated search handler with debounce
  const handleSearch = (e) => {
    const searchTerm = e.target.value;
    if (searchTimeoutRef.current) clearTimeout(searchTimeoutRef.current);
    searchTimeoutRef.current = setTimeout(async () => {
      try {
        // Call the search API; it should return an array of game results
        const results = await searchForGame(searchTerm);
        if (!results) {
          setNewsData([]);
          return;
        }
        setNewsData(results);
      } catch (error) {
        console.error(error);
        setError(error);
      }
    }, 500);
  };

  // Fetch genres when component mounts
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

  // Fetch default news when selectedGenre changes (only when not searching)
  useEffect(() => {
    async function getNews() {
      try {
        setLoading(true);
        const genreFilter = selectedGenre === "" ? null : selectedGenre;
        const data = await fetchAllNews(null, genreFilter);
        setNewsData(data);
      } catch (err) {
        console.error("Error fetching news:", err);
        setError(err);
      } finally {
        setLoading(false);
      }
    }
    // Only trigger if no search term is currently active (optional logic)
    getNews();
  }, [selectedGenre]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <div>
      <h1>News</h1>
      {/* Dropdown for genres */}
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
          onChange={handleSearch}
          type="text"
          placeholder="Search for Game"
          style={{ marginBottom: "1rem" }}
        />
      </div>
      {/* Display search results or default news */}
      <div>
        {newsData.map((item) => (
          <div
            key={item.slug}
            style={{
              border: "1px solid #ccc",
              margin: "1rem",
              padding: "1rem",
            }}
          >
            <h2>{item.name}</h2>
            <p>Released: {item.released}</p>
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
