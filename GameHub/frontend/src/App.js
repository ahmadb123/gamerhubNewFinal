// App.js
import React from "react";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import AppRoutes from "./Routes";

export default function App() {
  return (
    <>
      <ToastContainer position="top-right" autoClose={3000} />
      <AppRoutes />
    </>
  );
}
