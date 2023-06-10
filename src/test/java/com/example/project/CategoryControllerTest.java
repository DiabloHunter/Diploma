package com.example.project;

import com.example.project.controller.CategoryController;
import com.example.project.controller.DishController;
import com.example.project.dto.dish.DishDTO;
import com.example.project.model.Category;
import com.example.project.model.Dish;
import com.example.project.service.ICategoryService;
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
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private ICategoryService categoryService;

    @Test
    public void testGetAllCategory() throws Exception {
        Category category  = new Category();
        category.setId("1");
        List<Category> expectedCategory = List.of(
                category
        );
        Mockito.when(categoryService.getAllCategory()).thenReturn(expectedCategory);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        Assertions.assertEquals(expectedCategory,
                mockMvc.perform(get("/api/category/"))
                .andExpect(status().isOk())
                .andReturn().getAsyncResult());

        Mockito.verify(categoryService, Mockito.times(1)).getAllCategory();
    }
}
