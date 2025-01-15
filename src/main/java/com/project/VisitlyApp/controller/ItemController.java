package com.project.VisitlyApp.controller;

import com.project.VisitlyApp.entity.Item;
import com.project.VisitlyApp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService service;

    // Get all items
    @GetMapping
    public List<Item> getAllItems() {
        return service.getAllItems();
    }

    // Create a new item
    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return service.saveItem(item);
    }

    // Update an existing item
    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        item.setId(id);
        return service.saveItem(item);
    }

    // Delete an item
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
    }
}
