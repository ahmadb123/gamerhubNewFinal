import React from "react";

const apiUrl = 'http://localhost:8080';

export const searchUserProfile = async username =>{
    const jwtToken = localStorage.getItem("jwtToken");
    if(jwtToken === null){
        console.error("User is not authenticated");
        return {success: false, message: "User is not authenticated"};
    }
    try{
        
        // encodedURI component esnures that the username is properly encoded
        const response = await fetch(`${apiUrl}/api/search?username=${encodeURIComponent(username)}`, {
            method: "GET",
            headers:{
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + jwtToken,
            },
        });
        if(!response.ok){
            throw new Error(`Failed to search user profile. Status: ${response.status}`);
        }
        const data = await response.json();
        const profile = data.profile;
        const friends = data.friends;
        return {success: true, profile, friends};
    }catch(error){
        console.error(error);
        throw error;
    }
}