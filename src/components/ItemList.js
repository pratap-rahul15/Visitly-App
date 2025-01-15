import React, { useState, useEffect } from 'react';
import { ListGroup, Button, Container } from 'react-bootstrap';
import AddItemForm from './AddItemForm';

const ItemList = () => {
  const [items, setItems] = useState([]);
  const [editItem, setEditItem] = useState(null); 

const fetchItems = async () => {
  
  // We can retrieve the token from local storage
  const token = localStorage.getItem('token'); 
  const response = await fetch('http://localhost:8080/api/items', {
    headers: {
      Authorization: `Bearer ${token}`, 
    },
  });
  if (response.ok) {
    const data = await response.json();
    setItems(data);
  } else {
    alert('Failed to fetch items. Please login again.');
  }
};

const deleteItem = async (id) => {
  const token = localStorage.getItem('token');
  const response = await fetch(`http://localhost:8080/api/items/${id}`, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (response.ok) {
    fetchItems();
  } else {
    alert('Failed to delete item. Ensure you are authorized.');
  }
};

  // Edit an item
  const editItemHandler = (item) => {
    setEditItem(item);
  };

  // Initial fetch of items
  useEffect(() => {
    fetchItems();
  }, []);

  return (
    <Container className="py-4">
      <h2 className="text-center mb-4" style={{ color: '#4A90E2' }}>
        Item List
      </h2>
      <ListGroup className="shadow-sm mb-4">
        {items.length > 0 ? (
          items.map((item) => (
            <ListGroup.Item
              key={item.id}
              className="d-flex justify-content-between align-items-center"
              style={{ backgroundColor: '#F7F9FC', borderColor: '#DDE5ED' }}
            >
              <div>
                <strong style={{ color: '#4A90E2' }}>{item.name}</strong>: {item.description}
              </div>
              <div>
                <Button
                  variant="info"
                  size="sm"
                  onClick={() => editItemHandler(item)}
                  className="mx-1"
                >
                  Edit
                </Button>
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => deleteItem(item.id)}
                  className="mx-1"
                >
                  Delete
                </Button>
              </div>
            </ListGroup.Item>
          ))
        ) : (
          <ListGroup.Item>No items available</ListGroup.Item>
        )}
      </ListGroup>
      {editItem && <AddItemForm item={editItem} setEditItem={setEditItem} />}
    </Container>
  );
};

export default ItemList;
