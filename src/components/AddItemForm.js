import React, { useState, useEffect } from 'react';
import { Button, Form } from 'react-bootstrap';

const AddItemForm = ({ item, setEditItem }) => {
  const [name, setName] = useState(item ? item.name : '');
  const [description, setDescription] = useState(item ? item.description : '');
  const [isEdit, setIsEdit] = useState(item ? true : false); 


  useEffect(() => {
    if (item) {
      setName(item.name);
      setDescription(item.description);
    }
  }, [item]);

  // Hnadle Form
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!name || !description) return; 

    const payload = { name, description };
    let response;
    
    if (isEdit) {

      
      response = await fetch(`http://localhost:8080/api/items/${item.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });
    } else {
     
      response = await fetch('http://localhost:8080/api/items', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });
    }

    if (response.ok) {
      alert(isEdit ? 'Item updated successfully!' : 'Item added successfully!');
      setName('');
      setDescription('');
      setIsEdit(false);
      setEditItem(null); 
    } else {
      alert('Failed to add/update item');
    }
  };

  return (
    <div>
      <h2>{isEdit ? 'Edit Item' : 'Add New Item'}</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group>
          <Form.Label>Name</Form.Label>
          <Form.Control
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Enter item name"
          />
        </Form.Group>
        <Form.Group>
          <Form.Label>Description</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Enter item description"
          />
        </Form.Group>
        <Button variant="primary" type="submit">
          {isEdit ? 'Update Item' : 'Add Item'}
        </Button>
      </Form>
    </div>
  );
};

export default AddItemForm;
