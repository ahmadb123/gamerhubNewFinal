import React, { useEffect, useState } from "react";
import { fetchAllNews, getGenres } from "../service/NewsService";

function News() {
  const [genres, setGenres] = useState([]);
  const [selectedGenre, setSelectedGenre] = useState("");
  const [newsData, setNewsData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

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

  // Fetch news whenever selectedGenre changes
  useEffect(() => {
    async function getNews() {
      try {
        setLoading(true);
        // Use null if selectedGenre is an empty string
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
      {/* Display news items */}
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
                  <li key={g.name}>{g.name}</li>
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
