const apiUrl = 'http://localhost:8080';

const jwtToken = localStorage.getItem("jwtToken");

// get user all linked profiles->

export const getAllLinkedProfiles = async () => {
    try{
        const response = await fetch(`${apiUrl}/api/user/linked-profiles/getLinkedProfiles`, {
            method: "GET",
            headers : {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + jwtToken,
            },
        });
        if(!response.ok){
            throw new Error(`Failed to fetch all linked profiles. Status: ${response.status}`);
        }
        const data = await response.json();
        console.log("linkedACCOUNTS: data", data);
        return data;
    }catch(error){
        console.error(error);
        throw error;
    }
};