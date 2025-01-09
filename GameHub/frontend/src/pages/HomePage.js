import React, { Component } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import "../assests/HomePage.css";
import { fetchXboxProfile, fetchPSNProfile, fetchSteamProfile } from "../service/profileService";
import { fetchXboxFriends } from "../service/friendsService";
import { getRecentGames } from "../service/RecentGamesXbox";
import { fetchRecentNews } from "../service/NewsService";
import {postNews} from "../service/PostNewsService";
// import { FaDesktop, FaXbox, FaPlaystation, FaGamepad } from "react-icons/fa";
import {checkAccountType} from "../utility/CheckAccountType";
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
  };

  navigateClips = () => {
    this.props.navigate("/clips");
  };

  navigateCommunity = () => {
    this.props.navigate("/community");
  };

  handleShareNews = async (news) =>{
    const contentText = `Check out this news: ${news.name}`;
    const sharedNewsId = news.id;

    try{
      const result = await postNews(contentText, sharedNewsId);
      if(result.success){
        toast.success("News shared successfully.");
      }else{
        toast.error("Failed to share news.");
      }
    }catch(error){
      console.error(error);
      toast.error("Failed to share news.");
    }
  };


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

        // Fetch Xbox friends if the platform is Xbox
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
        try {
          // fetch recent games
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
            <button className="nav-button">SEARCH</button>
            <button onClick= {this.navigateClips} className="nav-button">CLIPS</button>
            <button onClick={this.navigateCommunity} className="nav-button">Community Insight</button> 
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
          <div className="main-content">
            <div className="news-and-games">
              <section className="news-feed">
                <h2>News Feed</h2>
                <div className="content-box">
                  {isFetchingRecentNews ? (
                    <p>Loading news...</p>
                  ) : recentNews.length > 0 ? (
                    <ul className="news-feed-container">
                      {recentNews.map((news, index) => (
                        <li className="news-item" key={index}>
                          <img
                            className="news-main-image"
                            src={news.background_image}
                            alt={news.name}
                          />
                          <div className="news-item-content">
                            <h2 className="news-title">{news.name}</h2>
                            <p className="news-available-on">
                                Available on:{" "}
                                {news.platforms && news.platforms.length > 0 ? (
                                    checkAccountType([...new Set(news.platforms.map(p => p.platform.name.toLowerCase()))])
                                ) : (
                                    "N/A"
                                )}
                                </p>

                            <div className="news-extra-details">
                              <p className="news-release-date">Release Date: {news.released}</p>
                              <div className="genres">
                                <ul>
                                  {news.genres?.map((genre, idx) => (
                                    <li key={idx}>{genre.name}</li>
                                  ))}
                                </ul>
                              </div>
                              <button 
                                className="share-button"
                                onClick={() => this.handleShareNews(news)}
                              >
                                Share to Community
                              </button>
                              <div className="screenshots-container">
                                {news.short_screenshots?.map((screenshot, idx) => (
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