import React, { useState, useEffect } from "react";
import { getAllLinkedProfiles } from "../service/UserLinkedProfiles";
import {
  updateBio,
  getUserBio,
  updateUsername,
  getUsername,
  updatePassword,
} from "../service/profileService";
// Import your shared stylesheet for consistent styling
import "../assests/HomePage.css";

function UserProfile() {
  const [linkedProfiles, setLinkedProfiles] = useState([]);
  const [userBio, setUserBio] = useState("");
  const [username, setUsername] = useState("");
  const [usernameInput, setUsernameInput] = useState("");
  const [isEdit, setIsEdit] = useState(false);

  // States for password change form
  const [currentPasswordInput, setCurrentPasswordInput] = useState("");
  const [newPasswordInput, setNewPasswordInput] = useState("");

  // Toggle for Manage Accounts
  const [showManageAccounts, setShowManageAccounts] = useState(false);

  useEffect(() => {
    async function fetchData() {
      try {
        // Fetch user’s bio
        const bio = await getUserBio();
        setUserBio(bio.bio);

        // Fetch linked profiles
        const results = await getAllLinkedProfiles();
        // Fetch primary username
        const fetchedUsername = await getUsername();

        setUsername(fetchedUsername);
        setUsernameInput(fetchedUsername);
        setLinkedProfiles(results);
      } catch (error) {
        console.error("Error fetching profile data:", error);
      }
    }

    fetchData();
  }, []);

  // Toggle form editing
  const handleEditBtnClick = () => {
    setIsEdit(!isEdit);
  };

  // Handlers for input changes
  const handleBioChange = (e) => {
    setUserBio(e.target.value);
  };

  const handleUsernameChange = (e) => {
    setUsernameInput(e.target.value);
  };

  const handleToggleManageAccounts = () => {
    setShowManageAccounts(!showManageAccounts);
  };

  // Dummy placeholders (replace with real logic)
  const handleLinkAccount = () => {
    alert("Link account functionality not implemented yet");
  };

  const handleUnlinkAccount = (accountId) => {
    alert(`Unlink account with id: ${accountId} not implemented yet`);
  };

  // Update Bio
  const handleBioUpdate = async (e) => {
    e.preventDefault();
    if (!isEdit) return;
    try {
      const updatedBio = await updateBio(userBio);
      setUserBio(updatedBio);
      setIsEdit(false);
    } catch (error) {
      console.error("Error updating user bio:", error);
    }
  };

  // Update Username
  const handleUsernameUpdate = async (e) => {
    e.preventDefault();
    if (!isEdit) return;
    try {
      const updatedName = await updateUsername(usernameInput);
      setUsername(updatedName);
      setIsEdit(false);
    } catch (error) {
      console.error("Error updating username:", error);
    }
  };

  // Update Password
  const handleUpdatePassword = async (e) => {
    e.preventDefault();
    if (!isEdit) return;
    try {
      // Check for same password
      if (currentPasswordInput === newPasswordInput) {
        alert("Current and new password cannot be the same.");
        return;
      }
      // Check for empty fields
      if (!currentPasswordInput || !newPasswordInput) {
        alert("Please enter both current and new password.");
        return;
      }
      await updatePassword({
        currentPassword: currentPasswordInput,
        newPassword: newPasswordInput,
      });
      setCurrentPasswordInput("");
      setNewPasswordInput("");
      setIsEdit(false);
      alert("Password updated successfully!");
    } catch (error) {
      alert("Error updating password: " + error.message);
    }
  };

  return (
    <div className="container" style={{ marginTop: "2rem" }}>
      <h1>User Profile</h1>

      {/* Username Section */}
      {isEdit ? (
        <form onSubmit={handleUsernameUpdate} style={{ marginBottom: "1rem" }}>
          <input
            type="text"
            value={usernameInput}
            onChange={handleUsernameChange}
            placeholder="Update username..."
          />
          <button type="submit" className="nav-button">
            Update Username
          </button>
        </form>
      ) : (
        <p>
          <strong>Username:</strong> {username || "No username set"}
        </p>
      )}

      {/* Bio Section */}
      {isEdit ? (
        <form onSubmit={handleBioUpdate} style={{ marginBottom: "1rem" }}>
          <textarea
            value={userBio}
            onChange={handleBioChange}
            placeholder="Enter your bio here..."
            rows={4}
            cols={50}
          />
          <br />
          <button type="submit" className="nav-button">
            Update Bio
          </button>
        </form>
      ) : (
        <p>
          <strong>Bio:</strong> {userBio || "No bio set"}
        </p>
      )}

      {/* Password Section */}
      {isEdit ? (
        <form onSubmit={handleUpdatePassword}>
          <input
            type="password"
            value={currentPasswordInput}
            onChange={(e) => setCurrentPasswordInput(e.target.value)}
            placeholder="Current password"
            style={{ marginRight: "1rem" }}
          />
          <input
            type="password"
            value={newPasswordInput}
            onChange={(e) => setNewPasswordInput(e.target.value)}
            placeholder="New password"
            style={{ marginRight: "1rem" }}
          />
          <button type="submit" className="nav-button">
            Update Password
          </button>
        </form>
      ) : (
        <p>
          <strong>Password:</strong> ********
        </p>
      )}

      {/* Edit / Cancel Button */}
      <button onClick={handleEditBtnClick} className="nav-button">
        {isEdit ? "Cancel" : "Edit"}
      </button>

      {/* Manage Accounts */}
      <button
        onClick={handleToggleManageAccounts}
        className="nav-button"
        style={{ marginLeft: "1rem" }}
      >
        {showManageAccounts ? "Hide Manage Accounts" : "Manage Accounts"}
      </button>

      {showManageAccounts && (
        <div style={{ marginTop: "2rem" }}>
          <h2>Linked Accounts</h2>
          {linkedProfiles.length ? (
            linkedProfiles.map((account) => (
              <div key={account.id} style={{ marginBottom: "0.5rem" }}>
                <span>
                  {account.platform}: {account.username}
                </span>
                <button
                  onClick={() => handleUnlinkAccount(account.id)}
                  className="nav-button"
                  style={{ marginLeft: "1rem" }}
                >
                  Unlink
                </button>
              </div>
            ))
          ) : (
            <p>No linked accounts.</p>
          )}
          <button onClick={handleLinkAccount} className="nav-button">
            Link New Account
          </button>
        </div>
      )}
    </div>
  );
}

export default UserProfile;
