/**
 * service class for direct messages 
 */

import SockJS from "sockjs-client";
import { over } from "stompjs";
const apiUrl = "http://localhost:8080";
const jwtToken = localStorage.getItem("jwtToken");

import React, { useEffect, useState, useRef } from "react";

let stompClient = null;
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


// Function to check if session exists, returns session id
export const getSession = async ({ userTwoId }) => {
  try {
    const response = await fetch(`${apiUrl}/api/dm/api/getSession/${userTwoId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + jwtToken,
      },
    });
    if (!response.ok) {
      throw new Error("Error fetching session");
    }
    const data = await response.text();
    console.log("Session data:", data);
    return data;
  } catch (error) {
    console.error("Error fetching session", error);
  }
};

// Function to get or create a session
export const getSessionOrCreate = async ({ userTwoId }) => {
  try {
    const response = await fetch(`${apiUrl}/api/dm/api/creatOrGetSession/${userTwoId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + jwtToken,
      },
      body: JSON.stringify({ userTwoId }),
    });
    if (!response.ok) {
      throw new Error("Error fetching session");
    }
    const data = await response.json();
    console.log("Session data:", data);
    return data;
  } catch (error) {
    console.error("Error fetching session", error);
  }
};

// get messages->

export const getSessionMessages = async ({ sessionId }) => {
    try{
        const response = await fetch(`${apiUrl}/api/dm/api/get-message-session/${sessionId}`, {
            method: "GET",
            headers:{
                "Content-Type": "application/json",
        }
    });
    if(!response.ok){
        throw new Error("Error fetching session");
    }
    const data = await response.json();
    console.log("Session data:", data);
    return data;
    }catch(error){
        console.error("Error fetching session", error);
    }
};

// Function to connect to the WebSocket and subscribe to the topic for the given session.
export const connectWebSocket = async ({ sessionId, onMessageReceived }) => {
    if(stompClient){
        disconnectWebSocket();
    }
  const socket = new SockJS(`${apiUrl}/ws`);
  stompClient = over(socket);
  stompClient.connect(
    {},
    (frame) => {
      console.log("Connected: " + frame);
      // Subscribe to the topic for this session
      stompClient.subscribe(`/topic/direct-message/${sessionId}`, (message) => {
        if (message.body) {
          onMessageReceived(JSON.parse(message.body));
        }
      });
    },
    (error) => {
      console.error("WebSocket connection error:", error);
    }
  );
};

// Function to disconnect from the WebSocket.
export const disconnectWebSocket = () => {
  if (stompClient) {
    stompClient.disconnect();
    stompClient = null;
    console.log("Disconnected from WebSocket");
  }
};

// Function to send a message using the WebSocket connection.
export const sendMessages = async ({ sessionId, message }) => {
  if (stompClient) {
    stompClient.send(
      `/app/direct-message/${sessionId}/send`,
      {
        Authorization: "Bearer " + jwtToken,
      },
      JSON.stringify(message)
    );
  } else {
    console.error("WebSocket is not connected");
  }
};
