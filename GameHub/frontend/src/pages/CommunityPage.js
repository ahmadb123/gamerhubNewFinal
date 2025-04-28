import React, { useEffect, useState, useRef } from "react";
import { useLocation } from "react-router-dom";
import {
  getAllNews,
  replyToPublisher,
  likePost,
  addComment,
  getComments
} from "../service/PostNewsService";
import "../assests/CommunityPage.css";

export default function CommunityPage() {
  const [news, setNews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const currentUser = localStorage.getItem("username");
  const postRefs = useRef({});
  const { search } = useLocation();
  const targetPostId = new URLSearchParams(search).get("postId");

  // ── Load all posts with initial like/comment counts ─────────────────────────
  useEffect(() => {
    (async () => {
      try {
        const items = await getAllNews();
        if (!Array.isArray(items)) {
          throw new Error("Unexpected response format");
        }
        const withMeta = items.map(post => ({
          ...post,
          likes:         post.likesCount   || 0,
          comments:      [],                // we'll fetch these on‐demand
          commentsCount: post.commentsCount || 0,
          showComments:  false
        }));
        setNews(withMeta);
      } catch (e) {
        console.error(e);
        setError("Failed to load posts");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  // ── Scroll into view if ?postId=… is present ────────────────────────────────
  useEffect(() => {
    if (!loading && targetPostId && postRefs.current[targetPostId]) {
      setTimeout(() =>
        postRefs.current[targetPostId].scrollIntoView({ behavior: "smooth", block: "start" }),
      100);
    }
  }, [loading, targetPostId, news]);

  // ── Like/unlike a post ──────────────────────────────────────────────────────
  const handleLike = async (postId, idx) => {
    try {
      const totalLikes = await likePost(postId);
      setNews(n =>
        n.map((p, i) =>
          i === idx ? { ...p, likes: totalLikes } : p
        )
      );
    } catch (e) {
      console.error(e);
      alert("Could not like post");
    }
  };

  // ── Add a new comment ───────────────────────────────────────────────────────
  const handleComment = async (postId, idx) => {
    const text = prompt("Enter your comment:");
    if (!text) return;
    try {
      const { user, text: commentText, time } = await addComment(postId, text);
      setNews(n =>
        n.map((p, i) =>
          i === idx
            ? {
                ...p,
                comments:      [{ user, text: commentText, time }, ...p.comments],
                commentsCount: p.commentsCount + 1
              }
            : p
        )
      );
    } catch (e) {
      console.error(e);
      alert("Could not add comment");
    }
  };

  // ── Toggle and load comments for a post ────────────────────────────────────
  const handleToggleComments = async (postId, idx) => {
    const post = news[idx];
    if (!post.showComments && post.comments.length === 0) {
      try {
        const cs = await getComments(postId);
        setNews(n =>
          n.map((p, i) =>
            i === idx
              ? {
                  ...p,
                  comments:      cs,
                  commentsCount: cs.length,
                  showComments:  true
                }
              : p
          )
        );
      } catch (e) {
        console.error(e);
      }
    } else {
      setNews(n =>
        n.map((p, i) =>
          i === idx ? { ...p, showComments: !p.showComments } : p
        )
      );
    }
  };

  // ── Reply via DM ───────────────────────────────────────────────────────────
  const handleReply = async post => {
    const replyMessage = prompt("Enter your reply message:");
    if (!replyMessage) return;
    const body = {
      newsId:      post.id,
      replyMessage,
      postImage:   post.backgroundImage,
      contentText: post.contentText
    };
    const res = await replyToPublisher(body);
    if (res.success) alert("Reply sent!");
  };

  if (loading) return (
    <div className="loading-spinner">
      <div className="spinner"></div>
      <p>Loading community posts...</p>
    </div>
  );
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="community-page">
      <h1>Community Page</h1>
      <div className="news-container">
        {news.map((post, idx) => (
          <div
            key={post.id}
            ref={el => (postRefs.current[post.id] = el)}
            className="news-card"
          >
            <h3 className="news-title">{post.contentText}</h3>

            {post.sharedClipsUrl ? (
              <video
                src={post.sharedClipsUrl}
                controls
                className="news-video"
              />
            ) : post.backgroundImage ? (
              <img
                src={post.backgroundImage}
                alt=""
                className="news-image"
              />
            ) : null}

            {post.released && (
              <p className="news-detail">Released: {post.released}</p>
            )}

            <p className="news-detail">
              Shared on: {new Date(post.timeShared).toLocaleString()} by{" "}
              <b>{post.username}</b>
            </p>

            <div className="interaction-bar">
              <button
                className="like-button"
                onClick={() => handleLike(post.id, idx)}
              >
                👍 Like ({post.likes})
              </button>
              <button
                className="comment-button"
                onClick={() => handleToggleComments(post.id, idx)}
              >
                💬 Comments ({post.commentsCount})
              </button>
              {post.username !== currentUser && (
                <button
                  className="reply-button"
                  onClick={() => handleReply(post)}
                >
                  💭 Reply
                </button>
              )}
            </div>

            {post.showComments && (
              <div className="comments-section">
                <h4>Comments:</h4>
                <ul>
                  {post.comments.map((c, i) => (
                    <li key={i}>
                      <strong>{c.user}</strong> (
                      {new Date(c.time).toLocaleString()}): {c.text}
                    </li>
                  ))}
                </ul>
                <button
                  className="add-comment-button"
                  onClick={() => handleComment(post.id, idx)}
                >
                  + Add Comment
                </button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
