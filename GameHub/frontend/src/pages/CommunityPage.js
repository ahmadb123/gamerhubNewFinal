import React, { useEffect, useState } from "react";
import { getAllNews } from "../service/PostNewsService";
import "../assests/CommunityPage.css"; // Add a CSS file for styling

function CommunityPage() {
  const [news, setNews] = useState([]);
  const [isFetching, setFetching] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchNews = async () => {
      const result = await getAllNews();
      if (result) {
        setNews(result);
        setFetching(false);
      } else {
        setError("Failed to load news");
        setFetching(false);
      }
    };
    fetchNews();
  }, []);

  const handleLike = (index) => {
    const updatedNews = [...news];
    if (!updatedNews[index].likes) {
      updatedNews[index].likes = 0;
    }
    updatedNews[index].likes += 1;
    setNews(updatedNews);
  };

  const handleAddComment = (index, comment) => {
    const updatedNews = [...news];
    if (!updatedNews[index].comments) {
      updatedNews[index].comments = [];
    }
    updatedNews[index].comments.push(comment);
    setNews(updatedNews);
  };

  return (
    <div className="community-page">
      <h1>Community Page</h1>
      {isFetching ? (
        <p>Loading...</p>
      ) : error ? (
        <p>{error}</p>
      ) : (
        <div className="news-container">
          {news.map((newsItem, index) => (
            <div key={index} className="news-card">
              <h3>{newsItem.contentText}</h3>
              <p>Shared by: <b>{newsItem.username}</b></p>
              <img
                src={newsItem.backgroundImage}
                alt={newsItem.name}
                className="news-image"
              />
              <p>Released: {newsItem.released}</p>
              <p>Time Shared: {new Date(newsItem.timeShared).toLocaleString()}</p>
              <div className="interaction-bar">
                <button onClick={() => handleLike(index)}>
                  👍 Like ({newsItem.likes || 0})
                </button>
                <button
                  onClick={() => {
                    const comment = prompt("Enter your comment:");
                    if (comment) {
                      handleAddComment(index, comment);
                    }
                  }}
                >
                  💬 Comment ({newsItem.comments?.length || 0})
                </button>
              </div>
              {newsItem.comments && newsItem.comments.length > 0 && (
                <div className="comments-section">
                  <h4>Comments:</h4>
                  <ul>
                    {newsItem.comments.map((comment, i) => (
                      <li key={i}>{comment}</li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default CommunityPage;
