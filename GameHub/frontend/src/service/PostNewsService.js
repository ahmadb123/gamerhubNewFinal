const apiUrl = "http://localhost:8080";

export const postNews = async (contentText, sharedNewsId, sharedClipsId = null) => {
  const token = localStorage.getItem("jwtToken"); // Get the JWT token from local storage

  if (!token) {
    console.error("User is not authenticated");
    return { success: false, message: "User is not authenticated" };
  }

  const postData = {
    contentText,
    sharedNewsId,
    sharedClipsId,
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
