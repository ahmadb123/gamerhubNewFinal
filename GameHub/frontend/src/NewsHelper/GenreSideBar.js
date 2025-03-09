import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { getGenres } from '../service/NewsService';
import '../NewsHelper/GenreSide.css';

function GenreSidebar() {
  const [genres, setGenres] = useState([]);
  const [expanded, setExpanded] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const selectedGenre = searchParams.get('genre') || '';

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

  const handleGenreClick = (slug) => {
    navigate(`/news?genre=${slug}`);
  };

  const displayedGenres = expanded ? genres : genres.slice(0, 8);

  return (
    <div className="genre-sidebar">
      <h2>Genres</h2>
      <ul>
        {displayedGenres.map((genre) => (
          <li
            key={genre.slug}
            className={selectedGenre === genre.slug ? 'active' : ''}
            onClick={() => handleGenreClick(genre.slug)}
          >
            <img
              src={genre.image_background}
              alt={genre.name}
            />
            <span>{genre.name}</span>
          </li>
        ))}
      </ul>
      {genres.length > 8 && (
        <button onClick={() => setExpanded(!expanded)}>
          {expanded ? 'Hide' : 'Show More'}
        </button>
      )}
    </div>
  );
}

export default GenreSidebar;