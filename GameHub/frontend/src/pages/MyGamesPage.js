import React, { useEffect, useState } from "react";
import { getGameDetailById } from "../service/NewsService"; // Make sure this is correctly exported from your service
import {deleteGameFromMyGames} from "../NewsHelper/AddNewsGamesToGameList";
import '../assests/MyGames.css';

function MyGames() {
    const user = localStorage.getItem("username");
    const [savedGames, setSavedGames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // add delete handler
    const handleDelete = async (id) => {
        try {
            await deleteGameFromMyGames(id);
            setSavedGames(prev => prev.filter(game => game.id !== id));
        } catch (err) {
            console.error("Failed to delete game:", err);
        }
    };

    useEffect(() => {
        const getSavedGames = async () => {
            try {
                // Fetch saved games from your service
                const data = await getGameDetailById();
                setSavedGames(data);
            } catch (err) {
                console.error("Failed to fetch saved games:", err);
                setError(err);
            } finally {
                setLoading(false);
            }
        };
        getSavedGames();
    }, []);

    if (loading) return <div>Loading saved games...</div>;
    if (error) return <div>Error loading saved games: {error.message}</div>;

    return (
        <div className="saved-games-container">
            <h2 className="my-games-title">Games Library For {user}</h2>
            <h2>My Games</h2>
            <div className="saved-games-list">
                {savedGames.map((game) => (
                    <div key={game.id} className="saved-game-card">
                        <h3>{game.name}</h3>
                        <p>Released: {game.released}</p>
                        {game.background_image && (
                            <img
                                src={game.background_image}
                                alt={game.name}
                            />
                        )}
                        <button onClick={() => handleDelete(game.id)}>
                            Delete
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default MyGames;
