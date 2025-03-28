import React, { useEffect, useState, useRef } from "react";
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
  const messagesEndRef = useRef(null);

  // Set the active flag when the chat window is open
  useEffect(() => {
    if (session && session.id) {
      localStorage.setItem(`chatActive_${currentUser}_${session.id}`, "true");
      return () => {
        localStorage.removeItem(`chatActive_${currentUser}_${session.id}`);
      };
    }
  }, [session, currentUser]);

  // Scroll to bottom on new messages
  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  // WebSocket message handler
  const onMessageReceived = (rawMsg) => {
    if (rawMsg.eventType === "READ_RECEIPT") {
      // Update messages that I sent when a read receipt is received
      setMessages(prevMessages =>
        prevMessages.map(msg => {
          if (msg.senderUsername === currentUser && msg.status !== "READ") {
            return {
              ...msg,
              status: "READ",
              readAt: rawMsg.readAt || rawMsg.timestamp
            };
          }
          return msg;
        })
      );
    } else {
      // A new incoming message
      const transformedMsg = {
        id: rawMsg.id,
        senderUsername: rawMsg.senderUsername,
        content: rawMsg.content,
        status: rawMsg.messageStatus,
        deliveredAt: rawMsg.deliveredAt,
        readAt: rawMsg.readAt
      };

      setMessages(prev => {
        const existingIndex = prev.findIndex(m => m.id === transformedMsg.id);
        if (existingIndex > -1) {
          const updated = [...prev];
          updated[existingIndex] = transformedMsg;
          return updated;
        }
        return [...prev, transformedMsg];
      });

      // Only trigger read receipt if I'm not the sender
      if (transformedMsg.senderUsername !== currentUser) {
        sendReadToBackend();
      }
    }
  };

  // Load messages and connect to WebSocket
  useEffect(() => {
    let isMounted = true;

    async function initializeWebSocket() {
      if (session?.id) {
        try {
          // Connect WebSocket and wait for connection
          await connectWebSocket({
            sessionId: session.id,
            onMessageReceived,
            onConnect: () => isMounted && setIsWsConnected(true), // Update connection status
            onDisconnect: () => isMounted && setIsWsConnected(false)
          });
          
          // Load messages AFTER connection is confirmed
          const loaded = await getSessionMessages({ sessionId: session.id });
          if (isMounted) setMessages(loaded);
          
          sendReadToBackend(); // Now safe to send
        } catch (error) {
          console.error("Error initializing WebSocket:", error);
        }
      }
    }

    initializeWebSocket();

    return () => {
      isMounted = false;
      disconnectWebSocket();
    };
  }, [session]);

  // Function to send a read receipt to the server if the user is active in the chat
  const sendReadToBackend = () => {
    if (!session || !session.id) return;
    // Check the active flag from localStorage
    const isActive = localStorage.getItem(`chatActive_${currentUser}_${session.id}`) === "true";
    if (!isActive) {
      console.log("User not active in chat, skipping read receipt");
      return;
    }
    const readPayload = {
      msgStatus: "READ",
      sessionId: session.id,
      receiverUsername: currentUser,
      timestamp: new Date().toISOString(),
      active: true
    };
    sendReadReceipt({ sessionId: session.id, receipt: readPayload });
    console.log("Sending read receipt");
  };

  const handleSend = () => {
    if (input.trim() === "") return;
    sendMessages({ sessionId: session.id, message: { content: input } });
    setInput("");
  };

  const handleCloseChat = () => {
    if (onClose) onClose();
  };

  return (
    <div className="chat-window">
      <div className="chat-header">
        <h3>{activeFriend ? activeFriend.username : "Chat"}</h3>
      </div>

      <div className="messages-list">
        {messages.map((msg, i) => {
          const isFromCurrentUser = msg.senderUsername === currentUser;
          const isLastMessage = i === messages.length - 1;
          const shouldShowStatus = isLastMessage && isFromCurrentUser;
          return (
            <div key={msg.id} className={`message ${isFromCurrentUser ? "sent" : "received"}`}>
              <strong>{isFromCurrentUser ? "You" : (activeFriend ? activeFriend.username : msg.senderUsername)}:</strong> {msg.content}
              {shouldShowStatus && (
                <div className="message-status">
                  {msg.status === "DELIVERED" && <div>Delivered</div>}
                  {msg.status === "READ" && msg.readAt && (
                    <div>Read at {new Date(msg.readAt).toLocaleTimeString()}</div>
                  )}
                </div>
              )}
            </div>
          );
        })}
        <div ref={messagesEndRef} />
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
