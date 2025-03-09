import React from "react";

const apiUrl = 'http://localhost:8080';

const jwt = localStorage.getItem("jwtToken");
export const AddNewsGamesToGameList = async ({id}) => {
    if(!jwt){
        console.error("User is not authenticated");
        return {success: false, message: "User is not authenticated"};
    }
    try{
        const response = await fetch(`${apiUrl}/api/save-games/save-game`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + jwt,
        },
        body: JSON.stringify({id}),
    });
    if(!response.ok){
        throw new Error(`Failed to save game. Status: ${response.status}`);
    }
    const data = await response.json();
    return {success: true, message: "Game saved successfully"};

    }catch(error){
        console.error(error);
        throw error;
    }
};

export const AddToWishList = ({game}) => {
    // api call to save..
    return (
        <div>
            saved game
        </div>
    )
};

export const SaveToCollection = ({collection , game}) => {
    // api call to save..
    return (
        <div>
            saved game to collection
        </div>
    )
};