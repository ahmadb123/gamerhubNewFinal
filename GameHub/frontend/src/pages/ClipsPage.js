// import React, { useState, useEffect } from "react";
// import { getGameClips } from "../service/XboxGameClips";
// import "../assests/ClipsPage.css" // Import CSS for styling
// import { toast } from "react-toastify";
// import{ postNews } from "../service/PostNewsService"; // Import the postNews function
// function ClipsPage() {
//     const [gameClips, setGameClips] = useState([]);
//     const [isFetchingClips, setIsFetchingClips] = useState(true);
//     const [error, setError] = useState(null);
//     const [filter, setFilter] = useState(""); // Filter by game title
//     const [currentPage, setCurrentPage] = useState(1); // Pagination
//     const clipsPerPage = 10;

//     const fetchGameClips = async () => {
//         try {
//             const clips = await getGameClips();
//             setGameClips(clips);
//         } catch (err) {
//             console.error("Error fetching game clips:", err);
//             setError("Failed to load game clips. Please try again.");
//         } finally {
//             setIsFetchingClips(false);
//         }
//     };

//     const shareWithCommunity = async (gameClips) => {
//         // Logic to share the clip with the community
//         const contextText = "tst 1";
//         const sharedClipId = gameClips.gameClipId;
//         try{
//             const result = await postNews(contextText, null, sharedClipId);
//             if(result.success){
//                 toast.success("Clip shared successfully!");
//             }else{
//                 toast.error("Failed to share clip.");
//             }
//         }catch(err){
//             console.error("Error sharing clip:", err);
//             toast.error("Error sharing clip.");
//         }
//     };

//     useEffect(() => {
//         fetchGameClips();
//     }, []);

//     const filteredClips = filter
//         ? gameClips.filter((clip) =>
//               clip.titleName?.toLowerCase().includes(filter.toLowerCase())
//           )
//         : gameClips;

//     const totalPages = Math.ceil(filteredClips.length / clipsPerPage);
//     const paginatedClips = filteredClips.slice(
//         (currentPage - 1) * clipsPerPage,
//         currentPage * clipsPerPage
//     );

//     const handlePageChange = (pageNumber) => {
//         setCurrentPage(pageNumber);
//     };

//     return (
//         <div className="clips-container">
//             <h2>Xbox Game Clips</h2>
//             <div className="filter-bar">
//                 <label htmlFor="filter">Filter by Game:</label>
//                 <input
//                     id="filter"
//                     type="text"
//                     value={filter}
//                     onChange={(e) => setFilter(e.target.value)}
//                     placeholder="Enter game title"
//                 />
//             </div>
//             {isFetchingClips ? (
//                 <p>Loading game clips...</p>
//             ) : error ? (
//                 <p>{error}</p>
//             ) : filteredClips.length === 0 ? (
//                 <p>No game clips found.</p>
//             ) : (
//                 <>
//                     <div className="clips-grid">
//                         {paginatedClips.map((clip) => (
//                             <div key={clip.gameClipId} className="clip-card">
//                                 <video
//                                     src={clip.gameClipUris[0]?.uri}
//                                     controls
//                                     className="clip-video"
//                                 >
//                                     Your browser does not support the video tag.
//                                 </video>
//                                 <div className="clip-info">
//                                     <h3 className="clip-title">
//                                         {clip.titleName || "Untitled Clip"}
//                                     </h3>
//                                     <p className="clip-date">
//                                         Recorded on:{" "}
//                                         {new Date(
//                                             clip.dateRecorded
//                                         ).toLocaleDateString()}
//                                     </p>
//                                     <button onClick={() => shareWithCommunity(clip)}>
//                                         Share with Community
//                                     </button>
//                                 </div>
//                             </div>
//                         ))}
//                     </div>
//                     <div className="pagination">
//                         {Array.from({ length: totalPages }, (_, index) => (
//                             <button
//                                 key={index}
//                                 className={`pagination-button ${
//                                     currentPage === index + 1 ? "active" : ""
//                                 }`}
//                                 onClick={() => handlePageChange(index + 1)}
//                             >
//                                 {index + 1}
//                             </button>
//                         ))}
//                     </div>
//                 </>
//             )}
//         </div>
//     );
// }

// export default ClipsPage;


// ClipsPage.js
import React, { useState, useEffect } from "react";
import { getGameClips } from "../service/XboxGameClips";
import { postNews }      from "../service/PostNewsService";
import { toast }         from "react-toastify";
import "../assests/ClipsPage.css";

export default function ClipsPage() {
  const [clips, setClips]         = useState([]);
  const [loading, setLoading]     = useState(true);
  const [error, setError]         = useState(null);
  const [filter, setFilter]       = useState("");
  const [page, setPage]           = useState(1);
  const clipsPerPage              = 10;

  // 1) Fetch all clips on mount
  useEffect(() => {
    (async () => {
      try {
        const data = await getGameClips();
        setClips(data);
      } catch (err) {
        console.error(err);
        setError("Failed to load game clips.");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  // 2) Share a clip: send contentText + null + clipId
  const shareWithCommunity = async (clip) => {
    const text        = `Check out my clip from ${clip.titleName}!`;
    const clipId      = clip.gameClipId;
    const clipUrl     = clip.gameClipUris?.[0]?.uri;
    try {
      const result = await postNews(text ,null, clipId, clipUrl);
      if (result.success) {
        toast.success("Clip shared successfully!");
      } else {
        toast.error("Failed to share clip.");
      }
    } catch (err) {
      console.error(err);
      toast.error("Error sharing clip.");
    }
  };

  if (loading) return <p>Loading game clips…</p>;
  if (error)   return <p className="error">{error}</p>;

  // 3) Filter + paginate
  const filtered = filter
    ? clips.filter(c =>
        c.titleName?.toLowerCase().includes(filter.toLowerCase())
      )
    : clips;

  const totalPages = Math.ceil(filtered.length / clipsPerPage);
  const pageClips  = filtered.slice(
    (page - 1) * clipsPerPage,
    page * clipsPerPage
  );

  return (
    <div className="clips-container">
      <h2>Xbox Game Clips</h2>

      <div className="filter-bar">
        <label htmlFor="filter">Filter by Game:</label>
        <input
          id="filter"
          type="text"
          value={filter}
          onChange={e => { setFilter(e.target.value); setPage(1); }}
          placeholder="Enter game title"
        />
      </div>

      {pageClips.length === 0 ? (
        <p>No game clips found.</p>
      ) : (
        <>
          <div className="clips-grid">
            {pageClips.map(clip => (
              <div key={clip.gameClipId} className="clip-card">
                <video
                  className="clip-video"
                  src={clip.gameClipUris?.[0]?.uri}
                  controls
                >
                  Your browser does not support the video tag.
                </video>
                <div className="clip-info">
                  <h3>{clip.titleName || "Untitled Clip"}</h3>
                  <p>
                    Recorded:{" "}
                    {new Date(clip.dateRecorded).toLocaleDateString()}
                  </p>
                  <button onClick={() => shareWithCommunity(clip)}>
                    Share with Community
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="pagination">
            {Array.from({ length: totalPages }, (_, i) => (
              <button
                key={i}
                className={page === i + 1 ? "active" : ""}
                onClick={() => setPage(i + 1)}
              >
                {i + 1}
              </button>
            ))}
          </div>
        </>
      )}
    </div>
  );
}
