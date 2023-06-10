package com.example.project;

import com.example.project.controller.DishController;
import com.example.project.dto.dish.DishDTO;
import com.example.project.model.Dish;
import com.example.project.service.IDishService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DishControllerTests {

    @InjectMocks
    private DishController dishController;

    @Mock
    private IDishService dishService;

    @Test
    public void testGetAllDishes() throws Exception {
        DishDTO dish = new DishDTO();
        dish.setSearchId("1");
        List<DishDTO> expectedDishes = List.of(
                dish
        );
        Mockito.when(dishService.getAllDishes()).thenReturn(expectedDishes);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(dishController).build();

        Assertions.assertEquals(mockMvc.perform(get("/api/dish/"))
                .andExpect(status().isOk())
                .andReturn().getAsyncResult(), ResponseEntity.ok(expectedDishes));

        Mockito.verify(dishService, Mockito.times(1)).getAllDishes();
    }

    @Test
    public void testGetDishBySearchId() throws Exception {
        Dish dish = new Dish();
        DishDTO dishDTO = new DishDTO();
        dish.setSearchId("1");
        dishDTO.setSearchId("1");
        Mockito.when(dishService.getDishBySearchId("1")).thenReturn(dish);
        Mockito.when(dishService.getDishDto(dish)).thenReturn(dishDTO);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(dishController).build();

        Assertions.assertEquals(mockMvc.perform(get("/api/dish/getBySearchId/").param("searchId", "1"))
                .andExpect(status().isOk())
                .andReturn().getAsyncResult(), ResponseEntity.ok(dishDTO));

        Mockito.verify(dishService, Mockito.times(1)).getDishBySearchId("1");
    }


}
