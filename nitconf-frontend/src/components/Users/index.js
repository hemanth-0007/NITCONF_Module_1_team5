import React, { useState, useEffect } from "react";
import "./index.css";

const Books = () => {
  const [data, setData] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const response = await fetch("http://localhost:8080/users");
      if (!response.ok) {
        throw new Error("Failed to fetch data");
      }
      const jsonData = await response.json();
      console.log(jsonData);
      setData(jsonData);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  return (
    <div>
      <h2>Data from Server:</h2>
      {data && data.length > 0 ? (
        <ul>
          {data.map((item) => (
            <li key={item.id}>{item.name} - {item.role}</li>
            
          ))}
        </ul>
      ) : (
        <p>No data available</p>
      )}
    </div>
  );
};

export default Books;
