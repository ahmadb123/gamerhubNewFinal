import React, { useState, useEffect } from "react";
import { getGameClips } from "../service/XboxGameClips";
import "../assests/ClipsPage.css" // Import CSS for styling

function ClipsPage() {
    const [gameClips, setGameClips] = useState([]);
    const [isFetchingClips, setIsFetchingClips] = useState(true);
    const [error, setError] = useState(null);
    const [filter, setFilter] = useState(""); // Filter by game title
    const [currentPage, setCurrentPage] = useState(1); // Pagination
    const clipsPerPage = 10;

    const fetchGameClips = async () => {
        try {
            const clips = await getGameClips();
            setGameClips(clips);
        } catch (err) {
            console.error("Error fetching game clips:", err);
            setError("Failed to load game clips. Please try again.");
        } finally {
            setIsFetchingClips(false);
        }
    };

    useEffect(() => {
        fetchGameClips();
    }, []);

    const filteredClips = filter
        ? gameClips.filter((clip) =>
              clip.titleName?.toLowerCase().includes(filter.toLowerCase())
          )
        : gameClips;

    const totalPages = Math.ceil(filteredClips.length / clipsPerPage);
    const paginatedClips = filteredClips.slice(
        (currentPage - 1) * clipsPerPage,
        currentPage * clipsPerPage
    );

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    return (
        <div className="clips-container">
            <h2>Xbox Game Clips</h2>
            <div className="filter-bar">
                <label htmlFor="filter">Filter by Game:</label>
                <input
                    id="filter"
                    type="text"
                    value={filter}
                    onChange={(e) => setFilter(e.target.value)}
                    placeholder="Enter game title"
                />
            </div>
            {isFetchingClips ? (
                <p>Loading game clips...</p>
            ) : error ? (
                <p>{error}</p>
            ) : filteredClips.length === 0 ? (
                <p>No game clips found.</p>
            ) : (
                <>
                    <div className="clips-grid">
                        {paginatedClips.map((clip) => (
                            <div key={clip.gameClipId} className="clip-card">
                                <video
                                    src={clip.gameClipUris[0]?.uri}
                                    controls
                                    className="clip-video"
                                >
                                    Your browser does not support the video tag.
                                </video>
                                <div className="clip-info">
                                    <h3 className="clip-title">
                                        {clip.titleName || "Untitled Clip"}
                                    </h3>
                                    <p className="clip-date">
                                        Recorded on:{" "}
                                        {new Date(
                                            clip.dateRecorded
                                        ).toLocaleDateString()}
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="pagination">
                        {Array.from({ length: totalPages }, (_, index) => (
                            <button
                                key={index}
                                className={`pagination-button ${
                                    currentPage === index + 1 ? "active" : ""
                                }`}
                                onClick={() => handlePageChange(index + 1)}
                            >
                                {index + 1}
                            </button>
                        ))}
                    </div>
                </>
            )}
        </div>
    );
}

export default ClipsPage;
