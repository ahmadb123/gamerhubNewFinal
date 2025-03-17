// src/components/HomePage.jsx
import React, { Component } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import "../assests/HomePage.css";
import NavHelper  from '../component/NavHelper';
import {
  fetchXboxProfile,
  fetchPSNProfile,
  fetchSteamProfile,
} from "../service/profileService";

import { fetchXboxFriends } from "../service/friendsService";
import { getRecentGames } from "../service/RecentGamesXbox";
import { fetchRecentNews } from "../service/NewsService";
import { postNews } from "../service/PostNewsService";
import { checkAccountType } from "../utility/CheckAccountType";
import {
  searchUserProfile,
  getAllLinkedProfiles,
} from "../service/searchUserProfile"; // Added getAllLinkedProfiles
import { addFriend } from "../service/AddFriendService"; // add friend service
import {
  checkFriendRequest,
  acceptFriendRequest,
  getAllFriends,
} from "../service/AddFriendService";

class HomePage extends Component {
  state = {
    accountInfo: null,
    platform: null,
    isFetching: true,

    friends: [], // from fetchXboxFriends (Xbox only)
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

    // For linked profiles (for example, Xbox linked accounts)
    linkedProfiles: [],
    showLinkedProfiles: false, // Controls whether the linked profiles list is visible

    pendingRequests: [],
    showPendingList: false,

    showFriendList: false,
    acceptFriendRequest: [],

    // For custom friend list from your backend
    friendsList: [], // Mismatch corrected: we store the entire friend object(s) here
    isFriendsDropdownOpen: false,
    // state for dropdwon visibility 
    accountOptionVisible: false,
  };

  // methods to handle mouse hover events - 
  mouseOverToggle = () =>{
    this.setState({accountOptionVisible: true});
  };

  mouseOutToggle = () =>{
    this.setState({accountOptionVisible: false});
  };

  componentDidMount() {
    this.fetchNews();
    const platform = localStorage.getItem("platform");
    console.log("HomePage mounting with platform:", platform);

    // if (!platform) {
    //   toast.error("No platform selected. Please log in again.");
    //   window.location.href = "/";
    //   return;
    // }

    // pick the right profile fetcher
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

    this.setState({ platform });

    // load the user account info, then friends & linked profiles if needed
    const fetchAccountInfo = async () => {
      try {
        const accountInfo = await fetchProfile();
        this.setState({ accountInfo, isFetching: false });

        if (platform === "xbox") {
          // Fetch friends for Xbox
          try {
            const friends = await fetchXboxFriends();
            this.setState({ friends, isFetchingFriends: false });
          } catch (error) {
            console.error(error);
            toast.error("Failed to fetch friends list.");
          }

          // Fetch linked profiles
          try {
            const linkedProfiles = await getAllLinkedProfiles();
            this.setState({ linkedProfiles });
          } catch (error) {
            console.error(error);
            toast.error("Failed to fetch linked profiles.");
          }
        }

        // Fetch recent games
        try {
          const recentGames = await getRecentGames();
          this.setState({ recentGames, isFetchingRecentGames: false });
        } catch (error) {
          console.error(error);
          toast.error("Failed to fetch recent games.");
        }
      } catch (error) {
        console.error(error);
        localStorage.removeItem("platform");
        toast.error("Failed to fetch profile information.");
        window.location.href = "/";
      }
    };

    fetchAccountInfo();
    this.checkForPendingRequests();

    // Optionally fetch the custom friend list from your own backend
    this.fetchFriendsList();
  }

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

  // fetch the custom friend list from your backend
  fetchFriendsList = async () => {
    try {
      const friendsList = await getAllFriends(); // from AddFriendService
      console.log("All friends list:", friendsList);
      this.setState({ friendsList });
    } catch (err) {
      console.error("Failed to fetch all friends list:", err);
    }
  };

  // Toggle the collapsible friend list
  toggleFriendsDropDown = async () => {
    // If about to open the dropdown, refresh the data (optional)
    if (!this.state.isFriendsDropdownOpen) {
      await this.fetchFriendsList();
    }
    this.setState((prev) => ({
      isFriendsDropdownOpen: !prev.isFriendsDropdownOpen,
    }));
  };

  // If you want to navigate to a friend’s page
  navigateToFriendPage = (friendId) => {
    this.props.navigate(`/user/${friendId}`);
  };

  // Check for pending friend requests
  checkForPendingRequests = async () => {
    try {
      const pendingRequests = await checkFriendRequest();
      console.log("Pending friend requests:", pendingRequests);
      this.setState({ pendingRequests });
      if (this.state.pendingRequests[0]?.status === "accept") {
        this.setState({ showFriendList: true, showPendingList: false });
      }
    } catch (err) {
      console.error("Failed to fetch pending friend requests:", err);
    }
  };

  togglePendingList = () => {
    this.setState((prev) => ({ showPendingList: !prev.showPendingList }));
  };

  // handle accept button
  handleAcceptBtn = async () => {
    try {
      // username of requester from the pending request
      const username = this.state.pendingRequests[0].username;
      const acceptFriend = await acceptFriendRequest(username);
      console.log("Accepting friend request:", acceptFriend);
      this.setState({ acceptFriendRequest: acceptFriend });
    } catch (err) {
      console.error("Failed to accept friend request:", err);
    }
  };

  // handle adding a friend
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

  // handle search user change
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

  // Close the selected user profile box
  handleCloseBtn = () => {
    this.setState({
      selectedUser: null,
      selectedUserProfile: null,
      selectedUserGames: [],
    });
  };

  // Toggle to show more user games (for search results)
  toggleShowMoreSelectedUserGames = () => {
    this.setState((prevState) => ({
      showMoreSelectedUserGames: !prevState.showMoreSelectedUserGames,
    }));
  };

  // Toggle to show/hide the linked profiles list
  toggleLinkedProfiles = () => {
    this.setState((prevState) => ({
      showLinkedProfiles: !prevState.showLinkedProfiles,
    }));
  };

  // Toggle to show more games (own profile)
  handleShowMoreGames = () => {
    this.setState((prevState) => ({
      showMoreGames: !prevState.showMoreGames,
    }));
  };

  // navigation helpers
  navigateClips = () => {
    this.props.navigate("/clips");
  };
  navigateCommunity = () => {
    this.props.navigate("/community");
  };
  navigateNews = () => {
    this.props.navigate("/news");
  }
  navigateMyGames = () => {
    this.props.navigate("/mygames");
  }

  // share news
  handleShareNews = async (news) => {
    const contentText = `Check out this news: ${news.name}`;
    const sharedNewsId = news.slug; 
    console.log("Sharing news with ID:", sharedNewsId);

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

  render() {
    // Destructure the main state variables you need in render
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
      showLinkedProfiles,
      pendingRequests,
      showPendingList,
      // newly destructured:
      friendsList,
      isFriendsDropdownOpen,
    } = this.state;

    if (isFetching) {
      return <p>Loading profile...</p>;
    }

    // Filter out linked profiles so that only unsinged (other) profiles are shown
    const nonSignedLinkedProfiles = linkedProfiles.filter(
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
              CLIPS
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
              {/* If user typed >=3 chars and we have a searchResult, show a preview */}
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

            {/* Account Info Section with Linked Profiles Toggle */}
            <div className="account-section" 
                onMouseEnter={() => this.setState({ accountOptionVisible: true })} 
                onMouseLeave={() => this.setState({ accountOptionVisible: false })}
              >
                <div className="gamertag-display">
                  <p>{accountInfo.gamertag}</p>
                  {nonSignedLinkedProfiles.length > 0 && (
                    <button
                      onClick={this.toggleLinkedProfiles}
                      className="toggle-linked-profiles"
                    >
                      {showLinkedProfiles ? "▲" : "▼"}
                    </button>
                  )}
                </div>

                {/* Profile Image */}
                <img
                  src={accountInfo.appDisplayPicRaw}
                  alt="Profile"
                  className="profile-image"
                />

                {/* Hover Dropdown */}
                {this.state.accountOptionVisible && (
                  <div className="account-options-dropdown">
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
              {/* Render additional linked profiles when toggled */}
              {showLinkedProfiles && (
                <div className="linked-profiles">
                  {nonSignedLinkedProfiles.map((profile, index) => (
                    <div key={index} className="linked-profile-item">
                      <img
                        src={profile.appDisplayPicRaw}
                        alt="Linked Profile"
                        style={{
                          width: "30px",
                          height: "30px",
                          borderRadius: "50%",
                        }}
                      />
                      <span>{profile.gamertag}</span>
                    </div>
                  ))}
                </div>
              )}
            {/* </div> */}

            {/* Only show mailbox if we have at least one pending request */}
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
                              {req.username} sent a follow request ({req.status})
                            </p>
                            <button onClick={this.handleAcceptBtn}>Accept</button>
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

        {/* Collapsible "My Friends" section (outside nav if you prefer) */}
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

        {/* If open, show the friend list */}
        {isFriendsDropdownOpen && (
          <div className="friends-dropdown-content" style={{ marginLeft: "32px" }}>
            {friendsList.length === 0 ? (
              <p style={{ padding: "8px" }}>No friends found.</p>
            ) : (
              <ul style={{ listStyle: "none", margin: 0, padding: 0 }}>
                {friendsList.map((friend) => {
                  // If your friend object includes an array of xboxProfiles,
                  // let's assume the first one holds the gamertag
                  
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
          {/* If a user was found (via search) and we have user profile data */}
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

              {/* Show some recent games for the searched user */}
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
                            {game.titleHistory &&
                            game.titleHistory.lastTimePlayedFormatted
                              ? `Last Played: ${game.titleHistory.lastTimePlayedFormatted}`
                              : "Playtime not available"}
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

            {/* FRIENDS LIST (Xbox friends, if platform is Xbox) */}
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
