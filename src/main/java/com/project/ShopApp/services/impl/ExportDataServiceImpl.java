package com.project.ShopApp.services.impl;

import com.project.ShopApp.models.Category;
import com.project.ShopApp.models.Product;
import com.project.ShopApp.repositories.CategoryRepository;
import com.project.ShopApp.repositories.ProductRepository;
import com.project.ShopApp.services.ExportDataService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportDataServiceImpl implements ExportDataService {
    private final int CATEGORY_ID_COLUMN = 0;
    private final int CATEGORY_NAME_COLUMN = 1;

    private final int PRODUCT_ID_COLUMN = 0;
    private final int PRODUCT_NAME_COLUMN = 1;
    private final int PRODUCT_PRICE_COLUMN = 2;
    private final int PRODUCT_THUMBNAIL_COLUMN = 3;
    private final int PRODUCT_DESCRIPTION_COLUMN = 4;
    private final int PRODUCT_CREATED_AT_COLUMN = 5;
    private final int PRODUCT_UPDATED_AT_COLUMN = 6;
    private final int PRODUCT_CATEGORY_ID_COLUMN = 7;
    private final int PRODUCT_CATEGORY_NAME_COLUMN = 8;

    private final CategoryRepository CategoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void exportCategories() throws IOException {
        List<Category> categories = CategoryRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Categories");

        int rowIndex = 0;
        Row rowHeader = sheet.createRow(rowIndex);
        rowHeader.createCell(CATEGORY_ID_COLUMN).setCellValue("ID");
        rowHeader.createCell(CATEGORY_NAME_COLUMN).setCellValue("Name");
        rowIndex++;

        for (Category category : categories) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(CATEGORY_ID_COLUMN).setCellValue(category.getId());
            row.createCell(CATEGORY_NAME_COLUMN).setCellValue(category.getName());
        }
        String fileName = "Cateories_";
        storeFile(fileName, workbook);
    }

    @Override
    public void exportProducts() throws IOException {
        List<Product> products = productRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");
        int rowIndex = 0;
        createRowHeaderProduct(rowIndex, sheet);
        rowIndex++;
        for (Product product : products) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(PRODUCT_ID_COLUMN).setCellValue(product.getId());
            row.createCell(PRODUCT_NAME_COLUMN).setCellValue(product.getName());
            row.createCell(PRODUCT_PRICE_COLUMN).setCellValue(product.getPrice());
            row.createCell(PRODUCT_THUMBNAIL_COLUMN).setCellValue(product.getThumbnail());
            row.createCell(PRODUCT_DESCRIPTION_COLUMN).setCellValue(product.getDescription());

            LocalDateTime createdAt = product.getCreatedAt();
            LocalDateTime updatedAt = product.getUpdatedAt();

            row.createCell(PRODUCT_CREATED_AT_COLUMN).setCellValue(createdAt.toString());
            row.createCell(PRODUCT_UPDATED_AT_COLUMN).setCellValue(updatedAt.toString());
            row.createCell(PRODUCT_CATEGORY_ID_COLUMN).setCellValue(product.getCategory().getId());
            row.createCell(PRODUCT_CATEGORY_NAME_COLUMN).setCellValue(product.getCategory().getName());
        }
        String fileName = "Products_";
        storeFile(fileName, workbook);
    }

    private void createRowHeaderProduct(int rowIndex, Sheet sheet) {
        Row rowHeader = sheet.createRow(rowIndex);
        rowHeader.createCell(PRODUCT_ID_COLUMN).setCellValue("ID");
        rowHeader.createCell(PRODUCT_NAME_COLUMN).setCellValue("Name");
        rowHeader.createCell(PRODUCT_PRICE_COLUMN).setCellValue("Price");
        rowHeader.createCell(PRODUCT_THUMBNAIL_COLUMN).setCellValue("Thumbnail");
        rowHeader.createCell(PRODUCT_DESCRIPTION_COLUMN).setCellValue("Description");
        rowHeader.createCell(PRODUCT_CREATED_AT_COLUMN).setCellValue("Created At");
        rowHeader.createCell(PRODUCT_UPDATED_AT_COLUMN).setCellValue("Updated At");
        rowHeader.createCell(PRODUCT_CATEGORY_ID_COLUMN).setCellValue("Category ID");
        rowHeader.createCell(PRODUCT_CATEGORY_NAME_COLUMN).setCellValue("Category Name");
    }

    private void storeFile(String fileName, Workbook workbook) throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();
        String uniqueFilename = fileName + dateTime.getYear() + dateTime.getMonthValue()
                + dateTime.getDayOfMonth() + ".xlsx";

        // Đường dẫn đến thư mục mà ban muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("export");

        //Kiểm tra và tạo thư mục nếu không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);

        FileOutputStream fileOutputStream = new FileOutputStream(destination.toFile());
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        // Sao chép file vào thư mục đích
        try {
            Files.copy((Path) fileOutputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
