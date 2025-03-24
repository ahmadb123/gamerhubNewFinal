import React, { useEffect, useState } from "react";
import {
  connectWebSocket,
  disconnectWebSocket,
  sendMessages,
  getSessionMessages
} from "../service/DirectMessagesService";
import '../assests/DirectMessages.css';

function ChatWindow({ session, activeFriend, onClose }) {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");

  // Retrieve the current user's username from localStorage.
  const currentUser = localStorage.getItem("username"); 

  // Callback that fires when a new message is received via WebSocket.
  const onMessageReceived = (rawMsg) => {
    const transformedMsg = {
      senderUsername: rawMsg.senderUsername, 
      content: rawMsg.content
    };
    // Append the new message to the state.
    setMessages((prev) => [...prev, transformedMsg]);
  };

  useEffect(() => {
    if (session && session.id) {
      // Connect to the WebSocket for real-time updates.
      connectWebSocket({ sessionId: session.id, onMessageReceived });
      
      // Load previous messages via REST.
      async function loadMessages() {
        try {
          const loadedMessages = await getSessionMessages({ sessionId: session.id });
          setMessages(loadedMessages);
        } catch (error) {
          console.error("Failed to load messages", error);
        }
      }
      loadMessages();
    }
    return () => disconnectWebSocket();
  }, [session]);

  const handleSend = () => {
    if (input.trim() === "") return;
    const messageObj = { content: input };
    // Send the message via WebSocket.
    sendMessages({ sessionId: session.id, message: messageObj });
    setInput("");
  };

  // Handler for closing chat window.
  const handleCloseChat = () => {
    if (onClose) {
      onClose();
    }
  };

  return (
    <div className="chat-window">
      <div className="chat-header">
        <h3>{activeFriend ? activeFriend.username : "Chat"}</h3>
      </div>
      <div className="messages-list">
        {messages.map((msg, idx) => (
          <div
            key={idx}
            className={`message ${
              msg.senderUsername === currentUser ? "sent" : "received"
            }`}
          >
            <strong>{msg.senderUsername}:</strong> {msg.content}
          </div>
        ))}
      </div>
      <div className="message-input">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Type your message..."
        />
        <button onClick={handleSend}>Send</button>
        <button onClick={handleCloseChat}>Close</button>
      </div>
    </div>
  );
}

export default ChatWindow;
