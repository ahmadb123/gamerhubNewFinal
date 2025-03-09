// import React, { useEffect, useState } from "react";
// import { getGameDetailById } from "../service/NewsService"; // make sure this function is correctly exported

// const SavedGamesView = () => {
//   const [savedGames, setSavedGames] = useState([]);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);

//   useEffect(() => {
//     async function fetchSavedGames() {
//       try {
//         // Call the service to fetch saved game details
//         const data = await getGameDetailById();
//         setSavedGames(data);
//       } catch (err) {
//         console.error("Failed to fetch saved games:", err);
//         setError(err);
//       } finally {
//         setLoading(false);
//       }
//     }
//     fetchSavedGames();
//   }, []);

//   if (loading) return <div>Loading saved games...</div>;
//   if (error) return <div>Error loading saved games: {error.message}</div>;

//   return (
//     <div className="saved-games-container">
//       <h2>My Saved Games</h2>
//       <div className="saved-games-list">
//         {savedGames.map((game) => (
//           <div key={game.id} className="saved-game-card">
//             <h3>{game.name}</h3>
//             <p>Released: {game.released}</p>
//             {game.background_image && (
//               <img
//                 src={game.background_image}
//                 alt={game.name}
//                 style={{ width: "200px", height: "auto" }}
//               />
//             )}
//           </div>
//         ))}
//       </div>
//     </div>
//   );
// };

// export default SavedGamesView;
