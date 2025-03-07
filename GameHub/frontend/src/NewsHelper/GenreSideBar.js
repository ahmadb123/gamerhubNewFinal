import React, { useState } from 'react';

import '../NewsHelper/GenreSide.css';
function GenreSidebar({ genres, onSelectGenre, selectedGenre }) {
  const [expanded, setExpanded] = useState(false);

  const displayedGenres = expanded ? genres : genres.slice(0, 8);

  return (
    <div className="genre-sidebar">
      <h2>Genres</h2>
      <ul>
        {displayedGenres.map((genre) => (
          <li
            key={genre.slug}
            className={selectedGenre === genre.slug ? 'active' : ''}
            onClick={() => onSelectGenre(genre.slug)}
          >
            <img
              src={genre.image_background}
              alt={genre.name}
              /* 

              */
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
