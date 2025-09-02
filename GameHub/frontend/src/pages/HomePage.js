// src/components/HomePage.jsx
import React, { Component } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import "../assests/HomePage.css";
import Dropdown from "../utility/DropDownUtilityClass";
import { fetchNewsByOrder } from "../service/NewsService";
import {
  fetchXboxProfile,
  fetchPSNProfile,
  fetchSteamProfile,
} from "../service/profileService";

import { fetchXboxFriends, fetchSteamFriends } from "../service/friendsService";
import { getRecentGames } from "../service/RecentGamesXbox";
import { postNews } from "../service/PostNewsService";
import { checkAccountType } from "../utility/CheckAccountType";
import { getSteamRecentPlayedGames } from "../service/SteamRecentPlayedAndOwnedGames";
import {
  searchUserProfile,
} from "../service/searchUserProfile";
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

    // New state for news selection options
    newsSelectionOptions: [
      { label: "Relevance", value: "-metacritic" },
      { label: "Date added", value: "-added" },
      { label: "Name", value: "name" },
      { label: "Release date", value: "-released" },
      { label: "Popularity", value: "-metacritic" },
      { label: "Average rating", value: "-rating" },
    ],
    selectedOrder: { label: "Relevance", value: "-metacritic" },
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

  // ----- Data Fetching Helpers -----

  fetchNews = async () => {
    this.setState({ isFetchingRecentNews: true });
    try {
      const { selectedOrder } = this.state;
      const order = selectedOrder.value || "-metacritic";
      const news = await fetchNewsByOrder(order);
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

  // Handler for order selection from dropdown
  handleOrderSelect = async (ordering) => {
    console.log("Selected ordering parameter:", ordering);
    try {
      const newsByOrder = await fetchNewsByOrder(ordering);
      this.setState({ recentNews: newsByOrder });
      toast.success("News order changed to: " + ordering);
    } catch (error) {
      console.error(error);
      toast.error("Failed to fetch news by order selection.");
    }
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
      newsSelectionOptions,
      selectedOrder,
    } = this.state;

    if (isFetching) {
      return <p>Loading profile...</p>;
    }

    return (
          <div className="main-content">
            <div className="news-and-games">
              {/* Order Selection Dropdown */}
              <div className="order-dropdown">
                <Dropdown
                  options={newsSelectionOptions}
                  defaultOption={selectedOrder}
                  label="Order"
                  onSelect={this.handleOrderSelect}
                />
              </div>

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
    );
  }
}

// Higher-order component to wrap HomePage with navigate
const HomePageWithNavigate = (props) => {
  const navigate = useNavigate();
  return <HomePage {...props} navigate={navigate} />;
};

export default HomePageWithNavigate;