package com.project.ShopApp.services.impl;

import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.models.Category;
import com.project.ShopApp.models.Product;
import com.project.ShopApp.repositories.CategoryRepository;
import com.project.ShopApp.repositories.ProductRepository;
import com.project.ShopApp.services.ImportDataService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportDataServiceImpl implements ImportDataService {
    private final int CATEGORY_NAME_COLUMN = 0;

    private final int PRODUCT_NAME_COLUMN = 0;
    private final int PRODUCT_PRICE_COLUMN = 1;
    private final int PRODUCT_DESCRIPTION_COLUMN = 2;
    private final int PRODUCT_CATEGORY_ID_COLUMN = 3;


    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public Object importCategories(MultipartFile file) throws IOException {
        List<Category> categories = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String name = row.getCell(CATEGORY_NAME_COLUMN).getStringCellValue();
            Category category = new Category();
            category.setName(name);
            categories.add(category);
        }
        categoryRepository.saveAll(categories);

        return categories;
    }

    @Override
    public Object importProducts(MultipartFile file) throws IOException {
        List<Product> products = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String name = row.getCell(PRODUCT_NAME_COLUMN).getStringCellValue();
            String description = row.getCell(PRODUCT_DESCRIPTION_COLUMN).getStringCellValue();
            float price = (float) row.getCell(PRODUCT_PRICE_COLUMN).getNumericCellValue();
            long categoryId = (long) row.getCell(PRODUCT_CATEGORY_ID_COLUMN).getNumericCellValue();
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(categoryRepository.findById(categoryId).orElseThrow(
                    () -> new ErrorResponse(ErrorResult.NOT_FOUND_DATA)
            ));
            products.add(product);
        }
        productRepository.saveAll(products);
        return products;
    }
}
