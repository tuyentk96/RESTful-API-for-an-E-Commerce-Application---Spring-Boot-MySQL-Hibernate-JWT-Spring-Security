package com.project.ShopApp.controllers;

import com.project.ShopApp.exception.SuccessResponse;
import com.project.ShopApp.exception.SuccessResult;
import com.project.ShopApp.services.ImportDataService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/import-data")
public class ImportDataController {
    private final ImportDataService importDataService;
    @PostMapping(value = "/categories",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> importCategories(@ModelAttribute("file") MultipartFile file) throws IOException {
        Object data = importDataService.importCategories(file);
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.IMPORT_SUCCESS,data));
    }

    @PostMapping(value = "/products",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> importProducts(@ModelAttribute("file") MultipartFile file) throws IOException {
        Object data = importDataService.importProducts(file);
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.IMPORT_SUCCESS,data));
    }
}
