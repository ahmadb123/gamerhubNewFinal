import React, { Component } from "react";
import { toast } from "react-toastify";
import "../assests/HomePage.css";
import { fetchXboxProfile, fetchPSNProfile, fetchSteamProfile } from "../service/profileService";

class HomePage extends Component {
    state = {
        accountInfo: null,
        platform: null,
        isFetching: true, // Add a loading state
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

        this.setState({ platform }, async () => {
            console.log(`Fetching profile for platform: ${platform}`);
            try {
                const accountInfo = await fetchProfile();
                console.log("Fetched account info:", accountInfo);
                this.setState({ accountInfo, isFetching: false });
            } catch (error) {
                console.error(error);
                localStorage.removeItem("platform"); // Clear invalid platform
                toast.error("Failed to fetch profile information.");
                window.location.href = "/";
            }
        });
    }

    render() {
        const { accountInfo, platform, isFetching } = this.state;

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
                            src={accountInfo.appDisplayPicRaw || accountInfo.avatar}
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
                  <div className="content-box">
                    <p>Display recent games played here...</p>
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
                    <p>Display user friends list here...</p>
                  </div>
                </section>
              </main>
        
              {/* Footer */}
              <footer className="footer">
                <p>About: Created by Ahmad Bishara</p>
                <a href="https://github.com/ahmadb123" target="_blank" rel="noopener noreferrer">GitHub: ahmadb123</a>
              </footer>
            </div>
          );
        }        
        
}

export default HomePage;
