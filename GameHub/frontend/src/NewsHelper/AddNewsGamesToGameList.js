// src/NewsHelper/AddNewsGamesToGameList.js
const apiUrl = 'http://localhost:8080';
const jwt = localStorage.getItem("jwtToken");

function authHeader() {
  if (!jwt) throw new Error("Not authenticated");
  return { Authorization: `Bearer ${jwt}` };
}

/** 1️⃣ Save to My Games */
export async function AddNewsGamesToGameList({ id }) {
  const res = await fetch(`${apiUrl}/api/save-games/save-game`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...authHeader(),
    },
    body: JSON.stringify({ id }),
  });
  if (!res.ok) throw new Error(`Status ${res.status}`);
  return res.json();
}

/** 2️⃣ Save to Wishlist */
export async function AddToWishList({ id }) {
  const res = await fetch(`${apiUrl}/api/save-games/add-to-wishlist`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...authHeader(),
    },
    body: JSON.stringify({ id }),
  });
  if (!res.ok) throw new Error(`Status ${res.status}`);
  return res.json();
}

/** 3️⃣ List your collections */
export async function fetchCollections() {
  const res = await fetch(`${apiUrl}/api/save-games/get-collection`, {
    headers: authHeader(),
  });
  if (!res.ok) throw new Error(`Fetch collections failed: ${res.status}`);
  return res.json(); // → [{ id, name, owner… }, …]
}

/** 4️⃣ Create a new collection */
export async function createCollection(name) {
  const res = await fetch(`${apiUrl}/api/save-games/add-to-collection`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...authHeader(),
    },
    body: JSON.stringify({ name }),
  });
  if (!res.ok) throw new Error(`Create collection failed: ${res.status}`);
  const { collection } = await res.json();
  return collection; // → the created { id, name, … }
}

/** 5️⃣ Add a game to an existing collection */
export async function addGameToCollection(collectionId, gameId) {
  const res = await fetch(
    `${apiUrl}/api/save-games/${collectionId}/games`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...authHeader(),
      },
      body: JSON.stringify({ gameId }),
    }
  );
  if (!res.ok) throw new Error(`Add to collection failed: ${res.status}`);
  return res.json();
}
