// src/pages/LoginPage.js
import React, { useState } from 'react';
import { useLocation } from 'react-router'

import { useNavigate } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const apiUrl = 'http://localhost:8080';

function togglePassword(){
    let password = document.getElementById("passwordInput");
    if(password.type === "password"){
        password.type = "text";
    }else{
        password.type = "password";
    }
}

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const location = useLocation(); // get current location


    const handleLogin = async (e) => {        
        e.preventDefault();
        try {
            const response = await fetch(`${apiUrl}/api/auth/login`, {
                method: 'POST',
                headers: { 'Content-type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });
            if(response.ok) {
                const data = await response.json();
                const token = data.token;
                const userId = data.id; 
                const username = data.username;
                localStorage.setItem('jwtToken', token);
                localStorage.setItem('username', username);
                localStorage.setItem('userId', userId);
                navigate('/landingpage')
            } else if(response.status === 401) {
                toast.error('Incorrect Password. Please try again.');
            } else {
                const textError = await response.text();
                toast.error(`Login failed: ${textError}`);
            }
        } catch (e) {
            console.error("Error", e);
            toast.error('Error occurred logging in');
        }
    };

    return (
        <div className="login-container">
            <h2> Sign in </h2>
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    <label>Email or Username</label>
                    <input 
                        type="text"
                        placeholder="Email/Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input 
                        type="password"
                        placeholder="Enter your password"
                        value={password}
                        id="passwordInput"
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div className="show-password">
                    <input type="checkbox" onClick={togglePassword} />
                    <label>Show Password</label>
                </div>
                <button type="submit" className="submit-button">Login</button>
            </form>
            <ToastContainer position="top-center" />
        </div>
    );
}

export default LoginPage;
