// import React, { useEffect, useState } from "react";
// import { toast } from "react-toastify";

// const apiUrl = 'http://localhost:8080';

// function LandingPage() {
//     const [accountInfo, setAccountInfo] = useState(null);

//     useEffect(() => {
//         const uhs = localStorage.getItem("uhs");
//         const XSTS_token = localStorage.getItem("XSTS_token");

//         if (uhs && XSTS_token) {
//             // User chose to sign in using Xbox - fetch profile
//             fetchProfile(uhs, XSTS_token);
//         } else {
//             toast.error("User signed in with a different platform or login error occurred.");
//         }
//     }, []);

//     // Fetch profile data
//     const fetchProfile = async (uhs, XSTS_token) => {
//         try {
//             const response = await fetch(`${apiUrl}/api/xbox/profile`, {
//                 method: "GET",
//                 headers: {
//                     "Content-Type": "application/json",
//                     Authorization: `XBL3.0 x=${uhs};${XSTS_token}`,
//                 },
//                 credentials: "include",
//             });

//             if (!response.ok) {
//                 toast.error("Error occurred while fetching profile data.");
//                 return; // Stop further execution
//             }

//             const data = await response.json(); // Extract account data from JSON
//             // if success navigate - 
//             navigate("/home"); // Redirect to the homepage

//             // save platform info 
//             localStorage.setItem("platform", "XBOX");
//             // save the date in local storage - 
//             localStorage.setItem("profileData", JSON.stringify(data));
//         } catch (error) {
//             console.error(error);
//             toast.error("Error fetching account information.");
//         }
//     };

//     return (
//         <div className="fetching-profile" style={{ textAlign: "center", marginTop: "50px" }}>
//             {/* Render account info / profile */}
//             <h1>Test Profile:</h1>
//             {accountInfo ? (
//                 <div>
//                     <h2>GAMERTAG: {accountInfo.gamertag}</h2>
//                     <h2>DISPLAY NAME: {accountInfo.gameDisplayName}</h2>
//                     <img
//                         src={accountInfo.appDisplayPicRaw}
//                         alt="Profile Pic"
//                         style={{ borderRadius: "50%", width: "100px", height: "100px" }}
//                     />
//                 </div>
//             ) : (
//                 <p>Loading profile information...</p>
//             )}
//         </div>
//     );
// }

// export default LandingPage;
