import React, { Component } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import "../assests/HomePage.css";
import {
  fetchXboxProfile,
  fetchPSNProfile,
  fetchSteamProfile
} from "../service/profileService";
import { fetchXboxFriends } from "../service/friendsService";
import { getRecentGames } from "../service/RecentGamesXbox";
import { fetchRecentNews } from "../service/NewsService";
import { postNews } from "../service/PostNewsService";
import { checkAccountType } from "../utility/CheckAccountType";
import { searchUserProfile } from "../service/searchUserProfile";

class HomePage extends Component {
  state = {
    accountInfo: null,
    platform: null,
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
    searchResult: null,   // store the found user (if only one)
    selectedUser: null,   // which user (from the search) was clicked to show details
    selectedUserProfile: null,
    selectedUserGames: [],
    showMoreSelectedUserGames: false,
  };

  // toggle to show more games - 
  handleShowMoreGames = () => {
    this.setState((prevState) => ({
      showMoreGames: !prevState.showMoreGames,
    }));
  };
  // Navigate to the "clips" page
  navigateClips = () => {
    this.props.navigate("/clips");
  };

  // Navigate to the "community" page
  navigateCommunity = () => {
    this.props.navigate("/community");
  };

  // Share news to the community
  handleShareNews = async (news) => {
    const contentText = `Check out this news: ${news.name}`;
    const sharedNewsId = news.slug; // slug is the name of the news item in the API
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

  /**
   * Auto-search as user types:
   * 1. Update searchQuery in state.
   * 2. If length >= 3, call searchUserProfile.
   * 3. If a user is found, store in searchResult. Otherwise, clear it.
   */
  handleSearchChange = async (event) => {
    const newQuery = event.target.value;
    this.setState({ searchQuery: newQuery, selectedUser: null }, async () => {
      // Optional: only search if user typed >= 3 characters
      if (newQuery.length < 3) {
        // Clear any previous search results
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
          // If no user found, clear the result
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

  /**
   * Fetch initial data once the component mounts:
   * - platform from localStorage
   * - accountInfo (profile) for that platform
   * - Xbox friends if platform === "xbox"
   * - recent games
   * - recent news
   */
  componentDidMount() {
    this.fetchNews();
    const platform = localStorage.getItem("platform");
    console.log("HomePage mounting with platform:", platform);

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

    this.setState({ platform });

    const fetchAccountInfo = async () => {
      console.log(`Fetching profile for platform: ${platform}`);
      try {
        const accountInfo = await fetchProfile();
        console.log("Fetched account info:", accountInfo);
        this.setState({ accountInfo, isFetching: false });
        // Fetch Xbox friends if platform is Xbox
        if (platform === "xbox") {
          try {
            const friends = await fetchXboxFriends();
            console.log("Fetched friends:", friends);
            this.setState({ friends, isFetchingFriends: false });
          } catch (error) {
            console.error(error);
            toast.error("Failed to fetch friends list.");
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
        localStorage.removeItem("platform"); // Clear invalid platform
        toast.error("Failed to fetch profile information.");
        window.location.href = "/";
      }
    };

    fetchAccountInfo();
  }

  // Fetch recent news
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

  /* Toggle to show more or fewer recent games for the searched user */
  toggleShowMoreSelectedUserGames = () => {
    this.setState((prevState) => ({
      showMoreSelectedUserGames: !prevState.showMoreSelectedUserGames,
    }));
  };

  render() {
    const {
      accountInfo,
      isFetching,
      recentNews,
      friends,
      isFetchingFriends,
      recentGames,
      isFetchingRecentGames,
      isFetchingRecentNews,
      searchQuery,
      searchResult,
      selectedUser,
      showMoreGames,
      selectedUserProfile,
      selectedUserGames,
      showMoreSelectedUserGames,
    } = this.state;

    if (isFetching) {
      return <p>Loading profile...</p>;
    }

    return (
      <div className="gamerhub">
        <header className="header">
          <h1 className="logo">GamerHUB</h1>
          <nav className="navbar">
            <button className="nav-button">NEWS</button>
            <button onClick={this.navigateClips} className="nav-button">
              CLIPS
            </button>
            <button onClick={this.navigateCommunity} className="nav-button">
              Community Insight
            </button>

            {/* Auto-search input */}
            <div className="search-bar" style={{ position: "relative" }}>
              <input
                type="text"
                placeholder="Search User..."
                className="search-input"
                value={searchQuery || ""}
                onChange={this.handleSearchChange}
              />

              {/**
               * If searchQuery >= 3, and searchResult is found,
               * show a "preview" below the input
               */}
              {searchQuery.length >= 3 && searchResult && (
                <div
                  className="search-result-preview"
                  onClick={() => this.setState({ selectedUser: searchResult,
                    searchQuery: '', searchResult: null 
                   })}
                >
                  <img className="search-avatar-preview"
                    src={searchResult.appDisplayPicRaw || "/default-avatar.png"}
                    alt="User Avatar"
                  />
                  <span>{searchResult.gamertag}</span>
                </div>
              )}
            </div>

            <div className="account-section">
              <div className="gamertag-display">
                <p>{accountInfo.gamertag}</p>
              </div>
              {accountInfo && (
                <img
                  src={accountInfo.appDisplayPicRaw}
                  alt="Profile"
                  style={{ borderRadius: "50%", width: "50px", height: "50px" }}
                />
              )}
            </div>
          </nav>
        </header>

        <div className="container">
          {/* If a user is selected, show their details below */}
          {selectedUserProfile && (
            <div className="selected-user-container">
              <h2>Xbox Profile Details</h2>
              <p>Gamertag: {selectedUserProfile.gamertag}</p>
              <p>Account Tier: {selectedUserProfile.accountTier}</p>
              <p>Gamerscore: {selectedUserProfile.gamerscore}</p>
              <p>Tenure Level: {selectedUserProfile.tenureLevel}</p>
              <img
                src={selectedUserProfile.gameDisplayPicRaw}
                alt="User Avatar"
                className="selected-user-avatar"
              />

              <div className="selected-user-games">
                <h3>Recent Games</h3>
                <div className="selected-user-games-list">
                  {selectedUserGames
                    .slice(0, showMoreSelectedUserGames ? selectedUserGames.length : 2)
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
              {/* --- News Feed Section --- */}
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
                                ? checkAccountType([
                                    ...new Set(
                                      newsItem.platforms.map((p) =>
                                        p.platform.name.toLowerCase()
                                      )
                                    ),
                                  ])
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
                                {newsItem.short_screenshots?.map((screenshot, idx) => (
                                  <img
                                    key={idx}
                                    className="screenshot-img"
                                    src={screenshot.image}
                                    alt={`Screenshot ${idx + 1}`}
                                  />
                                ))}
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

              {/* --- Recent Games Section --- */}
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
                            {game.titleHistory.lastTimePlayedFormatted
                              ? `Last Played: ${game.titleHistory.lastTimePlayedFormatted}`
                              : "Playtime not available"}
                          </div>
                          <div className="game-devices">
                            {game.devices.length > 0
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

            {/* --- Friends List Section --- */}
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
                        {friend.isFavorite && <span className="favorite-icon">★</span>}
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

const HomePageWithNavigate = (props) => {
  const navigate = useNavigate();
  return <HomePage {...props} navigate={navigate} />;
};

export default HomePageWithNavigate;
