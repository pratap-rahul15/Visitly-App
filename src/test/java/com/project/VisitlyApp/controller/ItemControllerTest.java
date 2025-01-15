package com.project.VisitlyApp.controller;

import com.project.VisitlyApp.entity.Item;
import com.project.VisitlyApp.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)  // Use WebMvcTest to load only the web layer
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService; // Mock the ItemService bean

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1L);
        item.setName("Item 1");
        item.setDescription("Description of Item 1");
    }

    @Test
    void testGetAllItems() throws Exception {
        when(itemService.getAllItems()).thenReturn(Arrays.asList(item));

        MvcResult result = mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Item 1"))
                .andExpect(jsonPath("$[0].description").value("Description of Item 1"))
                .andReturn();

        verify(itemService, times(1)).getAllItems();
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.saveItem(any(Item.class))).thenReturn(item);

        mockMvc.perform(post("/api/items")
                        .contentType("application/json")
                        .content("{\"name\":\"Item 1\", \"description\":\"Description of Item 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item 1"))
                .andExpect(jsonPath("$.description").value("Description of Item 1"));

        verify(itemService, times(1)).saveItem(any(Item.class));
    }

    @Test
    void testDeleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(1L);

        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isOk());

        verify(itemService, times(1)).deleteItem(1L);
    }
}
