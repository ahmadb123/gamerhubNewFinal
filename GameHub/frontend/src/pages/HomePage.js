import React, { Component } from "react";
import { toast } from "react-toastify";
import "../assests/HomePage.css";
import { fetchXboxProfile, fetchPSNProfile, fetchSteamProfile } from "../service/profileService";
import { fetchXboxFriends } from "../service/friendsService";
import { getRecentGames } from "../service/RecentGamesXbox";

class HomePage extends Component {
    state = {
        accountInfo: null,
        platform: null,
        isFetching: true, // Add a loading state
        friends: [],
        isFetchingFriends: true,
        recentGames: [],        
        isFetchingRecentGames: true,  
    };

    componentDidMount() {
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
              try{
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
    render() {
        const {
          accountInfo,
          platform,
          isFetching,
          friends,
          isFetchingFriends,
          recentGames,
          isFetchingRecentGames,
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
    
            {/* Main Content */}
            <main className="main-content">
             {/* Recent Games */}
                <section className="recent-games">
                <h2>Recent Games</h2>
                <div className="game-list">
                    {isFetchingRecentGames ? (
                    <p>Loading recent games...</p>
                    ) : recentGames.length > 0 ? (
                    recentGames.map((game, index) => (
                        <div className="game-card" key={index}>
                        <img src={game.displayImage} alt={game.name} />
                        <div className="game-name">{game.name}</div>
                        <div className="game-info">
                            {game.titleHistory.lastTimePlayedFormatted
                            ? `Last Played ${game.titleHistory.lastTimePlayedFormatted} ago`
                            : "Playtime not available"}
                        </div>
                        <div className="game-devices">
                            {game.devices.length > 0
                            ? `Available on: ${game.devices.join(", ")}`
                            : "No devices found"}
                        </div>
                        </div>
                    ))
                    ) : (
                    <p>No recent games found.</p>
                    )}
                </div>
                </section>

    
              {/* News Feeds */}
              <section className="news-feed">
                <h2>News Feeds</h2>
                <div className="content-box">
                  <p>Display top 5 recent game news here...</p>
                </div>
              </section>
    
              {/* Friends List */}
              <section className="friends-list">
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
              </section>
            </main>
    
            {/* Footer */}
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
    
 export default HomePage;