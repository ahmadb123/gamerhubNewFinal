// src/components/HomePage.jsx
import React, { Component } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import "../assests/HomePage.css";
import NavHelper from "../component/NavHelper";
import {
  fetchXboxProfile,
  fetchPSNProfile,
  fetchSteamProfile,
} from "../service/profileService";

import { fetchXboxFriends, fetchSteamFriends } from "../service/friendsService";
import { getRecentGames } from "../service/RecentGamesXbox";
import { fetchRecentNews } from "../service/NewsService";
import { postNews } from "../service/PostNewsService";
import { checkAccountType } from "../utility/CheckAccountType";
import { getSteamRecentPlayedGames } from "../service/SteamRecentPlayedAndOwnedGames";
import {
  searchUserProfile,
} from "../service/searchUserProfile";
import {switchUserAccount} from "../utility/SwitchUserAccount";
import {
  addFriend,
  checkFriendRequest,
  acceptFriendRequest,
  getAllFriends,
} from "../service/AddFriendService";

import { getAllLinkedProfiles } from "../service/UserLinkedProfiles";

class HomePage extends Component {
  state = {
    accountInfo: null,
    platform: localStorage.getItem("platform") ||  null,
    isFetching: true,

    friends: [],
    isFetchingFriends: true,

    recentGames: [],
    isFetchingRecentGames: true,

    recentNews: [],
    isFetchingRecentNews: true,

    showMoreGames: false,

    // For searching other users:
    searchQuery: "",
    searchResult: null,
    selectedUser: null,
    selectedUserProfile: null,
    selectedUserGames: [],
    showMoreSelectedUserGames: false,

    // Linked profiles (only additional accounts, not including the logged in user)
    linkedProfiles: [],

    // Pending friend requests
    pendingRequests: [],
    showPendingList: false,

    // For custom friend list from your backend
    friendsList: [],
    isFriendsDropdownOpen: false,

    // Dropdown visibility for account section
    accountOptionVisible: false,
  };

  componentDidMount(){
    this.fetchData();
  }

  componentDidUpdate(prevProps, prevState) {
    const currentPlatform = localStorage.getItem("platform");
    if (currentPlatform !== prevState.platform) {
      this.setState({ platform: currentPlatform }, () => {
        this.fetchData(true);
      });
    }
  }

  fetchData = (isPlatformSwitch = false) => {
    this.fetchNews();
    this.fetchAccountInfo(isPlatformSwitch);
    this.checkForPendingRequests();
    this.fetchFriendsList();
  };

  fetchAccountInfo = async (isPlatformSwitch = false) => {
    if (isPlatformSwitch) {
      this.setState({
        isFetching: true,
        friends: [],
        recentGames: [],
        isFetchingFriends: true,
        isFetchingRecentGames: true,
      });
    }

  
    const platform = localStorage.getItem("platform");
    console.log("Fetching data for platform:", platform);

    if (!platform) {
      toast.error("No platform selected. Please log in again.");
      window.location.href = "/";
      return;
    }

    const profileFetchers = {
      xbox: fetchXboxProfile,
      psn: fetchPSNProfile,
      steam: fetchSteamProfile,
    };

    const fetchProfile = profileFetchers[platform];
    if (!fetchProfile) {
      toast.error("Unsupported platform. Please log in again.");
      window.location.href = "/";
      return;
    }

    try {
      const accountInfo = await fetchProfile();
      this.setState({ accountInfo, isFetching: false });

      const linkedProfilesResponse = await getAllLinkedProfiles();
      const combinedProfiles = Array.isArray(linkedProfilesResponse)
        ? linkedProfilesResponse.flat()
        : [
            ...(linkedProfilesResponse.xboxLinkedProfiles || []),
            ...(linkedProfilesResponse.steamLinkedProfiles || []),
          ];
      this.setState({ linkedProfiles: combinedProfiles });

      if (platform === "xbox") {
        const [friends, recentGames] = await Promise.all([
          fetchXboxFriends(),
          getRecentGames(),
        ]);
        this.setState({
          friends,
          recentGames,
          isFetchingFriends: false,
          isFetchingRecentGames: false,
        });
      } else if (platform === "steam") {
        const [friends, recentGames] = await Promise.all([
          fetchSteamFriends(),
          getSteamRecentPlayedGames(),
        ]);
        this.setState({
          friends,
          recentGames,
          isFetchingFriends: false,
          isFetchingRecentGames: false,
        });
      }
    } catch (error) {
      console.error(error);
      localStorage.removeItem("platform");
      toast.error("Failed to fetch profile information.");
      window.location.href = "/";
    }
  };
  //   componentDidMount() {
  //   this.fetchNews();
  //   const platform = localStorage.getItem("platform");
  //   console.log("HomePage mounting with platform:", platform);

  //   if (!platform) {
  //     toast.error("No platform selected. Please log in again.");
  //     window.location.href = "/";
  //     return;
  //   }

  //   // Pick the right profile fetcher
  //   const profileFetchers = {
  //     xbox: fetchXboxProfile,
  //     psn: fetchPSNProfile,
  //     steam: fetchSteamProfile,
  //   };

  //   const fetchProfile = profileFetchers[platform];
  //   if (!fetchProfile) {
  //     toast.error("Unsupported platform. Please log in again.");
  //     window.location.href = "/";
  //     return;
  //   }

  //   this.setState({ platform });

  //   const fetchAccountInfo = async () => {
  //     try {
  //       const accountInfo = await fetchProfile();
  //       this.setState({ accountInfo, isFetching: false });

  //       // Fetch linked profiles and combine two array lists into one
  //       try {
  //         const linkedProfilesResponse = await getAllLinkedProfiles();
  //         // If getAllLinkedProfiles returns an array of two arrays:
  //         const combinedProfiles = Array.isArray(linkedProfilesResponse)
  //           ? linkedProfilesResponse.flat()
  //           : // Otherwise, if it returns an object with two arrays:
  //             [
  //               ...(linkedProfilesResponse.xboxLinkedProfiles || []),
  //               ...(linkedProfilesResponse.steamLinkedProfiles || []),
  //             ];
  //         this.setState({ linkedProfiles: combinedProfiles });
  //       } catch (error) {
  //         console.error("Error fetching linked profiles:", error);
  //       }

  //       if (platform === "xbox") {
  //         try {
  //           const friends = await fetchXboxFriends();
  //           const recentGames = await getRecentGames();
  //           this.setState({
  //             friends,
  //             recentGames,
  //             isFetchingFriends: false,
  //             isFetchingRecentGames: false,
  //           });
  //         } catch (error) {
  //           console.error("Xbox data fetch error:", error);
  //         }
  //       } else if (platform === "steam") {
  //         try {
  //           const friends = await fetchSteamFriends();
  //           const recentGames = await getSteamRecentPlayedGames();
  //           this.setState({
  //             friends,
  //             recentGames,
  //             isFetchingFriends: false,
  //             isFetchingRecentGames: false,
  //           });
  //         } catch (error) {
  //           console.error(error);
  //           toast.error("Failed to fetch recent games (Steam).");
  //         }
  //       }
  //     } catch (error) {
  //       console.error(error);
  //       localStorage.removeItem("platform");
  //       toast.error("Failed to fetch profile information.");
  //       window.location.href = "/";
  //     }
  //   };

  //   fetchAccountInfo();
  //   this.checkForPendingRequests();
  //   this.fetchFriendsList();
  // }

  // ----- Data Fetching Helpers -----

  fetchNews = async () => {
    this.setState({ isFetchingRecentNews: true });
    try {
      const news = await fetchRecentNews();
      this.setState({ recentNews: news, isFetchingRecentNews: false });
    } catch (error) {
      console.error(error);
      toast.error("Failed to fetch recent news.");
      this.setState({ isFetchingRecentNews: false });
    }
  };

  fetchFriendsList = async () => {
    try {
      const friendsList = await getAllFriends();
      console.log("All friends list:", friendsList);
      this.setState({ friendsList });
    } catch (err) {
      console.error("Failed to fetch all friends list:", err);
    }
  };

  // ----- Pending Friend Requests -----

  checkForPendingRequests = async () => {
    try {
      const pendingRequests = await checkFriendRequest();
      console.log("Pending friend requests:", pendingRequests);
      this.setState({ pendingRequests });
    } catch (err) {
      console.error("Failed to fetch pending friend requests:", err);
    }
  };

  togglePendingList = () => {
    this.setState((prev) => ({ showPendingList: !prev.showPendingList }));
  };

  handleAcceptBtn = async () => {
    try {
      const username = this.state.pendingRequests[0].username;
      const acceptFriend = await acceptFriendRequest(username);
      console.log("Accepting friend request:", acceptFriend);
      this.checkForPendingRequests();
      this.fetchFriendsList();
    } catch (err) {
      console.error("Failed to accept friend request:", err);
    }
  };

  // ----- Friend List -----

  toggleFriendsDropDown = async () => {
    if (!this.state.isFriendsDropdownOpen) {
      await this.fetchFriendsList();
    }
    this.setState((prev) => ({
      isFriendsDropdownOpen: !prev.isFriendsDropdownOpen,
    }));
  };

  navigateToFriendPage = (friendId) => {
    this.props.navigate(`/user/${friendId}`);
  };

  // ----- Search Handling -----

  handleSearchChange = async (event) => {
    const newQuery = event.target.value;
    this.setState({ searchQuery: newQuery, selectedUser: null }, async () => {
      if (newQuery.length < 3) {
        this.setState({ searchResult: null });
        return;
      }
      try {
        const result = await searchUserProfile(newQuery);
        console.log("Search result:", result);
        if (result) {
          this.setState({
            searchResult: result,
            selectedUserProfile: result.profile,
            selectedUserGames: result.recentGames,
          });
        } else {
          this.setState({
            searchResult: null,
            selectedUserProfile: null,
            selectedUserGames: [],
          });
        }
      } catch (error) {
        console.error("Auto-search error:", error);
        toast.error("Failed to search user profile.");
        this.setState({ searchResult: null });
      }
    });
  };

  handleCloseBtn = () => {
    this.setState({
      selectedUser: null,
      selectedUserProfile: null,
      selectedUserGames: [],
    });
  };

  toggleShowMoreSelectedUserGames = () => {
    this.setState((prevState) => ({
      showMoreSelectedUserGames: !prevState.showMoreSelectedUserGames,
    }));
  };

  // ----- Add Friend from Search -----

  handleFriendBtn = async () => {
    try {
      const userToRequest = this.state.selectedUserProfile.username;
      console.log("Adding friend:", userToRequest);
      const result = await addFriend(userToRequest);
      if (result.success) {
        toast.success("Friend request sent successfully.");
      } else {
        toast.error("Failed to send friend request.");
      }
    } catch (error) {
      console.error(error);
      toast.error("Failed to send friend request.");
    }
  };

  // ----- News Sharing -----

  handleShareNews = async (news) => {
    const contentText = `Check out this news: ${news.name}`;
    const sharedNewsId = news.slug;
    try {
      const result = await postNews(contentText, sharedNewsId);
      if (result.success) {
        toast.success("News shared successfully.");
      } else {
        toast.error("Failed to share news.");
      }
    } catch (error) {
      console.error(error);
      toast.error("Failed to share news.");
    }
  };

  // ----- Navigation Buttons -----

  navigateClips = () => {
    this.props.navigate("/clips");
  };

  navigateCommunity = () => {
    this.props.navigate("/community");
  };

  navigateNews = () => {
    this.props.navigate("/news");
  };

  navigateMyGames = () => {
    this.props.navigate("/my-games");
  };

  // ----- Render -----

  render() {
    const {
      accountInfo,
      isFetching,
      friends,
      isFetchingFriends,
      recentGames,
      isFetchingRecentGames,
      recentNews,
      isFetchingRecentNews,
      searchQuery,
      searchResult,
      selectedUserProfile,
      selectedUserGames,
      showMoreSelectedUserGames,
      linkedProfiles,
      pendingRequests,
      showPendingList,
      friendsList,
      isFriendsDropdownOpen,
      accountOptionVisible,
      platform,
    } = this.state;

    if (isFetching) {
      return <p>Loading profile...</p>;
    }

    // Filter out the logged-in account from the linkedProfiles list
    const nonSignedLinkedProfiles = (linkedProfiles || []).filter(
      (profile) => profile.gamertag !== accountInfo.gamertag
    );

    return (
      <div className="gamerhub">
        {/* HEADER / NAV */}
        <header className="header">
          <h1 className="logo">GamerHUB</h1>
          <nav className="navbar">
            <button onClick={this.navigateNews} className="nav-button">
              News
            </button>
            <button onClick={this.navigateClips} className="nav-button">
              Clips
            </button>
            <button onClick={this.navigateCommunity} className="nav-button">
              Community Insight
            </button>

            {/* SEARCH BAR */}
            <div className="search-bar">
              <input
                type="text"
                placeholder="Search User..."
                className="search-input"
                value={searchQuery || ""}
                onChange={this.handleSearchChange}
              />
              {searchQuery.length >= 3 && searchResult && (
                <div
                  className="search-result-preview"
                  onClick={() =>
                    this.setState({
                      selectedUser: searchResult,
                      searchQuery: "",
                      searchResult: null,
                    })
                  }
                >
                  <img
                    className="search-avatar-preview"
                    src={
                      searchResult.profile.appDisplayPicRaw ||
                      "/default-avatar.png"
                    }
                    alt="User Avatar"
                  />
                  <span>{searchResult.profile.gamertag}</span>
                </div>
              )}
            </div>

            {/* Account Section (Gamertag + Avatar) */}
            <div
              className="account-section"
              onMouseEnter={() =>
                this.setState({ accountOptionVisible: true })
              }
              onMouseLeave={() =>
                this.setState({ accountOptionVisible: false })
              }
            >
              <div className="gamertag-display">
                <p>
                  {accountInfo.gamertag} ({platform})
                </p>
              </div>
              <img
                src={accountInfo.appDisplayPicRaw}
                alt="Profile"
                className="profile-image"
              />

              {/* Hover Dropdown: Linked Profiles & Menu Options */}
              {accountOptionVisible && (
                <div className="account-options-dropdown">
                  <div className="linked-profiles-dropdown">
                    {nonSignedLinkedProfiles.length > 0 ? (
                      nonSignedLinkedProfiles.map((profile, index) => (
                        <div key={index} className="linked-profile-item">
                          <span className="linked-gamertag">
                            {profile.gamertag}
                          </span>
                          <span className="linked-platform">
                            ({profile.platform})
                            <button 
                              onClick={() => switchUserAccount(profile.platform, this.props.navigate)}
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
                    <li>Profile</li>
                    <li>Friends</li>
                    <li>
                      <NavHelper page="/my-games">My Games</NavHelper>
                    </li>
                    <li>Wishlist</li>
                    <li>Collections</li>
                    <li>Settings</li>
                  </ul>
                </div>
              )}
            </div>

            {/* Pending Friend Requests / Mailbox */}
            {pendingRequests.length > 0 && (
              <>
                <button
                  className="mailbox-button"
                  onClick={this.togglePendingList}
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
                            <button onClick={this.handleAcceptBtn}>
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
        </header>

        {/* Collapsible "My Friends" section */}
        <div
          className="friends-drop-down"
          onClick={this.toggleFriendsDropDown}
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
                      onClick={() => this.navigateToFriendPage(friend.id)}
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

        <div className="container">
          {selectedUserProfile && (
            <div className="selected-user-container">
              <button className="close-btn" onClick={this.handleCloseBtn}>
                X
              </button>
              <h2>Xbox Profile Details</h2>
              <p>Gamertag: {selectedUserProfile.gamertag}</p>
              <p>Account Tier: {selectedUserProfile.accountTier}</p>
              <p>Gamerscore: {selectedUserProfile.gamerscore}</p>
              <p>Tenure Level: {selectedUserProfile.tenureLevel}</p>
              <button className="add-friend-button" onClick={this.handleFriendBtn}>
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
                    onClick={this.toggleShowMoreSelectedUserGames}
                    className="toggle-games-button"
                  >
                    {showMoreSelectedUserGames ? "Show Less" : "Show More"}
                  </button>
                )}
              </div>
            </div>
          )}

          <div className="main-content">
            <div className="news-and-games">
              {/* NEWS FEED */}
              <section className="news-feed">
                <h2>News Feed</h2>
                <div className="content-box">
                  {isFetchingRecentNews ? (
                    <p>Loading news...</p>
                  ) : recentNews.length > 0 ? (
                    <ul className="news-feed-container">
                      {recentNews.map((newsItem, index) => (
                        <li className="news-item" key={index}>
                          <img
                            className="news-main-image"
                            src={newsItem.background_image}
                            alt={newsItem.name}
                          />
                          <div className="news-item-content">
                            <h2 className="news-title">{newsItem.name}</h2>
                            <p className="news-available-on">
                              Available on:{" "}
                              {newsItem.platforms && newsItem.platforms.length > 0
                                ? checkAccountType(
                                    [
                                      ...new Set(
                                        newsItem.platforms.map((p) =>
                                          p.platform.name.toLowerCase()
                                        )
                                      ),
                                    ]
                                  )
                                : "N/A"}
                            </p>
                            <div className="news-extra-details">
                              <p className="news-release-date">
                                Release Date: {newsItem.released}
                              </p>
                              <div className="genres">
                                <ul>
                                  {newsItem.genres?.map((genre, idx) => (
                                    <li key={idx}>{genre.name}</li>
                                  ))}
                                </ul>
                              </div>
                              <button
                                className="share-button"
                                onClick={() => this.handleShareNews(newsItem)}
                              >
                                Share to Community
                              </button>
                              <div className="screenshots-container">
                                {newsItem.short_screenshots?.map(
                                  (screenshot, idx) => (
                                    <img
                                      key={idx}
                                      className="screenshot-img"
                                      src={screenshot.image}
                                      alt={`Screenshot ${idx + 1}`}
                                    />
                                  )
                                )}
                              </div>
                            </div>
                          </div>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p>No news found.</p>
                  )}
                </div>
              </section>

              {/* RECENT GAMES */}
              <section className="recent-games">
                <h2>Recent Games</h2>
                <div className="content-box">
                  {isFetchingRecentGames ? (
                    <p>Loading recent games...</p>
                  ) : recentGames.length > 0 ? (
                    <div className="game-list">
                      {recentGames.map((game, index) => (
                        <div className="game-card" key={index}>
                          <img src={game.displayImage} alt={game.name} />
                          <div className="game-name">{game.name}</div>
                          <div className="game-info">
                            {game.titleHistory?.lastTimePlayedFormatted
                              ? `Last Played: ${game.titleHistory.lastTimePlayedFormatted}`
                              : "Recent Playtime: N/A"}
                          </div>
                          <div className="game-devices">
                            {game.devices && game.devices.length > 0
                              ? `Played on: ${game.devices.join(", ")}`
                              : "No devices found"}
                          </div>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <p>No recent games found.</p>
                  )}
                </div>
              </section>
            </div>

            {/* FRIENDS LIST */}
            <aside className="friends-list">
              <h2>Friends</h2>
              <div className="content-box">
                {isFetchingFriends ? (
                  <p>Loading friends...</p>
                ) : friends.length > 0 ? (
                  <ul className="friends-list-container">
                    {friends.map((friend, index) => (
                      <li className="friend-item" key={index}>
                        <img
                          className="friend-avatar"
                          src={friend.displayPicRaw || "/default-avatar.png"}
                          alt={`Avatar of ${friend.gamertag}`}
                        />
                        <div className="friend-details">
                          <p className="friend-gamertag">{friend.gamertag}</p>
                          <p className="friend-realname">
                            {friend.realName || "Unknown"}
                          </p>
                          <p className="friend-presence">
                            {friend.presenceState === "Online" ? (
                              <span style={{ color: "green" }}>Online</span>
                            ) : (
                              friend.presenceText || "Offline"
                            )}
                          </p>
                        </div>
                        {friend.isFavorite && (
                          <span className="favorite-icon">★</span>
                        )}
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p>No friends found.</p>
                )}
              </div>
            </aside>
          </div>
        </div>

        <footer className="footer">
          <p>About: Created by Ahmad Bishara</p>
          <a
            href="https://github.com/ahmadb123"
            target="_blank"
            rel="noopener noreferrer"
          >
            GitHub: ahmadb123
          </a>
        </footer>
      </div>
    );
  }
}

// Higher-order component to wrap HomePage with navigate
const HomePageWithNavigate = (props) => {
  const navigate = useNavigate();
  return <HomePage {...props} navigate={navigate} />;
};

export default HomePageWithNavigate;
