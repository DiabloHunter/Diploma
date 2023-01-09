package com.example.project.util;

public class ValidationUtil {

    public static void validateImageUrl(String imageUrl){
        if (imageUrl.length() > 240) {
            throw new IllegalArgumentException("Image URL is too long!");
        }
    }

}
