// // SavedGamesFolder.js
// import React, { useState } from "react";
// import SavedGamesView from "./SavedGamesView";
// import { FaFolder } from "react-icons/fa"; // using FontAwesome folder icon
// import "../component/savedGameFolder.css";

// const SavedGamesFolder = () => {
//   const [isOpen, setIsOpen] = useState(false);

//   const toggleFolder = () => {
//     setIsOpen(!isOpen);
//   };

//   return (
//     <div className="saved-games-folder">
//       <div className="folder-header" onClick={toggleFolder} style={{ cursor: "pointer", display: "flex", alignItems: "center" }}>
//         <FaFolder size={50} style={{ marginRight: "0.5rem" }} />
//         <span>My Saved Games</span>
//       </div>
//       {isOpen && (
//         <div className="folder-content">
//           <SavedGamesView />
//         </div>
//       )}
//     </div>
//   );
// };

// export default SavedGamesFolder;
