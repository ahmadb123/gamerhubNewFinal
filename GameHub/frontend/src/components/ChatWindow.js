import React, { useEffect, useState } from "react";
import {
  connectWebSocket,
  disconnectWebSocket,
  sendMessages,
  getSessionMessages,
  sendReadReceipt
} from "../service/DirectMessagesService"; 
import "../assests/DirectMessages.css";

function ChatWindow({ session, activeFriend, onClose }) {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const currentUser = localStorage.getItem("username");

  // Called whenever a new message or status update arrives
  const onMessageReceived = (rawMsg) => {
    const transformedMsg = {
      id: rawMsg.id,
      senderUsername: rawMsg.senderUsername,
      content: rawMsg.content,
      status: rawMsg.messageStatus, // e.g. SENT / DELIVERED / READ
      deliveredAt: rawMsg.deliveredAt,
      readAt: rawMsg.readAt
    };

    setMessages((prev) => {
      const existingIndex = prev.findIndex((m) => m.id === transformedMsg.id);
      if (existingIndex > -1) {
        const updated = [...prev];
        updated[existingIndex] = transformedMsg;
        return updated;
      }
      return [...prev, transformedMsg];
    });
    // if the receiever is in chat - 
    
  };

  // Load initial messages and connect WebSocket
  useEffect(() => {
    if (session && session.id) {
      // 1) Connect to the WebSocket
      connectWebSocket({ sessionId: session.id, onMessageReceived });

      // 2) Load previous messages from REST
      async function loadMessages() {
        try {
          const loaded = await getSessionMessages({ sessionId: session.id });
          setMessages(loaded);
          // 3) Mark them as READ once we've loaded them (the user sees them now)
          //    Only if the user is truly the "receiver" of these messages
          sendReadToBackend();
        } catch (error) {
          console.error("Error loading messages", error);
        }
      }
      loadMessages();
    }
    return () => disconnectWebSocket();
  }, [session]);

  // This function sends a read receipt to the server
  const sendReadToBackend = () => {
    if (!session || !session.id) return;
    const readPayload = {
      msgStatus: "READ",
      sessionId: session.id,
      receiverUsername: currentUser,
      timestamp: new Date().toISOString()
    };
    sendReadReceipt({ sessionId: session.id, receipt: readPayload });
  };

  // Sending new message
  const handleSend = () => {
    if (input.trim() === "") return;
    const messageObj = { content: input };
    sendMessages({ sessionId: session.id, message: messageObj });
    setInput("");
  };

  // Closing the chat
  const handleCloseChat = () => {
    if (onClose) onClose();
  };

  return (
    <div className="chat-window">
      <div className="chat-header">
        <h3>{activeFriend ? activeFriend.username : "Chat"}</h3>
      </div>
      <div className="messages-list">
        {messages.map((msg) => (
          <div
            key={msg.id} 
            className={`message ${
              msg.senderUsername === currentUser ? "sent" : "received"
            }`}
          >
            <strong>{msg.senderUsername}:</strong> {msg.content}
            {/* 
              Optionally show message.status, deliveredAt, readAt, etc.
              E.g. "READ at { msg.readAt }"
            */}
            {msg.status === "READ" && msg.readAt && (
              <div className="read-status">Read at {new Date(msg.readAt).toLocaleTimeString()}</div>
            )}
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
