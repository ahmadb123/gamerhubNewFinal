const apiUrl = 'http://localhost:8080';

// fetch news- 

// recent news for the homepage - 

export const fetchRecentNews = async () =>{
    try{
        const response = await fetch(`${apiUrl}/api/news/recent-news`, {
            method: "GET",
            headers : {
                "Content-Type": "application/json",
            },
        });
        if(!response.ok){
            throw new Error(`Failed to fetch recent news. Status: ${response.status}`);
        }
        const data = await response.json();
        const flattened = data.flatMap(item => item.results || []);
        return flattened;   
    }catch(error){
        console.error(error);
        throw error;
    }
};