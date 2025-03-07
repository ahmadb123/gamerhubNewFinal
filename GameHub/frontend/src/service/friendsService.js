
const apiUrl = 'http://localhost:8080';


// fetch xbox friends - 

export const fetchXboxFriends = async () =>{
    // uhs and token - 
    const uhs = localStorage.getItem("uhs");
    const XSTS_token = localStorage.getItem("XSTS_token");
    console.log("XBOX FRIENFS FETCH tokens - UHS:", uhs, "XSTS_token:", XSTS_token);

    if (!uhs || !XSTS_token) {
        console.error("Xbox authentication tokens missing. Please log in again.");
        throw new Error("Xbox authentication tokens missing.");
    }

    try{
        const response = await fetch(`${apiUrl}/api/xbox/friends/top-ten`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `XBL3.0 x=${uhs};${XSTS_token}`,
            },
        });
        if(!response.ok){
            throw new Error(`Failed to fetch Xbox friends. Status: ${response.status}`);
        }
        const data = await response.json();
        return data.people || [];
    }catch(error){
        console.error(error);
        console.error("Failed to fetch Xbox friends.");
        throw error;
    }
};