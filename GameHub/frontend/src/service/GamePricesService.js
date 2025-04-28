// src/service/GamePricesService.js

// We'll keep a simple in-memory cache to avoid fetching the store list every time
let storeMapCache = null;

/**
 * Grabs the store list from CheapShark, returning a map { "1": "Steam", ... }.
 */
async function getStoreMap() {
  // If we already cached store data, reuse it
  if (storeMapCache) {
    return storeMapCache;
  }

  const storeUrl = 'https://www.cheapshark.com/api/1.0/stores';
  const storeResponse = await fetch(storeUrl);
  if (!storeResponse.ok) {
    throw new Error("Failed to fetch store list");
  }
  const storeList = await storeResponse.json(); 
  // storeList is an array of { storeID, storeName, isActive, images:{} }

  // Build the map object
  const map = {};
  storeList.forEach((store) => {
    map[store.storeID] = store.storeName;
  });

  storeMapCache = map; // cache it in memory
  return storeMapCache;
}

/**
 * Searches CheapShark for a given game name, grabs the first match’s gameID,
 * fetches the detailed deals info for that gameID,
 * and replaces storeID with storeName in deals.
 */
export const getGamePrices = async ({ gameName }) => {
  try {
    // (A) Search for the game by name
    const searchUrl = `https://www.cheapshark.com/api/1.0/games?title=${encodeURIComponent(gameName)}`;
    const searchResponse = await fetch(searchUrl);
    if (!searchResponse.ok) {
      throw new Error("Search request failed");
    }
    const searchResults = await searchResponse.json();
    if (!Array.isArray(searchResults) || searchResults.length === 0) {
      // No matches found
      return null;
    }
    const cheapSharkGameId = searchResults[0].gameID;

    // (B) Fetch the deals detail for that CheapShark game ID
    const detailUrl = `https://www.cheapshark.com/api/1.0/games?id=${cheapSharkGameId}`;
    const detailResponse = await fetch(detailUrl);
    if (!detailResponse.ok) {
      throw new Error("Detail request failed");
    }
    const detailResults = await detailResponse.json(); 

    // (C) Get the store map and inject storeName into each deal
    const storeMap = await getStoreMap();
    if (detailResults.deals && Array.isArray(detailResults.deals)) {
      detailResults.deals = detailResults.deals.map((deal) => ({
        ...deal,
        storeName: storeMap[deal.storeID] || "Unknown Store",
      }));
    }

    return detailResults;
  } catch (error) {
    console.error("Error fetching game prices:", error);
    return null;
  }
};
