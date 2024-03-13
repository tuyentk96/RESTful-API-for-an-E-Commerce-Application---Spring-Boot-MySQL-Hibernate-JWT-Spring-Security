package com.project.ShopApp.services.impl;

import com.project.ShopApp.dto.request.ProductRequest;
import com.project.ShopApp.dto.respone.CreateProductResponse;
import com.project.ShopApp.dto.respone.UpdateProductResponse;
import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.exception.SuccessResponse;
import com.project.ShopApp.exception.SuccessResult;
import com.project.ShopApp.models.Category;
import com.project.ShopApp.models.Product;
import com.project.ShopApp.models.ProductImage;
import com.project.ShopApp.repositories.CategoryRepository;
import com.project.ShopApp.repositories.ProductImageRepository;
import com.project.ShopApp.repositories.ProductRepository;
import com.project.ShopApp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    public CreateProductResponse createProduct(ProductRequest productRequest){
        Category existingCategory = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
        CreateProductResponse createProductResponse = new CreateProductResponse();

        if (existsByName(productRequest.getName())){
            throw new ErrorResponse(ErrorResult.NAME_DATA_EXISTING);
        }

        Product newProduct = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .thumbnail(productRequest.getThumbnail())
                .category(existingCategory)
                .description(productRequest.getDescription())
                .build();

        createProductResponse.setData(newProduct);
        createProductResponse.setMessage(new SuccessResponse(SuccessResult.CREATED_SUCCESS).getMessage());

        productRepository.save(newProduct);

        return createProductResponse;
    }

    @Override
    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(
                () -> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        // lấy danh sách sản phẩm theo page và limit
        return productRepository.findAll(pageRequest);
    }

    @Override
    public UpdateProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = getProductById(id);
        UpdateProductResponse updateProductResponse = new UpdateProductResponse();

        Category existingCategory = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));

        existingProduct.setCategory(existingCategory);

        if (existsByName(productRequest.getName())){
            throw new ErrorResponse(ErrorResult.NAME_DATA_EXISTING);
        }
        existingProduct.setName(productRequest.getName());

        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setThumbnail(productRequest.getThumbnail());
        productRepository.saveAndFlush(existingProduct);

        updateProductResponse.setMessage(new SuccessResponse(SuccessResult.UPDATE_SUCCESS).getMessage());
        updateProductResponse.setData(existingProduct);

        return updateProductResponse;
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id);
        productRepository.delete(existingProduct);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }


    @Override
    public List<ProductImage> createProductImage(Long productId, List<MultipartFile> files) throws IOException {

        Product existingProduct = getProductById(productId);

        List<ProductImage> productImages = productImageRepository.findByProductId(productId);
        files = files == null ? new ArrayList<MultipartFile>() : files;
        List<ProductImage> listUploads = new ArrayList<>();

        for (MultipartFile file : files) {
            if (productImages.size()>=5){
                throw  new ErrorResponse(ErrorResult.NUMBER_OF_ITEMS_LIMIT);
            }

            if (file.getSize()==0){
                continue;
            }
            // kiem tra kich thuoc file
            if (file.getSize() > 10*1024*1024){
                throw new ErrorResponse(ErrorResult.PAYLOAD_TOO_LARGE);
            }
            // Kiem tra dinh dang co phai la file anh ko
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")){
                throw new ErrorResponse(ErrorResult.UNSUPPORTED_MEDIA_TYPE);
            }
            //Luu file va cap nhat thumbnail trong DTO
            String fileName = storeFile(file);

            // Luu vao doi tuong product_image trong CSDL
            ProductImage productImage = ProductImage.builder()
                            .product(existingProduct)
                            .imageUrl(fileName)
                            .build();
            productImageRepository.save(productImage);
            listUploads.add(productImage);
            productImages.add(productImage);
        }

        return listUploads;
    }

    @Override
    public Product updateThumbnailProduct(Long productImageId) {
        ProductImage productImage = productImageRepository.findById(productImageId)
                .orElseThrow(() -> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
        Product existingProduct = productImage.getProduct();
        existingProduct.setThumbnail(productImage.getImageUrl());
        productRepository.saveAndFlush(existingProduct);
        return existingProduct;
    }

    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Thêm UUID vào trước tên file để dảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString()+"_"+filename;

        // Đường dẫn đến thư mục mà ban muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");

        //Kiểm tra và tạo thư mục ếu không tồn tại
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(),uniqueFilename);

        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
}
