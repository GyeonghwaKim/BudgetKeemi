package com.example.budgeKeemi.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${IMG_SAVE_PATH}")
    private String imgSavePath;

    @Value("${PROFILEIMG_RESOURCE_PATH}")
    private String profileImgResourcePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//file:/// 붙어야하나?
        registry.addResourceHandler(profileImgResourcePath+"/**")
                .addResourceLocations("file:///"+imgSavePath+"/profile/");

    }
}
