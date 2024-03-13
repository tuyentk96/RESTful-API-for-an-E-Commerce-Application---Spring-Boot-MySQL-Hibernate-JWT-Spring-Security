package com.project.ShopApp.repositories;

import com.project.ShopApp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findCategoryByName(String name);
}
