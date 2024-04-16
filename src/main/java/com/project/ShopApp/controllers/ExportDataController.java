package com.project.ShopApp.controllers;

import com.project.ShopApp.exception.SuccessResponse;
import com.project.ShopApp.exception.SuccessResult;
import com.project.ShopApp.services.ExportDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/export-data")
public class ExportDataController {
    private final ExportDataService exportDataService;
    @PostMapping("/categories")
    public ResponseEntity<SuccessResponse> exportCategories() throws IOException {
        exportDataService.exportCategories();
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.EXPORT_SUCCESS));
    }
    @PostMapping("/products")
    public ResponseEntity<SuccessResponse> exportProducts() throws IOException {
        exportDataService.exportProducts();
        return ResponseEntity.ok(new SuccessResponse(SuccessResult.EXPORT_SUCCESS));
    }
}
