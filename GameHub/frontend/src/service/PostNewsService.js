const apiUrl = "http://localhost:8080";


// function to post news to the community insight
export const postNews = async (
  contentText,
  sharedNewsId    = null,
  sharedClipsId   = null,
  sharedClipsUrl  = null
) => { 
  const token = localStorage.getItem("jwtToken"); // Get the JWT token from local storage

  if (!token) {
    console.error("User is not authenticated");
    return { success: false, message: "User is not authenticated" };
  }

  const postData ={
    contentText,
    sharedNewsId,
    sharedClipsId,
    sharedClipsUrl
  };

  try {
    const response = await fetch(`${apiUrl}/api/community-insight/post-news`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer ' + token,
    },
      body: JSON.stringify(postData),
    });

    if (response.ok) {
      const data = await response.json();
      return { success: true, data }; // Return success and response data
    } else {
      const error = await response.text();
      console.error("Failed to share post. Status:", response.status, error);
      return { success: false, message: error };
    }
  } catch (error) {
    console.error("Error sharing post:", error);
    return { success: false, message: error.message };
  }
};

// function to get all news from the community insight

export const getAllNews = async () => {
  try{
    const response = await fetch(`${apiUrl}/api/community-insight/news/all`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
    if(response.ok){
      const data = await response.json();
      return data;
    }else{
      const error = await response.text();
      console.error("Failed to fetch news. Status:", response.status, error);
      return { success: false, message: error };
    }
  }catch(error){
    console.error("Error fetching news:", error);
    return { success: false, message: error.message };
  }
};


// reply to publisher - 

export const replyToPublisher = async ({ newsId, replyMessage, postImage, contentText }) => {
  const jwtToken = localStorage.getItem("jwtToken"); 
  try {
    const response = await fetch(`${apiUrl}/api/community-insight/news/reply`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + jwtToken,
      },
      body: JSON.stringify({ 
        newsId,
        reply: replyMessage, // <-- send 'replyMessage' as 'reply'
        backgroundImage: postImage,             // new field
        contentText            // new field
      }),
    });
    if (!response.ok) {
      const error = await response.text();
      console.error("Failed to reply to publisher. Status:", response.status, error);
      return { success: false, message: error };
    }
    const data = await response.json();
    return { success: true, ...data };
  } catch (error) {
    console.error("Error replying to publisher:", error);
    return { success: false, message: error.message };
  }
};



export const likePost = async (postId) => {
  const token = localStorage.getItem("jwtToken");
  const res = await fetch(
    `${apiUrl}/api/community-insight/news/${postId}/like`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    }
  );
  if (!res.ok) throw new Error("Failed to like post");
  const { totalLikes } = await res.json();
  return totalLikes;
};

export const addComment = async (postId, commentText) => {
  const token = localStorage.getItem("jwtToken");
  const res = await fetch(
    `${apiUrl}/api/community-insight/news/${postId}/comment`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
      body: JSON.stringify({ comment: commentText }),
    }
  );
  if (!res.ok) throw new Error("Failed to add comment");
  // Returns: { id, user, text, time }
  return await res.json();
};

export const getComments = async (postId) => {
  const res = await fetch(
    `${apiUrl}/api/community-insight/news/${postId}/comments`
  );
  if (!res.ok) throw new Error("Failed to fetch comments");
  return await res.json(); // array of { user, text, time }
};