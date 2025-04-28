import React from 'react';
import { useNavigate } from 'react-router-dom';

function WelcomePage() {
  const navigate = useNavigate();

  return (
    <div className="welcome-page">
      <h1>Welcome to GamerHub</h1>
      <p>Connect with gamers worldwide, share gaming news, and join the community!</p>
      <div className="auth-buttons">
        <button onClick={() => navigate('/auth')}>Register</button>
        <button onClick={() => navigate('/login')}>Login</button>
      </div>
    </div>
  );
}

export default WelcomePage;