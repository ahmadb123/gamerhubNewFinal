# GamerHub  
A unified platform for gamers to connect, share, and stay up-to-date with the latest in gaming.

---

## Table of Contents
- [About The Project](#about-the-project)  
- [Features](#features)  
- [Tech Stack](#tech-stack)  
- [Getting Started](#getting-started)  
  - [Prerequisites](#prerequisites)  
  - [Installation](#installation)  
- [Usage](#usage)  
- [Configuration](#configuration)  
- [Directory Structure](#directory-structure)  
- [Contact](#contact)  
- [Acknowledgments](#acknowledgments)  

---

## About The Project
GamerHub brings together your favorite gaming platforms—Xbox, PlayStation, Steam, Discord, and more—into one place.  
Create or join chats and groups, share news and game info, plan events, and discover up-to-date trailers and reviews.

---

## Features
- **Gamertag-Based Login**  
  Authenticate via Xbox, PlayStation, Steam, or a custom gamertag. Link multiple accounts under one profile.  
- **Chats & Groups**  
  Topic-based chat rooms (e.g., *Elden Ring Players*) with role management (admins, moderators) and threaded discussions.  
- **Centralized News Feed**  
  Aggregates updates from Xbox News, PlayStation Blog, Steam News, etc., with filters for platform, genre, and personal preferences.  
- **Game Info & Trailers**  
  Searchable database powered by IGDB for release dates, trailers, and reviews. Shareable game pages directly in chat.  
- **Community & Gamification**  
  Real-time notifications, badges for top contributors, in-chat event scheduling, and tournament planning.

---

## Tech Stack
- **Frontend**: React.js, Tailwind CSS  
- **Backend**: Java 17, Spring Boot  
- **Database**: MySQL  
- **Real-Time**: STOMP over SockJS (WebSockets)  
- **APIs & Authentication**:  
  - IGDB for game metadata  
  - Steam OpenID, Xbox OAuth2, Discord OAuth2  
  - YouTube API for trailers  
  - Platform-specific news endpoints  

---

## Getting Started

### Prerequisites
- Node.js v16+ & npm  
- Java 17+ & Maven  
- MySQL (local or remote)

### Installation

1. **Clone the repo**  
   ```bash
   git clone https://github.com/ahmadb123/GamerHub.git
   cd GamerHub
   cd backend
    Edit src/main/resources/application.properties (see Configuration below)
    mvn clean package
    mvn spring-boot:run
   Backend runs on http://localhost:8080
2. ##FrontEnd
   ```bash
    cd ../frontend
    npm install
    npm start
    Frontend runs on http://localhost:3000
 

  
## Usage
- **Sign up / Log in via your preferred gamertag.
- **Browse or create chat rooms and groups.
- ##Follow news feeds and share articles.
- ##Search for games, view trailers, and discuss in real time.

##Configuration

      Database
      spring.datasource.url=jdbc:mysql://localhost:3306/gamerhub
      spring.datasource.username=YOUR_DB_USER
      spring.datasource.password=YOUR_DB_PASS
      spring.jpa.hibernate.ddl-auto=update
    

    # JWT
    jwt.secret=your_jwt_secret_key
    jwt.expiration=86400000

    Directory Structure
    - ## GamerHub/
        ├── backend/               # Spring Boot service
        │   ├── src/
        │   │   ├── main/
        │   │   │   ├── java/...   # controllers, services, models, repos
        │   │   │   └── resources/
        │   │   │       └── application.properties
        │   └── pom.xml
        └── frontend/              # React application
            ├── public/
            ├── src/
            │   ├── components/
            │   ├── service/
            │   ├── App.jsx
            │   └── index.jsx
            └── package.json


## Contact
- **Author**: Ahmad Bishara  
- **Email**: [abishara@mail.endicott.edu](mailto:abishara@mail.endicott.edu)  
- **GitHub**: [ahmadb123](https://github.com/ahmadb123)


## Acknowledgments
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MySql](https://www.mysql.com/)
- [React.js](https://reactjs.org/)
- [JWT](https://jwt.io/)
- [Gradle](https://gradle.org/)



  


