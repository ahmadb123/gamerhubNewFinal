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
  const [input, setInput]       = useState("");
  const currentUser             = localStorage.getItem("username");
  const messagesEndRef          = useRef(null);

  // Render each line; if it’s “[...Reference] URL” show video if it’s an .mp4/.webm/.ogg
  function renderMessageContent(content) {
    return content.split("\n").map((line, idx) => {
      const urlMatch = line.match(/(https?:\/\/[^\]\s]+)/);
      if (urlMatch) {
        const url   = urlMatch[1];
        const text  = line.replace(urlMatch[0], "").replace(/[\[\]]/g, "").trim();
        const isVideo = /\.(mp4|webm|ogg)(\?|$)/i.test(url);
  
        return (
          <div key={idx} style={{ margin: "0.5rem 0" }}>
            {text && <p style={{ margin: 0 }}>{text}</p>}
            {isVideo ? (
              <video
                src={url}
                controls
                style={{
                  display: "block",
                  maxWidth: "300px",
                  borderRadius: "8px",
                  cursor: "pointer"
                }}
                onClick={() => {
                  const postIdMatch = content.match(/#(\d+)/);
                  if (postIdMatch) {
                    window.location.href = `/community?postId=${postIdMatch[1]}`;
                  }
                }}
              />
            ) : (
              <img
                src={url}
                alt=""
                style={{
                  display: "block",
                  maxWidth: "200px",
                  borderRadius: "8px",
                  cursor: "pointer"
                }}
                onClick={() => {
                  const postIdMatch = content.match(/#(\d+)/);
                  if (postIdMatch) {
                    window.location.href = `/community?postId=${postIdMatch[1]}`;
                  }
                }}
              />
            )}
          </div>
        );
      }
  
      return <p key={idx} style={{ margin: 0 }}>{line}</p>;
    });
  }
  

  // Scroll on new messages
  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  // Connect WS & load history
  useEffect(() => {
    if (!session?.id) return;
    let mounted = true;

    connectWebSocket({
      sessionId: session.id,
      onMessageReceived: (raw) => {
        if (raw.eventType === "READ_RECEIPT") {
          setMessages((prev) =>
            prev.map((m) =>
              m.senderUsername === currentUser && m.status !== "READ"
                ? { ...m, status: "READ", readAt: raw.readAt || raw.timestamp }
                : m
            )
          );
        } else {
          const msg = {
            id: raw.id,
            senderUsername: raw.senderUsername,
            content: raw.content,
            status: raw.messageStatus,
            deliveredAt: raw.deliveredAt,
            readAt: raw.readAt
          };
          setMessages((prev) => {
            const idx = prev.findIndex((x) => x.id === msg.id);
            if (idx > -1) {
              const copy = [...prev];
              copy[idx] = msg;
              return copy;
            }
            return [...prev, msg];
          });
          // if I received it, send READ receipt
          if (msg.senderUsername !== currentUser) {
            sendReadReceipt({
              sessionId: session.id,
              receipt: {
                msgStatus: "READ",
                sessionId: session.id,
                receiverUsername: currentUser,
                timestamp: new Date().toISOString(),
                active: true
              }
            });
          }
        }
      }
    });

    getSessionMessages({ sessionId: session.id })
      .then((loaded) => {
        if (mounted) {
          setMessages(loaded);
          // initial read
          sendReadReceipt({
            sessionId: session.id,
            receipt: {
              msgStatus: "READ",
              sessionId: session.id,
              receiverUsername: currentUser,
              timestamp: new Date().toISOString(),
              active: true
            }
          });
        }
      })
      .catch(console.error);

    return () => {
      mounted = false;
      disconnectWebSocket();
    };
  }, [session, currentUser]);

  const handleSend = () => {
    if (!input.trim()) return;
    sendMessages({ sessionId: session.id, message: { content: input } });
    setInput("");
  };

  return (
    <div className="chat-window">
      <div className="chat-header">
        <h3>{activeFriend?.username || "Chat"}</h3>
        <button onClick={onClose}>×</button>
      </div>

      <div className="messages-list">
        {messages.map((msg, idx) => {
          const isMe   = msg.senderUsername === currentUser;
          const isLast = idx === messages.length - 1;
          return (
            <div
              key={msg.id}
              className={`message ${isMe ? "sent" : "received"}`}
            >
              <strong>{isMe ? "You" : msg.senderUsername}:</strong>
              <div className="message-content">
                {renderMessageContent(msg.content)}
              </div>
              {isMe && isLast && (
                <div className="message-status">
                  {msg.status === "DELIVERED" && <small>Delivered</small>}
                  {msg.status === "READ" && msg.readAt && (
                    <small>Read at {new Date(msg.readAt).toLocaleTimeString()}</small>
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
      </div>
    </div>
  );
}

export default ChatWindow;
