import React from 'react';
import AddItemForm from './components/AddItemForm';
import ItemList from './components/ItemList';
import { Container } from 'react-bootstrap';
import './App.css';

function App() {
  return (
    <Container>
      <h1>Item Management</h1>
      <AddItemForm />
      <ItemList />
    </Container>
  );
}

export default App;
