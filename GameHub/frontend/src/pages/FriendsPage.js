import React, { useEffect, useState } from "react";
import { getAllFriends } from "../service/AddFriendService";
import MyFriendsPageComponent from "../component/MyFriendsPageComponent";

function FriendsPage() {
  // current user jwt token  
  const jwt = localStorage.getItem("jwtToken");

  // state for storing friends and fetching indicator
  const [friends, setFriends] = useState([]);
  const [fetching, setFetching] = useState(true);

  useEffect(() => {
    const getUserFriends = async () => {
      const fetchFriends = await getAllFriends();
      if (fetchFriends) {
        setFriends(fetchFriends);
      } else {
        console.error("Failed to fetch friends");
      }
      setFetching(false);
    };
    getUserFriends();
  }, []);

  return (
    <div style={{ margin: "20px", fontFamily: "Arial, sans-serif" }}>
      <h1 style={{ color: "#0af" }}>Friends</h1>
      {fetching ? (
        <p>Loading...</p>
      ) : (
        <div>
          {friends.length > 0 ? (
            friends.map((friend, index) => (
              <MyFriendsPageComponent key={index} friend={friend} />
            ))
          ) : (
            <p>No friends found.</p>
          )}
        </div>
      )}
    </div>
  );
}

export default FriendsPage;
