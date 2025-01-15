package com.project.VisitlyApp.service;

import com.project.VisitlyApp.entity.Item;
import com.project.VisitlyApp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repository;

    @Autowired
    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> getAllItems() {
        return repository.findAll();
    }

    public Item saveItem(Item item) {
        return repository.save(item);
    }

    public void deleteItem(Long id) {
        repository.deleteById(id);
    }
}
