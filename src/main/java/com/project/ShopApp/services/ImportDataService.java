package com.project.ShopApp.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportDataService {
    Object importCategories(MultipartFile file) throws IOException;

    Object importProducts(MultipartFile file) throws IOException;
}
