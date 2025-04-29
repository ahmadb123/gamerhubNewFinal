// src/pages/FriendsPage.jsx

import React, { useEffect, useState } from "react";
import { getAllFriends } from "../service/AddFriendService";
import MyFriendsPageComponent from "../component/MyFriendsPageComponent";
import { fetchFriendsCollections } from "../NewsHelper/AddNewsGamesToGameList";
import "../assests/MyFriendsPage.css" 
function FriendsPage() {
  const [friends, setFriends] = useState([]);
  const [friendsCollections, setFriendsCollections] = useState({});
  const [loadingFriends, setLoadingFriends] = useState(true);
  const [loadingCollections, setLoadingCollections] = useState(true);

  useEffect(() => {
    async function loadAll() {
      try {
        // 1. Fetch friends
        const friendsData = await getAllFriends();
        setFriends(friendsData || []);
        setLoadingFriends(false);

        // 2. Fetch each friend’s collections in parallel
        if (friendsData && friendsData.length > 0) {
          const collectionsMap = {};
          await Promise.all(
            friendsData.map(async (friend) => {
              try {
                const cols = await fetchFriendsCollections(friend.userId);
                console.log(`Fetched collections for ${friend.userId}:`, cols);
                collectionsMap[friend.userId] = cols;
              } catch (err) {
                console.error(`Failed to fetch collections for ${friend.userId}:`, err);
                collectionsMap[friend.userId] = [];
              }
            })
          );
          setFriendsCollections(collectionsMap);
        }
      } catch (err) {
        console.error("Failed to load friends:", err);
        setFriends([]);  
      } finally {
        setLoadingCollections(false);
      }
    }

    loadAll();
  }, []);

  if (loadingFriends) {
    return <p>Loading friends…</p>;
  }

  return (
    <div className="friends-list">
      {friends.length === 0 ? (
        <p>No friends found.</p>
      ) : (
        friends.map((f) => (
          <MyFriendsPageComponent
            key={f.userId}
            friend={f}
            collections={friendsCollections[f.userId] || []}
            isCollectionsLoading={loadingCollections}
          />
        ))
      )}
    </div>
  );
}

export default FriendsPage;
