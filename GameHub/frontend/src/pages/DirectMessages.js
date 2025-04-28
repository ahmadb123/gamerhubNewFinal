import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getAllFriends } from "../service/AddFriendService";
import { getSessionOrCreate } from "../service/DirectMessagesService";
import ChatWindow from "../components/ChatWindow";
import "../assests/DirectMessages.css";
import { useNavigate } from "react-router-dom";
import queryString from "query-string";

function DirectMessages() {
  const [friends, setFriends] = useState([]);
  const [activeSession, setActiveSession] = useState(null);
  const [activeFriend, setActiveFriend] = useState(null);
  const nav = useNavigate();
  const jwt = localStorage.getItem("jwtToken");

  // read possible friendId from the URL ?friendId=7&ref=news:22
  const params = queryString.parse(location.search);
  const friendIdFromQuery = params.friendId;
  const refParam = params.ref;

  // If we have a friendId, auto-start a conversation
  useEffect(() => {
    if (friendIdFromQuery) {
      autoStartConversation(friendIdFromQuery);
    }
  }, [friendIdFromQuery]);

  const autoStartConversation = async (friendId) => {
    try {
      const session = await getSessionOrCreate({ userTwoId: friendId });
      setActiveSession(session);

      // The actual friend object might come from your friends list, or minimal placeholder
      const friend = { userId: friendId, username: "Publisher" };
      setActiveFriend(friend);
    } catch (error) {
      console.error("Error auto-starting conversation:", error);
    }
  };

  // Load friend list
  useEffect(() => {
    async function fetchFriends() {
      try {
        const fetchedFriends = await getAllFriends();
        setFriends(fetchedFriends);
      } catch (error) {
        console.error("Error fetching friends list", error);
      }
    }
    fetchFriends();
  }, []);

  // Start or retrieve session on friend click
  const handleFriendClick = async (friend) => {
    try {
      const session = await getSessionOrCreate({ userTwoId: friend.userId });
      console.log("user two id:" + friend.userId);
      setActiveSession(session);
      setActiveFriend(friend);
    } catch (error) {
      console.error("Error starting conversation:", error);
      toast.error("Error starting conversation");
    }
  };

  // Close the chat window
  const handleCloseChat = () => {
    setActiveSession(null);
    setActiveFriend(null);
  };

  // If user wants to exit DM entirely
  const handleCloseEntireDirectMessages = () => {
    if (jwt) {
      nav("/main");
    } else {
      nav("/login");
    }
  };

  return (
    <div className="direct-messages-container">
      <div className="sidebar">
        <h2>Chats</h2>
        <div className="close-window">
          <button onClick={handleCloseEntireDirectMessages}></button>
        </div>

        <div className="friends-list">
          {friends.map((friend) => (
            <div
              key={friend.userId}
              className={`friend-item ${activeFriend?.userId === friend.userId ? "active" : ""}`}
              onClick={() => handleFriendClick(friend)}
            >
              {friend.username}
            </div>
          ))}
        </div>
      </div>

      <div className="chat-area">
        {activeSession ? (
          <ChatWindow
            session={activeSession}
            activeFriend={activeFriend}
            onClose={handleCloseChat}
          />
        ) : (
          <div className="empty-chat">
            <p>Select a friend to start a conversation</p>
            {/* If there's a reference param, we can display it */}
            {refParam && <p>Reply reference: {refParam}</p>}
          </div>
        )}
      </div>
    </div>
  );
}

export default DirectMessages;
