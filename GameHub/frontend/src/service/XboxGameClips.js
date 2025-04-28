
const apiUrl = 'http://localhost:8080';

export const getGameClips = async() => {
    const uhs = localStorage.getItem("uhs");
    const XSTS_token = localStorage.getItem("XSTS_token");
    try{
        const response = await fetch(`${apiUrl}/api/xbox/gameclips`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `XBL3.0 x=${uhs};${XSTS_token}`,
            },
        });
        if(response.ok){
            const data = await response.json();
            return data;
        }
    }catch(error){
        console.error(error);
        console.error("Failed to fetch Xbox game clips.");
        throw error;
    }
};


export const getGameClipById = async (clipId) => {
    const uhs        = localStorage.getItem("uhs");
    const xstsToken  = localStorage.getItem("XSTS_token");
    const res = await fetch(`${apiUrl}/api/xbox/gameclips/${clipId}`, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `XBL3.0 x=${uhs};${xstsToken}`,
        "x-xbl-contract-version": "2"
      }
    });
    if (!res.ok) throw new Error("Clip not found");
    return res.json();
  };