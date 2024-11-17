import logo from './logo.svg';
import './App.css';
import React, { useState, useEffect } from "react";
import { ReactFlow } from '@xyflow/react';
import axios from 'axios'

function App() {
  const [elements, setElements] = useState([]);
  axios.defaults.baseURL = 'http://localhost:8443';

  useEffect(() => {
    // Workflow-Daten vom Backend laden
    axios.get("/nifi/workflows").then((response) => {
      const workflows = response.data.processors.map((processor) => ({
        id: processor.id,
        data: { label: processor.name },
        position: { x: Math.random() * 250, y: Math.random() * 250 },
      }));
      setElements(workflows);
    });
  }, []);

  const onLoad = (reactFlowInstance) => {
    reactFlowInstance.fitView();
  };

  return (
    <div style={{ height: 600 }}>
      <ReactFlow elements={elements} onLoad={onLoad} />
    </div>
  );
}

export default App;
