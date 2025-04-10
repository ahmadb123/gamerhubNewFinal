import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import NavHelper from '../component/NavHelper';
import {
  fetchXboxProfile,
  fetchPSNProfile,
  fetchSteamProfile,
} from '../service/profileService';
import {
  checkFriendRequest,
  acceptFriendRequest,
  getAllFriends,
} from '../service/AddFriendService';
import { searchUserProfile } from '../service/searchUserProfile';
import { getAllLinkedProfiles } from '../service/UserLinkedProfiles';
import { switchUserAccount } from '../utility/SwitchUserAccount';
import '../assests/HomePage.css';

const MainLayout = () => {
  const navigate = useNavigate();
  const username = localStorage.getItem("username");

  // Layout state
  const [accountInfo, setAccountInfo] = useState(null);
  const [platform, setPlatform] = useState(localStorage.getItem("platform") || null);
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResult, setSearchResult] = useState(null);
  const [linkedProfiles, setLinkedProfiles] = useState([]);
  const [pendingRequests, setPendingRequests] = useState([]);
  const [showPendingList, setShowPendingList] = useState(false);
  const [friendsList, setFriendsList] = useState([]);
  const [isFriendsDropdownOpen, setIsFriendsDropdownOpen] = useState(false);
  const [accountOptionVisible, setAccountOptionVisible] = useState(false);
  const [selectedUserProfile, setSelectedUserProfile] = useState(null);
  const [selectedUserGames, setSelectedUserGames] = useState([]);
  const [showMoreSelectedUserGames, setShowMoreSelectedUserGames] = useState(false);
  const [isAccountLoading, setIsAccountLoading] = useState(false);

  // New helper functions
  const togglePendingList = () => setShowPendingList(!showPendingList);
  const navigateToFriendPage = (id) => {
    navigate(`/friend/${id}`);
  };

  // Fetch data on mount
  useEffect(() => {
    fetchLayoutData();
    fetchFriendsList();
    checkForPendingRequests();
  }, [platform]);

  // Fetch account info and linked profiles (with loading state)
  const fetchLayoutData = async () => {
    setIsAccountLoading(true);
    const currentPlatform = localStorage.getItem("platform");
    if (!currentPlatform) {
      toast.error("No platform selected. Please log in again.");
      navigate("/");
      return;
    }
    try {
      const profileFetchers = {
        xbox: fetchXboxProfile,
        psn: fetchPSNProfile,
        steam: fetchSteamProfile,
      };
      const accountData = await profileFetchers[currentPlatform]();
      setAccountInfo(accountData);
      setPlatform(currentPlatform);

      const linkedProfilesResponse = await getAllLinkedProfiles();
      const combinedProfiles = Array.isArray(linkedProfilesResponse)
        ? linkedProfilesResponse
        : [
            ...(linkedProfilesResponse.xboxLinkedProfiles || []),
            ...(linkedProfilesResponse.steamLinkedProfiles || []),
          ];
      setLinkedProfiles(combinedProfiles);
    } catch (error) {
      console.error("Failed to fetch layout data:", error);
      localStorage.removeItem("platform");
      toast.error("Failed to fetch account info. Please log in again.");
      navigate("/");
    }
    setIsAccountLoading(false);
  };

  // Handler to perform profile switching with delayed update
  const handleSwitchProfile = async (newPlatform) => {
    try {
      setIsAccountLoading(true);
      await switchUserAccount(newPlatform, navigate);
      await fetchLayoutData();
      toast.success(`Switched to ${localStorage.getItem("platform")}`);
    } catch (error) {
      toast.error("Failed to switch account.");
    } finally {
      setIsAccountLoading(false);
    }
  };

  const fetchFriendsList = async () => {
    try {
      const friends = await getAllFriends();
      setFriendsList(friends);
    } catch (err) {
      console.error("Failed to fetch friends list:", err);
      toast.error("Could not fetch friends list.");
    }
  };

  const checkForPendingRequests = async () => {
    try {
      const requests = await checkFriendRequest();
      setPendingRequests(requests || []);
    } catch (err) {
      console.error("Failed to fetch pending requests:", err);
    }
  };

  const handleAcceptRequest = async (reqUsername) => {
    try {
      await acceptFriendRequest(reqUsername);
      toast.success(`Accepted request from ${reqUsername}`);
      checkForPendingRequests();
    } catch (err) {
      toast.error("Failed to accept request.");
    }
  };

  const toggleFriendsDropDown = () => {
    setIsFriendsDropdownOpen(!isFriendsDropdownOpen);
  };

  // Search handler
  const handleSearchChange = async (event) => {
    const newQuery = event.target.value;
    setSearchQuery(newQuery);
    setSelectedUserProfile(null);
    
    if (newQuery.length < 3) {
      setSearchResult(null);
      setSelectedUserGames([]);
      return;
    }
    
    try {
      const result = await searchUserProfile(newQuery);
      console.log("Search result:", result);
      if (result) {
        setSearchResult(result);
        setSelectedUserProfile(result.profile);
        setSelectedUserGames(result.recentGames || []);
      } else {
        setSearchResult(null);
        setSelectedUserProfile(null);
        setSelectedUserGames([]);
      }
    } catch (error) {
      console.error("Auto-search error:", error);
      toast.error("Failed to search user profile.");
      setSearchResult(null);
      setSelectedUserProfile(null);
      setSelectedUserGames([]);
    }
  };

  const handleClose = () => {
    setSelectedUserProfile(null);
  };

  const handleFriendBtn = () => {
    toast.info("Add friend functionality is not implemented yet.");
  };

  const toggleShowMoreSelectedUserGames = () => {
    setShowMoreSelectedUserGames(!showMoreSelectedUserGames);
  };

  // Filter out the currently-signed-in account from linked profiles
  const nonSignedLinkedProfiles = linkedProfiles.filter(
    (p) => p.gamertag !== accountInfo?.gamertag
  );

  return (
    <div className="gamerhub">
      {/* HEADER / NAVBAR */}
      <header className="header">
      <div className="logo-container" onClick={() => navigate('/main')} style={{ cursor: 'pointer' }}>
        <h1 className="logo">GamerHUB</h1>
      </div>
        <nav className="navbar">
          <button onClick={() => navigate('/news')} className="nav-button">News</button>
          <button onClick={() => navigate('/clips')} className="nav-button">Clips</button>
          <button onClick={() => navigate('/community')} className="nav-button">Community</button>
          <button onClick={() => navigate('/direct-messages')} className="nav-button">Messages</button>

          {/* SEARCH BAR */}
          <div className="search-bar">
            <input
              type="text"
              placeholder="Search User..."
              className="search-input"
              value={searchQuery}
              onChange={handleSearchChange}
            />
            {searchResult && (
              <div 
                className="search-result-preview"
                onClick={() => {
                  navigate(`/user/${searchResult.profile?.username}`);
                  setSearchResult(null);
                  setSearchQuery("");
                }}
              >
                <img
                  className="search-avatar-preview"
                  src={searchResult.profile?.appDisplayPicRaw || "/default-avatar.png"}
                  alt="User Avatar"
                />
                <span>{searchResult.profile?.gamertag}</span>
              </div>
            )}
          </div>

          {/* ACCOUNT SECTION */}
          <div
            className="account-section"
            onMouseEnter={() => setAccountOptionVisible(true)}
            onMouseLeave={() => setAccountOptionVisible(false)}
          >
            {isAccountLoading ? (
              <div className="loading-display">Loading account info...</div>
            ) : (
              <div className="gamertag-display">
                <div className="username-display">Welcome {username}</div>
                <p>{accountInfo?.gamertag} ({platform})</p>
              </div>
            )}
            <img
              src={accountInfo?.appDisplayPicRaw || "/default-avatar.png"}
              alt="Profile"
              className="profile-image"
            />
            {accountOptionVisible && (
              <div className="account-options-dropdown">
                <div className="linked-profiles-dropdown">
                  {nonSignedLinkedProfiles.length > 0 ? (
                    nonSignedLinkedProfiles.map((profile, idx) => (
                      <div key={idx} className="linked-profile-item">
                        <span>{profile.gamertag}</span>
                        <span>
                          ({profile.platform})
                          <button
                          onClick={() => handleSwitchProfile(profile.platform)}
                          style={{ marginLeft: '6px' }}
                          >
                            Switch
                          </button>
                        </span>
                      </div>
                    ))
                  ) : (
                    <p className="no-linked-accounts">No linked accounts</p>
                  )}
                </div>
                <ul>
                  <li><NavHelper page="/my-profile">My Profile</NavHelper></li>
                  <li><NavHelper page="/my-games">My Games</NavHelper></li>
                </ul>
              </div>
            )}
          </div>

         {/* Pending Friend Requests / Mailbox */}
         {pendingRequests.length > 0 && (
              <>
                <button
                  className="mailbox-button"
                  onClick={togglePendingList}
                  style={{ position: "relative" }}
                >
                  Mailbox
                  <span className="badge">{pendingRequests.length}</span>
                </button>
                {showPendingList && (
                  <div className="pending-dropdown">
                    {pendingRequests.length === 0 ? (
                      <p>No pending requests</p>
                    ) : (
                      <ul>
                        {pendingRequests.map((req) => (
                          <li key={req.id}>
                            <p>
                              {req.username} sent a follow request (
                              {req.status})
                            </p>
                            <button onClick={() => handleAcceptRequest(req.username)}>
                              Accept
                            </button>
                            <button>Decline</button>
                          </li>
                        ))}
                      </ul>
                    )}
                  </div>
                )}
              </>
            )}
          </nav>
        {/* Collapsible "My Friends" section */}
        <div
          className="friends-drop-down"
          onClick={toggleFriendsDropDown}
          style={{ cursor: "pointer", margin: "8px 16px" }}
        >
          <span>My Friends</span>
          <span style={{ marginLeft: "8px" }}>
            {isFriendsDropdownOpen ? "▲" : "▼"}
          </span>
        </div>
        
        {isFriendsDropdownOpen && (
          <div className="friends-dropdown-content" style={{ marginLeft: "32px" }}>
            {friendsList.length === 0 ? (
              <p style={{ padding: "8px" }}>No friends found.</p>
            ) : (
              <ul style={{ listStyle: "none", margin: 0, padding: 0 }}>
                {friendsList.map((friend) => {
                  let gamertag = "";
                  if (friend.xboxProfiles && friend.xboxProfiles.length > 0) {
                    gamertag = friend.xboxProfiles[0].xboxGamertag;
                  }
                  return (
                    <li
                      key={friend.id}
                      onClick={() => navigateToFriendPage(friend.id)}
                      style={{
                        padding: "8px",
                        cursor: "pointer",
                        borderBottom: "1px solid #ccc",
                      }}
                    >
                      {friend.username}
                      {gamertag ? ` - ${gamertag}` : ""}
                    </li>
                  );
                })}
              </ul>
            )}
          </div>
        )}


      </header>
      <div className="container">
          {selectedUserProfile && (
            <div className="selected-user-container">
              <button className="close-btn" onClick={handleClose}>
                X
              </button>
              <h2>Xbox Profile Details</h2>
              <p>Gamertag: {selectedUserProfile.gamertag}</p>
              <p>Account Tier: {selectedUserProfile.accountTier}</p>
              <p>Gamerscore: {selectedUserProfile.gamerscore}</p>
              <p>Tenure Level: {selectedUserProfile.tenureLevel}</p>
              <button className="add-friend-button" onClick={handleFriendBtn}>
                Add Friend
              </button>
              <img
                src={selectedUserProfile.gameDisplayPicRaw}
                alt="User Avatar"
                className="selected-user-avatar"
              />

              <div className="selected-user-games">
                <h3>Recent Games</h3>
                <div className="selected-user-games-list">
                  {selectedUserGames
                    .slice(
                      0,
                      showMoreSelectedUserGames
                        ? selectedUserGames.length
                        : 2
                    )
                    .map((game, index) => (
                      <div className="selected-user-game-card" key={index}>
                        <img src={game.displayImage} alt={game.gameName} />
                        <div>{game.gameName}</div>
                      </div>
                    ))}
                </div>
                {selectedUserGames.length > 2 && (
                  <button
                    onClick={toggleShowMoreSelectedUserGames}
                    className="toggle-games-button"
                  >
                    {showMoreSelectedUserGames ? "Show Less" : "Show More"}
                  </button>
                )}
              </div>
            </div>
          )}
        </div>

      {/* MAIN CONTENT (Child routes render here) */}
      <div className="container">
        <Outlet />
      </div>

      {/* FOOTER */}
      <footer className="footer">
        <p>About: Created by Ahmad Bishara</p>
        <a href="https://github.com/ahmadb123" target="_blank" rel="noopener noreferrer">
          GitHub: ahmadb123
        </a>
      </footer>
    </div>
  );
};

export default MainLayout;
