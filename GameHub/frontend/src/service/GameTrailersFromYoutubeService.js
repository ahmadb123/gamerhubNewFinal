import React from "react";

const apiUrl = 'http://localhost:8080';


export const fetchGameTrailers = async ({gameName}) => {
    try{
        const response = await fetch(`${apiUrl}/api/game-trailers/game-trailers/${gameName}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });
        if(response.ok){
            const data = await response.json();
            return data || []
        }else{
            throw new Error(`Failed to fetch game trailers. Status: ${response.status}`);
        }
    }catch(error){
        console.error(error);
        console.error("Failed to fetch game trailers.");
        throw error;
    }
};