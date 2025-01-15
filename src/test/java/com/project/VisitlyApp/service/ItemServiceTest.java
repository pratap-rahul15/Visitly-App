package com.project.VisitlyApp.service;

import com.project.VisitlyApp.entity.Item;
import com.project.VisitlyApp.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        item = new Item();
        item.setId(1L);
        item.setName("Item 1");
        item.setDescription("Description of Item 1");
    }

    @Test
    void testGetAllItems() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));

        var items = itemService.getAllItems();

        assertNotNull(items);
        assertEquals(1, items.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testSaveItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item savedItem = itemService.saveItem(item);

        assertNotNull(savedItem);
        assertEquals("Item 1", savedItem.getName());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void testDeleteItem() {
        doNothing().when(itemRepository).deleteById(1L);

        itemService.deleteItem(1L);

        verify(itemRepository, times(1)).deleteById(1L);
    }
}
