package com.example.project.dto.filter;

public class FilterCategoryDTO {

    private String name;

    public FilterCategoryDTO() {
    }

    public FilterCategoryDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FilterCategoryDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
