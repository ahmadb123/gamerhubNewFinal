import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';
import '../assests/AuthPage.css'; // Ensure you have a CSS file for styling
const apiUrl = 'http://localhost:8080';

function AuthPage(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const navigate = useNavigate();
    const register = async(e) =>{
        e.preventDefault();
        try{
            const response = await fetch(`${apiUrl}/api/auth/register`, {
                method: "POST",
                headers : {
                    "Content-Type": "application/json",
                },
                body : JSON.stringify({
                    username,password,email
                }),
            });
        if(response.ok){
            console.log("complete success");
            navigate("/platform-select"); // After registration
        }
        else{
            const data = await response.json();
            console.error("error" + data);
        }
        }catch(e){
            console.error("Error", e);
        }
    };
    return (
        <div className="register-container">
            <h2>Create Account</h2>
            <form onSubmit={register}>
                <div className="form-group">
                    <label>Username</label>
                    <input
                        type="text"
                        placeholder="Username (e.g. John123)"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>

                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        placeholder="Password (e.g. Xb12@00)"
                        value={password}
                        id="passwordInput"
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>

                <div className="form-group">
                    <label>Email Address</label>
                    <input
                        type="text"
                        placeholder="Email (e.g. john@example.com)"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
            
                <button type="submit" className="submit-button">
                    Register
                </button>
            </form>
        </div>
    );
}

export default AuthPage;