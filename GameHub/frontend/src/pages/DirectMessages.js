/**
 * direct messages page->
 */

/* 
    * things to import/fetch/get:
    * 1.user data
    * 2.Show the friend list on your DM page with a “+ Start New Convo” button.
    * 3.When the user clicks on a friend, either
    * 4.check if there’s an existing conversation (by calling a REST endpoint) or create a new one.
    * once you have the session id, open a chat window for that friend.
    * When the chat window opens, 
    * use the REST endpoint (/api/direct-messages/{sessionId}) to load previous messages from that conversation.
    * Real-Time Messaging with WebSockets:
    * Use a library such as SockJS together with StompJS to connect to your backend WebSocket endpoint (configured in your backend at /ws).
    * Subscribe to the topic (e.g., /topic/direct-message/{sessionId}) to listen for new messages.
    * When a user sends a message, publish it to the destination
    * (e.g., /app/direct-message/{sessionId}/send). The backend will then broadcast it to all subscribers.
*/




import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getAllFriends } from "../service/AddFriendService";
import { getSessionOrCreate } from "../service/DirectMessagesService";
import ChatWindow from "../components/ChatWindow";
import '../assests/DirectMessages.css';
import { useNavigate } from 'react-router-dom';

function DirectMessages() {
  const [friends, setFriends] = useState([]);
  const [activeSession, setActiveSession] = useState(null);
  const [activeFriend, setActiveFriend] = useState(null);
  const nav = useNavigate();
  const jwt = localStorage.getItem('jwtToken');

  // Fetch friends once when the component mounts.
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

  // When a friend is clicked, either get the existing session or create a new one.
  const handleFriendClick = async (friend) => {
    try {
      const session = await getSessionOrCreate({ userTwoId: friend.userId });
      setActiveSession(session);
      setActiveFriend(friend);
    } catch (error) {
      console.error("Error starting conversation:", error);
      toast.error("Error starting conversation");
    }
  };

  // Add handler to close the chat window.
  const handleCloseChat = () => {
    setActiveSession(null);
    setActiveFriend(null);
  };

  const handleCloseEntireDirectMessages = () =>{
    if(jwt){
      nav('/main');
    }else{
      nav('login');
    } 
  }

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
          </div>
        )}
      </div>
    </div>
  );
}

export default DirectMessages;

