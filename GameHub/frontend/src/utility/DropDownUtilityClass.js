import React, { useState, useRef, useEffect } from "react";
import '../assests/Dropdown.css';

const Dropdown = ({ options, defaultOption, label, onSelect }) => {
  const [open, setOpen] = useState(false);
  // Expect defaultOption to be an object too, e.g., { label: "Relevance", value: "-metacritic" }
  const [selected, setSelected] = useState(defaultOption);
  const dropDownRef = useRef(null);

  const toggle = () => setOpen(!open);

  const selectOption = (option) => {
    setSelected(option);
    setOpen(false);
    if (onSelect) onSelect(option.value);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropDownRef.current && !dropDownRef.current.contains(event.target)) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <div className="dropdown" ref={dropDownRef}>
      <button className="dropbtn" onClick={toggle}>
        {label}: <span>{selected.label}</span>
        <span className="arrow">&#x25BC;</span>
      </button>
      {open && (
        <div className="dropdown-content">
          {options.map((option, idx) => (
            <a key={idx} onClick={() => selectOption(option)}>
              {option.label}
            </a>
          ))}
        </div>
      )}
    </div>
  );
};

export default Dropdown;
